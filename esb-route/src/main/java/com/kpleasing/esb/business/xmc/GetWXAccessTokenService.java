package com.kpleasing.esb.business.xmc;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.xmc002.XMC002Request;

public class GetWXAccessTokenService extends AbstractRouteFactory<XMC002Request> {
	
	@Override
	protected XMC002Request init() {
		return new XMC002Request();
	}


	@Override
	protected void verify(XMC002Request t) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
