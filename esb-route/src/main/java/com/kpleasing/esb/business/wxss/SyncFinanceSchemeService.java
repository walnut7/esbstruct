package com.kpleasing.esb.business.wxss;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.wxss003.WXSS003Request;

public class SyncFinanceSchemeService extends AbstractRouteFactory<WXSS003Request> {
	
	@Override
	protected WXSS003Request init() {
		return new WXSS003Request();
	}


	@Override
	protected void verify(WXSS003Request wxss003Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
