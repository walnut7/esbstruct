package com.kpleasing.esb.business.pub;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.pub001.PUB001Request;

public class PerfectCustomerInfoService extends AbstractRouteFactory<PUB001Request> {

	@Override
	protected PUB001Request init() {
		return new PUB001Request();
	}

	@Override
	protected void verify(PUB001Request pub001Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
