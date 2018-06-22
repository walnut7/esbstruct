package com.kpleasing.esb.xmc.process;

import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.xmc001.FirstAuditRequest;
import com.kpleasing.esb.model.xmc001.FirstAuditResponse;
import com.kpleasing.esb.model.xmc001.XMC001Request;
import com.kpleasing.esb.model.xmc001.XMC001Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.xmc.AbstractXMCProcessor;

public class FirstAuditProcess extends AbstractXMCProcessor<XMC001Request, XMC001Response, FirstAuditRequest, FirstAuditResponse>  {
	
	private static Logger logger = Logger.getLogger(FirstAuditProcess.class);

	@Override
	protected FirstAuditResponse initCrmResponse() {
		return new FirstAuditResponse();
	}


	@Override
	public XMC001Response initResponse() {
		return new XMC001Response();
	}


	@Override
	public FirstAuditRequest generateRequestEntity(XMC001Request xmc001Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		FirstAuditRequest firstAuditRequest = new FirstAuditRequest();
		firstAuditRequest.setApi_code("XMC001");
		firstAuditRequest.setReq_serial_no(StringUtil.getSerialNo32());
		firstAuditRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		firstAuditRequest.setSecurity_code(config.getAppCode());
		firstAuditRequest.setSecurity_value(config.getAppSecurity());
		firstAuditRequest.setCust_id(xmc001Request.getCust_id());
		
		firstAuditRequest.setSign(Security.getSign(firstAuditRequest, config.getSignKey()));
	
		return firstAuditRequest;
	}


	@Override
	public void covertToEBody(FirstAuditResponse firstAuditResponse, XMC001Response xmc001Response, String key) throws ESBException {
		xmc001Response.setResult_code(firstAuditResponse.getResult_code());
		xmc001Response.setResult_desc(firstAuditResponse.getResult_desc());
		
		try {
			xmc001Response.setSign(Security.getSign(xmc001Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
	}


	@Override
	public void resultException(XMC001Response xmc001Response, String key, String code, String msg) {
		try {
			xmc001Response.setResult_code(code);
			xmc001Response.setResult_desc(msg);
			xmc001Response.setSign(Security.getSign(xmc001Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
