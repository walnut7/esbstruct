package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing006.LEASING006Repay;
import com.kpleasing.esb.model.leasing006.LEASING006Request;
import com.kpleasing.esb.model.leasing006.LEASING006Response;
import com.kpleasing.esb.model.leasing006.QueryOrderInfoRequest;
import com.kpleasing.esb.model.leasing006.QueryOrderInfoResponse;
import com.kpleasing.esb.model.leasing006.QueryOrderInfoRepay;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryOrderInfoProcess extends AbstractLeasingProcessor<LEASING006Request, LEASING006Response, QueryOrderInfoRequest, QueryOrderInfoResponse>  {
	
	private static Logger logger = Logger.getLogger(QueryOrderInfoProcess.class);
	
	@Override
	public LEASING006Response initResponse() {
		return new LEASING006Response();
	}


	@Override
	public QueryOrderInfoRequest generateRequestEntity(LEASING006Request leasing006Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		QueryOrderInfoRequest queryOrderInfoRequest = new QueryOrderInfoRequest();
		queryOrderInfoRequest.setRequestNum(StringUtil.getDateSerialNo6());
		queryOrderInfoRequest.setSyscode(config.getAppCode());
		queryOrderInfoRequest.setSyspwd(config.getAppSecurity());
		queryOrderInfoRequest.setPhone(leasing006Request.getPhone());
		queryOrderInfoRequest.setCertType(leasing006Request.getCert_type());
		queryOrderInfoRequest.setCertCode(leasing006Request.getCert_code());
		queryOrderInfoRequest.setApplyno(leasing006Request.getApplyno());
		
		logger.info("Sign Before MD5："+queryOrderInfoRequest.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(queryOrderInfoRequest.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		queryOrderInfoRequest.setSign(sign);
		
		return queryOrderInfoRequest;
	}


	@Override
	public String generateRequestXML(QueryOrderInfoRequest queryOrderInfoRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(queryOrderInfoRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(queryOrderInfoRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(queryOrderInfoRequest.getSyspwd()).append("</sch:password>");
		if(queryOrderInfoRequest.getCertType()!=null && !"null".equals(queryOrderInfoRequest.getCertType())) {
			reqXml.append("<sch:id_card_type>").append(queryOrderInfoRequest.getCertType()).append("</sch:id_card_type>");
		}
		if(queryOrderInfoRequest.getCertCode()!=null && !"null".equals(queryOrderInfoRequest.getCertCode())) {
			reqXml.append("<sch:id_card_no>").append(queryOrderInfoRequest.getCertCode()).append("</sch:id_card_no>");
		}
		if(queryOrderInfoRequest.getPhone()!=null && !"null".equals(queryOrderInfoRequest.getPhone())) {
			reqXml.append("<sch:mobile>").append(queryOrderInfoRequest.getPhone()).append("</sch:mobile>");
		}
		if(queryOrderInfoRequest.getApplyno()!=null && !"null".equals(queryOrderInfoRequest.getApplyno())) {
			reqXml.append("<sch:applyno>").append(queryOrderInfoRequest.getApplyno()).append("</sch:applyno>");
		}
		reqXml.append("<sch:sign>").append(queryOrderInfoRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return reqXml.toString();
	}


	@Override
	public QueryOrderInfoResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			QueryOrderInfoResponse queryOrderInfoResponse = new QueryOrderInfoResponse();
	
			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 结果状态
			String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
			
			String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
			queryOrderInfoResponse.setStatus(status);
			queryOrderInfoResponse.setMessage(message);
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:PROJECT_INFO/ns:record", queryOrderInfoResponse);
			
			// 还款计划解析
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, nsMap, "//ns:REPAY_PLAN_LIST/ns:record");
			List<QueryOrderInfoRepay> beanList = new ArrayList<QueryOrderInfoRepay>();
			for(Map<String, String> map : mapList) {
				QueryOrderInfoRepay repay = new QueryOrderInfoRepay();
				BeanUtils.populate(repay, map);
				beanList.add(repay);
			}
			queryOrderInfoResponse.setRepayList(beanList);

			return queryOrderInfoResponse;
			
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
	public void covertToEBody(QueryOrderInfoResponse queryOrderInfoResponse, LEASING006Response leasing006Response, String key) throws ESBException {
		if(queryOrderInfoResponse.getStatus()!=null && "Y".equals(queryOrderInfoResponse.getStatus())) {
			leasing006Response.setResult_code("SUCCESS");
		} else {
			leasing006Response.setResult_code("FAILED");
		}
		leasing006Response.setResult_desc(queryOrderInfoResponse.getMessage());
		leasing006Response.setDeposit(queryOrderInfoResponse.getDeposit());
		leasing006Response.setStatus(queryOrderInfoResponse.getDocument_status());
		leasing006Response.setApplyno(queryOrderInfoResponse.getApplyno());
		leasing006Response.setFinance_id(queryOrderInfoResponse.getPlan_session_id());
		leasing006Response.setBrand(queryOrderInfoResponse.getBrand_desc());
		leasing006Response.setSeries(queryOrderInfoResponse.getSeries_desc());
		leasing006Response.setModel(queryOrderInfoResponse.getModel_desc());
		leasing006Response.setSku_desc(queryOrderInfoResponse.getSku());
		leasing006Response.setDown_payment(queryOrderInfoResponse.getDown_payment());
		leasing006Response.setDeposit(queryOrderInfoResponse.getDeposit());
		leasing006Response.setFinance_amount(queryOrderInfoResponse.getFinance_amount());
		leasing006Response.setTotal_term(queryOrderInfoResponse.getLoantenor());
		leasing006Response.setRepay_date(queryOrderInfoResponse.getRepay_day());	
		leasing006Response.setTotal_principal(queryOrderInfoResponse.getTotal_principal());
		leasing006Response.setTotal_interest(queryOrderInfoResponse.getTotal_interest());
		leasing006Response.setReceived_principal(queryOrderInfoResponse.getReceived_principal());
		leasing006Response.setLast_interest(queryOrderInfoResponse.getLast_interest());
		
		List<QueryOrderInfoRepay> repays = queryOrderInfoResponse.getRepayList();
		List<LEASING006Repay> leasing006RepayList = new ArrayList<LEASING006Repay>();
		for(QueryOrderInfoRepay repay : repays) {
			LEASING006Repay leasing006Repay = new LEASING006Repay();
			leasing006Repay.setTerm(repay.getTimes());
			leasing006Repay.setDue_date(repay.getDue_date());
			leasing006Repay.setDue_amount(repay.getDue_amount());
			leasing006Repay.setReceive_amount(repay.getReceived_amount());
			leasing006Repay.setReceive_status(repay.getWrite_off_flag());
			leasing006RepayList.add(leasing006Repay);
		}
		leasing006Response.setRepays(leasing006RepayList);
		
		try {
			leasing006Response.setSign(Security.getSign(leasing006Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING006Response leasing006Response, String key, String code, String msg) {
		try {
			leasing006Response.setResult_code(code);
			leasing006Response.setResult_desc(msg);
			leasing006Response.setSign(Security.getSign(leasing006Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
	
	
	@Override
	public String generateResponseXML(LEASING006Response leasing006Response) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder response = new StringBuilder();
		response.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(leasing006Response.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(leasing006Response.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(leasing006Response.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(leasing006Response.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(leasing006Response.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(leasing006Response.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(leasing006Response.getSign()).append("]]></sign>")
		.append("</head><body>");
		response.append(XMLHelper.getXMLFromBean(leasing006Response));
		
		List<LEASING006Repay> leasingRepays = leasing006Response.getRepays();
		if(null == leasingRepays || leasingRepays.isEmpty()) {
			response.append("<repays/>");
		} else {
			response.append("<repays>");
			for(LEASING006Repay leasingRepay : leasingRepays) {
				response.append("<repay>").append(XMLHelper.getXMLFromBean(leasingRepay)).append("</repay>");
			}
			response.append("</repays>");
		}
		response.append("</body></esb>");
		
		logger.info("ESB响应报文明文"+response.toString());
		return response.toString();
	}
}
