package com.kpleasing.esb.kpfs.business;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.kpleasing.esb.common.XMLHelper;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;
import com.kpleasing.esb.kpfs.exceptions.KPFSException;
import com.kpleasing.esb.kpfs.exceptions.KPFSINDataException;


public class KPFSBusinessService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KPFSBusinessService.class);
	
	public static String processKpfsApiCenter(String xmlString) throws KPFSException {
		
		Map<String, Object> map = null;
		String serialNo = "";
		String receDate = "";
		
		try {
			LOGGER.info("开始解析报文......");
			map = XMLHelper.getMapFromXML(xmlString);
			
			serialNo = (String) map.get("req_serial_no");
			receDate = (String) map.get("req_date");
			LOGGER.info("请求流水号：req_serial_no="+serialNo+",\t请求时间：req_date="+receDate);
					
			// 根据协议代码处理对应业务逻辑
			LOGGER.info("开始处理协议["+map.get("api_code")+"]......");
			IProcessService business = (IProcessService) Class.forName(ConfigUtil.getProtoMap().get(map.get("api_code"))).newInstance();
			
			return business.process(map);
			
		} catch (InstantiationException e) {
			
			throw new KPFSException(serialNo, receDate, e.getMessage());
		} catch (IllegalAccessException e) {

			throw new KPFSException(serialNo, receDate, e.getMessage());
		} catch (ClassNotFoundException e) {

			throw new KPFSException(serialNo, receDate, "请求接口不存在");
		} catch (ParserConfigurationException e) {
			
			throw new KPFSException(serialNo, receDate, e.getMessage());
		} catch (IOException e) {
			
			throw new KPFSException(serialNo, receDate, e.getMessage());
		} catch (SAXException e) {
			
			throw new KPFSException(serialNo, receDate, e.getMessage());
		} catch(KPFSINDataException e) {
		
			throw new KPFSException(serialNo, receDate, e.getDescription());
	    } catch(KPFSBusinessException e) {
	    	
	    	throw new KPFSException(serialNo, receDate, e.getDescription());
	    } catch(Exception e) {
			
			throw new KPFSException(serialNo, receDate, e.getMessage());
		}
	}
}
