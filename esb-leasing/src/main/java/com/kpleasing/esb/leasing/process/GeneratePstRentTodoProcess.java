package com.kpleasing.esb.leasing.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import com.kpleasing.esb.model.leasing015.GeneratePstRentTodoRequest;
import com.kpleasing.esb.model.leasing015.GeneratePstRentTodoResponse;
import com.kpleasing.esb.model.leasing015.LEASING015Request;
import com.kpleasing.esb.model.leasing015.LEASING015Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class GeneratePstRentTodoProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(GeneratePstRentTodoProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		LEASING015Request leasRequest = getLEASING015RequestObject((byte[]) in.getBody());
		
		LEASING015Response respBean = generateLeasing015Response(leasRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口LEASING015响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	/**
	 * 创建响应实体
	 * 
	 * @param leasing015Request
	 * @return
	 */
	private LEASING015Response generateLeasing015Response(LEASING015Request leasing015Request) {
		LEASING015Response leasing015Resp = new LEASING015Response();
		leasing015Resp.setReq_serial_no(leasing015Request.getReq_serial_no());
		leasing015Resp.setReq_date(leasing015Request.getReq_date());
		leasing015Resp.setRes_serial_no(StringUtil.getSerialNo32());
		leasing015Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		leasing015Resp.setReturn_code("SUCCESS");
		leasing015Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = leasing015Request.getParamConfig();
		GeneratePstRentTodoRequest pstRentTodoRequest = getDestRequestBean(leasing015Request, paramConfig);
		try {
			String result = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), getRequestXml(pstRentTodoRequest), HttpHelper.SOAP);
			
			// 解析报文信息
			GeneratePstRentTodoResponse pstRentTodoResponse = parsePstRentTodoResponse(result);
			
			// 获取配置终端签名KEY
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(leasing015Request.getSecurity_code().equals(cParam.getClientCode()) && 
						leasing015Request.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			setResponseBody(leasing015Resp, pstRentTodoResponse, key);
			
		} catch (ESBException e) {
			leasing015Resp.setResult_code(e.getCode());
			leasing015Resp.setResult_desc(e.getDescription());
		} catch (DocumentException e) {
			leasing015Resp.setResult_code("FAILED");
			leasing015Resp.setResult_desc(e.getMessage());
		} catch (IllegalAccessException e) {
			leasing015Resp.setResult_code("FAILED");
			leasing015Resp.setResult_desc("签名失败");
			leasing015Resp.setSign("");
		} 
		return leasing015Resp;
	}


	/**
	 * 目标响应实体->ESB响应实体
	 * @param leasing015Resp
	 * @param pstRentTodoResponse
	 * @param key
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setResponseBody(LEASING015Response leasing015Resp, GeneratePstRentTodoResponse pstRentTodoResponse,
			String key) throws IllegalArgumentException, IllegalAccessException {
		if(pstRentTodoResponse.getStatus()!=null && "Y".equals(pstRentTodoResponse.getStatus())) {
			leasing015Resp.setResult_code("SUCCESS");
		} else {
			leasing015Resp.setResult_code("FAILED");
		}
		leasing015Resp.setResult_desc(pstRentTodoResponse.getMessage());
		
		leasing015Resp.setSign(Security.getSign(leasing015Resp, key));
	}


	/**
	 * 解析目标报文XML-BEAN实体
	 * @param resultXml
	 * @return
	 * @throws DocumentException
	 */
	private GeneratePstRentTodoResponse parsePstRentTodoResponse(String resultXml) throws DocumentException {
		GeneratePstRentTodoResponse pstRentTodoResponse = new GeneratePstRentTodoResponse();

		Map<String, String> nsMap = new HashMap<String, String>();
		nsMap.put("ns", "http://www.aurora-framework.org/schema");
		
		// 结果状态
		String status = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:status");
		String message = XMLHelper.getNodeTextByTagName(resultXml, nsMap, "//ns:message");
		pstRentTodoResponse.setStatus(status);
		pstRentTodoResponse.setMessage(message);
		
		return pstRentTodoResponse;
	}


	/**
	 * 生成目标报文XML报文
	 * 
	 * @param pstRentTodoRequest
	 * @return
	 */
	private String getRequestXml(GeneratePstRentTodoRequest pstRentTodoRequest) {
		StringBuilder sendXml = new StringBuilder();
		sendXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(pstRentTodoRequest.getRequestNum()).append("</sch:request_number>")
		.append("<sch:user_account>").append(pstRentTodoRequest.getSyscode()).append("</sch:user_account>")
		.append("<sch:password>").append(pstRentTodoRequest.getSyspwd()).append("</sch:password>")
		.append("<sch:applyno>").append(StringUtil.setNullToBlank(pstRentTodoRequest.getApplyno())).append("</sch:applyno>")
		.append("<sch:apply_type>").append(StringUtil.setNullToBlank(pstRentTodoRequest.getApplyType())).append("</sch:apply_type>")
		.append("<sch:name>").append(StringUtil.setNullToBlank(pstRentTodoRequest.getName())).append("</sch:name>")
		.append("<sch:delivery_addr>").append(StringUtil.setNullToBlank(pstRentTodoRequest.getDeliveryAddr())).append("</sch:delivery_addr>")
		.append("<sch:phone>").append(StringUtil.setNullToBlank(pstRentTodoRequest.getPhone())).append("</sch:phone>")
		.append("<sch:sign>").append(pstRentTodoRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
		return sendXml.toString();
	}


	/**
	 * 创建目标报文实体
	 * @param leasing015Request
	 * @param param
	 * @return
	 */
	private GeneratePstRentTodoRequest getDestRequestBean(LEASING015Request leasing015Request, ParameterConfig param) {
		GeneratePstRentTodoRequest pstRentTodoRequest = new GeneratePstRentTodoRequest();
		pstRentTodoRequest.setRequestNum(StringUtil.getDateSerialNo6());
		pstRentTodoRequest.setSyscode(param.getAppCode());
		pstRentTodoRequest.setSyspwd(param.getAppSecurity());
		pstRentTodoRequest.setApplyno(leasing015Request.getApplyno());
		pstRentTodoRequest.setApplyType(leasing015Request.getApply_type());
		pstRentTodoRequest.setName(leasing015Request.getRecipients());
		pstRentTodoRequest.setDeliveryAddr(leasing015Request.getMail_address());
		pstRentTodoRequest.setPhone(leasing015Request.getContact_phone());
		
		logger.info("Sign Before MD5："+pstRentTodoRequest.getSignContent() + param.getSignKey());
		String sign = Security.MD5Encode(pstRentTodoRequest.getSignContent() + param.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		pstRentTodoRequest.setSign(sign);
		
		return pstRentTodoRequest;
	}


	/**
	 * 生成ESB响应报文
	 * 
	 * @param respBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private String generateMsgResponseXML(LEASING015Response respBean) throws IllegalArgumentException, IllegalAccessException {
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
	private LEASING015Request getLEASING015RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			LEASING015Request leasing015Request = (LEASING015Request) ois.readObject();

			return leasing015Request;

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
