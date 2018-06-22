package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing014.LEASING014Request;

public class QueryBuyoutAmountService extends AbstractRouteFactory<LEASING014Request> {
	
	@Override
	protected LEASING014Request init() {
		return new LEASING014Request();
	}


	@Override
	protected void verify(LEASING014Request leasing014Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing014Req.getApplyno())) {
			throw new InputNDataParamException("申请编号【applyno】不能为空！", leasing014Req.getReq_serial_no(), leasing014Req.getReq_date());
		}
	}
}
