package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing004.LEASING004Request;

public class AutoTrialService extends AbstractRouteFactory<LEASING004Request> {

	@Override
	protected LEASING004Request init() {
		return new LEASING004Request();
	}

	
	@Override
	protected void verify(LEASING004Request leasing004Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing004Req.getPhone())) {
			throw new InputNDataParamException("手机号码不能为空！", leasing004Req.getReq_serial_no(), leasing004Req.getReq_date());
		}
		
		if(leasing004Req.getPhone().length()!=11) {
			throw new InputNDataParamException("手机号码格式不正确！", leasing004Req.getReq_serial_no(), leasing004Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing004Req.getCert_code())) {
			throw new InputNDataParamException("短信内容不能为空！", leasing004Req.getReq_serial_no(), leasing004Req.getReq_date());
		}
	}
}
