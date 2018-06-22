package com.kpleasing.esb.business.crm;


import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm001.CRM001Request;

public class RegisterService extends AbstractRouteFactory<CRM001Request> {

	@Override
	protected CRM001Request init() {
		return new CRM001Request();
	}

	@Override
	protected void verify(CRM001Request crm001Request) throws InputNDataParamException {
		if(StringUtils.isBlank(crm001Request.getPhone())) {
			throw new InputNDataParamException("手机号码不能为空！", crm001Request.getReq_serial_no(), crm001Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm001Request.getChannel_type())) {
			throw new InputNDataParamException("渠道类型【channel_type】不能为空！", crm001Request.getReq_serial_no(), crm001Request.getReq_date());
		}
		
		if(crm001Request.getChannel_type().equals("0") || crm001Request.getChannel_type().equals("1")) {
			if(crm001Request.getChannel_type().equals("1") && StringUtils.isBlank(crm001Request.getChannel_id())) {
				throw new InputNDataParamException("【channel_id】不能为空，请提供微信OPEN_ID！", crm001Request.getReq_serial_no(), crm001Request.getReq_date());
			} 
		} else {
			throw new InputNDataParamException("渠道类型【channel_type】参数错误！", crm001Request.getReq_serial_no(), crm001Request.getReq_date());
		}
	}
}
