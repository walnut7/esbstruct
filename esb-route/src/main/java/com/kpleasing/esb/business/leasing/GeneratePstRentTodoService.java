package com.kpleasing.esb.business.leasing;


import org.apache.commons.lang3.StringUtils;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.model.leasing015.LEASING015Request;

public class GeneratePstRentTodoService extends AbstractRouteFactory<LEASING015Request> {
	
	@Override
	protected LEASING015Request init() {
		return new LEASING015Request();
	}

	
	@Override
	protected void verify(LEASING015Request leasing015Req) throws InputNDataParamException {
		if(StringUtils.isBlank(leasing015Req.getApplyno())) {
			throw new InputNDataParamException("申请编号【applyno】不能为空！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
		}
		
		if(StringUtils.isBlank(leasing015Req.getApply_type())) {
			throw new InputNDataParamException("申请类型【apply_type】不能为空！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
		}
		
		if("1".equals(leasing015Req.getApply_type()) || "2".equals(leasing015Req.getApply_type()) || "3".equals(leasing015Req.getApply_type())) {
			if("3".equals(leasing015Req.getApply_type())) {
				if(StringUtils.isBlank(leasing015Req.getRecipients())) {
					throw new InputNDataParamException("申请类型3时，recipients不能为空！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
				}
				
				if(StringUtils.isBlank(leasing015Req.getMail_address())) {
					throw new InputNDataParamException("申请类型3时，mail_address不能为空！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
				}
				
				if(StringUtils.isBlank(leasing015Req.getContact_phone())) {
					throw new InputNDataParamException("申请类型3时，contact_phone不能为空！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
				}
			}
		} else {
			throw new InputNDataParamException("申请类型【apply_type】参数错误！", leasing015Req.getReq_serial_no(), leasing015Req.getReq_date());
		}
	}
}
