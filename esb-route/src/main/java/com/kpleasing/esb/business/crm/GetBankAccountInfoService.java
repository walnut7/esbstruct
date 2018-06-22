package com.kpleasing.esb.business.crm;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm011.CRM011Request;

public class GetBankAccountInfoService extends AbstractRouteFactory<CRM011Request> {

	@Override
	protected CRM011Request init() {
		return new CRM011Request();
	}

	@Override
	protected void verify(CRM011Request crm011Request) throws InputNDataParamException {
		if(StringUtils.isBlank(crm011Request.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", crm011Request.getReq_serial_no(), crm011Request.getReq_date());
		}
	}
}
