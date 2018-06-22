package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing008.LEASING008Request;

public class CancelBusinessOrderService extends AbstractRouteFactory<LEASING008Request> {

	@Override
	protected LEASING008Request init() {
		return new LEASING008Request();
	}


	@Override
	protected void verify(LEASING008Request leasing008Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
