package com.kpleasing.esb.business.crm;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm003.CRM003Request;

public class SetUserProfileService extends AbstractRouteFactory<CRM003Request> {
	
	@Override
	protected CRM003Request init() {
		return new CRM003Request();
	}


	@Override
	protected void verify(CRM003Request crm003Req) throws InputNDataParamException {
		if(StringUtils.isBlank(crm003Req.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", crm003Req.getReq_serial_no(), crm003Req.getReq_date());
		}
		
		if(StringUtils.isBlank(crm003Req.getPhone())) {
			throw new InputNDataParamException("更新手机号【phone】不能为空！", crm003Req.getReq_serial_no(), crm003Req.getReq_date());
		}
	}
}
