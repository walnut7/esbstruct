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
import com.kpleasing.esb.model.crm003.CRM003Request;
import com.kpleasing.esb.model.crm003.CRM003Response;
import com.kpleasing.esb.model.crm003.SetUserProfileRequest;
import com.kpleasing.esb.model.crm003.SetUserProfileResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;


public class SetUserProfileProcess implements Processor {

	private static Logger logger = Logger.getLogger(SetUserProfileProcess.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM003Request crm003Requ = getSetUserProfileRequestObject((byte[]) in.getBody());
		
		CRM003Response crm003RespBean = generateCRM003ResponseBean(crm003Requ);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm003RespBean));
		logger.info("ESB接口CRM003响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}
	
	
	private CRM003Response generateCRM003ResponseBean(CRM003Request crm003Requ) {
		CRM003Response crm003Resp = new CRM003Response();
		crm003Resp.setReq_serial_no(crm003Requ.getReq_serial_no());
		crm003Resp.setReq_date(crm003Requ.getReq_date());
		crm003Resp.setRes_serial_no(StringUtil.getSerialNo32());
		crm003Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm003Resp.setReturn_code("SUCCESS");
		crm003Resp.setReturn_desc("请求成功");
		
		try {
			ParameterConfig paramConfig = crm003Requ.getParamConfig();
			SetUserProfileRequest setProfileRequest = getDestRequestBean(crm003Requ, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(setProfileRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口SET_USER_PROFILE返回明文信息：" + RespXml);
			
			SetUserProfileResponse setProfileResp = new SetUserProfileResponse();
			XMLHelper.getBeanFromXML(RespXml, setProfileResp);
			if("SUCCESS".equals(setProfileResp.getReturn_code()) && "SUCCESS".equals(setProfileResp.getResult_code())) {
				verificationReturnMsg(setProfileResp, paramConfig.getSignKey());
			}
			
			crm003Resp.setResult_code(setProfileResp.getResult_code());
			crm003Resp.setResult_desc(setProfileResp.getResult_desc());
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams ) {
				if(crm003Requ.getSecurity_code().equals(cParam.getClientCode()) && 
						crm003Requ.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			crm003Resp.setSign(Security.getSign(crm003Resp, key));
		} catch (Exception  e) {
			crm003Resp.setResult_code("FAILED");
			crm003Resp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} 
		return crm003Resp;
	}
	
	
	/**
	 * 验证目标系统返回值
	 * @param setProfileResp
	 * @param key
	 * @throws ESBException 
	 */
	private void verificationReturnMsg(SetUserProfileResponse setProfileResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = setProfileResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "CRM API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		setProfileResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(setProfileResp, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "CRM API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "CRM API签名出錯!!!");
		}
	}


	/**
	 * CRM客户手机重置接口
	 * @param setProfileRequ
	 * @return
	 */
	private String getRequestMsg(SetUserProfileRequest setProfileRequ) {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(setProfileRequ.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(setProfileRequ.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(setProfileRequ.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(setProfileRequ.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(setProfileRequ.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(setProfileRequ.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<cust_id><![CDATA[").append(setProfileRequ.getCust_id()).append("]]></cust_id>")
		.append("<phone><![CDATA[").append(setProfileRequ.getPhone()).append("]]></phone>")
		.append("<cust_name><![CDATA[").append(setProfileRequ.getCust_name()).append("]]></cust_name>")
		.append("<cert_type><![CDATA[").append(setProfileRequ.getCert_type()).append("]]></cert_type>")
		.append("<cert_code><![CDATA[").append(setProfileRequ.getCert_code()).append("]]></cert_code>")
		.append("</body></crm>");
		
		logger.info("CRM接口SET_USER_PROFILE请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	}


	/**
	 * 创建系统调用实体bean
	 * @param crm001Requ
	 * @param paramConfig
	 * @return
	 * @throws IllegalAccessException
	 */
	private SetUserProfileRequest getDestRequestBean(CRM003Request crm003Requ, ParameterConfig paramConfig) throws IllegalAccessException {
		SetUserProfileRequest setProfileBean = new SetUserProfileRequest();
		setProfileBean.setApi_code("SET_USER_PROFILE");
		setProfileBean.setReq_serial_no(StringUtil.getSerialNo32());
		setProfileBean.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		setProfileBean.setSecurity_code(paramConfig.getAppCode());
		setProfileBean.setSecurity_value(paramConfig.getAppSecurity());
		setProfileBean.setPhone(crm003Requ.getPhone());
		setProfileBean.setCust_id(crm003Requ.getCust_id());
		setProfileBean.setCust_name(crm003Requ.getCust_name());
		setProfileBean.setCert_type(crm003Requ.getCert_type());
		setProfileBean.setCert_code(crm003Requ.getCert_code());
		setProfileBean.setSign(Security.getSign(setProfileBean, paramConfig.getSignKey()));
	
		return setProfileBean;
	}


	/**
	 * ESB响应 报文
	 * @param respBean
	 * @return
	 */
	private String generateMsgResponseXML(CRM003Response crm003RespBean) {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code>").append(crm003RespBean.getReturn_code()).append("</return_code>")
		.append("<return_desc>").append(crm003RespBean.getReturn_desc()).append("</return_desc>")
		.append("<req_serial_no>").append(crm003RespBean.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(crm003RespBean.getReq_date()).append("</req_date>")
		.append("<res_serial_no>").append(crm003RespBean.getRes_serial_no()).append("</res_serial_no>")
		.append("<res_date>").append(crm003RespBean.getRes_date()).append("</res_date>")
		.append("<sign>").append(crm003RespBean.getSign()).append("</sign>")
		.append("</head><body>")
		.append("<result_code>").append(crm003RespBean.getResult_code()).append("</result_code>")
		.append("<result_desc>").append(crm003RespBean.getResult_desc()).append("</result_desc>")
		.append("</body></esb>");
		
		logger.info("ESB接口CRM003响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	private CRM003Request getSetUserProfileRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM003Request crm003Request = (CRM003Request) ois.readObject();
			
			return crm003Request;

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
