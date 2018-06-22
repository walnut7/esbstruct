package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing012.LEASING012Request;

public class QueryOverdueOrderService extends AbstractRouteFactory<LEASING012Request> {
	
	@Override
	protected LEASING012Request init() {
		return new LEASING012Request();
	}


	@Override
	protected void verify(LEASING012Request leasing012Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
