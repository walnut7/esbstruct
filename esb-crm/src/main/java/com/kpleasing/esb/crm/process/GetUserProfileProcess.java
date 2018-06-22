package com.kpleasing.esb.crm.process;

import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.crm.AbstractCrmProcessor;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm002.CRM002Request;
import com.kpleasing.esb.model.crm002.CRM002Response;
import com.kpleasing.esb.model.crm002.GetUserProfileRequest;
import com.kpleasing.esb.model.crm002.GetUserProfileResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;

public class GetUserProfileProcess extends AbstractCrmProcessor<CRM002Request, CRM002Response, GetUserProfileRequest, GetUserProfileResponse> {
	
	private static Logger logger = Logger.getLogger(GetUserProfileProcess.class);

	@Override
	public CRM002Response initResponse() {
		return new CRM002Response();
	}


	@Override
	public GetUserProfileRequest generateRequestEntity(CRM002Request crm002Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		GetUserProfileRequest userProfileRequest = new GetUserProfileRequest();
		userProfileRequest.setApi_code("GET_USER_PROFILE");
		userProfileRequest.setReq_serial_no(StringUtil.getSerialNo32());
		userProfileRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		userProfileRequest.setSecurity_code(config.getAppCode());
		userProfileRequest.setSecurity_value(config.getAppSecurity());
		userProfileRequest.setChannel_id(crm002Request.getChannel_id());
		userProfileRequest.setChannel_type(crm002Request.getChannel_type());
		userProfileRequest.setSign(Security.getSign(userProfileRequest, config.getSignKey()));
	
		return userProfileRequest;
	}
	
	
	@Override
	protected GetUserProfileResponse initCrmResponse() {
		return new GetUserProfileResponse();
	}
	

	@Override
	public void covertToEBody(GetUserProfileResponse userProfileResponse, CRM002Response crm002Response, String key) throws ESBException {
		crm002Response.setCust_id(userProfileResponse.getCust_id());
		crm002Response.setCust_name(userProfileResponse.getCust_name());
		crm002Response.setPhone(userProfileResponse.getPhone());
		crm002Response.setResult_code(userProfileResponse.getResult_code());
		crm002Response.setResult_desc(userProfileResponse.getResult_desc());
		crm002Response.setCert_code(userProfileResponse.getCert_code());
		crm002Response.setCert_type(userProfileResponse.getCert_type());
		crm002Response.setWx_id(userProfileResponse.getWx_id());
		
		try {
			crm002Response.setSign(Security.getSign(crm002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}


	@Override
	public void resultException(CRM002Response crm002Response, String key, String code, String msg) {
		try {
			crm002Response.setResult_code(code);
			crm002Response.setResult_desc(msg);
			crm002Response.setSign(Security.getSign(crm002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
