package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing013.LEASING013Request;

public class QueryRepayOrderService extends AbstractRouteFactory<LEASING013Request> {
	
	@Override
	protected LEASING013Request init() {
		return new LEASING013Request();
	}

	@Override
	protected void verify(LEASING013Request leasing013Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
