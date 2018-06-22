package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing006.LEASING006Request;

public class QueryOrderInfoService extends AbstractRouteFactory<LEASING006Request> {

	@Override
	protected LEASING006Request init() {
		return new LEASING006Request();
	}

	@Override
	protected void verify(LEASING006Request leasing006Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
