package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing019.LEASING019Request;
import com.kpleasing.esb.model.leasing019.LEASING019Response;
import com.kpleasing.esb.model.leasing019.QueryPFStCardNumRequest;
import com.kpleasing.esb.model.leasing019.QueryPFStCardNumResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryPFStCardNumProcess  extends AbstractLeasingProcessor<LEASING019Request, LEASING019Response, QueryPFStCardNumRequest, QueryPFStCardNumResponse> {
	
	private static Logger logger = Logger.getLogger(QueryPFStCardNumProcess.class);

	@Override
	public LEASING019Response initResponse() {
		return new LEASING019Response();
	}

	@Override
	public QueryPFStCardNumRequest generateRequestEntity(LEASING019Request leasing019Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		QueryPFStCardNumRequest queryStCardReq = new QueryPFStCardNumRequest();
		queryStCardReq.setRequest_number(StringUtil.getDateSerialNo6());
		queryStCardReq.setUser_account(config.getAppCode());
		queryStCardReq.setPassword(config.getAppSecurity());
		queryStCardReq.setId_card_no(leasing019Request.getCert_code());
		
		logger.info("Sign Before MD5："+queryStCardReq.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(queryStCardReq.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		queryStCardReq.setSign(sign);
		
		return queryStCardReq;
	}

	@Override
	public String generateRequestXML(QueryPFStCardNumRequest queryStCardReq, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(queryStCardReq.getRequest_number()).append("</sch:request_number>")
		.append("<sch:user_account>").append(queryStCardReq.getUser_account()).append("</sch:user_account>")
		.append("<sch:password>").append(queryStCardReq.getPassword()).append("</sch:password>")
		.append("<sch:id_card_no>").append(StringUtil.setNullToBlank(queryStCardReq.getId_card_no())).append("</sch:id_card_no>")
		.append("<sch:sign>").append(queryStCardReq.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}

	@Override
	public QueryPFStCardNumResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			QueryPFStCardNumResponse queryStCardNumResp = new QueryPFStCardNumResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", queryStCardNumResp);
			
			return queryStCardNumResp;
			
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-QueryPFStCardNum解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-QueryPFStCardNum解析失败！");
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-QueryPFStCardNum解析失败！");
		}
	}

	@Override
	public void covertToEBody(QueryPFStCardNumResponse queryStCardNumResp, LEASING019Response leasing019Response, String key) throws ESBException {
		if(queryStCardNumResp.getStatus()!=null && "Y".equals(queryStCardNumResp.getStatus())) {
			leasing019Response.setResult_code("SUCCESS");
		} else {
			leasing019Response.setResult_code("FAILED");
		}
		leasing019Response.setResult_desc(queryStCardNumResp.getMessage());
		leasing019Response.setSpdb_stCard_no(queryStCardNumResp.getStcardnum());
		
		try {
			leasing019Response.setSign(Security.getSign(leasing019Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}

	@Override
	public void resultException(LEASING019Response leasing019Response, String key, String code, String msg) {
		try {
			leasing019Response.setResult_code(code);
			leasing019Response.setResult_desc(msg);
			leasing019Response.setSign(Security.getSign(leasing019Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
		
	}

}
