package com.kpleasing.esb.crm.process;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.crm.AbstractCrmProcessor;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm004.Account;
import com.kpleasing.esb.model.crm004.CRM004AccountBean;
import com.kpleasing.esb.model.crm004.CRM004ContactBean;
import com.kpleasing.esb.model.crm004.CRM004Request;
import com.kpleasing.esb.model.crm004.CRM004Response;
import com.kpleasing.esb.model.crm004.Contact;
import com.kpleasing.esb.model.crm004.GetUserInfoRequest;
import com.kpleasing.esb.model.crm004.GetUserInfoResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class GetUserInfoProcess extends AbstractCrmProcessor<CRM004Request, CRM004Response, GetUserInfoRequest, GetUserInfoResponse> {
	private static Logger logger = Logger.getLogger(GetUserInfoProcess.class);
	
	@Override
	public CRM004Response initResponse() {
		return new CRM004Response();
	}

	@Override
	public GetUserInfoRequest generateRequestEntity(CRM004Request crm004Request, ParameterConfig config) throws IllegalArgumentException, IllegalAccessException {
		GetUserInfoRequest userRequest = new GetUserInfoRequest();
		userRequest.setApi_code("GET_USER_INFO");
		userRequest.setReq_serial_no(StringUtil.getSerialNo32());
		userRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		userRequest.setSecurity_code(config.getAppCode());
		userRequest.setSecurity_value(config.getAppSecurity());
		userRequest.setCust_id(crm004Request.getCust_id());
		
		userRequest.setSign(Security.getSign(userRequest, config.getSignKey()));
	
		return userRequest;
	}


	@Override
	public String generateRequestXML(GetUserInfoRequest userRequest, ParameterConfig config) throws Exception {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(userRequest.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(userRequest.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(userRequest.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(userRequest.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(userRequest.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(userRequest.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<cust_id><![CDATA[").append(userRequest.getCust_id()).append("]]></cust_id>")
		.append("</body></crm>");
		
		logger.info("【CRM004】接口GET_USER_INFO请求报文");
		
		return EncryptUtil.encrypt(config.getDesKey(), config.getDesIv(), msgRequest.toString());
	}

	
	@Override
	protected GetUserInfoResponse initCrmResponse() {
		return new GetUserInfoResponse();
	}
	

	@Override
	public GetUserInfoResponse parseResponseEntity(String resultXml) throws ESBException {
		logger.info("转换CRM用户信息报文为[GetUserInfoResponse]实体Bean......");
		try {
			GetUserInfoResponse userResponse = initCrmResponse();
			
			logger.info("客户信息......");
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, "/crm/head|/crm/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(userResponse, map);
			}
			
			logger.info("转换联系人信息......");
			List<Map<String, String>> mapList1 = XMLHelper.parseMultNodesXml(resultXml, "//contact");
			List<Contact> contacts = new ArrayList<Contact>();
			for(Map<String, String> map1 : mapList1) {
				Contact contact = new Contact();
				BeanUtils.populate(contact, map1);
				contacts.add(contact);
			}
			userResponse.setContacts(contacts);
			
			logger.info("转换账户信息......");
			List<Map<String,String>> mapList2 = XMLHelper.parseMultNodesXml(resultXml, "//account");
			List<Account> accounts = new ArrayList<Account>();
			for(Map<String,String> map2 : mapList2) {
				Account account = new Account();
				BeanUtils.populate(account, map2);
				accounts.add(account);
			}
			userResponse.setAccounts(accounts);
			
			return userResponse;
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "CRM-GET_USER_INFO报文XML解析失败！");
		}
	}


	@Override
	public void covertToEBody(GetUserInfoResponse userResponse, CRM004Response crm004Response, String key) throws ESBException {
		crm004Response.setResult_code(userResponse.getResult_code());
		crm004Response.setResult_desc(userResponse.getResult_desc());
		crm004Response.setCust_name(userResponse.getCust_name());
		crm004Response.setCust_name_spell(userResponse.getCust_name_spell());
		crm004Response.setCert_type(userResponse.getCert_type());
		crm004Response.setCert_code(userResponse.getCert_code());
		crm004Response.setPhone(userResponse.getPhone());
		crm004Response.setCust_type(userResponse.getCust_type());
		crm004Response.setChannel_source(userResponse.getChannel_source());
		crm004Response.setRegister_time(userResponse.getRegister_time());
		crm004Response.setBirthday(userResponse.getBirthday());
		crm004Response.setGender(userResponse.getGender());
		crm004Response.setNation(userResponse.getNation());
		crm004Response.setDrive_model(userResponse.getDrive_model());
		crm004Response.setAnnual_income(userResponse.getAnnual_income());
		crm004Response.setRel_flag(userResponse.getRel_flag());
		crm004Response.setIncome_from(userResponse.getIncome_from());
		crm004Response.setIncome_status(userResponse.getIncome_status());
		crm004Response.setEntry_year(userResponse.getEntry_year());
		crm004Response.setLive_status(userResponse.getLive_status());
		crm004Response.setWork_unit(userResponse.getWork_unit());
		crm004Response.setPosition(userResponse.getPosition());
		crm004Response.setEdu_level(userResponse.getEdu_level());
		crm004Response.setMarr_status(userResponse.getMarr_status());
		crm004Response.setSpouse_name(userResponse.getSpouse_name());
		crm004Response.setSpouse_cert_type(userResponse.getSpouse_cert_type());
		crm004Response.setSpouse_cert_code(userResponse.getSpouse_cert_code());
		crm004Response.setSpouse_phone(userResponse.getSpouse_phone());
		crm004Response.setSpouse_income_from(userResponse.getSpouse_income_from());
		crm004Response.setSpouse_annual_income(userResponse.getSpouse_annual_income());
		crm004Response.setSpouse_work_unit(userResponse.getSpouse_work_unit());
		crm004Response.setSpouse_contact_addr(userResponse.getSpouse_contact_addr());
		crm004Response.setIndustry(userResponse.getIndustry());
		crm004Response.setMax_quota(userResponse.getMax_quota());
		crm004Response.setCompany_nature(userResponse.getCompany_nature());
		crm004Response.setWork_year(userResponse.getWork_year());
		crm004Response.setUnit_tel(userResponse.getUnit_tel());
		crm004Response.setEmail(userResponse.getEmail());
		crm004Response.setCert_org(userResponse.getCert_org());
		crm004Response.setRegular_deposit_amt(userResponse.getRegular_deposit_amt());
		crm004Response.setZip_code(userResponse.getZip_code());
		crm004Response.setCert_addr(userResponse.getCert_addr());
		crm004Response.setFamily_tel(userResponse.getFamily_tel());
		crm004Response.setContact_addr(userResponse.getContact_addr());
		
		List<Contact> contacts = userResponse.getContacts();
		List<CRM004ContactBean> Crm004Contacts = new ArrayList<CRM004ContactBean>();
		for(Contact contact : contacts) {
			CRM004ContactBean Crm004Contact = new CRM004ContactBean();
			Crm004Contact.setContact_name(contact.getContact_name());
			Crm004Contact.setIs_important_contact(contact.getIs_important_contact());
			Crm004Contact.setContact_cert_type(contact.getContact_cert_type());
			Crm004Contact.setContact_cert_code(contact.getContact_cert_code());
			Crm004Contact.setRelation(contact.getRelation());
			Crm004Contact.setContact_work_unit(contact.getContact_work_unit());
			Crm004Contact.setContact_email(contact.getContact_email());
			Crm004Contact.setContact_fax(contact.getContact_fax());
			Crm004Contact.setContact_phone(contact.getContact_phone());
			Crm004Contact.setContact_addr(contact.getContact_addr());
			Crm004Contact.setIs_send_sms(contact.getIs_send_sms());
			
			Crm004Contacts.add(Crm004Contact);
		}
		crm004Response.setContacts(Crm004Contacts);
		
		List<Account> accounts = userResponse.getAccounts();
		List<CRM004AccountBean> Crm004Accounts = new ArrayList<CRM004AccountBean>();
		for(Account account : accounts) {
			CRM004AccountBean Crm004Account = new CRM004AccountBean();
			Crm004Account.setAcc_name(account.getAcc_name());
			Crm004Account.setAcc_no(account.getAcc_no());
			Crm004Account.setBank_code(account.getBank_code());
			Crm004Account.setBank_full_name(account.getBank_full_name());
			Crm004Account.setBranch_bank_name(account.getBranch_bank_name());
			Crm004Account.setCurrency(account.getCurrency());
			Crm004Account.setWithhold_unit(account.getWithhold_unit());
			Crm004Account.setIs_withhold_acc(account.getIs_withhold_acc());
			Crm004Account.setAcc_status(account.getAcc_status());
			
			Crm004Accounts.add(Crm004Account);
		}
		crm004Response.setAccounts(Crm004Accounts);
		
		try {
			crm004Response.setSign(Security.getSign(crm004Response, key));
		} catch (IllegalArgumentException e) {
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}


	@Override
	public void resultException(CRM004Response crm004Response, String key, String code, String msg) {
		try {
			crm004Response.setResult_code(code);
			crm004Response.setResult_desc(msg);
			crm004Response.setSign(Security.getSign(crm004Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
	
	
	@Override
	public String generateResponseXML(CRM004Response crm004Response) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code>").append(crm004Response.getReturn_code()).append("</return_code>")
		.append("<return_desc>").append(crm004Response.getReturn_desc()).append("</return_desc>")
		.append("<req_serial_no>").append(crm004Response.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(crm004Response.getReq_date()).append("</req_date>")
		.append("<res_serial_no>").append(crm004Response.getRes_serial_no()).append("</res_serial_no>")
		.append("<res_date>").append(crm004Response.getRes_date()).append("</res_date>")
		.append("<sign>").append(crm004Response.getSign()).append("</sign>")
		.append("</head><body>");
		msgResponse.append(XMLHelper.getXMLFromBean(crm004Response));
		
		List<CRM004ContactBean> crm004Contacts = crm004Response.getContacts();
		if(null == crm004Contacts || crm004Contacts.isEmpty()) {
			msgResponse.append("<contacts/>");
		} else {
			msgResponse.append("<contacts>");
			for(CRM004ContactBean crm004Contact : crm004Contacts) {
				msgResponse.append("<contact>").append(XMLHelper.getXMLFromBean(crm004Contact)).append("</contact>");
			}
			msgResponse.append("</contacts>");
		}
		
		List<CRM004AccountBean> crm004Accounts = crm004Response.getAccounts();
		if(null == crm004Accounts || crm004Accounts.isEmpty()) {
			msgResponse.append("<accounts/>");
		} else {
			msgResponse.append("<accounts>");
			for(CRM004AccountBean crm004Account : crm004Accounts) {
				msgResponse.append("<account>").append(XMLHelper.getXMLFromBean(crm004Account)).append("</account>");
			}
			msgResponse.append("</accounts>");
		}
		msgResponse.append("</body></esb>");
		
		logger.info("ESB接口CRM004响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}
}
