package com.kpleasing.esb.business;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.exception.RouteException;
import com.kpleasing.esb.model.RequestHeader;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.XMLHelper;


/**
 * 
 * @author howard.huang
 *
 * @param <T>
 */
public abstract class AbstractRouteFactory<T> {
	private static Logger logger = Logger.getLogger(AbstractRouteFactory.class);
	protected abstract T init();
	protected abstract void verify(T t) throws InputNDataParamException ;
	
	
	/**
	 * 
	 * @param xml
	 * @param t
	 * @throws RouteException
	 */
	protected void parseXmlToBean(String xml, T t) throws RouteException {
		logger.info("转换XML报文为实体Bean......");
		try {
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(xml, "/esb/head|/esb/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(t, map);
			}
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new RouteException("FAILED", "XML解析失败！");
		}
	}

	
	/**
	 * 签名校验
	 * @param t
	 * @param paraConfig
	 * @throws InputNDataParamException 
	 */
	private void pubVerify(T t, ParameterConfig paraConfig) throws InputNDataParamException {
		// 访问权限校验
		RequestHeader reqHeader = (RequestHeader) t;
		List<ClientSecurityKey>  endpoints = paraConfig.getClientSecurityKey();
		String key = null; 
		for(ClientSecurityKey endpoint : endpoints) {
			if (endpoint.getClientCode().equals(reqHeader.getSecurity_code())
					&& endpoint.getClientSecurity().equals(reqHeader.getSecurity_value())) {
				key = endpoint.getClientSignKey(); break;
			}
		}
		if(null == key) {
			throw new InputNDataParamException("权限受限！", reqHeader.getReq_serial_no(), reqHeader.getReq_date());
		}
		
		// 签名校验
		String signFromAPIResponse = reqHeader.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new InputNDataParamException("API返回的数据签名数据不存在，有可能被第三方篡改!!!", reqHeader.getReq_serial_no(), reqHeader.getReq_date());
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		((RequestHeader) t).setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(t, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验不过，表示这个API返回的数据有可能已经被篡改了
				throw new InputNDataParamException("API返回的数据签名验证不通过，有可能被第三方篡改!!!", reqHeader.getReq_serial_no(), reqHeader.getReq_date());
			}
		} catch (IllegalAccessException e) {
			throw new InputNDataParamException("API签名出錯!!!", reqHeader.getReq_serial_no(), reqHeader.getReq_date());
		}
	}
	
	
	/**
	 * 
	 * @param reqXml
	 * @param paraConfig
	 * @return
	 * @throws InputNDataParamException
	 * @throws RouteException
	 */
	public T process(String reqXml, ParameterConfig paraConfig) throws InputNDataParamException, RouteException {
		T t = init();
		
		logger.info("开始解析请求报文XML信息......");
		parseXmlToBean(reqXml,  t);
		
		logger.info("开始进行签名验证.......");
		pubVerify(t, paraConfig);
		
		logger.info("开始进行参数有效性验证.......");
		verify(t);
		
		((RequestHeader) t).setParamConfig(paraConfig);
		return t;
	}
}
