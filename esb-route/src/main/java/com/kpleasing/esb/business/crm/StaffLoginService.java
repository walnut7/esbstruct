package com.kpleasing.esb.business.crm;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm005.CRM005Request;

public class StaffLoginService extends AbstractRouteFactory<CRM005Request> {
	
	@Override
	protected CRM005Request init() {
		return new CRM005Request();
	}


	@Override
	protected void verify(CRM005Request crm005Req) throws InputNDataParamException {
		if(StringUtils.isBlank(crm005Req.getLogin_id())) {
			throw new InputNDataParamException("登录账户【login_id】不能为空！", crm005Req.getReq_serial_no(), crm005Req.getReq_date());
		}
		
		if(StringUtils.isBlank(crm005Req.getPassword())) {
			throw new InputNDataParamException("登录密码【password】不能为空！", crm005Req.getReq_serial_no(), crm005Req.getReq_date());
		}
	}
}
