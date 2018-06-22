package com.kpleasing.esb.crm.process;

import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.crm.AbstractCrmProcessor;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.crm012.CRM012Request;
import com.kpleasing.esb.model.crm012.CRM012Response;
import com.kpleasing.esb.model.crm012.SetPayAccountBindRequest;
import com.kpleasing.esb.model.crm012.SetPayAccountBindResponse;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;

public class SetPayAccountBindProcess extends AbstractCrmProcessor<CRM012Request, CRM012Response, SetPayAccountBindRequest, SetPayAccountBindResponse> {

	private static Logger logger = Logger.getLogger(SetPayAccountBindProcess.class);
	
	@Override
	protected SetPayAccountBindResponse initCrmResponse() {
		return new SetPayAccountBindResponse();
	}

	
	@Override
	public CRM012Response initResponse() {
		return new CRM012Response();
	}

	
	@Override
	public SetPayAccountBindRequest generateRequestEntity(CRM012Request crm012Repuest, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		SetPayAccountBindRequest payAccountBindRequest = new SetPayAccountBindRequest();
		payAccountBindRequest.setApi_code("SET_PAYMENT_ACCOUNT_BIND");
		payAccountBindRequest.setReq_serial_no(StringUtil.getSerialNo32());
		payAccountBindRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		payAccountBindRequest.setSecurity_code(config.getAppCode());
		payAccountBindRequest.setSecurity_value(config.getAppSecurity());
		payAccountBindRequest.setCust_id(crm012Repuest.getCust_id());
		payAccountBindRequest.setAccount_name(crm012Repuest.getAccount_name());
		payAccountBindRequest.setAccount_no(crm012Repuest.getAccount());
		payAccountBindRequest.setShort_account_no(crm012Repuest.getShort_account_no());
		payAccountBindRequest.setBank_phone(crm012Repuest.getBank_phone());
		payAccountBindRequest.setIs_yjzf_bind(crm012Repuest.getIs_yjzf_bind());
		
		payAccountBindRequest.setSign(Security.getSign(payAccountBindRequest, config.getSignKey()));
	
		return payAccountBindRequest;
	}

	
	@Override
	public void covertToEBody(SetPayAccountBindResponse payAccountBindResponse, CRM012Response crm012Response, String key) throws ESBException {
		crm012Response.setResult_code(payAccountBindResponse.getResult_code());
		crm012Response.setResult_desc(payAccountBindResponse.getResult_desc());
		
		try {
			crm012Response.setSign(Security.getSign(crm012Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}

	@Override
	public void resultException(CRM012Response crm012Response, String key, String code, String msg) {
		try {
			crm012Response.setResult_code(code);
			crm012Response.setResult_desc(msg);
			crm012Response.setSign(Security.getSign(crm012Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
