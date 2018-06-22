package com.kpleasing.esb.business.leasing;

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
import com.kpleasing.esb.model.leasing002.LEASING002AccountInfo;
import com.kpleasing.esb.model.leasing002.LEASING002AttachmentInfo;
import com.kpleasing.esb.model.leasing002.LEASING002ContactInfo;
import com.kpleasing.esb.model.leasing002.LEASING002Request;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncUserInfoService extends AbstractRouteFactory<LEASING002Request> {
	
	private static Logger logger = Logger.getLogger(SyncUserInfoService.class);
	
	/**
	 * XML对象转换
	 * @param reqXml
	 * @param syncUserRequest
	 * @throws RouteException 
	 */
	@Override
	protected void parseXmlToBean(String xml, LEASING002Request leasing002Req) throws RouteException {
		logger.info("转换XML报文为[LEASING002Request]实体Bean......");
		try {
			logger.info("转换客户信息......");
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(xml, "/esb/head|/esb/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(leasing002Req, map);
			}
			
			logger.info("转换联系人信息......");
			List<Map<String,String>> mapList1 = XMLHelper.parseMultNodesXml(xml, "//contact");
			List<LEASING002ContactInfo> leasing002Contacts = new ArrayList<LEASING002ContactInfo>();
			for(Map<String,String> map1 : mapList1) {
				LEASING002ContactInfo leasing002Contact = new LEASING002ContactInfo();
				BeanUtils.populate(leasing002Contact, map1);
				leasing002Contacts.add(leasing002Contact);
			}
			leasing002Req.setContacts(leasing002Contacts);
			
			logger.info("转换账户信息......");
			List<Map<String,String>> mapList2 = XMLHelper.parseMultNodesXml(xml, "//account");
			List<LEASING002AccountInfo> leasing002Aaccounts = new ArrayList<LEASING002AccountInfo>();
			for(Map<String,String> map2 : mapList2) {
				LEASING002AccountInfo leasing002Account = new LEASING002AccountInfo();
				BeanUtils.populate(leasing002Account, map2);
				leasing002Aaccounts.add(leasing002Account);
			}
			leasing002Req.setAccounts(leasing002Aaccounts);
			
			logger.info("转换附件信息......");
			List<Map<String,String>> mapList3 = XMLHelper.parseMultNodesXml(xml, "//attach");
			List<LEASING002AttachmentInfo> leasing002Attachments = new ArrayList<LEASING002AttachmentInfo>();
			for(Map<String,String> map3 : mapList3) {
				LEASING002AttachmentInfo leasing002Attach = new LEASING002AttachmentInfo();
				BeanUtils.populate(leasing002Attach, map3);
				leasing002Attachments.add(leasing002Attach);
			}
			leasing002Req.setAttachs(leasing002Attachments);
			
			
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new RouteException("FAILED", "XML解析失败！");
		}
	}
	
	@Override
	protected LEASING002Request init() {
		return new LEASING002Request();
	}

	@Override
	protected void verify(LEASING002Request leasing002Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
