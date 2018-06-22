package com.kpleasing.esb.business.leasing;

import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing016.LEASING016Request;

public class PayAuthenticationService extends AbstractRouteFactory<LEASING016Request> {
	
	@Override
	protected LEASING016Request init() {
		return new LEASING016Request();
	}


	@Override
	protected void verify(LEASING016Request leasing016Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing016Req.getExternal_no())) {
			throw new InputNDataParamException("外部流水码【external_no】不能为空！", leasing016Req.getReq_serial_no(), leasing016Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing016Req.getCust_id())) {
			throw new InputNDataParamException("客户编号【cust_id】不能为空！", leasing016Req.getReq_serial_no(), leasing016Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing016Req.getCust_name())) {
			throw new InputNDataParamException("客户姓名【cust_name】不能为空！", leasing016Req.getReq_serial_no(), leasing016Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing016Req.getRepay_card_no())) {
			throw new InputNDataParamException("银行卡号【repay_card_no】不能为空！", leasing016Req.getReq_serial_no(), leasing016Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing016Req.getBank_phone())) {
			throw new InputNDataParamException("银行预留手机号【bank_phone】不能为空！", leasing016Req.getReq_serial_no(), leasing016Req.getReq_date());
		}
	}
}
