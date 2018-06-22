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
import com.kpleasing.esb.model.leasing013.LEASING013Repay;
import com.kpleasing.esb.model.leasing013.LEASING013Request;
import com.kpleasing.esb.model.leasing013.LEASING013Response;
import com.kpleasing.esb.model.leasing013.RepayOrder;
import com.kpleasing.esb.model.leasing013.RepayOrderRequest;
import com.kpleasing.esb.model.leasing013.RepayOrderResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryRepayOrderProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(QueryRepayOrderProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING013Request leasing013Request = getLEASING013RequestObject((byte[]) in.getBody());
		
		LEASING013Response respBean = generateLeasing013Response(leasing013Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING013响应报文密文：" + msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	
	/**
	 * 生成响应实体
	 * @param leasing013Request
	 * @return
	 */
	private LEASING013Response generateLeasing013Response(LEASING013Request leasing013Request) {
		LEASING013Response leasing013Resp = new LEASING013Response();
		leasing013Resp.setReq_serial_no(leasing013Request.getReq_serial_no());
		leasing013Resp.setReq_date(leasing013Request.getReq_date());
		leasing013Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing013Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing013Resp.setReturn_code("SUCCESS");
		leasing013Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing013Request.getParamConfig();
		RepayOrderRequest repayOrderRequest = getDestRequestBean(leasing013Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(repayOrderRequest), HttpHelper.SOAP);
			
			// 解析报文信息
			RepayOrderResponse repayOrderResponse = parseQueryOrderInfoResponse(result);
			
			// 获取配置终端签名KEY
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing013Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing013Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			setResponseBody(leasing013Resp, repayOrderResponse, key);
			
		} catch (ESBException e) {
			leasing013Resp.setResult_code(e.getCode());
			leasing013Resp.setResult_desc(e.getDescription());
		} catch (InvocationTargetException | DocumentException e) {
			leasing013Resp.setResult_code("FAILED");
			leasing013Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing013Resp.setResult_code("FAILED");
			leasing013Resp.setResult_desc("签名失败");
			leasing013Resp.setSign("");
		} 
		return leasing013Resp;
	}

	
	
	/**
	 * ESB 报文响应实体
	 * @param leasing013Resp
	 * @param repayOrderResponse
	 * @param key
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void setResponseBody(LEASING013Response leasing013Resp, RepayOrderResponse repayOrderResponse, String key) throws IllegalArgumentException, IllegalAccessException {
		if(repayOrderResponse.getStatus()!=null && "Y".equals(repayOrderResponse.getStatus())) {
			leasing013Resp.setResult_code("SUCCESS");
		} else {
			leasing013Resp.setResult_code("FAILED");
		}
		leasing013Resp.setResult_desc(repayOrderResponse.getMessage());
		
		List<RepayOrder> repayOrderList = repayOrderResponse.getRepayOrders();
		List<LEASING013Repay> leasing013RepayList = new ArrayList<LEASING013Repay>();
		for(RepayOrder repayOrder : repayOrderList) {
			LEASING013Repay leasing013Repay = new LEASING013Repay();
			leasing013Repay.setApplyno(repayOrder.getApplyno());
			leasing013Repay.setItem(repayOrder.getTimes());
			leasing013Repay.setRepay_amt(repayOrder.getDue_amount());
			leasing013Repay.setPrincipal(repayOrder.getPrincipal());
			leasing013Repay.setInterest(repayOrder.getInterest());
			leasing013Repay.setCust_id(repayOrder.getCust_id());
			leasing013Repay.setCust_name(repayOrder.getCust_name());
			leasing013Repay.setPhone(repayOrder.getCell_phone());
			
			leasing013RepayList.add(leasing013Repay);
		}
		leasing013Resp.setRepay_list(leasing013RepayList);
		
		leasing013Resp.setSign(Security.getSign(leasing013Resp, key));
	}


	/**
	 * 解析响应报文
	 * @param resultXml
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws DocumentException 
	 */
	private RepayOrderResponse parseQueryOrderInfoResponse(String resultXml) throws IllegalAccessException, InvocationTargetException, DocumentException {
		RepayOrderResponse repayOrderResponse = new RepayOrderResponse();

		Map<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("ns", "http://www.aurora-framework.org/schema");
		
		// 结果状态
		String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
		String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
		repayOrderResponse.setStatus(status);
		repayOrderResponse.setMessage(message);
		
		// 还款计划解析
		List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, nsMap, "//ns:REPAY_LIST/ns:record");
		List<RepayOrder> beanList = new ArrayList<RepayOrder>();
		for(Map<String, String> map : mapList) {
			RepayOrder repay = new RepayOrder();
			BeanUtils.populate(repay, map);
			beanList.add(repay);
		}
		repayOrderResponse.setRepayOrders(beanList);

		return repayOrderResponse;
	}



	/**
	 * 获得请求XML
	 * @param repayOrderRequest
	 * @return
	 */
	private String getRequestXml(RepayOrderRequest repayOrderRequest) {
		StringBuilder sendXml = new StringBuilder();
		sendXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(repayOrderRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(repayOrderRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(repayOrderRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:due_date>").append(repayOrderRequest.getDue_date()).append("</sch:due_date>")
		.append("<sch:sign>").append(repayOrderRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sendXml.toString();
	}

	
	/**
	 * 封装请求实体Bean
	 * @param leasing013Request
	 * @param param
	 * @return
	 */
	private RepayOrderRequest getDestRequestBean(LEASING013Request leasing013Request,
			ParameterConfig param) {
		RepayOrderRequest repayOrderRequest = new RepayOrderRequest();
		repayOrderRequest.setRequestNum(StringUtil.getDateSerialNo6());
		repayOrderRequest.setSyscode(param.getAppCode());
		repayOrderRequest.setSyspwd(param.getAppSecurity());
		repayOrderRequest.setDue_date(leasing013Request.getRepay_date());
		
		logger.info("Sign Before MD5："+repayOrderRequest.getSignContent() + param.getSignKey());
		String sign = Security.MD5Encode(repayOrderRequest.getSignContent() + param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		repayOrderRequest.setSign(sign);
		
		return repayOrderRequest;
	}


	/**
	 * 生成响应报文
	 * @param respBean
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String generateMsgResponseXML(LEASING013Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
		
		List<LEASING013Repay> repayList = respBean.getRepay_list();
		if(null == repayList || repayList.isEmpty()) {
			respXml.append("<repay_list/>");
		} else {
			respXml.append("<repay_list>");
			for(LEASING013Repay leasingRepay : repayList) {
				respXml.append("<repay>").append(XMLHelper.getXMLFromBean(leasingRepay)).append("</repay>");
			}
			respXml.append("</repay_list>");
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
	private LEASING013Request getLEASING013RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING013Request leasing013Request = (LEASING013Request) ois.readObject();

			return leasing013Request;

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
