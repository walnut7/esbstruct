package com.kpleasing.esb.business.xmc;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.xmc001.XMC001Request;

public class FirstAuditService extends AbstractRouteFactory<XMC001Request> {

	@Override
	protected XMC001Request init() {
		return new XMC001Request();
	}


	@Override
	protected void verify(XMC001Request xmc001Request) throws InputNDataParamException {
		if(StringUtils.isBlank(xmc001Request.getCust_id())) {
			throw new InputNDataParamException("客户ID【cust_id】不能为空！", xmc001Request.getReq_serial_no(), xmc001Request.getReq_date());
		}
	}
}
