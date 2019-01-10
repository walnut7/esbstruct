package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing020.LEASING020Request;
import com.kpleasing.esb.model.leasing020.LEASING020Response;
import com.kpleasing.esb.model.leasing020.SavePFStCardNumRequest;
import com.kpleasing.esb.model.leasing020.SavePFStCardNumResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SavePFStCardNumProcess extends AbstractLeasingProcessor<LEASING020Request, LEASING020Response, SavePFStCardNumRequest, SavePFStCardNumResponse> {
	
	private static Logger logger = Logger.getLogger(SavePFStCardNumProcess.class);

	@Override
	public LEASING020Response initResponse() {
		return new LEASING020Response();
	}

	@Override
	public SavePFStCardNumRequest generateRequestEntity(LEASING020Request leasing020Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		SavePFStCardNumRequest saveStCardReq = new SavePFStCardNumRequest();
		saveStCardReq.setRequest_number(StringUtil.getDateSerialNo6());
		saveStCardReq.setUser_account(config.getAppCode());
		saveStCardReq.setPassword(config.getAppSecurity());
		saveStCardReq.setId_card_no(leasing020Request.getCert_code());
		saveStCardReq.setStcardnum(leasing020Request.getSpdb_stCard_no());
		
		logger.info("Sign Before MD5："+saveStCardReq.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(saveStCardReq.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		saveStCardReq.setSign(sign);
		
		return saveStCardReq;
	}

	@Override
	public String generateRequestXML(SavePFStCardNumRequest saveStCardReq, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(saveStCardReq.getRequest_number()).append("</sch:request_number>")
		.append("<sch:user_account>").append(saveStCardReq.getUser_account()).append("</sch:user_account>")
		.append("<sch:password>").append(saveStCardReq.getPassword()).append("</sch:password>")
		.append("<sch:id_card_no>").append(StringUtil.setNullToBlank(saveStCardReq.getId_card_no())).append("</sch:id_card_no>")
		.append("<sch:stcardnum>").append(StringUtil.setNullToBlank(saveStCardReq.getStcardnum())).append("</sch:stcardnum>")
		.append("<sch:sign>").append(saveStCardReq.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}

	@Override
	public SavePFStCardNumResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			SavePFStCardNumResponse saveStCardNumResp = new SavePFStCardNumResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", saveStCardNumResp);
			
			return saveStCardNumResp;
			
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SavePFStCardNum解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SavePFStCardNum解析失败！");
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-SavePFStCardNum解析失败！");
		}
	}

	@Override
	public void covertToEBody(SavePFStCardNumResponse saveStCardNumResp, LEASING020Response leasing020Response, String key) throws ESBException {
		if(saveStCardNumResp.getStatus()!=null && "Y".equals(saveStCardNumResp.getStatus())) {
			leasing020Response.setResult_code("SUCCESS");
		} else {
			leasing020Response.setResult_code("FAILED");
		}
		leasing020Response.setResult_desc(saveStCardNumResp.getMessage());
		
		try {
			leasing020Response.setSign(Security.getSign(leasing020Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}

	@Override
	public void resultException(LEASING020Response LEASING020Response, String key, String code, String msg) {
		try {
			LEASING020Response.setResult_code(code);
			LEASING020Response.setResult_desc(msg);
			LEASING020Response.setSign(Security.getSign(LEASING020Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
		
	}

}
