package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing009.LEASING009Request;
import com.kpleasing.esb.model.leasing009.LEASING009Response;
import com.kpleasing.esb.model.leasing009.QueryOrderByCustIdRequest;
import com.kpleasing.esb.model.leasing009.QueryOrderByCustIdResponse;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryOrderByCustIdProcess extends AbstractLeasingProcessor<LEASING009Request, LEASING009Response, QueryOrderByCustIdRequest, QueryOrderByCustIdResponse> {
	
	private static Logger logger = Logger.getLogger(QueryOrderByCustIdProcess.class);

	@Override
	public LEASING009Response initResponse() {
		return new LEASING009Response();
	}


	@Override
	public QueryOrderByCustIdRequest generateRequestEntity(LEASING009Request leasing009Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		QueryOrderByCustIdRequest queryOrderByCustIdRequest = new QueryOrderByCustIdRequest();
		queryOrderByCustIdRequest.setRequestNum(StringUtil.getDateSerialNo6());
		queryOrderByCustIdRequest.setSyscode(config.getAppCode());
		queryOrderByCustIdRequest.setSyspwd(config.getAppSecurity());
		queryOrderByCustIdRequest.setCust_id(leasing009Request.getCust_id());
		
		logger.info("Sign Before MD5："+queryOrderByCustIdRequest.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(queryOrderByCustIdRequest.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		queryOrderByCustIdRequest.setSign(sign);
		
		return queryOrderByCustIdRequest;
	}


	@Override
	public String generateRequestXML(QueryOrderByCustIdRequest queryOrderByCustIdRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(queryOrderByCustIdRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(queryOrderByCustIdRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(queryOrderByCustIdRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:cust_id>").append(queryOrderByCustIdRequest.getCust_id()).append("</sch:cust_id>")
		.append("<sch:sign>").append(queryOrderByCustIdRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}


	@Override
	public QueryOrderByCustIdResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			QueryOrderByCustIdResponse queryOrderByCustIdResponse = new QueryOrderByCustIdResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 结果状态
			String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
			String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
			queryOrderByCustIdResponse.setStatus(status);
			queryOrderByCustIdResponse.setMessage(message);
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:PROJECT_INFO/ns:record", queryOrderByCustIdResponse);
			
			return queryOrderByCustIdResponse;
			
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		}
	}


	@Override
	public void covertToEBody(QueryOrderByCustIdResponse queryOrderByCustIdResponse, LEASING009Response leasing009Response, String key) throws ESBException {
		if(queryOrderByCustIdResponse.getStatus()!=null && "Y".equals(queryOrderByCustIdResponse.getStatus())) {
			leasing009Response.setResult_code("SUCCESS");
		} else {
			leasing009Response.setResult_code("FAILED");
		}
		leasing009Response.setResult_desc(queryOrderByCustIdResponse.getMessage());
		leasing009Response.setDeposit(queryOrderByCustIdResponse.getDeposit());
		leasing009Response.setStatus(queryOrderByCustIdResponse.getDocument_status());
		leasing009Response.setApplyno(queryOrderByCustIdResponse.getApplyno());
		leasing009Response.setFinance_id(queryOrderByCustIdResponse.getPlan_session_id());
		leasing009Response.setBrand(queryOrderByCustIdResponse.getBrand_desc());
		leasing009Response.setSeries(queryOrderByCustIdResponse.getSeries_desc());
		leasing009Response.setModel(queryOrderByCustIdResponse.getModel_desc());
		leasing009Response.setSku_desc(queryOrderByCustIdResponse.getSku());
		leasing009Response.setDown_payment(queryOrderByCustIdResponse.getDown_payment());
		leasing009Response.setDeposit(queryOrderByCustIdResponse.getDeposit());
		leasing009Response.setFinance_amount(queryOrderByCustIdResponse.getFinance_amount());
		leasing009Response.setTotal_term(queryOrderByCustIdResponse.getLoantenor());
		leasing009Response.setTotal_principal(queryOrderByCustIdResponse.getTotal_principal());
		leasing009Response.setTotal_interest(queryOrderByCustIdResponse.getTotal_interest());
		leasing009Response.setReceived_principal(queryOrderByCustIdResponse.getReceived_principal());
		leasing009Response.setLast_interest(queryOrderByCustIdResponse.getLast_interest());
		
		if(null != queryOrderByCustIdResponse.getUser_pf_capital_flag() && "Y".equals(queryOrderByCustIdResponse.getUser_pf_capital_flag())) {
			leasing009Response.setFinance_type("1");   // 浦发资金渠道
		} else {
			leasing009Response.setFinance_type("0");   // 自有资金渠道
		}
		
		try {
			leasing009Response.setSign(Security.getSign(leasing009Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING009Response leasing009Response, String key, String code, String msg) {
		try {
			leasing009Response.setResult_code(code);
			leasing009Response.setResult_desc(msg);
			leasing009Response.setSign(Security.getSign(leasing009Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}