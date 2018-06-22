package com.kpleasing.esb.pub.process;

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
import com.kpleasing.esb.model.crm007.CRM007Request;
import com.kpleasing.esb.model.crm007.CRM007Response;
import com.kpleasing.esb.model.leasing002.LEASING002AccountInfo;
import com.kpleasing.esb.model.leasing002.LEASING002Request;
import com.kpleasing.esb.model.leasing002.LEASING002Response;
import com.kpleasing.esb.model.pub001.PUB001Request;
import com.kpleasing.esb.model.pub001.PUB001Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class PerfectCustomerInfoProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(PerfectCustomerInfoProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) in.getHeader("DESKEY");
		String DESIv = (String) in.getHeader("DESIV");
		PUB001Request pubRequest = getPUB001RequestObject((byte[]) in.getBody());
		
		PUB001Response respBean = generatePUB001Response(pubRequest);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(respBean));
		logger.info("ESB接口PUB001响应报文密文："+msgResponse);
		exchange.getOut().setBody(msgResponse);
	}

	
	/**
	 * 
	 * @param pubRequest
	 * @return
	 */
	private PUB001Response generatePUB001Response(PUB001Request pubRequest) {
		PUB001Response pub001Response = new PUB001Response();
		pub001Response.setReq_serial_no(pubRequest.getReq_serial_no());
		pub001Response.setReq_date(pubRequest.getReq_date());
		pub001Response.setRes_serial_no(StringUtil.getSerialNo32());
		pub001Response.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		pub001Response.setReturn_code("SUCCESS");
		pub001Response.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = pubRequest.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(pubRequest.getSecurity_code().equals(cParam.getClientCode()) && 
					pubRequest.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		
		try {
			// 推送补充资料至CRM
//			CRM007Request crm007Request = getCrmDestRequestBean(pubRequest, paramConfig);
//			String reqCrmMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getCrmRequestMsg(crm007Request));
//			
//			logger.info("开始发起CRM数据推送请求.......");
//			String respCrmMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqCrmMsg);
//			String RespCrmXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respCrmMsg);
//			logger.info("ESB接口CRM007返回明文信息：" + RespCrmXml);
//			
//			CRM007Response crm007Response = new CRM007Response();
//			XMLHelper.getBeanFromXML(RespCrmXml, crm007Response);
//			if("SUCCESS".equals(crm007Response.getReturn_code()) && "SUCCESS".equals(crm007Response.getResult_code())) {
//				verificationCrmReturnMsg(crm007Response, paramConfig.getSignKey());
//			}
			
			// 推送资料至租赁业务系统
			LEASING002Request leasing002Request = getLeasingDestRequestBean(pubRequest, paramConfig);
			String reqLeasingMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getLeasingRequestMsg(leasing002Request));
			
			logger.info("开始发起LEASING数据推送请求.......");
			String respLeasingMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqLeasingMsg);
			String RespLeasingXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respLeasingMsg);
			logger.info("ESB接口LEASING002返回明文信息：" + RespLeasingXml);
			
			LEASING002Response leasing002Resp = new LEASING002Response();
			XMLHelper.getBeanFromXML(RespLeasingXml, leasing002Resp);
			if("SUCCESS".equals(leasing002Resp.getReturn_code()) && "SUCCESS".equals(leasing002Resp.getResult_code())) {
				verificationLeasingReturnMsg(leasing002Resp, paramConfig.getSignKey());
			}
			
			pub001Response.setResult_code("SUCCESS");
			pub001Response.setResult_desc("操作成功");
			
		} catch(ESBException e) {
			pub001Response.setResult_code(e.getCode());
			pub001Response.setResult_desc(e.getDescription());
			
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			pub001Response.setResult_code("FAILED");
			pub001Response.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				pub001Response.setSign(Security.getSign(pub001Response, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
	
		return pub001Response;
	}
	
	
	private void verificationLeasingReturnMsg(LEASING002Response leasing002Resp, String signKey) {
		// TODO Auto-generated method stub
		
	}


	private void verificationCrmReturnMsg(CRM007Response crm007Response, String signKey) {
		// TODO Auto-generated method stub
		
	}


	private String getCrmRequestMsg(CRM007Request crm007Request) {
		// TODO Auto-generated method stub
		return null;
	}


	private CRM007Request getCrmDestRequestBean(PUB001Request pubRequest, ParameterConfig paramConfig) {
		// TODO Auto-generated method stub
		return null;
	}


	private String getLeasingRequestMsg(LEASING002Request leasing002Request) {
		// TODO Auto-generated method stub
		return null;
	}


	private LEASING002Request getLeasingDestRequestBean(PUB001Request pubRequest,
			ParameterConfig paramConfig) {
		LEASING002Request leasing002Request = new LEASING002Request();
		leasing002Request.setCust_id(pubRequest.getCust_id());
		leasing002Request.setCust_name(pubRequest.getCust_name());
		leasing002Request.setCert_type("");
		leasing002Request.setCert_code("");
		leasing002Request.setPhone("");
		leasing002Request.setCust_name_spell("");
		leasing002Request.setBirthday("");
		leasing002Request.setGender("");
		leasing002Request.setNation("");
		leasing002Request.setDrive_model("");
		leasing002Request.setAnnual_income("");
		leasing002Request.setRel_flag("");
		leasing002Request.setIncome_from("");
		leasing002Request.setIncome_status("");
		leasing002Request.setEntry_year("");
		leasing002Request.setLive_status("");
		leasing002Request.setWork_unit("");
		leasing002Request.setWork_addr("");
		leasing002Request.setPosition("");
		leasing002Request.setEdu_level("");
		leasing002Request.setMarr_status("");
		leasing002Request.setSpouse_name("");
		leasing002Request.setSpouse_cert_type("");
		leasing002Request.setSpouse_cert_code("");
		leasing002Request.setSpouse_phone("");
		leasing002Request.setSpouse_income_from("");
		leasing002Request.setSpouse_annual_income("");
		leasing002Request.setSpouse_work_unit("");
		leasing002Request.setSpouse_contact_addr("");
		leasing002Request.setIndustry("");
		leasing002Request.setMax_quota("");
		leasing002Request.setWork_year("");
		leasing002Request.setUnit_tel("");
		leasing002Request.setEmail("");
		leasing002Request.setCert_org("");
		leasing002Request.setRegular_deposit_amt("");
		leasing002Request.setZip_code("");
		leasing002Request.setCert_addr("");
		leasing002Request.setFamily_tel("");
		leasing002Request.setContact_address("");
		leasing002Request.setCust_type("");
		leasing002Request.setCust_memo("");
		leasing002Request.setCust_status("");
		leasing002Request.setMemo("");
		
		
		List<LEASING002AccountInfo> accounts = new ArrayList<LEASING002AccountInfo>();
		LEASING002AccountInfo account = new LEASING002AccountInfo();
//		private String aid;
//		
//		private String acc_name;
//		
//		private String acc_no;
//		
//		private String bank_code;
//		
//		private String bank_full_name; 
//		
//		private String branch_bank_name;
//		
//		private String currency;
//		
//		private String withhold_unit;
//		
//		private String is_withhold_acc;
//		
//		private String acc_status;
		
		
		
//		
//		private List<LEASING002ContactInfo> contacts;
//		
//		private List<LEASING002AccountInfo> accounts;



		
		
		
		
		
		
		
		return leasing002Request;
	}


	/**
	 * esb 响应报文
	 * @param respBean
	 * @return
	 */
	private String generateMsgResponseXML(PUB001Response respBean) {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(respBean.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(respBean.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(respBean.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(respBean.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(respBean.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(respBean.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(respBean.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<result_code><![CDATA[").append(respBean.getResult_code()).append("]]></result_code>")
		.append("<result_desc><![CDATA[").append(respBean.getResult_desc()).append("]]></result_desc>")
		.append("</body></esb>");
		
		logger.info("ESB响应报文明文"+respXml.toString());
		return respXml.toString();
	}

	
	
	/**
	 *  反序列化参数
	 * @param body
	 * @return
	 */
	private PUB001Request getPUB001RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			PUB001Request pub001Request = (PUB001Request) ois.readObject();

			return pub001Request;

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
