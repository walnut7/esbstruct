package com.kpleasing.esb.business.crm;


import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm008.CRM008Request;

public class SyncReserverCustomerService extends AbstractRouteFactory<CRM008Request> {
	
	@Override
	protected CRM008Request init() {
		return new CRM008Request();
	}


	@Override
	protected void verify(CRM008Request crm008Req) throws InputNDataParamException {
		if(StringUtils.isBlank(crm008Req.getPhone())) {
			throw new InputNDataParamException("手机号码【phone】不能为空！", crm008Req.getReq_serial_no(), crm008Req.getReq_date());
		}
	}
}
