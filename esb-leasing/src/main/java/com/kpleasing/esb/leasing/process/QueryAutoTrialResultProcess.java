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
import com.kpleasing.esb.model.leasing005.LEASING005Request;
import com.kpleasing.esb.model.leasing005.LEASING005Response;
import com.kpleasing.esb.model.leasing005.QueryAutoTrialResultRequest;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class QueryAutoTrialResultProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(QueryAutoTrialResultProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING005Request leasRequest = getLEASING005RequestObject((byte[]) in.getBody());
		
		LEASING005Response respBean = generateLeasing005Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING005响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	/**
	 *  创建响应实体
	 * @param leasRequest
	 * @return
	 */
	private LEASING005Response generateLeasing005Response(LEASING005Request leasing005Requ) {
		LEASING005Response leasing005Resp = new LEASING005Response();
		leasing005Resp.setReq_serial_no(leasing005Requ.getReq_serial_no());
		leasing005Resp.setReq_date(leasing005Requ.getReq_date());
		leasing005Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing005Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing005Resp.setReturn_code("SUCCESS");
		leasing005Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing005Requ.getParamConfig();
		QueryAutoTrialResultRequest autoTrialResultRequest = getDestRequestBean(leasing005Requ, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(autoTrialResultRequest), HttpHelper.SOAP);
			Map<String, String> map = XMLHelper.getMapFromXML(result);
			if(map.get("status")!=null && "Y".equals((String)map.get("status"))) {
				leasing005Resp.setResult_code("SUCCESS");
			} else {
				leasing005Resp.setResult_code("FAILED");
			}
			leasing005Resp.setResult_desc((String)map.get("message"));
			
			String creditCode = (String)map.get("credit_result");
			if("ERROR".equals(creditCode)) {
				leasing005Resp.setCredit_code("FAILED");
			} else {
				leasing005Resp.setCredit_code((String)map.get("credit_result"));
			}
			// leasing005Resp.setCredit_note((String)map.get("credit_note"));
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing005Requ.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing005Requ.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			leasing005Resp.setSign(Security.getSign(leasing005Resp, key));
		} catch (ESBException e) {
			leasing005Resp.setResult_code(e.getCode());
			leasing005Resp.setResult_desc(e.getDescription());
		} catch (DocumentException e) {
			leasing005Resp.setResult_code("FAILED");
			leasing005Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing005Resp.setResult_code("FAILED");
			leasing005Resp.setResult_desc("签名失败");
			leasing005Resp.setSign("");
		}
		return leasing005Resp;
	}
	

	/**
	 * 生成响应报文
	 * @param autoTrialResultRequest
	 * @return
	 */
	private String getRequestXml(QueryAutoTrialResultRequest autoTrialResultRequest) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(autoTrialResultRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(autoTrialResultRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(autoTrialResultRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:report_number>").append(autoTrialResultRequest.getReport_number()).append("</sch:report_number>")
		.append("<sch:sign>").append(autoTrialResultRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sb.toString();
	}


	/**
	 * 封装请求实体Bean
	 * @param leasing005Requ
	 * @param param
	 * @return
	 */
	private QueryAutoTrialResultRequest getDestRequestBean(LEASING005Request leasing005Requ, ParameterConfig param) {
		QueryAutoTrialResultRequest autoTrialResultRequest = new QueryAutoTrialResultRequest();
		autoTrialResultRequest.setRequestNum(StringUtil.getDateSerialNo6());
		autoTrialResultRequest.setSyscode(param.getAppCode());
		autoTrialResultRequest.setSyspwd(param.getAppSecurity());
		autoTrialResultRequest.setReport_number(leasing005Requ.getApplication_no());
		
		logger.info("Sign Before MD5："+autoTrialResultRequest.getSignContent()+param.getSignKey());
		String sign = Security.MD5Encode(autoTrialResultRequest.getSignContent()+param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		autoTrialResultRequest.setSign(sign);
		
		return autoTrialResultRequest;
	}


	/**
	 * 生成响应报文
	 * @param respBean
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String generateMsgResponseXML(LEASING005Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING005Request getLEASING005RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING005Request leasing005Request = (LEASING005Request) ois.readObject();

			return leasing005Request;

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
