package com.kpleasing.esb.leasing.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.leasing012.LEASING012OverdueOrder;
import com.kpleasing.esb.model.leasing012.LEASING012Request;
import com.kpleasing.esb.model.leasing012.LEASING012Response;
import com.kpleasing.esb.model.leasing012.OverdueOrder;
import com.kpleasing.esb.model.leasing012.QueryOverdueOrderRequest;
import com.kpleasing.esb.model.leasing012.QueryOverdueOrderResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryOverdueOrderProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(QueryOverdueOrderProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING012Request leasing012Request = getLEASING012RequestObject((byte[]) in.getBody());
		
		LEASING012Response respBean = generateLeasing012Response(leasing012Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING012响应报文密文：" + msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	/**
	 * 生成响应实体
	 * @param leasing006Request
	 * @return
	 */
	private LEASING012Response generateLeasing012Response(LEASING012Request leasing012Request) {
		LEASING012Response leasing012Resp = new LEASING012Response();
		leasing012Resp.setReq_serial_no(leasing012Request.getReq_serial_no());
		leasing012Resp.setReq_date(leasing012Request.getReq_date());
		leasing012Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing012Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing012Resp.setReturn_code("SUCCESS");
		leasing012Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing012Request.getParamConfig();
		QueryOverdueOrderRequest overdueOrderRequest = getDestRequestBean(leasing012Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(overdueOrderRequest), HttpHelper.SOAP);
			
			// 解析报文信息
			QueryOverdueOrderResponse overdueOrderResponse = parseQueryOrderInfoResponse(result);
			
			// 获取配置终端签名KEY
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing012Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing012Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			setResponseBody(leasing012Resp, overdueOrderResponse, key);
			
		} catch (ESBException e) {
			leasing012Resp.setResult_code(e.getCode());
			leasing012Resp.setResult_desc(e.getDescription());
		} catch (InvocationTargetException | DocumentException e) {
			leasing012Resp.setResult_code("FAILED");
			leasing012Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing012Resp.setResult_code("FAILED");
			leasing012Resp.setResult_desc("签名失败");
			leasing012Resp.setSign("");
		} 
		return leasing012Resp;
	}
	
	
	/**
	 *  ESB 报文响应实体
	 * @param leasing012Resp
	 * @param queryOrderResponse
	 * @param key
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void setResponseBody(LEASING012Response leasing012Resp, QueryOverdueOrderResponse queryOrderResponse,
			String key) throws IllegalArgumentException, IllegalAccessException {
		if(queryOrderResponse.getStatus()!=null && "Y".equals(queryOrderResponse.getStatus())) {
			leasing012Resp.setResult_code("SUCCESS");
		} else {
			leasing012Resp.setResult_code("FAILED");
		}
		leasing012Resp.setResult_desc(queryOrderResponse.getMessage());
		
		List<OverdueOrder> overdueList = queryOrderResponse.getOverdueList();
		List<LEASING012OverdueOrder> leasing012DueList = new ArrayList<LEASING012OverdueOrder>();
		for(OverdueOrder overdue : overdueList) {
			LEASING012OverdueOrder leasing012DueOrder = new LEASING012OverdueOrder();
			leasing012DueOrder.setApplyno(overdue.getApplyno());
			leasing012DueOrder.setItem(overdue.getTimes());
			leasing012DueOrder.setRepay_date(overdue.getDue_date());
			leasing012DueOrder.setOverdue_amt(overdue.getDue_amount());
			leasing012DueOrder.setDays(overdue.getOverdue_days());
			leasing012DueOrder.setPenalty_amt(overdue.getPenalty_amount());
			leasing012DueOrder.setCust_id(overdue.getCust_id());
			leasing012DueOrder.setPhone(overdue.getCell_phone());
			leasing012DueOrder.setCust_name(overdue.getCust_name());
			
			leasing012DueList.add(leasing012DueOrder);
		}
		leasing012Resp.setOverdueList(leasing012DueList);
		
		leasing012Resp.setSign(Security.getSign(leasing012Resp, key));
	}


	/**
	 * 解析响应报文
	 * @param resultXml
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws DocumentException
	 */
	private QueryOverdueOrderResponse parseQueryOrderInfoResponse(String resultXml) throws IllegalAccessException, InvocationTargetException, DocumentException {
		QueryOverdueOrderResponse overdueOrderResponse = new QueryOverdueOrderResponse();

		Map<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("ns", "http://www.aurora-framework.org/schema");
		
		// 结果状态
		String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
		String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
		overdueOrderResponse.setStatus(status);
		overdueOrderResponse.setMessage(message);
		
		// 还款计划解析
		List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, nsMap, "//ns:OVERDUE_LIST/ns:record");
		List<OverdueOrder> beanList = new ArrayList<OverdueOrder>();
		for(Map<String, String> map : mapList) {
			OverdueOrder overdue = new OverdueOrder();
			BeanUtils.populate(overdue, map);
			beanList.add(overdue);
		}
		overdueOrderResponse.setOverdueList(beanList);

		return overdueOrderResponse;
	}


	/**
	 * 获得请求XML
	 * @param queryOrderRequest
	 * @return
	 */
	private String getRequestXml(QueryOverdueOrderRequest queryOrderRequest) {
		StringBuilder sendXml = new StringBuilder();
		sendXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(queryOrderRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(queryOrderRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(queryOrderRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:sign>").append(queryOrderRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sendXml.toString();
	}


	/**
	 * 封装请求实体Bean
	 * @param leasing012Request
	 * @param param
	 * @return
	 */
	private QueryOverdueOrderRequest getDestRequestBean(LEASING012Request leasing012Request, ParameterConfig param) {
		QueryOverdueOrderRequest overdueOrderRequest = new QueryOverdueOrderRequest();
		overdueOrderRequest.setRequestNum(StringUtil.getDateSerialNo6());
		overdueOrderRequest.setSyscode(param.getAppCode());
		overdueOrderRequest.setSyspwd(param.getAppSecurity());
		
		logger.info("Sign Before MD5："+overdueOrderRequest.getSignContent() + param.getSignKey());
		String sign = Security.MD5Encode(overdueOrderRequest.getSignContent() + param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		overdueOrderRequest.setSign(sign);
		
		return overdueOrderRequest;
	}


	/**
	 * 生成响应报文
	 * @param respBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(LEASING012Response respBean) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(respBean.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(respBean.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(respBean.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(respBean.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(respBean.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(respBean.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(respBean.getSign()).append("]]></sign>")
		.append("</head><body>");
		respXml.append(XMLHelper.getXMLFromBean(respBean));
		
		List<LEASING012OverdueOrder> overdueList = respBean.getOverdueList();
		if(null == overdueList || overdueList.isEmpty()) {
			respXml.append("<overdue_list/>");
		} else {
			respXml.append("<overdue_list>");
			for(LEASING012OverdueOrder leasingOverdue : overdueList) {
				respXml.append("<overdue>").append(XMLHelper.getXMLFromBean(leasingOverdue)).append("</overdue>");
			}
			respXml.append("</overdue_list>");
		}
		respXml.append("</body></esb>");
		
		logger.info("ESB响应报文明文"+respXml.toString());
		return respXml.toString();
	}

	
	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	private LEASING012Request getLEASING012RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING012Request leasing012Request = (LEASING012Request) ois.readObject();

			return leasing012Request;

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
