package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing007.LEASING007Request;

public class GenerateBusinessOrderService extends AbstractRouteFactory<LEASING007Request> {
	
	@Override
	protected LEASING007Request init() {
		return new LEASING007Request();
	}


	@Override
	protected void verify(LEASING007Request leasing007Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
