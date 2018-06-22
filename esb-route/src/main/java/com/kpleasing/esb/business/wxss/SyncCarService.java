package com.kpleasing.esb.business.wxss;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.exception.RouteException;
import com.kpleasing.esb.model.wxss002.WXSS002CarParam;
import com.kpleasing.esb.model.wxss002.WXSS002Request;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncCarService extends AbstractRouteFactory<WXSS002Request> {
	
	private static Logger logger = Logger.getLogger(SyncCarService.class);
	
	/**
	 * XML对象转换
	 * 
	 * @param xml
	 * @param wxss002Req
	 * @throws RouteException
	 */
	@Override
	protected void parseXmlToBean(String xml, WXSS002Request wxss002Req) throws RouteException {
		logger.info("转换XML报文为[WXSS002Request]实体Bean......");
		try {
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(xml, "/esb/head|/esb/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(wxss002Req, map);
			}
			
			logger.info("转换车辆配置参数......");
			List<Map<String,String>> mapList1 = XMLHelper.parseMultNodesXml(xml, "//car_param");
			List<WXSS002CarParam> wxss002CarParams = new ArrayList<WXSS002CarParam>();
			for(Map<String,String> map1 : mapList1) {
				WXSS002CarParam wxss002CarParam = new WXSS002CarParam();
				BeanUtils.populate(wxss002CarParam, map1);
				wxss002CarParams.add(wxss002CarParam);
			}
			wxss002Req.setCar_params(wxss002CarParams);
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new RouteException("FAILED", "XML解析失败！");
		}
	}

	

	@Override
	protected WXSS002Request init() {
		return new WXSS002Request();
	}


	@Override
	protected void verify(WXSS002Request wxss002Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
