package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing009.LEASING009Request;

public class QueryOrderByCustIdService extends AbstractRouteFactory<LEASING009Request>  {
	
	@Override
	protected LEASING009Request init() {
		return new LEASING009Request();
	}


	@Override
	protected void verify(LEASING009Request leasing009Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing009Req.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", leasing009Req.getReq_serial_no(), leasing009Req.getReq_date());
		}
	}
}
