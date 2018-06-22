package com.kpleasing.esb.business.crm;



import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm002.CRM002Request;

public class GetUserProfileService extends AbstractRouteFactory<CRM002Request> {
	
	@Override
	protected CRM002Request init() {
		return new CRM002Request();
	}

	@Override
	protected void verify(CRM002Request crm002Request) throws InputNDataParamException {
		if(StringUtils.isBlank(crm002Request.getChannel_type())) {
			throw new InputNDataParamException("渠道类型【channel_type】不能为空！", crm002Request.getReq_serial_no(), crm002Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm002Request.getChannel_id())) {
			throw new InputNDataParamException("渠道ID【channel_id】不能为空！", crm002Request.getReq_serial_no(), crm002Request.getReq_date());
		}
		
		if(!crm002Request.getChannel_type().equals("0") && !crm002Request.getChannel_type().equals("1") 
				&& !crm002Request.getChannel_type().equals("2")) {
			throw new InputNDataParamException("渠道类型【channel_type】参数错误！", crm002Request.getReq_serial_no(), crm002Request.getReq_date());
		} 
	}
}
