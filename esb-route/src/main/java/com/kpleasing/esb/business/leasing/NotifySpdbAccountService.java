package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing020.LEASING020Request;

public class NotifySpdbAccountService extends AbstractRouteFactory<LEASING020Request>{
	@Override
	protected LEASING020Request init() {
		return new LEASING020Request();
	}


	@Override
	protected void verify(LEASING020Request leasing020Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing020Req.getCert_code())) {
			throw new InputNDataParamException("身份证【cert_code】不能为空！", leasing020Req.getReq_serial_no(), leasing020Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing020Req.getSpdb_stCard_no())) {
			throw new InputNDataParamException("浦发Ⅱ/Ⅲ类户账号【spdb_account_id】不能为空！", leasing020Req.getReq_serial_no(), leasing020Req.getReq_date());
		}
	}
}
