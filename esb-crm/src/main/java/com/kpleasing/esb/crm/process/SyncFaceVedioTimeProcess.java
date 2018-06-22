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
import com.kpleasing.esb.model.crm010.CRM010Request;
import com.kpleasing.esb.model.crm010.CRM010Response;
import com.kpleasing.esb.model.crm010.SyncFaceVedioTimeRequest;
import com.kpleasing.esb.model.crm010.SyncFaceVedioTimeResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncFaceVedioTimeProcess implements Processor {

	private static Logger logger = Logger.getLogger(SyncFaceVedioTimeProcess.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM010Request crm010Request = getRegisterRequestObject((byte[]) in.getBody());
		
		CRM010Response crm010RespBean = generateCRM010ResponseBean(crm010Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm010RespBean));
		logger.info("ESB接口CRM010响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}

	
	/**
	 * ESB 响应实体
	 * @param crm010Request
	 * @return
	 */
	private CRM010Response generateCRM010ResponseBean(CRM010Request crm010Request) {
		CRM010Response crm010Response = new CRM010Response();
		crm010Response.setReq_serial_no(crm010Request.getReq_serial_no());
		crm010Response.setReq_date(crm010Request.getReq_date());
		crm010Response.setRes_serial_no(StringUtil.getSerialNo32());
		crm010Response.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm010Response.setReturn_code("SUCCESS");
		crm010Response.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = crm010Request.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(crm010Request.getSecurity_code().equals(cParam.getClientCode()) && 
					crm010Request.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		try {
			SyncFaceVedioTimeRequest syncFVTimeRequest = getDestRequestBean(crm010Request, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(syncFVTimeRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口SYNC_FACEVEDIO_TIME返回明文信息：" + RespXml);
			
			SyncFaceVedioTimeResponse syncFVTimeResponse = new SyncFaceVedioTimeResponse();
			XMLHelper.getBeanFromXML(RespXml, syncFVTimeResponse);
			if("SUCCESS".equals(syncFVTimeResponse.getReturn_code()) && "SUCCESS".equals(syncFVTimeResponse.getResult_code())) {
				verificationReturnMsg(syncFVTimeResponse, paramConfig.getSignKey());
			}
			
			crm010Response.setResult_code(syncFVTimeResponse.getResult_code());
			crm010Response.setResult_desc(syncFVTimeResponse.getResult_desc());
			
		} catch(ESBException e) {
			crm010Response.setResult_code(e.getCode());
			crm010Response.setResult_desc(e.getDescription());
			
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			crm010Response.setResult_code("FAILED");
			crm010Response.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				crm010Response.setSign(Security.getSign(crm010Response, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
		return crm010Response;
	}
	
	
	/**
	 * CRM 请求实体
	 * @param crm010Request
	 * @param paramConfig
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private SyncFaceVedioTimeRequest getDestRequestBean(CRM010Request crm010Request, ParameterConfig paramConfig) throws IllegalArgumentException, IllegalAccessException {
		SyncFaceVedioTimeRequest syncFVTimeRequest = new SyncFaceVedioTimeRequest();
		syncFVTimeRequest.setApi_code("SYNC_FACEVEDIO_TIME");
		syncFVTimeRequest.setReq_serial_no(StringUtil.getSerialNo32());
		syncFVTimeRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		syncFVTimeRequest.setSecurity_code(paramConfig.getAppCode());
		syncFVTimeRequest.setSecurity_value(paramConfig.getAppSecurity());
		syncFVTimeRequest.setCust_id(crm010Request.getCust_id());
		syncFVTimeRequest.setCust_name(crm010Request.getCust_name());
		syncFVTimeRequest.setFirst_date(crm010Request.getFirst_date());
		syncFVTimeRequest.setSecond_date(crm010Request.getSecond_date());
		
		syncFVTimeRequest.setSign(Security.getSign(syncFVTimeRequest, paramConfig.getSignKey()));

		return syncFVTimeRequest;
	}
	
	
	/**
	 * CRM 请求报文
	 * @param syncFVTimeRequest
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String getRequestMsg(SyncFaceVedioTimeRequest syncFVTimeRequest) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(syncFVTimeRequest.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(syncFVTimeRequest.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(syncFVTimeRequest.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(syncFVTimeRequest.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(syncFVTimeRequest.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(syncFVTimeRequest.getSign()).append("]]></sign>")
		.append("</head><body>");
		msgRequest.append(XMLHelper.getXMLFromBean(syncFVTimeRequest));
		msgRequest.append("</body></crm>");
		
		logger.info("CRM接口SYNC_FACEVEDIO_TIME请求报文信息：" + msgRequest.toString());
		return msgRequest.toString();
	}

	
	/**
	 * CRM防篡改安全校验
	 * @param syncFVTimeResponse
	 * @param key
	 * @throws ESBException
	 */
	private void verificationReturnMsg(SyncFaceVedioTimeResponse syncFVTimeResponse, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = syncFVTimeResponse.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "CRM API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		syncFVTimeResponse.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(syncFVTimeResponse, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "CRM API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "CRM API签名出錯!!!");
		}
	}

	
	/**
	 *  ESB 响应报文
	 * @param crm010Request
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(CRM010Response crm010Request) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(crm010Request.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(crm010Request.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(crm010Request.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(crm010Request.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(crm010Request.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(crm010Request.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(crm010Request.getSign()).append("]]></sign>")
		.append("</head><body>");
		msgResponse.append(XMLHelper.getXMLFromBean(crm010Request));
		msgResponse.append("</body></esb>");
		
		logger.info("ESB接口CRM010响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}
	
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private CRM010Request getRegisterRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM010Request crm010Request = (CRM010Request) ois.readObject();
			
			return crm010Request;

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
