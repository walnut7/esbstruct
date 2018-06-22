package com.kpleasing.esb.business.leasing;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing003.LEASING003Request;

public class SyncPartnerInfoService extends AbstractRouteFactory<LEASING003Request> {

	@Override
	protected LEASING003Request init() {
		return new LEASING003Request();
	}


	@Override
	protected void verify(LEASING003Request leasing003Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
