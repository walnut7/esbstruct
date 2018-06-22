package com.kpleasing.esb.business.leasing;


import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing001.LEASING001Request;

public class SendSMSService extends AbstractRouteFactory<LEASING001Request> {
	
	@Override
	protected LEASING001Request init() {
		return new LEASING001Request();
	}


	@Override
	protected void verify(LEASING001Request leasing001Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing001Req.getPhone())) {
			throw new InputNDataParamException("手机号码不能为空！", leasing001Req.getReq_serial_no(), leasing001Req.getReq_date());
		}
		
		if(leasing001Req.getPhone().length()!=11) {
			throw new InputNDataParamException("手机号码格式不正确！", leasing001Req.getReq_serial_no(), leasing001Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing001Req.getContent())) {
			throw new InputNDataParamException("短信内容不能为空！", leasing001Req.getReq_serial_no(), leasing001Req.getReq_date());
		}
	}
}
