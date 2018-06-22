package com.kpleasing.esb.crm.process;

import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.crm.AbstractCrmProcessor;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm001.CRM001Request;
import com.kpleasing.esb.model.crm001.CRM001Response;
import com.kpleasing.esb.model.crm001.RegisterRequest;
import com.kpleasing.esb.model.crm001.RegisterResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;

public class RegisterProcess extends AbstractCrmProcessor<CRM001Request, CRM001Response, RegisterRequest, RegisterResponse> {
	
	private static Logger logger = Logger.getLogger(RegisterProcess.class);

	@Override
	protected RegisterResponse initCrmResponse() {
		return new RegisterResponse();
	}


	@Override
	public CRM001Response initResponse() {
		return new CRM001Response();
	}


	@Override
	public RegisterRequest generateRequestEntity(CRM001Request crm001Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setApi_code("REGISTER");
		registerRequest.setReq_serial_no(StringUtil.getSerialNo32());
		registerRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		registerRequest.setSecurity_code(config.getAppCode());
		registerRequest.setSecurity_value(config.getAppSecurity());
		registerRequest.setPhone(crm001Request.getPhone());
		registerRequest.setChannel_id(crm001Request.getChannel_id());
		registerRequest.setChannel_type(crm001Request.getChannel_type());
		
		registerRequest.setSign(Security.getSign(registerRequest, config.getSignKey()));
	
		return registerRequest;
	}


	@Override
	public void covertToEBody(RegisterResponse registerResponse, CRM001Response crm001Response, String key) throws ESBException {
		crm001Response.setCust_id(registerResponse.getCust_id());
		crm001Response.setPhone(registerResponse.getPhone());
		crm001Response.setResult_code(registerResponse.getResult_code());
		crm001Response.setResult_desc(registerResponse.getResult_desc());
		
		try {
			crm001Response.setSign(Security.getSign(crm001Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}


	@Override
	public void resultException(CRM001Response crm001Response, String key, String code, String msg) {
		try {
			crm001Response.setResult_code(code);
			crm001Response.setResult_desc(msg);
			crm001Response.setSign(Security.getSign(crm001Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
