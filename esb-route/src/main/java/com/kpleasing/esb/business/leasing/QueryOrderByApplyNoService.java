package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing010.LEASING010Request;

public class QueryOrderByApplyNoService extends AbstractRouteFactory<LEASING010Request> {
	
	@Override
	protected LEASING010Request init() {
		return new LEASING010Request();
	}


	@Override
	protected void verify(LEASING010Request leasing010Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing010Req.getApplyno())) {
			throw new InputNDataParamException("申请编号【applyno】不能为空！", leasing010Req.getReq_serial_no(), leasing010Req.getReq_date());
		}
	}
}
