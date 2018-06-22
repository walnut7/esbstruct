package com.kpleasing.esb.business.crm;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm006.CRM006Request;

public class StaffModifyPwdService extends AbstractRouteFactory<CRM006Request> {

	@Override
	protected CRM006Request init() {
		return new CRM006Request();
	}


	@Override
	protected void verify(CRM006Request crm006Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
