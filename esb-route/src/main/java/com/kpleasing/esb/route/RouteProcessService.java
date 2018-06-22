package com.kpleasing.esb.route;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.kpleasing.esb.config.Configure;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.exception.RouteException;
import com.kpleasing.esb.route.control.RouteControlCenter;
import com.kpleasing.esb.route.util.ConfigUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class RouteProcessService implements Processor {

	private static Logger logger = Logger.getLogger(RouteProcessService.class);
	
	@Override
	public void process(Exchange exchange) {
		try {
			String requestBody = exchange.getIn().getBody(String.class);
			logger.info("请求密文："+requestBody);
			
			Configure config = ConfigUtil.getInstance().getConfig();
			String requestXml = EncryptUtil.decrypt(config.DES_KEY, config.DES_IV, requestBody);
			logger.info("请求明文："+requestXml);
			
			String apiCode = XMLHelper.getNodeTextByTagName(requestXml, "api_code");
			logger.info("开始处理API接口：api-code:" + apiCode);
			
			Object obj = RouteControlCenter.getDynamicsRouteObject(apiCode, requestXml);
			
			exchange.getOut().setHeader("RouteName", apiCode);
			exchange.getOut().setHeader("DESKEY", config.DES_KEY);
			exchange.getOut().setHeader("DESIV", config.DES_IV);
			exchange.getOut().setBody(transObjectSerializable(obj));
		} catch(InputNDataParamException e) {
			exchange.getOut().setHeader("RouteName", "ERROR");
			exchange.getOut().setHeader("ReturnCode", e.getCode());
			exchange.getOut().setHeader("ReturnDesc", e.getDescription());
			exchange.getOut().setHeader("ReqSerialNo", e.getReq_serial_no());
			exchange.getOut().setHeader("ReqDate", e.getReq_date());
			logger.error("参数错误.", e);
		} catch(RouteException e) {        
			exchange.getOut().setHeader("RouteName", "ERROR");
			exchange.getOut().setHeader("ReturnCode", e.getCode());
			exchange.getOut().setHeader("ReturnDesc", e.getDescription());
			logger.error("报文处理出错.", e);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			exchange.getOut().setHeader("RouteName", "ERROR");
			exchange.getOut().setHeader("ReturnCode", "SYSERROR");
			exchange.getOut().setHeader("ReturnDesc", "报文解析出错："+e.getMessage());
			logger.error("报文处理出错.", e);
		} catch (Exception e) {
			exchange.getOut().setHeader("RouteName", "ERROR");
			logger.error("系统错误："+e.getMessage(), e);
		}
	}
	
	
	/**
	 * 序列化对象
	 * @param obj
	 * @return
	 */
	private byte[] transObjectSerializable(Object obj) {
		ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
		ObjectOutputStream objOutStream = null;
		try {
			objOutStream = new ObjectOutputStream(baoStream);
			objOutStream.writeObject(obj);
			
			return baoStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != objOutStream) {
				try {
					objOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != baoStream) {
				try {
					baoStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}