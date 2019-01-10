package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing019.LEASING019Request;

public class QuerySpdbAccountService extends AbstractRouteFactory<LEASING019Request>{
	@Override
	protected LEASING019Request init() {
		return new LEASING019Request();
	}


	@Override
	protected void verify(LEASING019Request leasing019Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing019Req.getCert_code())) {
			throw new InputNDataParamException("身份证【cert_code】不能为空！", leasing019Req.getReq_serial_no(), leasing019Req.getReq_date());
		}
	}
}
