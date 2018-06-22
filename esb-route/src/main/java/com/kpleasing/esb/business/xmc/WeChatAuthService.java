package com.kpleasing.esb.business.xmc;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.xmc001.XMC001Request;

public class WeChatAuthService extends AbstractRouteFactory<XMC001Request> {
	
	@Override
	protected XMC001Request init() {
		return new XMC001Request();
	}


	@Override
	protected void verify(XMC001Request xmc001Req) throws InputNDataParamException {
	}
}
