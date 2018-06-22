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
import com.kpleasing.esb.model.leasing011.EarlyRepaymentRequest;
import com.kpleasing.esb.model.leasing011.EarlyRepaymentResponse;
import com.kpleasing.esb.model.leasing011.LEASING011Request;
import com.kpleasing.esb.model.leasing011.LEASING011Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryEarlyRepaymentAmountProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(QueryEarlyRepaymentAmountProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING011Request leasRequest = getLEASING011RequestObject((byte[]) in.getBody());
		
		LEASING011Response respBean = generateLeasing011Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING011响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}


	/**
	 *  创建响应实体
	 * @param leasing011Request
	 * @return
	 */
	private LEASING011Response generateLeasing011Response(LEASING011Request leasing011Request) {
		LEASING011Response leasing011Resp = new LEASING011Response();
		leasing011Resp.setReq_serial_no(leasing011Request.getReq_serial_no());
		leasing011Resp.setReq_date(leasing011Request.getReq_date());
		leasing011Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing011Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing011Resp.setReturn_code("SUCCESS");
		leasing011Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing011Request.getParamConfig();
		EarlyRepaymentRequest earlyRepaymentRequest = getDestRequestBean(leasing011Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(earlyRepaymentRequest), HttpHelper.SOAP);
			
			// 解析报文信息
			EarlyRepaymentResponse earlyRepaymentResponse = parseQueryOrderInfoResponse(result);
			
			// 获取配置终端签名KEY
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams ) {
				if(leasing011Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing011Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			setResponseBody(leasing011Resp, earlyRepaymentResponse, key);
			
		} catch (ESBException e) {
			leasing011Resp.setResult_code(e.getCode());
			leasing011Resp.setResult_desc(e.getDescription());
		} catch (InvocationTargetException | DocumentException e) {
			leasing011Resp.setResult_code("FAILED");
			leasing011Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing011Resp.setResult_code("FAILED");
			leasing011Resp.setResult_desc("签名失败");
			leasing011Resp.setSign("");
		} 
		return leasing011Resp;
	}
	
	
	/**
	 * 创建响应实体
	 * @param leasing011Resp
	 * @param advanceClearanceResponse
	 * @param key
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setResponseBody(LEASING011Response leasing011Resp, EarlyRepaymentResponse earlyRepaymentResponse,
			String key) throws IllegalArgumentException, IllegalAccessException {
		if(earlyRepaymentResponse.getStatus()!=null && "Y".equals(earlyRepaymentResponse.getStatus())) {
			leasing011Resp.setResult_code("SUCCESS");
		} else {
			leasing011Resp.setResult_code("FAILED");
		}
		leasing011Resp.setResult_desc(earlyRepaymentResponse.getMessage());
		leasing011Resp.setLast_total_amount(earlyRepaymentResponse.getEt_amount());
		leasing011Resp.setLast_principal(earlyRepaymentResponse.getLast_principal());
		leasing011Resp.setLast_rental(earlyRepaymentResponse.getLast_rental());
		leasing011Resp.setLast_penalty(earlyRepaymentResponse.getLast_penalty());
		leasing011Resp.setService_amount(earlyRepaymentResponse.getCharge_amount());
		leasing011Resp.setDeposit_amount(earlyRepaymentResponse.getDeposit_amount());
		leasing011Resp.setOther_amount(earlyRepaymentResponse.getOther_amount());
		
		leasing011Resp.setSign(Security.getSign(leasing011Resp, key));
	}


	/**
	 * 解析响应报文
	 * @param resultXml
	 * @return
	 * @throws DocumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private EarlyRepaymentResponse parseQueryOrderInfoResponse(String resultXml) throws DocumentException, IllegalAccessException, InvocationTargetException {
		EarlyRepaymentResponse earlyRepaymentResponse = new EarlyRepaymentResponse();

		Map<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("ns", "http://www.aurora-framework.org/schema");
		
		// 结果状态
		String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
		String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
		earlyRepaymentResponse.setStatus(status);
		earlyRepaymentResponse.setMessage(message);
		
		// 项目信息解析
		XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:ET_RESULT/ns:record", earlyRepaymentResponse);
		
		return earlyRepaymentResponse;
	}


	/**
	 * 生成响应报文
	 * @param advanceClearanceRequest
	 * @return
	 */
	private String getRequestXml(EarlyRepaymentRequest earlyRepaymentRequest) {
		StringBuilder sendXml = new StringBuilder();
		sendXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(earlyRepaymentRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(earlyRepaymentRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(earlyRepaymentRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:applyno>").append(earlyRepaymentRequest.getApplyno()).append("</sch:applyno>")
//		.append("<sch:et_date>").append(advanceClearanceRequest.getEt_date()).append("</sch:et_date>")
		.append("<sch:sign>").append(earlyRepaymentRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sendXml.toString();
	}


	/**
	 * 封装请求实体Bean
	 * @param leasing011Request
	 * @param param
	 * @return
	 */
	private EarlyRepaymentRequest getDestRequestBean(LEASING011Request leasing011Request,
			ParameterConfig param) {
		EarlyRepaymentRequest earlyRequestRequest = new EarlyRepaymentRequest();
		earlyRequestRequest.setRequestNum(StringUtil.getDateSerialNo6());
		earlyRequestRequest.setSyscode(param.getAppCode());
		earlyRequestRequest.setSyspwd(param.getAppSecurity());
		earlyRequestRequest.setApplyno(leasing011Request.getApplyno());
		
		logger.info("Sign Before MD5："+earlyRequestRequest.getSignContent() + param.getSignKey());
		String sign = Security.MD5Encode(earlyRequestRequest.getSignContent() + param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		earlyRequestRequest.setSign(sign);
		
		return earlyRequestRequest;
	}



	/**
	 * 生成ESB响应XML报文
	 * @param respBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(LEASING011Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING011Request getLEASING011RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING011Request leasing011Request = (LEASING011Request) ois.readObject();

			return leasing011Request;

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
