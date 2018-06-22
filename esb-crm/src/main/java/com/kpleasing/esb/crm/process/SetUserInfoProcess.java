package com.kpleasing.esb.crm.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm007.CRM007AccountBean;
import com.kpleasing.esb.model.crm007.CRM007ContactBean;
import com.kpleasing.esb.model.crm007.CRM007Request;
import com.kpleasing.esb.model.crm007.CRM007Response;
import com.kpleasing.esb.model.crm007.SetUserInfoAccount;
import com.kpleasing.esb.model.crm007.SetUserInfoContact;
import com.kpleasing.esb.model.crm007.SetUserInfoRequest;
import com.kpleasing.esb.model.crm007.SetUserInfoResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SetUserInfoProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(SetUserInfoProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM007Request crm007Requ = getSetUserInfoRequestObject((byte[]) in.getBody());

		CRM007Response crm007RespBean = generateCRM007ResponseBean(crm007Requ);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm007RespBean));
		logger.info("ESB接口CRM007响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}
	
	
	/**
	 * 创建响应实体Bean
	 * @param crm007Requ
	 * @return
	 */
	private CRM007Response generateCRM007ResponseBean(CRM007Request crm007Requ) {
		CRM007Response crm007Resp = new CRM007Response();
		crm007Resp.setReq_serial_no(crm007Requ.getReq_serial_no());
		crm007Resp.setReq_date(crm007Requ.getReq_date());
		crm007Resp.setRes_serial_no(StringUtil.getSerialNo32());
		crm007Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm007Resp.setReturn_code("SUCCESS");
		crm007Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = crm007Requ.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(crm007Requ.getSecurity_code().equals(cParam.getClientCode()) && 
					crm007Requ.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		try {
			SetUserInfoRequest setUserInfoRequest = getDestRequestBean(crm007Requ, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(setUserInfoRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口SET_USER_INFO返回明文信息：" + RespXml);
			
			SetUserInfoResponse setUserInfoResp = new SetUserInfoResponse();
			XMLHelper.getBeanFromXML(RespXml, setUserInfoResp);
			if("SUCCESS".equals(setUserInfoResp.getReturn_code()) && "SUCCESS".equals(setUserInfoResp.getResult_code())) {
				verificationReturnMsg(setUserInfoResp, paramConfig.getSignKey());
			}
			
			crm007Resp.setResult_code(setUserInfoResp.getResult_code());
			crm007Resp.setResult_desc(setUserInfoResp.getResult_desc());
			
		} catch(ESBException e) {
			crm007Resp.setResult_code(e.getCode());
			crm007Resp.setResult_desc(e.getDescription());
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			crm007Resp.setResult_code("FAILED");
			crm007Resp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				crm007Resp.setSign(Security.getSign(crm007Resp, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
		return crm007Resp;
	}
	
	
	/**
	 * 报文校验
	 * @param setUserInfoResp
	 * @param key
	 */
	private void verificationReturnMsg(SetUserInfoResponse setUserInfoResp, String key) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 生成CRM SET_USER_INFO请求报文
	 * @param setUserInfoRequest
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException 
	 */
	private String getRequestMsg(SetUserInfoRequest setUserInfoRequest) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(setUserInfoRequest.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(setUserInfoRequest.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(setUserInfoRequest.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(setUserInfoRequest.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(setUserInfoRequest.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(setUserInfoRequest.getSign()).append("]]></sign>")
		.append("</head><body>");
		
		msgRequest.append(XMLHelper.getXMLFromBean(setUserInfoRequest));
		
		List<SetUserInfoContact> contacts = setUserInfoRequest.getContacts();
		if(null == contacts || contacts.isEmpty()) {
			msgRequest.append("<contacts/>");
		} else {
			msgRequest.append("<contacts>");
			for(SetUserInfoContact contact : contacts) {
				msgRequest.append("<contact>").append(XMLHelper.getXMLFromBean(contact)).append("</contact>");
			}
			msgRequest.append("</contacts>");
		}
		
		List<SetUserInfoAccount> accounts = setUserInfoRequest.getAccounts();
		if(null == accounts || accounts.isEmpty()) {
			msgRequest.append("<accounts/>");
		} else {
			msgRequest.append("<accounts>");
			for(SetUserInfoAccount account : accounts) {
				msgRequest.append("<account>").append(XMLHelper.getXMLFromBean(account)).append("</account>");
			}
			msgRequest.append("</accounts>");
		}
		msgRequest.append("</body></crm>");
		
		logger.info("CRM接口SET_USER_INFO请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	}


	
	/**
	 * 生成CRM SET_USER_INFO请求实体
	 * @param crm007Requ
	 * @param paramConfig
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private SetUserInfoRequest getDestRequestBean(CRM007Request crm007Requ, ParameterConfig paramConfig) throws IllegalArgumentException, IllegalAccessException {
		SetUserInfoRequest setUserInfoRequest = new SetUserInfoRequest();
		setUserInfoRequest.setApi_code("SET_USER_INFO");
		setUserInfoRequest.setReq_serial_no(StringUtil.getSerialNo32());
		setUserInfoRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		setUserInfoRequest.setSecurity_code(paramConfig.getAppCode());
		setUserInfoRequest.setSecurity_value(paramConfig.getAppSecurity());
		
		logger.info("转换客户详情信息.......");
		setUserInfoRequest.setCust_id(crm007Requ.getCust_id());
		setUserInfoRequest.setCust_name(crm007Requ.getCust_name());
		setUserInfoRequest.setCert_type(crm007Requ.getCert_type());
		setUserInfoRequest.setCert_code(crm007Requ.getCert_code());
		setUserInfoRequest.setPhone(crm007Requ.getPhone());
		setUserInfoRequest.setCust_name_spell(crm007Requ.getCust_name_spell());
		setUserInfoRequest.setBirthday(crm007Requ.getBirthday());
		setUserInfoRequest.setGender(crm007Requ.getGender());
		setUserInfoRequest.setNation(crm007Requ.getNation());
		setUserInfoRequest.setDrive_model(crm007Requ.getDrive_model());
		setUserInfoRequest.setAnnual_income(crm007Requ.getAnnual_income());
		setUserInfoRequest.setRel_flag(crm007Requ.getRel_flag());
		setUserInfoRequest.setIncome_from(crm007Requ.getIncome_from());
		setUserInfoRequest.setIncome_status(crm007Requ.getIncome_status());
		setUserInfoRequest.setEntry_year(crm007Requ.getEntry_year());
		setUserInfoRequest.setLive_status(crm007Requ.getLive_status());
		setUserInfoRequest.setWork_unit(crm007Requ.getWork_unit());
		setUserInfoRequest.setWork_addr(crm007Requ.getWork_addr());
		setUserInfoRequest.setPosition(crm007Requ.getPosition());
		setUserInfoRequest.setEdu_level(crm007Requ.getEdu_level());
		setUserInfoRequest.setMarr_status(crm007Requ.getMarr_status());
		setUserInfoRequest.setSpouse_name(crm007Requ.getSpouse_name());
		setUserInfoRequest.setSpouse_cert_type(crm007Requ.getSpouse_cert_type());
		setUserInfoRequest.setSpouse_cert_code(crm007Requ.getSpouse_cert_code());
		setUserInfoRequest.setSpouse_phone(crm007Requ.getSpouse_phone());
		setUserInfoRequest.setSpouse_income_from(crm007Requ.getSpouse_income_from());
		setUserInfoRequest.setSpouse_annual_income(crm007Requ.getSpouse_annual_income());
		setUserInfoRequest.setSpouse_work_unit(crm007Requ.getSpouse_work_unit());
		setUserInfoRequest.setSpouse_contact_addr(crm007Requ.getSpouse_contact_addr());
		setUserInfoRequest.setIndustry(crm007Requ.getIndustry());
		setUserInfoRequest.setMax_quota(crm007Requ.getMax_quota());
		setUserInfoRequest.setWork_year(crm007Requ.getWork_year());
		setUserInfoRequest.setUnit_tel(crm007Requ.getUnit_tel());
		setUserInfoRequest.setEmail(crm007Requ.getEmail());
		setUserInfoRequest.setCert_org(crm007Requ.getCert_org());
		setUserInfoRequest.setRegular_deposit_amt(crm007Requ.getRegular_deposit_amt());
		setUserInfoRequest.setZip_code(crm007Requ.getZip_code());
		setUserInfoRequest.setCert_addr(crm007Requ.getCert_addr());
		setUserInfoRequest.setFamily_tel(crm007Requ.getFamily_tel());
		setUserInfoRequest.setContact_address(crm007Requ.getContact_addr());
		setUserInfoRequest.setCust_type(crm007Requ.getCust_type());
		setUserInfoRequest.setCust_memo(crm007Requ.getCust_memo());
		setUserInfoRequest.setCust_status(crm007Requ.getCust_status());
		setUserInfoRequest.setMemo(crm007Requ.getMemo());
		setUserInfoRequest.setIdcard_front_img(crm007Requ.getIdcard_front_img());
		setUserInfoRequest.setIf_file_type(crm007Requ.getIf_file_type());
		setUserInfoRequest.setIdcard_back_img(crm007Requ.getIdcard_back_img());
		setUserInfoRequest.setIb_file_type(crm007Requ.getIb_file_type());
		setUserInfoRequest.setDrivelicense_img(crm007Requ.getDrivelicense_img());
		setUserInfoRequest.setDf_file_type(crm007Requ.getDf_file_type());
		setUserInfoRequest.setDrivelicense_back_img(crm007Requ.getDrivelicense_back_img());
		setUserInfoRequest.setDb_file_type(crm007Requ.getDb_file_type());
		setUserInfoRequest.setBankcard_front_img(crm007Requ.getBankcard_front_img());
		setUserInfoRequest.setBf_file_type(crm007Requ.getBf_file_type());
		setUserInfoRequest.setBankcard_back_img(crm007Requ.getBankcard_back_img());
		setUserInfoRequest.setBb_file_type(crm007Requ.getBb_file_type());
		
		
		logger.info("转换客户联系人信息.......");
		List<SetUserInfoContact> contacts = new ArrayList<SetUserInfoContact>();
		List<CRM007ContactBean> crm007Contacts = crm007Requ.getContacts();
		for(CRM007ContactBean crm007Contact : crm007Contacts) {
			SetUserInfoContact contact = new SetUserInfoContact();
			contact.setContact_name(crm007Contact.getContact_name());
			contact.setIs_important_contact(crm007Contact.getIs_important_contact());
			contact.setContact_cert_type(crm007Contact.getContact_cert_type());
			contact.setContact_cert_code(crm007Contact.getContact_cert_code());
			contact.setRelation(crm007Contact.getRelation());
			contact.setContact_work_unit(crm007Contact.getContact_work_unit());
			contact.setContact_phone(crm007Contact.getContact_phone());
			contact.setContact_email(crm007Contact.getContact_email());
			contact.setContact_fax(crm007Contact.getContact_fax());
			contact.setContact_addr(crm007Contact.getContact_addr());
			
			contacts.add(contact);
		}
		setUserInfoRequest.setContacts(contacts);
		
		logger.info("转换客户账户信息.......");
		List<SetUserInfoAccount> accounts = new ArrayList<SetUserInfoAccount>();
		List<CRM007AccountBean> crm007Accounts = crm007Requ.getAccounts();
		for(CRM007AccountBean crm007Account : crm007Accounts) {
			SetUserInfoAccount account = new SetUserInfoAccount();
			account.setAcc_name(crm007Account.getAcc_name());
			account.setAcc_no(crm007Account.getAcc_no());
			account.setBank_code(crm007Account.getBank_code());
			account.setBank_full_name(crm007Account.getBank_full_name());
			account.setBranch_bank_name(crm007Account.getBranch_bank_name());
			account.setBank_phone(crm007Account.getBank_phone());
			account.setCurrency(crm007Account.getCurrency());
			account.setWithhold_unit(crm007Account.getWithhold_unit());
			account.setIs_withhold_acc(crm007Account.getIs_withhold_acc());
			
			accounts.add(account);
		}
		setUserInfoRequest.setAccounts(accounts);
		
		logger.info("签名...");
		setUserInfoRequest.setSign(Security.getSign(setUserInfoRequest, paramConfig.getSignKey()));
	
		return setUserInfoRequest;
	}

	
	/**
	 * ESB 响应报文
	 * @param crm007RespBean
	 * @return
	 */
	private String generateMsgResponseXML(CRM007Response crm007RespBean) {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code>").append(crm007RespBean.getReturn_code()).append("</return_code>")
		.append("<return_desc>").append(crm007RespBean.getReturn_desc()).append("</return_desc>")
		.append("<req_serial_no>").append(crm007RespBean.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(crm007RespBean.getReq_date()).append("</req_date>")
		.append("<res_serial_no>").append(crm007RespBean.getRes_serial_no()).append("</res_serial_no>")
		.append("<res_date>").append(crm007RespBean.getRes_date()).append("</res_date>")
		.append("<sign>").append(crm007RespBean.getSign()).append("</sign>")
		.append("</head><body>")
		.append("<result_code>").append(crm007RespBean.getResult_code()).append("</result_code>")
		.append("<result_desc>").append(crm007RespBean.getResult_desc()).append("</result_desc>")
		.append("</body></esb>");
		
		logger.info("ESB接口CRM007响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}

	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	private CRM007Request getSetUserInfoRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM007Request crm007Request = (CRM007Request) ois.readObject();
			
			return crm007Request;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != baiStream) {
				try {
					baiStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != ois) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
