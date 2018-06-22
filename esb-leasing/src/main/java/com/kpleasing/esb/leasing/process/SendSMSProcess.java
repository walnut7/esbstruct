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
import com.kpleasing.esb.model.leasing001.LEASING001Request;
import com.kpleasing.esb.model.leasing001.LEASING001Response;
import com.kpleasing.esb.model.leasing001.SendSMSRequest;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SendSMSProcess implements Processor {

	private static Logger logger = Logger.getLogger(SendSMSProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception   {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING001Request leasRequest = getLEASING001RequestObject((byte[]) in.getBody());
		
		LEASING001Response respBean = generateLeasing001Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING001响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	/**
	 * 生成响应实体
	 * @param leasRequest
	 * @return
	 */
	private LEASING001Response generateLeasing001Response(LEASING001Request leasRequest) {
		LEASING001Response leasResp = new LEASING001Response();
		leasResp.setReq_serial_no(leasRequest.getReq_serial_no());
		leasResp.setReq_date(leasRequest.getReq_date());
		leasResp.setRes_serial_no(StringUtil.getSerialNo32());
		leasResp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasResp.setReturn_code("SUCCESS");
		leasResp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasRequest.getParamConfig();
		SendSMSRequest smsRequest = getDestRequestBean(leasRequest, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(smsRequest), HttpHelper.SOAP);
			Map<String, String> map = XMLHelper.getMapFromXML(result);
			if(map.get("status")!=null && "Y".equals((String)map.get("status"))) {
				leasResp.setResult_code("SUCCESS");
			} else {
				leasResp.setResult_code("FAILED");
			}
			leasResp.setResult_desc((String)map.get("message"));
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasRequest.getSecurity_code().equals(cParam.getClientCode()) && 
						leasRequest.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			leasResp.setSign(Security.getSign(leasResp, key));
		} catch (ESBException e) {
			leasResp.setResult_code(e.getCode());
			leasResp.setResult_desc(e.getDescription());
		} catch (DocumentException e) {
			leasResp.setResult_code("FAILED");
			leasResp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasResp.setResult_code("FAILED");
			leasResp.setResult_desc("签名失败");
			leasResp.setSign("");
		}
		return leasResp;
	}


	/**
	 * 生成响应报文
	 * @param leasResp
	 * @return
	 */
	private String generateMsgResponseXML(LEASING001Response leasResp) {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(leasResp.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(leasResp.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(leasResp.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(leasResp.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(leasResp.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(leasResp.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(leasResp.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<result_code><![CDATA[").append(leasResp.getResult_code()).append("]]></result_code>")
		.append("<result_desc><![CDATA[").append(leasResp.getResult_desc()).append("]]></result_desc>")
		.append("</body></esb>");
		logger.info("ESB响应报文明文"+respXml.toString());
		return respXml.toString();
	}


	/**
	 * 封装请求实体Bean
	 * @param leas
	 * @param param
	 * @return
	 */
	private SendSMSRequest getDestRequestBean(LEASING001Request leasReq, ParameterConfig param) {
		SendSMSRequest sms = new SendSMSRequest();
		sms.setRequestNum(StringUtil.getDateSerialNo6());
		sms.setSyscode(param.getAppCode());
		sms.setSyspwd(param.getAppSecurity());
		sms.setPhone(leasReq.getPhone());
		sms.setContent(leasReq.getContent());
		
		logger.info("Sign Before MD5："+sms.getSignContent()+param.getSignKey());
		String sign = Security.MD5Encode(sms.getSignContent()+param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		sms.setSign(sign);
		
		return sms;
	}

	
	/**
	 * 获得请求XML
	 * 
	 * @return
	 */
	private String getRequestXml(SendSMSRequest smsReq) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(smsReq.getRequestNum()).append("</sch:request_number>")
		.append("<sch:content>").append(smsReq.getContent()).append("</sch:content>")
		.append("<sch:user_account>").append(smsReq.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(smsReq.getSyspwd()).append("</sch:password>")
		.append("<sch:mobile>").append(smsReq.getPhone()).append("</sch:mobile>")
		.append("<sch:sign>").append(smsReq.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sb.toString();
	}

	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	private LEASING001Request getLEASING001RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING001Request leasingRequest = (LEASING001Request) ois.readObject();

			return leasingRequest;

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
