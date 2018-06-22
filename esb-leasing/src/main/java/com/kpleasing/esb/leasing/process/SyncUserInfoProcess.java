package com.kpleasing.esb.leasing.process;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.leasing.AbstractLeasingProcessor;
import com.kpleasing.esb.model.leasing002.LEASING002AccountInfo;
import com.kpleasing.esb.model.leasing002.LEASING002AttachmentInfo;
import com.kpleasing.esb.model.leasing002.LEASING002ContactInfo;
import com.kpleasing.esb.model.leasing002.LEASING002Request;
import com.kpleasing.esb.model.leasing002.LEASING002Response;
import com.kpleasing.esb.model.leasing002.SyncUserInfoRequest;
import com.kpleasing.esb.model.leasing002.SyncUserInfoResponse;
import com.kpleasing.esb.model.leasing002.UserAccountInfo;
import com.kpleasing.esb.model.leasing002.UserAttachmentInfo;
import com.kpleasing.esb.model.leasing002.UserContactInfo;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncUserInfoProcess extends AbstractLeasingProcessor<LEASING002Request, LEASING002Response, SyncUserInfoRequest, SyncUserInfoResponse>  {
	
	private static Logger logger = Logger.getLogger(SyncUserInfoProcess.class);

	@Override
	public LEASING002Response initResponse() {
		return new LEASING002Response();
	}


	@Override
	public SyncUserInfoRequest generateRequestEntity(LEASING002Request leasing002Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		SyncUserInfoRequest sui = new SyncUserInfoRequest();
		sui.setRequest_number(StringUtil.getDateSerialNo6());
		sui.setUser_account(config.getAppCode());
		sui.setPassword(config.getAppSecurity());
		sui.setEsb_bp_id(leasing002Request.getCust_id());
		sui.setWxFlag(leasing002Request.getWxFlag());		//
		sui.setBp_name(leasing002Request.getCust_name());
		sui.setBp_name_spell(leasing002Request.getCust_name_spell());
		sui.setId_type(leasing002Request.getCert_type());
		sui.setId_card_no(leasing002Request.getCert_code());
		sui.setLocal_person_flag(leasing002Request.getRel_flag());
		sui.setGender(leasing002Request.getGender());
		sui.setDate_of_birth(leasing002Request.getBirthday());
		sui.setAcademic_background(leasing002Request.getEdu_level());
		sui.setMarital_status(leasing002Request.getMarr_status());
		sui.setEntire_period(leasing002Request.getWork_year());
		sui.setLicensing_organization(leasing002Request.getCert_org());
		sui.setDeposit_certificate(leasing002Request.getRegular_deposit_amt());
		sui.setNationality(leasing002Request.getNation());
		sui.setDriver_license(leasing002Request.getDrive_model());
		sui.setAnnual_income(leasing002Request.getAnnual_income());
		sui.setIncome_from(leasing002Request.getIncome_from());
		sui.setIncome_status(leasing002Request.getIncome_status());
		sui.setWorking_duration(leasing002Request.getEntry_year());
		sui.setWorking_place(leasing002Request.getWork_unit());
		sui.setOffice_address(leasing002Request.getWork_addr());
		sui.setPosition(leasing002Request.getPosition());
		sui.setIndustry(leasing002Request.getIndustry());
		sui.setCredit_limit(leasing002Request.getMax_quota());
		sui.setPhone(leasing002Request.getUnit_tel());
		sui.setCell_phone(leasing002Request.getPhone());
		sui.setEmail(leasing002Request.getEmail());
		sui.setPostcode(leasing002Request.getZip_code());
		sui.setHome_phone(leasing002Request.getFamily_tel());
		sui.setLive_status(leasing002Request.getLive_status());
		sui.setId_address(leasing002Request.getCert_addr());
		sui.setContact_address(leasing002Request.getContact_address());
		if(leasing002Request.getCust_type() != null && "3".equals(leasing002Request.getCust_type())){
			sui.setBlacklist_flag("Y");
			sui.setBlacklist_note(leasing002Request.getCust_memo());
		}
		sui.setEnabled_flag(leasing002Request.getCust_status());
		sui.setNote(leasing002Request.getMemo());
		sui.setBp_name_sp(leasing002Request.getSpouse_name());
		sui.setId_type_sp(leasing002Request.getSpouse_cert_type());
		sui.setId_card_no_sp(leasing002Request.getSpouse_cert_code());
		sui.setSpouse_phone(leasing002Request.getSpouse_phone());
		sui.setSpouse_income_from(leasing002Request.getSpouse_income_from());
		sui.setSpouse_annual_income(leasing002Request.getSpouse_annual_income()+"");
		sui.setSpouse_work_unit(leasing002Request.getSpouse_work_unit());
		sui.setAddress_sp(leasing002Request.getSpouse_contact_addr());
		
		List<LEASING002ContactInfo> cont_list = leasing002Request.getContacts();
		if(cont_list != null && cont_list.size()>0){
			List<UserContactInfo> uci_list = new ArrayList<UserContactInfo>();
			for(LEASING002ContactInfo ci:cont_list){
				UserContactInfo tmp_obj = new UserContactInfo();
				tmp_obj.setEsb_contact_id(ci.getCid());
				tmp_obj.setContact_person(ci.getContact_name());
				tmp_obj.setEmergent_person_flag(ci.getIs_important_contact());
				tmp_obj.setRelation(ci.getRelation());
				tmp_obj.setId_type(ci.getContact_cert_type());
				tmp_obj.setId_card_no(ci.getContact_cert_code());
				tmp_obj.setCell_phone(ci.getContact_phone());
				tmp_obj.setFax(ci.getContact_fax());
				tmp_obj.setEmail(ci.getContact_email());
				tmp_obj.setContact_info(ci.getContact_addr());
				tmp_obj.setWork_unit(ci.getContact_work_unit());
				tmp_obj.setSend_sms_flag(ci.getIs_send_sms());
				uci_list.add(tmp_obj);
			}
			sui.setContactInfo(uci_list);
		}
		
		List<LEASING002AccountInfo> acc_list = leasing002Request.getAccounts();
		if(acc_list != null && acc_list.size()>0){
			List<UserAccountInfo> uai_list = new ArrayList<UserAccountInfo>();
			for(LEASING002AccountInfo ai:acc_list){
				UserAccountInfo tmp_acc = new UserAccountInfo();
				tmp_acc.setEsb_bank_account_id(ai.getAid()+"");
				tmp_acc.setBank_account_name(ai.getAcc_name());
				tmp_acc.setDeduction_bank_code(ai.getBank_code());
				tmp_acc.setBank_full_name(ai.getBank_full_name());
				tmp_acc.setBank_account_num(ai.getAcc_no());
				tmp_acc.setBank_branch_name(ai.getBranch_bank_name());
				tmp_acc.setCurrency(ai.getCurrency());
				tmp_acc.setDeduction_flag(ai.getIs_withhold_acc());
				tmp_acc.setDeduction_agency_id(ai.getWithhold_unit());
				tmp_acc.setCell_phone(ai.getBank_phone());
				tmp_acc.setStorable_card_no(ai.getAbbreviation_card_no());
				if(ai.getIs_yjzf_bind() !=null && "1".equals(ai.getIs_yjzf_bind())){
					tmp_acc.setInd_auth_flag("Y");
				}else{
					tmp_acc.setInd_auth_flag("N");
				}
				tmp_acc.setEnabled_flag(ai.getAcc_status()==null?"N":ai.getAcc_status());
				uai_list.add(tmp_acc);
			}
			sui.setAccountInfo(uai_list);
		}
		
		List<LEASING002AttachmentInfo> att_list = leasing002Request.getAttachs();
		if(att_list != null && att_list.size()>0){
			List<UserAttachmentInfo> ai_list = new ArrayList<UserAttachmentInfo>();
			for(LEASING002AttachmentInfo att:att_list){
				UserAttachmentInfo ai = new UserAttachmentInfo();
				ai.setAttach_name(att.getAttach_name());
				ai.setAttach_type(att.getAttach_type());
				ai.setAttach_url(att.getAttach_url());
				ai.setFile_type_code(att.getFile_type_code());
				ai_list.add(ai);
			}
			sui.setAttachs(ai_list);
		}
		
		logger.info("Sign Before MD5："+sui.getSignContent()+config.getSignKey());
		String sign = Security.MD5Encode(sui.getSignContent()+config.getSignKey()).toUpperCase();
		logger.info("Sign Result："+sign);
		sui.setSign(sign);
		
		return sui;
	}


	@Override
	public String generateRequestXML(SyncUserInfoRequest syncUserInfoRequest, ParameterConfig config) throws Exception {
		StringBuilder reqXml = new StringBuilder();
		reqXml.append("<?xml version=\"1.0\"?>")
		.append("<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sch='http://www.aurora-framework.org/schema/'>")
		.append("<soapenv:Header/>")
		.append("<soapenv:Body>")
		.append("<sch:REQUEST>")
		.append("<sch:HEADER>")
		.append("<sch:request_number>").append(syncUserInfoRequest.getRequest_number()).append("</sch:request_number>")
		.append("<sch:user_account>").append(syncUserInfoRequest.getUser_account()).append("</sch:user_account>")
		.append("<sch:password>").append(syncUserInfoRequest.getPassword()).append("</sch:password>")
		.append("<sch:sign>").append(syncUserInfoRequest.getSign()).append("</sch:sign>")
		.append("</sch:HEADER>")
		.append("<sch:CONTEXT>");
		
		syncUserInfoRequest.setRequest_number("");
		syncUserInfoRequest.setUser_account("");
		syncUserInfoRequest.setPassword("");
		syncUserInfoRequest.setSign("");
		reqXml.append(XMLHelper.getXMLFromBean(syncUserInfoRequest, "sch:"));
		
		List<UserContactInfo> cont_list = syncUserInfoRequest.getContactInfo();
		if(cont_list != null && cont_list.size()>0){
			reqXml.append("<sch:CONTACT_INFO>");
			for(UserContactInfo uci :cont_list){
				reqXml.append("<sch:RECORD>");
				reqXml.append(XMLHelper.getXMLFromBean(uci, "sch:"));
				reqXml.append("</sch:RECORD>");
			}
			reqXml.append("</sch:CONTACT_INFO>");
		}
        
        List<UserAccountInfo> acc_list = syncUserInfoRequest.getAccountInfo();
        if(acc_list != null && acc_list.size()>0){
        	reqXml.append("<sch:BANK_INFO>");
	        for(UserAccountInfo uai : acc_list){
	        	reqXml.append("<sch:RECORD>");
	        	reqXml.append(XMLHelper.getXMLFromBean(uai, "sch:"));
	        	reqXml.append("</sch:RECORD>");
	        }
	        reqXml.append("</sch:BANK_INFO>");
        }
        
        List<UserAttachmentInfo> att_list = syncUserInfoRequest.getAttachs();
        if(att_list != null && att_list.size()>0){
        	reqXml.append("<sch:ATTACHMENT_INFO>");
	        for(UserAttachmentInfo ai : att_list){
	        	reqXml.append("<sch:RECORD>");
	        	reqXml.append(XMLHelper.getXMLFromBean(ai, "sch:"));
	        	reqXml.append("</sch:RECORD>");
	        }
	        reqXml.append("</sch:ATTACHMENT_INFO>");
        }
        
        reqXml.append("</sch:CONTEXT>")
		.append("</sch:REQUEST>")
		.append("</soapenv:Body>")
		.append("</soapenv:Envelope>");
        
		return reqXml.toString();
	}


	@Override
	public SyncUserInfoResponse parseResponseEntity(String resultXml) throws ESBException {
		try {
			SyncUserInfoResponse syncUserInfoResponse = new SyncUserInfoResponse();

			Map<String, String> nsMap = new HashMap<String, String>();
			nsMap.put("ns", "http://www.aurora-framework.org/schema");
			
			// 项目信息解析
			XMLHelper.parseSingleNodeXml(resultXml, nsMap, "//ns:soapResponse", syncUserInfoResponse);
			
			return syncUserInfoResponse;
			
		} catch (DocumentException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (IllegalAccessException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		} catch (InvocationTargetException e) {
			logger.error("请求报文XML解析处理错误."+e.getMessage(), e);
			throw new ESBException("FAILED", "LEASING-PayAuthentication解析失败！");
		}
	}


	@Override
	public void covertToEBody(SyncUserInfoResponse syncUserInfoResponse, LEASING002Response leasing002Response, String key) throws ESBException {
		if(syncUserInfoResponse.getStatus()!=null && "Y".equals(syncUserInfoResponse.getStatus())) {
			leasing002Response.setResult_code("SUCCESS");
		} else {
			leasing002Response.setResult_code("FAILED");
		}
		leasing002Response.setResult_desc(syncUserInfoResponse.getMessage());

		try {
			leasing002Response.setSign(Security.getSign(leasing002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		} catch (IllegalAccessException e) {
			logger.error("签名出错:"+e.getMessage(), e);
			throw new ESBException("FAILED", "签名出错！");
		}
	}


	@Override
	public void resultException(LEASING002Response leasing002Response, String key, String code, String msg) {
		try {
			leasing002Response.setResult_code(code);
			leasing002Response.setResult_desc(msg);
			leasing002Response.setSign(Security.getSign(leasing002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
