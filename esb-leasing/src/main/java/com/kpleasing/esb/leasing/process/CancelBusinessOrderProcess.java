package com.kpleasing.esb.leasing.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import com.kpleasing.esb.model.leasing008.CancelBusinessOrderRequest;
import com.kpleasing.esb.model.leasing008.LEASING008Request;
import com.kpleasing.esb.model.leasing008.LEASING008Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class CancelBusinessOrderProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(CancelBusinessOrderProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING008Request leasing008Request = getLEASING008RequestObject((byte[]) in.getBody());
		
		LEASING008Response respBean = generateLeasing008Response(leasing008Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING008响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}
	
	
	/**
	 * 生成响应BEAN
	 * @param leasing008Request
	 * @return
	 */
	private LEASING008Response generateLeasing008Response(LEASING008Request leasing008Request) {
		LEASING008Response leasing008Response = new LEASING008Response();
		leasing008Response.setReq_serial_no(leasing008Request.getReq_serial_no());
		leasing008Response.setReq_date(leasing008Request.getReq_date());
		leasing008Response.setRes_serial_no(StringUtil.getSerialNo32());
		leasing008Response.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing008Response.setReturn_code("SUCCESS");
		leasing008Response.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing008Request.getParamConfig();
		CancelBusinessOrderRequest cancelOrderRequest = getDestRequestBean(leasing008Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(cancelOrderRequest), HttpHelper.SOAP);
			Map<String, String> map = XMLHelper.getMapFromXML(result);
			if(map.get("status")!=null && "Y".equals((String)map.get("status"))) {
				leasing008Response.setResult_code("SUCCESS");
			} else {
				leasing008Response.setResult_code("FAILED");
			}
			leasing008Response.setResult_desc((String)map.get("message"));
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing008Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing008Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			leasing008Response.setSign(Security.getSign(leasing008Response, key));
		} catch (ESBException e) {
			leasing008Response.setResult_code(e.getCode());
			leasing008Response.setResult_desc(e.getDescription());
		} catch (DocumentException e) {
			leasing008Response.setResult_code("FAILED");
			leasing008Response.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing008Response.setResult_code("FAILED");
			leasing008Response.setResult_desc("签名失败");
			leasing008Response.setSign("");
		}
		return leasing008Response;
	}


	/**
	 * 生成业务处理对象
	 * @param leasing008Request
	 * @param param
	 * @return
	 */
	private CancelBusinessOrderRequest getDestRequestBean(LEASING008Request leasing008Request, ParameterConfig param) {
		CancelBusinessOrderRequest cancelOrderRequest = new CancelBusinessOrderRequest();
		cancelOrderRequest.setRequestNum(StringUtil.getDateSerialNo6());
		cancelOrderRequest.setSyscode(param.getAppCode());
		cancelOrderRequest.setSyspwd(param.getAppSecurity());
		cancelOrderRequest.setApplyno(leasing008Request.getApplyno());
		cancelOrderRequest.setReason(leasing008Request.getReason());

		logger.info("Sign Before MD5："+cancelOrderRequest.getSignContent()+param.getSignKey());
		String sign = Security.MD5Encode(cancelOrderRequest.getSignContent()+param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		cancelOrderRequest.setSign(sign);
		
		return cancelOrderRequest;
	}
	
	
	/**
	 * 封装业务处理报文
	 * @param cancelOrderRequest
	 * @return
	 */
	private String getRequestXml(CancelBusinessOrderRequest cancelOrderRequest) {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(cancelOrderRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(cancelOrderRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(cancelOrderRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:applyno>").append(cancelOrderRequest.getApplyno()).append("</sch:applyno>")
		.append("<sch:cancel_reason>").append(cancelOrderRequest.getReason()).append("</sch:cancel_reason>")
		.append("<sch:sign>").append(cancelOrderRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return reqXml.toString();
	}


	/**
	 * 返回结果
	 * @param respBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(LEASING008Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING008Request getLEASING008RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING008Request leasing008Request = (LEASING008Request) ois.readObject();

			return leasing008Request;

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
