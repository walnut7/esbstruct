package com.kpleasing.esb.business.crm;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm010.CRM010Request;

public class SyncFaceVedioTimeService extends AbstractRouteFactory<CRM010Request> {
	
	@Override
	protected CRM010Request init() {
		return new CRM010Request();
	}


	@Override
	protected void verify(CRM010Request crm010Req) throws InputNDataParamException {
		// TODO Auto-generated method stub
		
	}
}
