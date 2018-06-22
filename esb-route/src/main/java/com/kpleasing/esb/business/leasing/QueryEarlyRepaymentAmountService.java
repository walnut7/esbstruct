package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing011.LEASING011Request;

public class QueryEarlyRepaymentAmountService extends AbstractRouteFactory<LEASING011Request> {

	@Override
	protected LEASING011Request init() {
		return new LEASING011Request();
	}


	@Override
	protected void verify(LEASING011Request leasing011Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing011Req.getApplyno())) {
			throw new InputNDataParamException("申请编号【applyno】不能为空！", leasing011Req.getReq_serial_no(), leasing011Req.getReq_date());
		}
		
	}
}
