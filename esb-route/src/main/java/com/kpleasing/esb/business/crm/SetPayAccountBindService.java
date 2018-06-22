package com.kpleasing.esb.business.crm;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.crm012.CRM012Request;

public class SetPayAccountBindService extends AbstractRouteFactory<CRM012Request> {

	@Override
	protected CRM012Request init() {
		return new CRM012Request();
	}

	@Override
	protected void verify(CRM012Request crm012Request) throws InputNDataParamException {
		if(StringUtils.isBlank(crm012Request.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", crm012Request.getReq_serial_no(), crm012Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm012Request.getAccount_name())) {
			throw new InputNDataParamException("账户姓名【account_name】不能为空！", crm012Request.getReq_serial_no(), crm012Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm012Request.getAccount())) {
			throw new InputNDataParamException("银行卡号【account】不能为空！", crm012Request.getReq_serial_no(), crm012Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm012Request.getBank_phone())) {
			throw new InputNDataParamException("银行预留手机号【bank_phone】不能为空！", crm012Request.getReq_serial_no(), crm012Request.getReq_date());
		}
		
		if(StringUtils.isBlank(crm012Request.getIs_yjzf_bind())) {
			throw new InputNDataParamException("是否一键支付绑定【is_yjzf_bind】不能为空！", crm012Request.getReq_serial_no(), crm012Request.getReq_date());
		}
	}
}
