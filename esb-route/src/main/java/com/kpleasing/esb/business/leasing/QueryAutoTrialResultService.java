package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing005.LEASING005Request;

public class QueryAutoTrialResultService extends AbstractRouteFactory<LEASING005Request> {

	@Override
	protected LEASING005Request init() {
		return new LEASING005Request();
	}


	@Override
	protected void verify(LEASING005Request leasing005Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing005Req.getApplication_no())) {
			throw new InputNDataParamException("申请编号不能为空！", leasing005Req.getReq_serial_no(), leasing005Req.getReq_date());
		}
	}
}
