package com.kpleasing.esb.base;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.ResponseHeader;
import com.kpleasing.esb.model.RequestHeader;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;


/**
 * 
 * @author howard.huang
 *
 * @param <T>
 * @param <E>
 * @param <M>
 * @param <N>
 */
public abstract class AbstractBaseProcessor<T, E, M, N> implements Processor {
	
	private static Logger logger = Logger.getLogger(AbstractBaseProcessor.class);
	
	/**
	 * 
	 * @return
	 */
	public abstract E initResponse();
	
	
	/**
	 * 
	 * @param t
	 * @param config
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public abstract M generateRequestEntity(T t, ParameterConfig config) throws IllegalArgumentException, IllegalAccessException ;
	
	
	/**
	 * 
	 * @param config 
	 * @param request
	 * @param config 
	 * @return
	 * @throws Exception 
	 */
	public abstract String generateRequestXML(M m, ParameterConfig config) throws Exception ;
	
	
	/**
	 * 
	 * @param config
	 * @param reqXml
	 * @return
	 * @throws ESBException
	 */
	public String sendHttpRequest(ParameterConfig config, String reqXml) throws ESBException {
		return HttpHelper.doHttpPost(config.getDestSystemUrl(), reqXml);
	}
	
	
	/**
	 * 
	 * @param resultXml
	 * @return
	 * @throws ESBException 
	 */
	public abstract N parseResponseEntity(String resultXml) throws ESBException ;
	
	
	/**
	 * 验证响应报文
	 * @param config 
	 * @param response
	 * @throws ESBException 
	 */
	public abstract void verifyResponse(N n, ParameterConfig config) throws ESBException ;
	
	
	/**
	 * 
	 * @param response
	 * @param e
	 * @param key 
	 * @return
	 * @throws ESBException 
	 */
	public abstract void covertToEBody(N n, E e, String key) throws ESBException ;

	
	/**
	 * 
	 * @param e
	 * @param key
	 * @param code
	 * @param description
	 */
	public abstract void resultException(E e, String key, String code, String msg) ;
	
	
	/**
	 * 
	 * @param t
	 * @return
	 * @throws ESBException
	 */
	private E generateEResponse(T t) {
		E e = initResponse();
		String key = null;
		try {
			((ResponseHeader) e).setReq_serial_no(((RequestHeader) t).getReq_serial_no());
			((ResponseHeader) e).setReq_date(((RequestHeader) t).getReq_date());
			((ResponseHeader) e).setRes_serial_no(StringUtil.getSerialNo32());
			((ResponseHeader) e).setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
			((ResponseHeader) e).setReturn_code("SUCCESS");
			((ResponseHeader) e).setReturn_desc("请求成功");
			
			ParameterConfig config = ((RequestHeader) t).getParamConfig();
			List<ClientSecurityKey> clientParams = config.getClientSecurityKey();
			for(ClientSecurityKey cParam : clientParams) {
				if(((RequestHeader) t).getSecurity_code().equals(cParam.getClientCode()) && 
						((RequestHeader) t).getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			logger.info("创建请求报文实体......");
			M request = generateRequestEntity(t, config);
			
			logger.info("创建请求报文......");
			String reqXml = generateRequestXML(request, config);
			
			logger.info("请求报文:"+reqXml);
			
			String resultXml = sendHttpRequest(config, reqXml);
			
			logger.info("解析响应报文......");
			N response = parseResponseEntity(resultXml);
			
			verifyResponse(response, config);
			
			logger.info("转换ESB响应实体报文体......");
			covertToEBody(response, e, key);
			
			return e;
			
		} catch (ESBException ex) {
			 resultException(e, key, ex.getCode(), ex.getDescription());
			
			logger.error(ex.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			resultException(e, key, "FAILED", ex.getMessage());
			
			logger.error(ex.getMessage(), ex);
		} catch (IllegalAccessException ex) {
			resultException(e, key, "FAILED", ex.getMessage());

			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			resultException(e, key, "FAILED", ex.getMessage());
			
			logger.error(ex.getMessage(), ex);
		}
		return e;
	}
	

	/**
	 * 
	 * @param respOjb
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String generateResponseXML(E respOjb) throws IllegalArgumentException, IllegalAccessException {
		ResponseHeader response = (ResponseHeader) respOjb;
		
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(response.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(response.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(response.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(response.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(response.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(response.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(response.getSign()).append("]]></sign>")
		.append("</head><body>");
		respXml.append(XMLHelper.getXMLFromBean(respOjb));
		respXml.append("</body></esb>");
		
		logger.info("ESB响应报文明文："+respXml.toString());
		return respXml.toString();
	}


	/**
	 * 
	 * @param bytes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T getTObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			return (T) ois.readObject();

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
	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		T reqObj = getTObject((byte[]) in.getBody());
		
		E respOjb = generateEResponse(reqObj);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateResponseXML(respOjb));
		logger.info("ESB接口响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}
}
