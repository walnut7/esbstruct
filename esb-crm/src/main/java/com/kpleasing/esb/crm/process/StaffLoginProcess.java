package com.kpleasing.esb.crm.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm005.CRM005Request;
import com.kpleasing.esb.model.crm005.CRM005Response;
import com.kpleasing.esb.model.crm005.StaffLoginRequest;
import com.kpleasing.esb.model.crm005.StaffLoginResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class StaffLoginProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(StaffLoginProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM005Request crm005Requ = getStaffInfoRequestObject((byte[]) in.getBody());
		
		CRM005Response crm005RespBean = generateCRM005ResponseBean(crm005Requ);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm005RespBean));
		logger.info("ESB接口CRM005响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}
	
	
	/**
	 * 创建响应实体Bean
	 * @param crm005Requ
	 * @return
	 */
	private CRM005Response generateCRM005ResponseBean(CRM005Request crm005Requ) {
		CRM005Response crm005Resp = new CRM005Response();
		
		crm005Resp.setReq_serial_no(crm005Requ.getReq_serial_no());
		crm005Resp.setReq_date(crm005Requ.getReq_date());
		crm005Resp.setRes_serial_no(StringUtil.getSerialNo32());
		crm005Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm005Resp.setReturn_code("SUCCESS");
		crm005Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = crm005Requ.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(crm005Requ.getSecurity_code().equals(cParam.getClientCode()) && 
					crm005Requ.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		try {
			StaffLoginRequest staffLoginRequest = getDestRequestBean(crm005Requ, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(staffLoginRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口STAFF_LOGIN返回明文信息：" + RespXml);
			
			StaffLoginResponse staffLoginResp = new StaffLoginResponse();
			XMLHelper.getBeanFromXML(RespXml, staffLoginResp);
			if("SUCCESS".equals(staffLoginResp.getReturn_code()) && "SUCCESS".equals(staffLoginResp.getResult_code())) {
				verificationReturnMsg(staffLoginResp, paramConfig.getSignKey());
			}
			
			crm005Resp.setResult_code(staffLoginResp.getResult_code());
			crm005Resp.setResult_desc(staffLoginResp.getResult_desc());
			crm005Resp.setStaff_name(staffLoginResp.getStaff_name());
			crm005Resp.setStaff_no(staffLoginResp.getStaff_no());
			crm005Resp.setStaff_email(staffLoginResp.getStaff_email());
			crm005Resp.setIs_modify_pwd(staffLoginResp.getIs_modify_pwd());
			crm005Resp.setPhone(staffLoginResp.getPhone());
			
		} catch(ESBException e) {
			crm005Resp.setResult_code(e.getCode());
			crm005Resp.setResult_desc(e.getDescription());
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			crm005Resp.setResult_code("FAILED");
			crm005Resp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				crm005Resp.setSign(Security.getSign(crm005Resp, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
		return crm005Resp;
	}


	/**
	 * 验证目标系统返回值
	 * @param staffLoginResp
	 * @param key
	 * @throws ESBException 
	 */
	private void verificationReturnMsg(StaffLoginResponse staffLoginResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = staffLoginResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "CRM API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		staffLoginResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(staffLoginResp, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "CRM API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "CRM API签名出錯!!!");
		}
	}


	/**
	 * 生成请求报文
	 * @param staffLoginRequest
	 * @return
	 */
	private String getRequestMsg(StaffLoginRequest staffLoginRequest) {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code>").append(staffLoginRequest.getApi_code()).append("</api_code>")
		.append("<req_serial_no>").append(staffLoginRequest.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(staffLoginRequest.getReq_date()).append("</req_date>")
		.append("<security_code>").append(staffLoginRequest.getSecurity_code()).append("</security_code>")
		.append("<security_value>").append(staffLoginRequest.getSecurity_value()).append("</security_value>")
		.append("<sign>").append(staffLoginRequest.getSign()).append("</sign>")
		.append("</head><body>")
		.append("<appcode>").append(staffLoginRequest.getAppcode()).append("</appcode>")
		.append("<login_id>").append(staffLoginRequest.getLogin_id()).append("</login_id>")
		.append("<password>").append(staffLoginRequest.getPassword()).append("</password>")
		.append("</body></crm>");
		
		logger.info("CRM接口STAFF_LOGIN请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	
	}


	/**
	 * 创建系统调用实体bean
	 * @param crm005Requ
	 * @param paramConfig
	 * @return
	 * @throws IllegalAccessException
	 */
	private StaffLoginRequest getDestRequestBean(CRM005Request crm005Requ, ParameterConfig paramConfig) throws IllegalAccessException {
		StaffLoginRequest staffLoginRequest = new StaffLoginRequest();
		staffLoginRequest.setApi_code("STAFF_LOGIN");
		staffLoginRequest.setReq_serial_no(StringUtil.getSerialNo32());
		staffLoginRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		staffLoginRequest.setSecurity_code(paramConfig.getAppCode());
		staffLoginRequest.setSecurity_value(paramConfig.getAppSecurity());
		staffLoginRequest.setAppcode(crm005Requ.getAppcode());
		staffLoginRequest.setLogin_id(crm005Requ.getLogin_id());
		staffLoginRequest.setPassword(crm005Requ.getPassword());
		staffLoginRequest.setSign(Security.getSign(staffLoginRequest, paramConfig.getSignKey()));
	
		return staffLoginRequest;
	}


	/**
	 * ESB响应 报文
	 * @param crm005RespBean
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String generateMsgResponseXML(CRM005Response respBean) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(respBean.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(respBean.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(respBean.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(respBean.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(respBean.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(respBean.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(respBean.getSign()).append("]]></sign>")
		.append("</head><body>");
		msgResponse.append(XMLHelper.getXMLFromBean(respBean));
//		msgResponse.append("<result_code><![CDATA[").append(respBean.getResult_code()).append("]]></result_code>")
//		.append("<result_desc><![CDATA[").append(respBean.getResult_desc()).append("]]></result_desc>")
//		.append("<staff_name><![CDATA[").append(respBean.getStaff_name()).append("]]></staff_name>")
//		.append("<staff_no><![CDATA[").append(respBean.getStaff_no()).append("]]></staff_no>")
//		.append("<staff_email><![CDATA[").append(respBean.getStaff_email()).append("]]></staff_email>")
//		.append("<is_modify_pwd><![CDATA[").append(respBean.getIs_modify_pwd()).append("]]></is_modify_pwd>")
//		.append("<phone><![CDATA[").append(respBean.getPhone()).append("]]></phone>");
		msgResponse.append("</body></esb>");
		
		logger.info("ESB接口CRM005响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}

	
	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	private CRM005Request getStaffInfoRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM005Request crm005Request = (CRM005Request) ois.readObject();
			
			return crm005Request;

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
