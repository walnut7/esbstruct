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
import com.kpleasing.esb.model.leasing004.AutoTrialRequest;
import com.kpleasing.esb.model.leasing004.LEASING004Request;
import com.kpleasing.esb.model.leasing004.LEASING004Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class AutoTrialProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(AutoTrialProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING004Request leasRequest = getLEASING004RequestObject((byte[]) in.getBody());
		
		LEASING004Response respBean = generateLeasing004Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING004响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}
	
	
	/**
	 * 创建响应实体
	 * @param leasing004Request
	 * @return
	 */
	private LEASING004Response generateLeasing004Response(LEASING004Request leasing004Request) {
		LEASING004Response leasing004Resp = new LEASING004Response();
		leasing004Resp.setReq_serial_no(leasing004Request.getReq_serial_no());
		leasing004Resp.setReq_date(leasing004Request.getReq_date());
		leasing004Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing004Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing004Resp.setReturn_code("SUCCESS");
		leasing004Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing004Request.getParamConfig();
		AutoTrialRequest autoTrialRequest = getDestRequestBean(leasing004Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(autoTrialRequest), HttpHelper.SOAP);
			Map<String, String> map = XMLHelper.getMapFromXML(result);
			if(map.get("status")!=null && "Y".equals((String)map.get("status"))) {
				leasing004Resp.setResult_code("SUCCESS");
			} else {
				leasing004Resp.setResult_code("FAILED");
			}
			leasing004Resp.setResult_desc((String)map.get("message"));
			leasing004Resp.setApplication_no((String)map.get("report_number"));
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing004Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing004Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			leasing004Resp.setSign(Security.getSign(leasing004Resp, key));
		} catch (ESBException e) {
			leasing004Resp.setResult_code(e.getCode());
			leasing004Resp.setResult_desc(e.getDescription());
		} catch (DocumentException e) {
			leasing004Resp.setResult_code("FAILED");
			leasing004Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing004Resp.setResult_code("FAILED");
			leasing004Resp.setResult_desc("签名失败");
			leasing004Resp.setSign("");
		} 
		return leasing004Resp;
	}

	
	/**
	 * 生成响应报文
	 * @param autoTrialRequest
	 * @return
	 */
	private String getRequestXml(AutoTrialRequest autoTrialRequest) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(autoTrialRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:id_card_type>").append(autoTrialRequest.getCert_type()).append("</sch:id_card_type>")
		.append("<sch:id_card_no>").append(autoTrialRequest.getCert_code()).append("</sch:id_card_no>")
		.append("<sch:user_account>").append(autoTrialRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(autoTrialRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:name>").append(autoTrialRequest.getCust_name()).append("</sch:name>")
		.append("<sch:mobile>").append(autoTrialRequest.getPhone()).append("</sch:mobile>")
		.append("<sch:sign>").append(autoTrialRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sb.toString();
	}


	/**
	 * 封装请求实体Bean
	 * @param leasing004Request
	 * @param param
	 * @return
	 */
	private AutoTrialRequest getDestRequestBean(LEASING004Request leasing004Request, ParameterConfig param) {
		AutoTrialRequest autoTrialRequest = new AutoTrialRequest();
		autoTrialRequest.setRequestNum(StringUtil.getDateSerialNo6());
		autoTrialRequest.setSyscode(param.getAppCode());
		autoTrialRequest.setSyspwd(param.getAppSecurity());
		autoTrialRequest.setPhone(leasing004Request.getPhone());
		autoTrialRequest.setCust_name(leasing004Request.getCust_name());
		autoTrialRequest.setCert_type(leasing004Request.getCert_type());
		autoTrialRequest.setCert_code(leasing004Request.getCert_code());
		
		logger.info("Sign Before MD5："+autoTrialRequest.getSignContent()+param.getSignKey());
		String sign = Security.MD5Encode(autoTrialRequest.getSignContent()+param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		autoTrialRequest.setSign(sign);
		
		return autoTrialRequest;
	}


	/**
	 * 生成响应报文
	 * @param respBean
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String generateMsgResponseXML(LEASING004Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING004Request getLEASING004RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING004Request leasing004Request = (LEASING004Request) ois.readObject();

			return leasing004Request;

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
