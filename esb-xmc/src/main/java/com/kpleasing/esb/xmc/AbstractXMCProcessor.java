package com.kpleasing.esb.xmc;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.base.AbstractBaseProcessor;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.xmc.XMCRequestHeader;
import com.kpleasing.esb.model.xmc.XMCResponseHeader;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.XMLHelper;

public abstract class AbstractXMCProcessor<T, E, M, N> extends AbstractBaseProcessor<T, E, M, N> {
	
	private static Logger logger = Logger.getLogger(AbstractXMCProcessor.class);
	
	protected abstract N initCrmResponse() ;
	
	
	@Override
	public String sendHttpRequest(ParameterConfig config, String reqXml) throws ESBException {
		String responseResult = HttpHelper.doHttpPost(config.getDestSystemUrl(), reqXml, HttpHelper.XML);
		try {
			return EncryptUtil.decrypt(config.getDesKey(), config.getDesIv(), responseResult);
		} catch (Exception e) {
			logger.error("响应报文解密出错." + e.getMessage(), e);
			throw new ESBException("FAILED", "响应报文解密出错！");
		}
	}
	
	
	@Override
	public String generateRequestXML(M m, ParameterConfig config) throws Exception {
		StringBuilder requestXml = new StringBuilder();
		requestXml.append("<?xml version=\"1.0\"?>")
		.append("<xmc><head>")
		.append("<api_code><![CDATA[").append(((XMCRequestHeader) m).getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(((XMCRequestHeader) m).getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(((XMCRequestHeader) m).getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(((XMCRequestHeader) m).getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(((XMCRequestHeader) m).getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(((XMCRequestHeader) m).getSign()).append("]]></sign>")
		.append("</head><body>");
		
		requestXml.append(XMLHelper.getXMLFromBean(m));
		requestXml.append("</body></xmc>");
		
		logger.info("请求报文明文：" + requestXml.toString());
		return EncryptUtil.encrypt(config.getDesKey(), config.getDesIv(), requestXml.toString());
	}
	
	
	@Override
	public N parseResponseEntity(String resultXml) throws ESBException {
		logger.info("响应报文明文：" + resultXml);
		try {
			logger.info("转换XML报文为实体Bean......");
			N n = initCrmResponse();
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, "/xmc/head|/xmc/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(n, map);
			}
			return n;
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误." + e.getMessage(), e);
			throw new ESBException("FAILED", "XML解析失败！");
		}
	}
	

	@Override
	public void verifyResponse(N n, ParameterConfig config) throws ESBException {
		if("SUCCESS".equals(((XMCResponseHeader) n).getReturn_code())) {
			// 签名校验
			String signFromAPIResponse = ((XMCResponseHeader) n).getSign();
			if (StringUtils.isBlank(signFromAPIResponse)) {
				throw new ESBException("FAILED", "XMC API返回的数据签名数据不存在，有可能被第三方篡改!!!");
			}
			
			logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
			// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
			((XMCResponseHeader) n).setSign("");
			// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
			try {
				String signForAPIResponse = Security.getSign(n, config.getSignKey());
				
				if (!signForAPIResponse.equals(signFromAPIResponse)) {
					// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
					throw new ESBException("FAILED", "XMC API返回的数据签名验证不通过，有可能被第三方篡改!!!");
				}
			} catch (IllegalAccessException e) {
				throw new ESBException("FAILED", "XMC API签名出錯!!!");
			}
		}
	}
}
