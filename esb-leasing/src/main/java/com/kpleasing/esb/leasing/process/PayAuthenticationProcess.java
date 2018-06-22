package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing016.LEASING016Request;
import com.kpleasing.esb.model.leasing016.LEASING016Response;
import com.kpleasing.esb.model.leasing016.PayAuthenticationRequest;
import com.kpleasing.esb.model.leasing016.PayAuthenticationResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class PayAuthenticationProcess extends AbstractLeasingProcessor<LEASING016Request, LEASING016Response, PayAuthenticationRequest, PayAuthenticationResponse> {
	
	private static Logger logger = Logger.getLogger(PayAuthenticationProcess.class);

	@Override
	public LEASING016Response initResponse() {
		return new LEASING016Response();
	}


	@Override
	public PayAuthenticationRequest generateRequestEntity(LEASING016Request leasing016Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		PayAuthenticationRequest payAutherntRequest = new PayAuthenticationRequest();
		payAutherntRequest.setRequestNum(StringUtil.getDateSerialNo6());
		payAutherntRequest.setSyscode(config.getAppCode());
		payAutherntRequest.setSyspwd(config.getAppSecurity());
		payAutherntRequest.setExternal_ref_number(leasing016Request.getExternal_no());
		payAutherntRequest.setCustomer_id(leasing016Request.getCust_id());
		payAutherntRequest.setBank_account_num(leasing016Request.getRepay_card_no());
		payAutherntRequest.setName(leasing016Request.getCust_name());
		// payAutherntRequest.setId_type(leasing016Request.getCert_type());
		payAutherntRequest.setId_type("0");
		payAutherntRequest.setId_card_no(leasing016Request.getCert_code());
		payAutherntRequest.setPhone(leasing016Request.getBank_phone());
		
		logger.info("Sign Before MD5："+payAutherntRequest.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(payAutherntRequest.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		payAutherntRequest.setSign(sign);
		
		return payAutherntRequest;
	}


	@Override
	public String generateRequestXML(PayAuthenticationRequest payAuthentRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(payAuthentRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(payAuthentRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(payAuthentRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:external_ref_number>").append(StringUtil.setNullToBlank(payAuthentRequest.getExternal_ref_number())).append("</sch:external_ref_number>")
		.append("<sch:customer_id>").append(StringUtil.setNullToBlank(payAuthentRequest.getCustomer_id())).append("</sch:customer_id>")
		.append("<sch:bank_account_num>").append(StringUtil.setNullToBlank(payAuthentRequest.getBank_account_num())).append("</sch:bank_account_num>")
		.append("<sch:name>").append(StringUtil.setNullToBlank(payAuthentRequest.getName())).append("</sch:name>")
		.append("<sch:id_type>").append(StringUtil.setNullToBlank(payAuthentRequest.getId_type())).append("</sch:id_type>")
		.append("<sch:id_card_no>").append(StringUtil.setNullToBlank(payAuthentRequest.getId_card_no())).append("</sch:id_card_no>")
		.append("<sch:phone>").append(StringUtil.setNullToBlank(payAuthentRequest.getPhone())).append("</sch:phone>")
		.append("<sch:sign>").append(payAuthentRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}


	@Override
	public PayAuthenticationResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			PayAuthenticationResponse payAuthentResponse = new PayAuthenticationResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", payAuthentResponse);
			
			return payAuthentResponse;
			
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
	public void covertToEBody(PayAuthenticationResponse payAuthentResponse, LEASING016Response leasing016Response, String key) throws ESBException {
		if(payAuthentResponse.getStatus()!=null && "Y".equals(payAuthentResponse.getStatus())) {
			leasing016Response.setResult_code("SUCCESS");
		} else {
			leasing016Response.setResult_code("FAILED");
		}
		leasing016Response.setResult_desc(payAuthentResponse.getMessage());
		leasing016Response.setToken(payAuthentResponse.getToken());
		leasing016Response.setStorable_pan(payAuthentResponse.getStorable_pan());
		leasing016Response.setResponse_code(payAuthentResponse.getResponse_code());
		leasing016Response.setResponse_message(payAuthentResponse.getResponse_text_message());
		leasing016Response.setError_code(payAuthentResponse.getError_code());
		leasing016Response.setError_message(payAuthentResponse.getError_message());
		
		try {
			leasing016Response.setSign(Security.getSign(leasing016Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING016Response leasing016Response, String key, String code, String msg) {
		try {
			leasing016Response.setResult_code(code);
			leasing016Response.setResult_desc(msg);
			leasing016Response.setSign(Security.getSign(leasing016Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
