package com.kpleasing.esb.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wutka.jox.JOXBeanInputStream;
import com.wutka.jox.JOXBeanOutputStream;


public class BeanXMLMapping {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanXMLMapping.class);
	
	@SuppressWarnings("rawtypes") 
	public static Object fromXML(String xml, Class className) throws Exception {
		ByteArrayInputStream xmlData = null;
		try {
			xmlData = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			LOGGER.error("解释XML字符串失败！", e1);
			throw new Exception("解释XML字符串失败！");
		}
		JOXBeanInputStream joxIn = new JOXBeanInputStream(xmlData);

		try {
			
			return joxIn.readObject(className);
		} catch (IOException exc) {
			LOGGER.error("解释XML字符串失败！", exc);
			throw new Exception("解释XML字符串失败！");
		} finally {
			try {
				xmlData.close();
				joxIn.close();
			} catch (Exception e) {
				LOGGER.error("解释XML字符串失败！", e);
				throw new Exception("解释XML字符串失败！");
			}
		}

	}
	

	/**
	 * Returns a XML document String for the received bean
	 */
	public static String toXML(Object bean) {
		return toXML(beanName(bean), bean);
	}
	
	
	/**
	 * Returns a XML document String for the received bean
	 */
	public static String toXML(String tagName, Object bean) {
		ByteArrayOutputStream xmlData = new ByteArrayOutputStream();
		JOXBeanOutputStream joxOut = new JOXBeanOutputStream(xmlData, "UTF-8");
		try {
			joxOut.writeObject(tagName, bean);
			return xmlData.toString();
		} catch (IOException exc) {
			exc.printStackTrace();
			return null;
		} finally {
			try {
				xmlData.close();
				joxOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Find out the bean class name
	 */
	private static String beanName(Object bean) {
		String fullClassName = bean.getClass().getName();
		String classNameTemp = fullClassName.substring(
				fullClassName.lastIndexOf(".") + 1, fullClassName.length());
		return classNameTemp.substring(0, 1) + classNameTemp.substring(1);
	}

}
