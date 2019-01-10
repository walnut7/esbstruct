package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing018.LEASING018Request;
import com.kpleasing.esb.model.leasing018.LEASING018Response;
import com.kpleasing.esb.model.leasing018.SendTDIdentityAuthRequest;
import com.kpleasing.esb.model.leasing018.SendTDIdentityAuthResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SendTDIdentityAuthProcess extends AbstractLeasingProcessor<LEASING018Request, LEASING018Response, SendTDIdentityAuthRequest, SendTDIdentityAuthResponse> {
	
	private static Logger logger = Logger.getLogger(SendTDIdentityAuthProcess.class);

	@Override
	public LEASING018Response initResponse() {
		return new LEASING018Response();
	}


	@Override
	public SendTDIdentityAuthRequest generateRequestEntity(LEASING018Request leasing018Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		SendTDIdentityAuthRequest sendIdentityAuthReq = new SendTDIdentityAuthRequest();
		sendIdentityAuthReq.setRequest_number(StringUtil.getDateSerialNo6());
		sendIdentityAuthReq.setUser_account(config.getAppCode());
		sendIdentityAuthReq.setPassword(config.getAppSecurity());
		sendIdentityAuthReq.setName(leasing018Request.getName());
		sendIdentityAuthReq.setMobile(leasing018Request.getPhone());
		sendIdentityAuthReq.setId_card_no(leasing018Request.getCert_code());
		
		logger.info("Sign Before MD5："+sendIdentityAuthReq.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(sendIdentityAuthReq.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		sendIdentityAuthReq.setSign(sign);
		
		return sendIdentityAuthReq;
	}


	@Override
	public String generateRequestXML(SendTDIdentityAuthRequest sendIdentityAuthReq, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(sendIdentityAuthReq.getRequest_number()).append("</sch:request_number>")
		.append("<sch:user_account>").append(sendIdentityAuthReq.getUser_account()).append("</sch:user_account>")
		.append("<sch:password>").append(sendIdentityAuthReq.getPassword()).append("</sch:password>")
		.append("<sch:name>").append(StringUtil.setNullToBlank(sendIdentityAuthReq.getName())).append("</sch:name>")
		.append("<sch:mobile>").append(StringUtil.setNullToBlank(sendIdentityAuthReq.getMobile())).append("</sch:mobile>")
		.append("<sch:id_card_no>").append(StringUtil.setNullToBlank(sendIdentityAuthReq.getId_card_no())).append("</sch:id_card_no>")
		.append("<sch:sign>").append(sendIdentityAuthReq.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}


	@Override
	public SendTDIdentityAuthResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			SendTDIdentityAuthResponse sendAuthResp = new SendTDIdentityAuthResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", sendAuthResp);
			
			return sendAuthResp;
			
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SendTDIdentityAuth解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SendTDIdentityAuth解析失败！");
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SendTDIdentityAuth解析失败！");
		}
	}


	@Override
	public void covertToEBody(SendTDIdentityAuthResponse sendAuthResp, LEASING018Response leasing018Response, String key) throws ESBException {
		if(sendAuthResp.getStatus()!=null && "Y".equals(sendAuthResp.getStatus())) {
			if("0".equals(sendAuthResp.getAuth_status()) || "2".equals(sendAuthResp.getAuth_status())){
				leasing018Response.setResult_code("SUCCESS");
				leasing018Response.setResult_desc(sendAuthResp.getMessage());
			}else{
				leasing018Response.setResult_code("FAILED");
				leasing018Response.setResult_desc("姓名、手机号、身份证三要素认证未通过");
			}
		} else {
			leasing018Response.setResult_code("FAILED");
			leasing018Response.setResult_desc(sendAuthResp.getMessage());
		}
		//leasing018Response.setAuth_status(sendAuthResp.getAuth_status());
		
		try {
			leasing018Response.setSign(Security.getSign(leasing018Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING018Response leasing018Response, String key, String code, String msg) {
		try {
			leasing018Response.setResult_code(code);
			leasing018Response.setResult_desc(msg);
			leasing018Response.setSign(Security.getSign(leasing018Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}