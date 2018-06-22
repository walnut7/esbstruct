package com.kpleasing.esb.business.crm;


import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm004.CRM004Request;

public class GetUserInfoService extends AbstractRouteFactory<CRM004Request>  {
	
	@Override
	protected CRM004Request init() {
		return new CRM004Request();
	}


	@Override
	protected void verify(CRM004Request crm004Request) throws InputNDataParamException {
		if(StringUtils.isBlank(crm004Request.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", crm004Request.getReq_serial_no(), crm004Request.getReq_date());
		}
	}
}
