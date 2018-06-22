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
import com.kpleasing.esb.model.crm011.BankAccount;
import com.kpleasing.esb.model.crm011.CRM011BankAccount;
import com.kpleasing.esb.model.crm011.CRM011Request;
import com.kpleasing.esb.model.crm011.CRM011Response;
import com.kpleasing.esb.model.crm011.GetBankAccountInfoRequest;
import com.kpleasing.esb.model.crm011.GetBankAccountInfoResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

/**
 * 
 * @author howard.huang
 *
 */
public class GetBankAccountInfoProcess extends AbstractCrmProcessor<CRM011Request, CRM011Response, GetBankAccountInfoRequest, GetBankAccountInfoResponse> {
	private static Logger logger = Logger.getLogger(GetBankAccountInfoProcess.class);
	
	@Override
	public CRM011Response initResponse() {
		return new CRM011Response();
	}
	
	
	@Override
	protected GetBankAccountInfoResponse initCrmResponse() {
		return new GetBankAccountInfoResponse();
	}

	
	@Override
	public GetBankAccountInfoRequest generateRequestEntity(CRM011Request crm011Repuest, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		GetBankAccountInfoRequest bankAccountRequest = new GetBankAccountInfoRequest();
		bankAccountRequest.setApi_code("GET_BANK_ACCOUNT_INFO");
		bankAccountRequest.setReq_serial_no(StringUtil.getSerialNo32());
		bankAccountRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		bankAccountRequest.setSecurity_code(config.getAppCode());
		bankAccountRequest.setSecurity_value(config.getAppSecurity());
		bankAccountRequest.setCust_id(crm011Repuest.getCust_id());
		bankAccountRequest.setAccount(crm011Repuest.getAccount());
		
		bankAccountRequest.setSign(Security.getSign(bankAccountRequest, config.getSignKey()));
	
		return bankAccountRequest;
	}
	
	
	@Override
	public GetBankAccountInfoResponse parseResponseEntity(String resultXml) throws ESBException {
		logger.info("响应报文明文：" + resultXml);
		try {
			logger.info("转换XML报文为实体Bean......");
			GetBankAccountInfoResponse bankAccountResponse = initCrmResponse();
			List<Map<String, String>> mapList = XMLHelper.parseMultNodesXml(resultXml, "/crm/head|/crm/body", false);
			for(Map<String, String> map : mapList) {
				BeanUtils.populate(bankAccountResponse, map);
			}
			
			logger.info("转换账户信息......");
			List<Map<String,String>> mapList1 = XMLHelper.parseMultNodesXml(resultXml, "//account");
			List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
			for(Map<String,String> map1 : mapList1) {
				BankAccount bankAccount = new BankAccount();
				BeanUtils.populate(bankAccount, map1);
				bankAccounts.add(bankAccount);
			}
			bankAccountResponse.setAccounts(bankAccounts);
			
			return bankAccountResponse;
		} catch (IllegalAccessException | InvocationTargetException | DocumentException e) {
			logger.error("请求报文XML解析处理错误." + e.getMessage(), e);
			throw new ESBException("FAILED", "XML解析失败！");
		}
	}
	

	@Override
	public void covertToEBody(GetBankAccountInfoResponse bankAccountResponse, CRM011Response crm011Response, String key) throws ESBException {
		crm011Response.setResult_code(bankAccountResponse.getResult_code());
		crm011Response.setResult_desc(bankAccountResponse.getResult_desc());
		crm011Response.setCust_id(bankAccountResponse.getCust_id());
		
		List<CRM011BankAccount> crm011BankAccounts = new ArrayList<CRM011BankAccount>();
		List<BankAccount> bankAccounts = bankAccountResponse.getAccounts();
		for(BankAccount bankAccount : bankAccounts) {
			CRM011BankAccount crm011BankAccount = new CRM011BankAccount();
			crm011BankAccount.setAccount_name(bankAccount.getAccount_name());
			crm011BankAccount.setAccount_id(bankAccount.getAccount_id());
			crm011BankAccount.setAccount_no(bankAccount.getAccount_no());
			crm011BankAccount.setShort_account_no(bankAccount.getShort_account_no());
			crm011BankAccount.setBank_name(bankAccount.getBank_name());
			crm011BankAccount.setBranch_bank_name(bankAccount.getBranch_bank_name());
			crm011BankAccount.setBank_code(bankAccount.getBank_code());
			crm011BankAccount.setBank_phone(bankAccount.getBank_phone());
			crm011BankAccount.setWithhold_unit(bankAccount.getWithhold_unit());
			crm011BankAccount.setIs_withhold_acc(bankAccount.getIs_withhold_acc());
			crm011BankAccount.setIs_yjzf_bind(bankAccount.getIs_yjzf_bind());
			
			crm011BankAccounts.add(crm011BankAccount);
		}
		crm011Response.setAccounts(crm011BankAccounts);
		
		try {
			crm011Response.setSign(Security.getSign(crm011Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}
	

	@Override
	public void resultException(CRM011Response crm011Response, String key, String code, String msg) {
		try {
			crm011Response.setResult_code(code);
			crm011Response.setResult_desc(msg);
			crm011Response.setSign(Security.getSign(crm011Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
	
	
	@Override
	public String generateResponseXML(CRM011Response crm011Response) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(crm011Response.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(crm011Response.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(crm011Response.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(crm011Response.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(crm011Response.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(crm011Response.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(crm011Response.getSign()).append("]]></sign>")
		.append("</head><body>");
		respXml.append(XMLHelper.getXMLFromBean(crm011Response));
		
		List<CRM011BankAccount> accounts = crm011Response.getAccounts();
		if(null == accounts || accounts.isEmpty()) {
			respXml.append("<accounts/>");
		} else {
			respXml.append("<accounts>");
			for(CRM011BankAccount account : accounts) {
				respXml.append("<account>").append(XMLHelper.getXMLFromBean(account)).append("</account>");
			}
			respXml.append("</accounts>");
		}
		respXml.append("</body></esb>");
		
		logger.info("ESB响应报文明文："+respXml.toString());
		return respXml.toString();
	}
}
