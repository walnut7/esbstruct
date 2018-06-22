package com.kpleasing.esb.leasing.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.leasing014.BuyoutRequest;
import com.kpleasing.esb.model.leasing014.BuyoutResponse;
import com.kpleasing.esb.model.leasing014.LEASING014Request;
import com.kpleasing.esb.model.leasing014.LEASING014Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryBuyoutAmountProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(QueryBuyoutAmountProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING014Request leasRequest = getLEASING014RequestObject((byte[]) in.getBody());
		
		LEASING014Response respBean = generateLeasing014Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING014响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
		
	}
	
	
	/**
	 * 创建ESB响应实体Bean
	 * 
	 * @param leasing014Request
	 * @return
	 */
	private LEASING014Response generateLeasing014Response(LEASING014Request leasing014Request) {
		LEASING014Response leasing014Resp = new LEASING014Response();
		leasing014Resp.setReq_serial_no(leasing014Request.getReq_serial_no());
		leasing014Resp.setReq_date(leasing014Request.getReq_date());
		leasing014Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing014Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing014Resp.setReturn_code("SUCCESS");
		leasing014Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing014Request.getParamConfig();
		BuyoutRequest buyoutRequest = getDestRequestBean(leasing014Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(buyoutRequest), HttpHelper.SOAP);
			
			// 解析报文信息
			BuyoutResponse buyoutResponse = parseQueryOrderInfoResponse(result);
			
			// 获取配置终端签名KEY
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing014Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing014Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			setResponseBody(leasing014Resp, buyoutResponse, key);
			
		} catch (ESBException e) {
			leasing014Resp.setResult_code(e.getCode());
			leasing014Resp.setResult_desc(e.getDescription());
		} catch (InvocationTargetException | DocumentException e) {
			leasing014Resp.setResult_code("FAILED");
			leasing014Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing014Resp.setResult_code("FAILED");
			leasing014Resp.setResult_desc("签名失败");
			leasing014Resp.setSign("");
		} 
		return leasing014Resp;
	}
	
	
	/**
	 * 创建目标系统实体对象Bean
	 * 
	 * @param leasing014Request
	 * @param param
	 * @return
	 */
	private BuyoutRequest getDestRequestBean(LEASING014Request leasing014Request, ParameterConfig param) {
		BuyoutRequest buyoutRequest = new BuyoutRequest();
		buyoutRequest.setRequestNum(StringUtil.getDateSerialNo6());
		buyoutRequest.setSyscode(param.getAppCode());
		buyoutRequest.setSyspwd(param.getAppSecurity());
		buyoutRequest.setApplyno(leasing014Request.getApplyno());
		
		logger.info("Sign Before MD5："+buyoutRequest.getSignContent() + param.getSignKey());
		String sign = Security.MD5Encode(buyoutRequest.getSignContent() + param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		buyoutRequest.setSign(sign);
		
		return buyoutRequest;
	}
	
	
	/**
	 * 生成目标系统请求报文
	 * 
	 * @param buyoutRequest
	 * @return
	 */
	private String getRequestXml(BuyoutRequest buyoutRequest) {
		StringBuilder sendXml = new StringBuilder();
		sendXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(buyoutRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(buyoutRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(buyoutRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:applyno>").append(buyoutRequest.getApplyno()).append("</sch:applyno>")
		.append("<sch:sign>").append(buyoutRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sendXml.toString();
	}


	/**
	 * 解析目标系统响应报文
	 * 
	 * @param resultXml
	 * @return
	 * @throws DocumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private BuyoutResponse parseQueryOrderInfoResponse(String resultXml) throws DocumentException, IllegalAccessException, InvocationTargetException {
		BuyoutResponse buyoutResponse = new BuyoutResponse();

		Map<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("ns", "http://www.aurora-framework.org/schema");
		
		// 结果状态
		String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
		String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
		buyoutResponse.setStatus(status);
		buyoutResponse.setMessage(message);
		
		// 项目信息解析
		XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:BUYOUT_INFO/ns:record", buyoutResponse);
		
		return buyoutResponse;
	}

	
	/**
	 * 生成ESB响应实体对象Bean
	 * 
	 * @param leasing014Resp
	 * @param buyoutResponse
	 * @param key
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void setResponseBody(LEASING014Response leasing014Resp, BuyoutResponse buyoutResponse,
			String key) throws IllegalArgumentException, IllegalAccessException {
		if(buyoutResponse.getStatus()!=null && "Y".equals(buyoutResponse.getStatus())) {
			leasing014Resp.setResult_code("SUCCESS");
		} else {
			leasing014Resp.setResult_code("FAILED");
		}
		leasing014Resp.setResult_desc(buyoutResponse.getMessage());
		leasing014Resp.setByout_total_amount(buyoutResponse.getEt_amount());
		leasing014Resp.setByout_amount(buyoutResponse.getBuyout_amount());
		leasing014Resp.setLast_rental(buyoutResponse.getLast_rental());
		leasing014Resp.setLast_penalty(buyoutResponse.getLast_penalty());
		leasing014Resp.setService_amount(buyoutResponse.getCharge_amount());
		leasing014Resp.setDeposit_amount(buyoutResponse.getDeposit_amount());
		
		leasing014Resp.setSign(Security.getSign(leasing014Resp, key));
	}


	/**
	 * 生成ESB响应XML报文
	 * 
	 * @param respBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(LEASING014Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
		respXml.append("</body></esb>");
		
		logger.info("ESB响应报文明文"+respXml.toString());
		return respXml.toString();
	}
	
	
	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	private LEASING014Request getLEASING014RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING014Request leasing014Request = (LEASING014Request) ois.readObject();

			return leasing014Request;

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
