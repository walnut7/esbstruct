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
import com.kpleasing.esb.model.wxss001.WXSS001Request;
import com.kpleasing.esb.model.wxss001.WXSS001SaleInfo;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncSPInfoService extends AbstractRouteFactory<WXSS001Request> {
	
	private static Logger logger = Logger.getLogger(SyncSPInfoService.class);

	/**
	 * XML对象转换
	 * @param xml
	 * @param wxss001Req
	 * @throws RouteException
	 */
	@Override
	protected void parseXmlToBean(String xml, WXSS001Request wxss001Req) throws RouteException {
		logger.info("转换XML报文为[WXSS001Request]实体Bean......");
		try {
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(xml, "/esb/head|/esb/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(wxss001Req, map);
			}
			
			logger.info("转换销售信息......");
			List<Map<String,String>> mapList1 = XMLHelper.parseMultNodesXml(xml, "//sale");
			List<WXSS001SaleInfo> wxss001Sales = new ArrayList<WXSS001SaleInfo>();
			for(Map<String,String> map1 : mapList1) {
				WXSS001SaleInfo wxss001Sale = new WXSS001SaleInfo();
				BeanUtils.populate(wxss001Sale, map1);
				wxss001Sales.add(wxss001Sale);
			}
			wxss001Req.setSales(wxss001Sales);
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new RouteException("FAILED", "XML解析失败！");
		}
	}
	

	@Override
	protected WXSS001Request init() {
		return new WXSS001Request();
	}


	@Override
	protected void verify(WXSS001Request wxss001Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
