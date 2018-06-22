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
import com.kpleasing.esb.model.leasing007.CreateOrderRequest;
import com.kpleasing.esb.model.leasing007.LEASING007Request;
import com.kpleasing.esb.model.leasing007.LEASING007Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class GenerateBusinessOrderProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(GenerateBusinessOrderProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING007Request leasing007Request = getLEASING007RequestObject((byte[]) in.getBody());
		
		LEASING007Response respBean = generateLeasing007Response(leasing007Request);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING007响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	/**
	 * 生成响应实体
	 * @param leasRequest
	 * @return
	 */
	private LEASING007Response generateLeasing007Response(LEASING007Request leasRequest) {
		LEASING007Response leasResp = new LEASING007Response();
		leasResp.setReq_serial_no(leasRequest.getReq_serial_no());
		leasResp.setReq_date(leasRequest.getReq_date());
		leasResp.setRes_serial_no(StringUtil.getSerialNo32());
		leasResp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasResp.setReturn_code("SUCCESS");
		leasResp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasRequest.getParamConfig();
		CreateOrderRequest coRequest = getDestRequestBean(leasRequest, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(coRequest), HttpHelper.SOAP);
			Map<String, String> map = XMLHelper.getMapFromXML(result);
			leasResp.setApplyno((String)map.get("applyno"));
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
	 * 封装请求实体Bean
	 * @param leasReq
	 * @param param
	 * @return
	 */
	private CreateOrderRequest getDestRequestBean(LEASING007Request leasReq, ParameterConfig param) {
		CreateOrderRequest cor = new CreateOrderRequest();
		cor.setRequest_number(StringUtil.getDateSerialNo6());
		cor.setUser_account(param.getAppCode());
		cor.setPassword(param.getAppSecurity());
		cor.setEsb_bp_id(leasReq.getCust_id());
		cor.setId_card_no(leasReq.getCert_code());
		cor.setAgent_bp_name(leasReq.getAgent_name());
		cor.setPlan_session_id(leasReq.getFinance_plan());
		cor.setSale_id(leasReq.getSale_id());
		cor.setAgent_code(leasReq.getAgent_code());
		
		logger.info("Sign Before MD5："+cor.getSignContent()+param.getSignKey());
		String sign = Security.MD5Encode(cor.getSignContent()+param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		cor.setSign(sign);
		
		return cor;
	}
	
	/**
	 * 获得请求XML
	 * @param coReq
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String getRequestXml(CreateOrderRequest coReq) throws IllegalArgumentException, IllegalAccessException{
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(coReq.getRequest_number()).append("</sch:request_number>")
		.append("<sch:user_account>").append(coReq.getUser_account()).append("</sch:user_account>")
		.append("<sch:password>").append(coReq.getPassword()).append("</sch:password>")
		.append("<sch:sign>").append(coReq.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("<sch:CONTEXT>");
		
		coReq.setRequest_number("");
		coReq.setUser_account("");
		coReq.setPassword("");
		coReq.setSign("");
		sb.append(XMLHelper.getXMLFromBean(coReq, "sch:"));
		sb.append("</sch:CONTEXT>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		
		return sb.toString();
	}
	
	private String generateMsgResponseXML(LEASING007Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING007Request getLEASING007RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING007Request leasing007Request = (LEASING007Request) ois.readObject();

			return leasing007Request;

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
