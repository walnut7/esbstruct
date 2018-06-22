package com.kpleasing.esb.crm.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm008.CRM008Request;
import com.kpleasing.esb.model.crm008.CRM008Response;
import com.kpleasing.esb.model.crm008.SyncReserverCustomerRequest;
import com.kpleasing.esb.model.crm008.SyncReserverCustomerResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncReserverCustomerProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(SyncReserverCustomerProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM008Request crm008Request = getRegisterRequestObject((byte[]) in.getBody());
		
		CRM008Response crm008RespBean = generateCRM008ResponseBean(crm008Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm008RespBean));
		logger.info("ESB接口CRM008响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}

	
	/**
	 * 创建ESB响应实体对象
	 * @param crm008Request
	 * @return
	 */
	private CRM008Response generateCRM008ResponseBean(CRM008Request crm008Request) {
		CRM008Response crm008Response = new CRM008Response();
		crm008Response.setReq_serial_no(crm008Request.getReq_serial_no());
		crm008Response.setReq_date(crm008Request.getReq_date());
		crm008Response.setRes_serial_no(StringUtil.getSerialNo32());
		crm008Response.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm008Response.setReturn_code("SUCCESS");
		crm008Response.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = crm008Request.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(crm008Request.getSecurity_code().equals(cParam.getClientCode()) && 
					crm008Request.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		try {
			SyncReserverCustomerRequest reserverCarRequest = getDestRequestBean(crm008Request, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(reserverCarRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口SYNC_RESERVER_CUSTOMER返回明文信息：" + RespXml);
			
			SyncReserverCustomerResponse reserverCarResponse = new SyncReserverCustomerResponse();
			XMLHelper.getBeanFromXML(RespXml, reserverCarResponse);
			if("SUCCESS".equals(reserverCarResponse.getReturn_code()) && "SUCCESS".equals(reserverCarResponse.getResult_code())) {
				verificationReturnMsg(reserverCarResponse, paramConfig.getSignKey());
			}
			
			crm008Response.setResult_code(reserverCarResponse.getResult_code());
			crm008Response.setResult_desc(reserverCarResponse.getResult_desc());
			
		} catch(ESBException e) {
			crm008Response.setResult_code(e.getCode());
			crm008Response.setResult_desc(e.getDescription());
			
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			crm008Response.setResult_code("FAILED");
			crm008Response.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				crm008Response.setSign(Security.getSign(crm008Response, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
		return crm008Response;
	}

	
	/**
	 * 验证CRM返回值
	 * @param reserverCarResponse
	 * @param key
	 * @throws ESBException
	 */
	private void verificationReturnMsg(SyncReserverCustomerResponse reserverCarResponse, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = reserverCarResponse.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "CRM API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		reserverCarResponse.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(reserverCarResponse, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "CRM API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "CRM API签名出錯!!!");
		}
	}


	/**
	 * 创建CRM请求报文
	 * @param reserverCarRequest
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String getRequestMsg(SyncReserverCustomerRequest reserverCarRequest) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(reserverCarRequest.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(reserverCarRequest.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(reserverCarRequest.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(reserverCarRequest.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(reserverCarRequest.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(reserverCarRequest.getSign()).append("]]></sign>")
		.append("</head><body>");
		msgRequest.append(XMLHelper.getXMLFromBean(reserverCarRequest));
		msgRequest.append("</body></crm>");
		
		logger.info("CRM接口SYNC_RESERVER_CUSTOMER请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	}


	/**
	 * 创建请求CRM对象实体
	 * @param crm008Request
	 * @param paramConfig
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private SyncReserverCustomerRequest getDestRequestBean(CRM008Request crm008Request, ParameterConfig paramConfig) throws IllegalArgumentException, IllegalAccessException {
		SyncReserverCustomerRequest reserverCarRequest = new SyncReserverCustomerRequest();
		reserverCarRequest.setApi_code("SYNC_RESERVER_CUSTOMER");
		reserverCarRequest.setReq_serial_no(StringUtil.getSerialNo32());
		reserverCarRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		reserverCarRequest.setSecurity_code(paramConfig.getAppCode());
		reserverCarRequest.setSecurity_value(paramConfig.getAppSecurity());
		reserverCarRequest.setCust_id(crm008Request.getCust_id());
		reserverCarRequest.setCust_name(crm008Request.getCust_name());
		
		if("1".equals(crm008Request.getGender())) {
			reserverCarRequest.setGender("MALE");
		} else {
			reserverCarRequest.setGender("FEMALE");
		}
		
		reserverCarRequest.setPhone(crm008Request.getPhone());
		reserverCarRequest.setReserve_time(crm008Request.getReserve_time());
		reserverCarRequest.setReserve_store(crm008Request.getReserve_store());
		reserverCarRequest.setProduct_title(crm008Request.getProduct_title());
		reserverCarRequest.setProduct_desc(crm008Request.getProduct_desc());
		reserverCarRequest.setMemo(crm008Request.getMemo());
		reserverCarRequest.setSign(Security.getSign(reserverCarRequest, paramConfig.getSignKey()));
	
		return reserverCarRequest;
	}


	/**
	 * ESB 响应报文
	 * @param crm008RespBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(CRM008Response crm008RespBean) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code>").append(crm008RespBean.getReturn_code()).append("</return_code>")
		.append("<return_desc>").append(crm008RespBean.getReturn_desc()).append("</return_desc>")
		.append("<req_serial_no>").append(crm008RespBean.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(crm008RespBean.getReq_date()).append("</req_date>")
		.append("<res_serial_no>").append(crm008RespBean.getRes_serial_no()).append("</res_serial_no>")
		.append("<res_date>").append(crm008RespBean.getRes_date()).append("</res_date>")
		.append("<sign>").append(crm008RespBean.getSign()).append("</sign>")
		.append("</head><body>");
		msgResponse.append(XMLHelper.getXMLFromBean(crm008RespBean));
		msgResponse.append("</body></esb>");
		
		logger.info("ESB接口CRM008响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}


	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	private CRM008Request getRegisterRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM008Request crm008Request = (CRM008Request) ois.readObject();
			
			return crm008Request;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != baiStream) {
				try {
					baiStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != ois) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
