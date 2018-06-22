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
import com.kpleasing.esb.model.crm006.CRM006Request;
import com.kpleasing.esb.model.crm006.CRM006Response;
import com.kpleasing.esb.model.crm006.StaffModifyPwdRequest;
import com.kpleasing.esb.model.crm006.StaffModifyPwdResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class StaffModifyPwdProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(StaffModifyPwdProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		CRM006Request crm006Requ = getCRM006RequestObject((byte[]) in.getBody());
		
		CRM006Response crm006RespBean = generateCRM006ResponseBean(crm006Requ);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(crm006RespBean));
		logger.info("ESB接口CRM005响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}

	
	/**
	 * 创建响应实体Bean
	 * @param crm006Requ
	 * @return
	 */
	private CRM006Response generateCRM006ResponseBean(CRM006Request crm006Requ) {
		CRM006Response crm006Resp = new CRM006Response();
		
		crm006Resp.setReq_serial_no(crm006Requ.getReq_serial_no());
		crm006Resp.setReq_date(crm006Requ.getReq_date());
		crm006Resp.setRes_serial_no(StringUtil.getSerialNo32());
		crm006Resp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		crm006Resp.setReturn_code("SUCCESS");
		crm006Resp.setReturn_desc("请求成功");
		
		ParameterConfig paramConfig = crm006Requ.getParamConfig();
		List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
		String key = null;
		for(ClientSecurityKey cParam : clientParams ) {
			if(crm006Requ.getSecurity_code().equals(cParam.getClientCode()) && 
					crm006Requ.getSecurity_value().equals(cParam.getClientSecurity())) {
				key = cParam.getClientSignKey(); break;
			}
		}
		
		try {
			StaffModifyPwdRequest staffModifyPwdRequest = getDestRequestBean(crm006Requ, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestMsg(staffModifyPwdRequest));
			
			logger.info("开始发起请求.......");
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("CRM接口STAFF_MODIFY_PWD返回明文信息：" + RespXml);
			
			StaffModifyPwdResponse staffModfiyPwdResp = new StaffModifyPwdResponse();
			XMLHelper.getBeanFromXML(RespXml, staffModfiyPwdResp);
			if("SUCCESS".equals(staffModfiyPwdResp.getReturn_code()) && "SUCCESS".equals(staffModfiyPwdResp.getResult_code())) {
				verificationReturnMsg(staffModfiyPwdResp, paramConfig.getSignKey());
			}
			
			crm006Resp.setResult_code(staffModfiyPwdResp.getResult_code());
			crm006Resp.setResult_desc(staffModfiyPwdResp.getResult_desc());
			
		} catch(ESBException e) {
			crm006Resp.setResult_code(e.getCode());
			crm006Resp.setResult_desc(e.getDescription());
			logger.error(e.getMessage(), e);
		} catch (Exception  e) {
			crm006Resp.setResult_code("FAILED");
			crm006Resp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} finally {
			try {
				crm006Resp.setSign(Security.getSign(crm006Resp, key));
			} catch (IllegalAccessException e) {
				logger.error("签名出错", e);
			}
		}
		return crm006Resp;
	}
	
	
	/**
	 * 验证目标系统返回值
	 * @param staffModifyPwdResp
	 * @param key
	 * @throws ESBException
	 */
	private void verificationReturnMsg(StaffModifyPwdResponse staffModifyPwdResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = staffModifyPwdResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "CRM API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		staffModifyPwdResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(staffModifyPwdResp, key);
			
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
	 * @param staffModifyPwdRequest
	 * @return
	 */
	private String getRequestMsg(StaffModifyPwdRequest staffModifyPwdRequest) {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\"?>")
		.append("<crm><head>")
		.append("<api_code><![CDATA[").append(staffModifyPwdRequest.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(staffModifyPwdRequest.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(staffModifyPwdRequest.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(staffModifyPwdRequest.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(staffModifyPwdRequest.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(staffModifyPwdRequest.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<login_id><![CDATA[").append(staffModifyPwdRequest.getLogin_id()).append("]]></login_id>")
		.append("<password><![CDATA[").append(staffModifyPwdRequest.getPassword()).append("]]></password>")
		.append("<new_password><![CDATA[").append(staffModifyPwdRequest.getNew_password()).append("]]></new_password>")
		.append("</body></crm>");
		
		logger.info("CRM接口STAFF_MODIFY_PWD请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	}


	/**
	 * 创建密码修改实体
	 * @param crm006Requ
	 * @param paramConfig
	 * @return
	 * @throws IllegalAccessException
	 */
	private StaffModifyPwdRequest getDestRequestBean(CRM006Request crm006Requ, ParameterConfig paramConfig) throws IllegalAccessException {
		StaffModifyPwdRequest staffModifyPwdRequest = new StaffModifyPwdRequest();
		staffModifyPwdRequest.setApi_code("STAFF_MODIFY_PWD");
		staffModifyPwdRequest.setReq_serial_no(StringUtil.getSerialNo32());
		staffModifyPwdRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		staffModifyPwdRequest.setSecurity_code(paramConfig.getAppCode());
		staffModifyPwdRequest.setSecurity_value(paramConfig.getAppSecurity());
		staffModifyPwdRequest.setLogin_id(crm006Requ.getLogin_id());
		staffModifyPwdRequest.setPassword(crm006Requ.getPassword());
		staffModifyPwdRequest.setNew_password(crm006Requ.getNew_password());
		staffModifyPwdRequest.setSign(Security.getSign(staffModifyPwdRequest, paramConfig.getSignKey()));
	
		return staffModifyPwdRequest;
	}


	
	/**
	 * 
	 * @param crm006RespBean
	 * @return
	 */
	private String generateMsgResponseXML(CRM006Response crm006RespBean) {
		StringBuilder msgResponse = new StringBuilder();
		msgResponse.append("<?xml version=\"1.0\"?>")
		.append("<esb><head>")
		.append("<return_code>").append(crm006RespBean.getReturn_code()).append("</return_code>")
		.append("<return_desc>").append(crm006RespBean.getReturn_desc()).append("</return_desc>")
		.append("<req_serial_no>").append(crm006RespBean.getReq_serial_no()).append("</req_serial_no>")
		.append("<req_date>").append(crm006RespBean.getReq_date()).append("</req_date>")
		.append("<res_serial_no>").append(crm006RespBean.getRes_serial_no()).append("</res_serial_no>")
		.append("<res_date>").append(crm006RespBean.getRes_date()).append("</res_date>")
		.append("<sign>").append(crm006RespBean.getSign()).append("</sign>")
		.append("</head><body>")
		.append("<result_code>").append(crm006RespBean.getResult_code()).append("</result_code>")
		.append("<result_desc>").append(crm006RespBean.getResult_desc()).append("</result_desc>")
		.append("</body></esb>");
		
		logger.info("ESB接口CRM006响应报文明文：" + msgResponse.toString());
		return msgResponse.toString();
	}

	
	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	private CRM006Request getCRM006RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			CRM006Request crm006Request = (CRM006Request) ois.readObject();
			
			return crm006Request;

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
