package com.kpleasing.esb.business.crm;

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
import com.kpleasing.esb.model.crm007.CRM007AccountBean;
import com.kpleasing.esb.model.crm007.CRM007ContactBean;
import com.kpleasing.esb.model.crm007.CRM007Request;
import com.kpleasing.esb.tools.XMLHelper;

public class SetUserInfoService extends AbstractRouteFactory<CRM007Request> {
	
	private static Logger logger = Logger.getLogger(SetUserInfoService.class);

	/**
	 *  XML对象转换
	 * @param xml
	 * @param crm007Req
	 * @throws RouteException
	 */
	@Override
	protected void parseXmlToBean(String xml, CRM007Request crm007Req) throws RouteException {
		logger.info("转换XML报文为[CRM007Request]实体Bean......");
		try {
			logger.info("客户信息......");
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(xml, "/esb/head|/esb/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(crm007Req, map);
			}
			
			logger.info("转换联系人信息......");
			List<Map<String,String>> mapList1 = XMLHelper.parseMultNodesXml(xml, "//contact");
			List<CRM007ContactBean> crm007Contacts = new ArrayList<CRM007ContactBean>();
			for(Map<String,String> map1 : mapList1) {
				CRM007ContactBean crm007Contact = new CRM007ContactBean();
				BeanUtils.populate(crm007Contact, map1);
				crm007Contacts.add(crm007Contact);
			}
			crm007Req.setContacts(crm007Contacts);
			
			logger.info("转换账户信息......");
			List<Map<String,String>> mapList2 = XMLHelper.parseMultNodesXml(xml, "//account");
			List<CRM007AccountBean> crm007Accounts = new ArrayList<CRM007AccountBean>();
			for(Map<String,String> map2 : mapList2) {
				CRM007AccountBean crm007Account = new CRM007AccountBean();
				BeanUtils.populate(crm007Account, map2);
				crm007Accounts.add(crm007Account);
			}
			crm007Req.setAccounts(crm007Accounts);
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new RouteException("FAILED", "XML解析失败！");
		}
	}
	

	@Override
	protected CRM007Request init() {
		return new CRM007Request();
	}


	@Override
	protected void verify(CRM007Request crm007Req) throws InputNDataParamException {
		
	}
}
