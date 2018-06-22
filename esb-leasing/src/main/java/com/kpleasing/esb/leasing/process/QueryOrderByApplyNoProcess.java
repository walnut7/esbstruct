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
import com.kpleasing.esb.model.leasing010.LEASING010Request;
import com.kpleasing.esb.model.leasing010.LEASING010Response;
import com.kpleasing.esb.model.leasing010.LEASINGRepaid;
import com.kpleasing.esb.model.leasing010.LEASINGRepay;
import com.kpleasing.esb.model.leasing010.QueryOrderByApplyNoRequest;
import com.kpleasing.esb.model.leasing010.QueryOrderByApplyNoResponse;
import com.kpleasing.esb.model.leasing010.Repaid;
import com.kpleasing.esb.model.leasing010.Repay;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryOrderByApplyNoProcess extends AbstractLeasingProcessor<LEASING010Request, LEASING010Response, QueryOrderByApplyNoRequest, QueryOrderByApplyNoResponse>  {
	
	private static Logger logger = Logger.getLogger(QueryOrderByApplyNoProcess.class);

	@Override
	public LEASING010Response initResponse() {
		return new LEASING010Response();
	}


	@Override
	public QueryOrderByApplyNoRequest generateRequestEntity(LEASING010Request leasing010Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		QueryOrderByApplyNoRequest queryOrderByApplayRequest = new QueryOrderByApplyNoRequest();
		queryOrderByApplayRequest.setRequestNum(StringUtil.getDateSerialNo6());
		queryOrderByApplayRequest.setSyscode(config.getAppCode());
		queryOrderByApplayRequest.setSyspwd(config.getAppSecurity());
		queryOrderByApplayRequest.setApplyno(leasing010Request.getApplyno());
		
		logger.info("Sign Before MD5："+queryOrderByApplayRequest.getSignContent() + config.getSignKey());
		String sign = Security.MD5Encode(queryOrderByApplayRequest.getSignContent() + config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		queryOrderByApplayRequest.setSign(sign);
		
		return queryOrderByApplayRequest;
	}


	@Override
	public String generateRequestXML(QueryOrderByApplyNoRequest queryOrderByApplayRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(queryOrderByApplayRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(queryOrderByApplayRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(queryOrderByApplayRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:applyno>").append(queryOrderByApplayRequest.getApplyno()).append("</sch:applyno>")
		.append("<sch:sign>").append(queryOrderByApplayRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return reqXml.toString();
	}


	@Override
	public QueryOrderByApplyNoResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			QueryOrderByApplyNoResponse queryOrderByApplayResponse = new QueryOrderByApplyNoResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 结果状态
			String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
			String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
			queryOrderByApplayResponse.setStatus(status);
			queryOrderByApplayResponse.setMessage(message);
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:PROJECT_INFO/ns:record", queryOrderByApplayResponse);
			
			// 还款计划解析
			List<Map<String, String>> mapRepayList = XMLHelper.parseMultNodesXml(resultXml, nsMap, "//ns:REPAY_PLAN_LIST/ns:record");
			List<Repay> repays = new ArrayList<Repay>();
			for(Map<String, String> map : mapRepayList) {
				Repay repay = new Repay();
				BeanUtils.populate(repay, map);
				repays.add(repay);
			}
			queryOrderByApplayResponse.setRepays(repays);
			
			// 实际还款清单
			List<Map<String, String>> mapRepaidList = XMLHelper.parseMultNodesXml(resultXml, nsMap, "//ns:WRITE_OFF_LIST/ns:record");
			List<Repaid> repaids = new ArrayList<Repaid>();
			for(Map<String, String> map : mapRepaidList) {
				Repaid repaid = new Repaid();
				BeanUtils.populate(repaid, map);
				repaids.add(repaid);
			}
			queryOrderByApplayResponse.setRepaids(repaids);
			
			return queryOrderByApplayResponse;
			
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
	public void covertToEBody(QueryOrderByApplyNoResponse queryOrderByApplayResponse, LEASING010Response leasing010Response, String key) throws ESBException {
		if(queryOrderByApplayResponse.getStatus()!=null && "Y".equals(queryOrderByApplayResponse.getStatus())) {
			leasing010Response.setResult_code("SUCCESS");
		} else {
			leasing010Response.setResult_code("FAILED");
		}
		leasing010Response.setResult_desc(queryOrderByApplayResponse.getMessage());
		leasing010Response.setStatus(queryOrderByApplayResponse.getDocument_status());
		leasing010Response.setApplyno(queryOrderByApplayResponse.getApplyno());
		leasing010Response.setFinance_id(queryOrderByApplayResponse.getPlan_session_id());
		leasing010Response.setLease_channel(queryOrderByApplayResponse.getLease_channel());
		leasing010Response.setBrand(queryOrderByApplayResponse.getBrand_desc());
		leasing010Response.setSeries(queryOrderByApplayResponse.getSeries_desc());
		leasing010Response.setModel(queryOrderByApplayResponse.getModel_desc());
		leasing010Response.setSku_desc(queryOrderByApplayResponse.getSku());
		leasing010Response.setEngin_number(queryOrderByApplayResponse.getEngine());
		leasing010Response.setSerial_number(queryOrderByApplayResponse.getSerial_number());
		leasing010Response.setOverdue_status(queryOrderByApplayResponse.getOverdue_status());
		leasing010Response.setDown_payment(queryOrderByApplayResponse.getDown_payment());
		leasing010Response.setDeposit(queryOrderByApplayResponse.getDeposit());
		leasing010Response.setFinance_amount(queryOrderByApplayResponse.getFinance_amount());
		leasing010Response.setTotal_term(queryOrderByApplayResponse.getLoantenor());
		leasing010Response.setRepay_date(queryOrderByApplayResponse.getRepay_day());
		leasing010Response.setLease_start_date(queryOrderByApplayResponse.getLease_start_date());
		leasing010Response.setTotal_principal(queryOrderByApplayResponse.getTotal_principal());
		leasing010Response.setTotal_interest(queryOrderByApplayResponse.getTotal_interest());
		leasing010Response.setReceived_principal(queryOrderByApplayResponse.getReceived_principal());
		leasing010Response.setLast_interest(queryOrderByApplayResponse.getLast_interest());
		
		if(null != queryOrderByApplayResponse.getUser_pf_capital_flag() && "Y".equals(queryOrderByApplayResponse.getUser_pf_capital_flag())) {
			leasing010Response.setFinance_type("1");   // 浦发资金渠道
		} else {
			leasing010Response.setFinance_type("0");   // 自有资金渠道
		}
		
		List<Repay> repays = queryOrderByApplayResponse.getRepays();
		List<LEASINGRepay> leasingRepays = new ArrayList<LEASINGRepay>();
		for(Repay repay : repays) {
			LEASINGRepay leasingRepay = new LEASINGRepay();
			leasingRepay.setTerm(repay.getTimes());
			leasingRepay.setDue_date(repay.getDue_date());
			leasingRepay.setDue_amount(repay.getDue_amount());
			leasingRepay.setReceive_amount(repay.getReceived_amount());
			leasingRepay.setReceive_status(repay.getWrite_off_flag());
			leasingRepays.add(leasingRepay);
		}
		leasing010Response.setRepays(leasingRepays);
		
		List<Repaid> repaids = queryOrderByApplayResponse.getRepaids();
		List<LEASINGRepaid> leasingRepaids = new ArrayList<LEASINGRepaid>();
		for(Repaid repaid : repaids) {
			LEASINGRepaid leasingRepaid = new LEASINGRepaid();
			leasingRepaid.setTerm(repaid.getTimes());
			leasingRepaid.setRepaid_type(repaid.getCf_item());
			leasingRepaid.setDue_amount(repaid.getDue_amount());
			leasingRepaid.setDue_date(repaid.getDue_date());
			leasingRepaid.setRepaid_amount(repaid.getReceived_amount());
			leasingRepaid.setRepaid_date(repaid.getReceived_date());
			leasingRepaids.add(leasingRepaid);
		}
		leasing010Response.setRepaids(leasingRepaids);
		
		try {
			leasing010Response.setSign(Security.getSign(leasing010Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING010Response leasing010Response, String key, String code, String msg) {
		try {
			leasing010Response.setResult_code(code);
			leasing010Response.setResult_desc(msg);
			leasing010Response.setSign(Security.getSign(leasing010Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
	
	
	@Override
	public String generateResponseXML(LEASING010Response leasing010Response) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder response = new StringBuilder();
		response.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(leasing010Response.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(leasing010Response.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(leasing010Response.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(leasing010Response.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(leasing010Response.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(leasing010Response.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(leasing010Response.getSign()).append("]]></sign>")
		.append("</head><body>");
		response.append(XMLHelper.getXMLFromBean(leasing010Response));
		
		List<LEASINGRepay> leasingRepays = leasing010Response.getRepays();
		if(null == leasingRepays || leasingRepays.isEmpty()) {
			response.append("<repays/>");
		} else {
			response.append("<repays>");
			for(LEASINGRepay leasingRepay : leasingRepays) {
				response.append("<repay>").append(XMLHelper.getXMLFromBean(leasingRepay)).append("</repay>");
			}
			response.append("</repays>");
		}
		
		List<LEASINGRepaid> leasingRepaids = leasing010Response.getRepaids();
		if(null == leasingRepaids || leasingRepaids.isEmpty()) {
			response.append("<repaids/>");
		} else {
			response.append("<repaids>");
			for(LEASINGRepaid leasingRepaid : leasingRepaids) {
				response.append("<repaid>").append(XMLHelper.getXMLFromBean(leasingRepaid)).append("</repaid>");
			}
			response.append("</repaids>");
		}
		response.append("</body></esb>");
		
		logger.info("ESB响应报文明文"+response.toString());
		return response.toString();
	}
}