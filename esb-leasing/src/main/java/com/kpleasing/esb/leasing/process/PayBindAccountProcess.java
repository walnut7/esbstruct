package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing017.LEASING017Request;
import com.kpleasing.esb.model.leasing017.LEASING017Response;
import com.kpleasing.esb.model.leasing017.PayBindAccountRequest;
import com.kpleasing.esb.model.leasing017.PayBindAccountResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class PayBindAccountProcess  extends AbstractLeasingProcessor<LEASING017Request, LEASING017Response, PayBindAccountRequest, PayBindAccountResponse> {
	
	private static Logger logger = Logger.getLogger(PayBindAccountProcess.class);

	@Override
	public LEASING017Response initResponse() {
		return new LEASING017Response();
	}


	@Override
	public PayBindAccountRequest generateRequestEntity(LEASING017Request leasing017Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		PayBindAccountRequest payBindAccountRequest = new PayBindAccountRequest();
		payBindAccountRequest.setRequestNum(StringUtil.getDateSerialNo6());
		payBindAccountRequest.setSyscode(config.getAppCode());
		payBindAccountRequest.setSyspwd(config.getAppSecurity());
		payBindAccountRequest.setExternal_ref_number(leasing017Request.getExternal_no());
		payBindAccountRequest.setCustomer_id(leasing017Request.getCust_id());
		payBindAccountRequest.setBank_account_num(leasing017Request.getRepay_card_no());
		payBindAccountRequest.setValid_code(leasing017Request.getValid_code());
		payBindAccountRequest.setToken(leasing017Request.getToken());
		payBindAccountRequest.setPhone(leasing017Request.getBank_phone());
		
		logger.info("Sign Before MD5："+payBindAccountRequest.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(payBindAccountRequest.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		payBindAccountRequest.setSign(sign);
		
		return payBindAccountRequest;
	}


	@Override
	public String generateRequestXML(PayBindAccountRequest payBindAccountRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(payBindAccountRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(payBindAccountRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(payBindAccountRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:external_ref_number>").append(StringUtil.setNullToBlank(payBindAccountRequest.getExternal_ref_number())).append("</sch:external_ref_number>")
		.append("<sch:customer_id>").append(StringUtil.setNullToBlank(payBindAccountRequest.getCustomer_id())).append("</sch:customer_id>")
		.append("<sch:bank_account_num>").append(StringUtil.setNullToBlank(payBindAccountRequest.getBank_account_num())).append("</sch:bank_account_num>")
		.append("<sch:valid_code>").append(StringUtil.setNullToBlank(payBindAccountRequest.getValid_code())).append("</sch:valid_code>")
		.append("<sch:token>").append(StringUtil.setNullToBlank(payBindAccountRequest.getToken())).append("</sch:token>")
		.append("<sch:phone>").append(StringUtil.setNullToBlank(payBindAccountRequest.getPhone())).append("</sch:phone>")
		.append("<sch:sign>").append(payBindAccountRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}


	@Override
	public PayBindAccountResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			PayBindAccountResponse payBindAccountResponse = new PayBindAccountResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", payBindAccountResponse);
			
			return payBindAccountResponse;
			
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		}
	}


	@Override
	public void covertToEBody(PayBindAccountResponse payBindAccountResponse, LEASING017Response leasing017Response, String key) throws ESBException {
		if(payBindAccountResponse.getStatus()!=null && "Y".equals(payBindAccountResponse.getStatus())) {
			leasing017Response.setResult_code("SUCCESS");
		} else {
			leasing017Response.setResult_code("FAILED");
		}
		leasing017Response.setResult_desc(payBindAccountResponse.getMessage());
		leasing017Response.setStorable_pan(payBindAccountResponse.getStorable_pan());
		leasing017Response.setResponse_code(payBindAccountResponse.getResponse_code());
		leasing017Response.setResponse_message(payBindAccountResponse.getResponse_text_message());
		leasing017Response.setError_code(payBindAccountResponse.getError_code());
		leasing017Response.setError_message(payBindAccountResponse.getError_message());
		
		try {
			leasing017Response.setSign(Security.getSign(leasing017Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING017Response leasing017Response, String key, String code, String msg) {
		try {
			leasing017Response.setResult_code(code);
			leasing017Response.setResult_desc(msg);
			leasing017Response.setSign(Security.getSign(leasing017Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
