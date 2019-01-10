package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing018.LEASING018Request;

public class CheckAutoTrialService extends AbstractRouteFactory<LEASING018Request>{
	@Override
	protected LEASING018Request init() {
		return new LEASING018Request();
	}


	@Override
	protected void verify(LEASING018Request leasing018Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing018Req.getName())) {
			throw new InputNDataParamException("姓名【name】不能为空！", leasing018Req.getReq_serial_no(), leasing018Req.getReq_date());
		}
		if(StringUtils.isBlank(leasing018Req.getPhone())) {
			throw new InputNDataParamException("手机号【phone】不能为空！", leasing018Req.getReq_serial_no(), leasing018Req.getReq_date());
		}
		if(StringUtils.isBlank(leasing018Req.getCert_code())) {
			throw new InputNDataParamException("身份证【cert_code】不能为空！", leasing018Req.getReq_serial_no(), leasing018Req.getReq_date());
		}
	}
}
