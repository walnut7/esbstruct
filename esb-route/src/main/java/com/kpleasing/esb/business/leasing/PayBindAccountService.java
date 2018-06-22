package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing017.LEASING017Request;

public class PayBindAccountService extends AbstractRouteFactory<LEASING017Request> {
	
	@Override
	protected LEASING017Request init() {
		return new LEASING017Request();
	}

	@Override
	protected void verify(LEASING017Request leasing017Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
