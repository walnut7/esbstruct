package com.kpleasing.esb.xmc.process;

import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.xmc002.GetWeChatAccessTokenRequest;
import com.kpleasing.esb.model.xmc002.GetWeChatAccessTokenResponse;
import com.kpleasing.esb.model.xmc002.XMC002Request;
import com.kpleasing.esb.model.xmc002.XMC002Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.xmc.AbstractXMCProcessor;

public class GetWXAccessTokenProcess extends AbstractXMCProcessor<XMC002Request, XMC002Response, GetWeChatAccessTokenRequest, GetWeChatAccessTokenResponse>   {

	private static Logger logger = Logger.getLogger(GetWXAccessTokenProcess.class);
	
	@Override
	protected GetWeChatAccessTokenResponse initCrmResponse() {
		return new GetWeChatAccessTokenResponse();
	}

	@Override
	public XMC002Response initResponse() {
		return new XMC002Response();
	}

	@Override
	public GetWeChatAccessTokenRequest generateRequestEntity(XMC002Request xmc002Request, ParameterConfig config)
			throws IllegalArgumentException, IllegalAccessException {
		GetWeChatAccessTokenRequest weChatAccessTokenRequest = new GetWeChatAccessTokenRequest();
		weChatAccessTokenRequest.setApi_code("XMC002");
		weChatAccessTokenRequest.setReq_serial_no(StringUtil.getSerialNo32());
		weChatAccessTokenRequest.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		weChatAccessTokenRequest.setSecurity_code(config.getAppCode());
		weChatAccessTokenRequest.setSecurity_value(config.getAppSecurity());
		
		weChatAccessTokenRequest.setSign(Security.getSign(weChatAccessTokenRequest, config.getSignKey()));
	
		return weChatAccessTokenRequest;
	}
	

	@Override
	public void covertToEBody(GetWeChatAccessTokenResponse weChatAccessTokenResponse, XMC002Response xmc002Response, String key) throws ESBException {
		xmc002Response.setResult_code(weChatAccessTokenResponse.getResult_code());
		xmc002Response.setResult_desc(weChatAccessTokenResponse.getResult_desc());
		
		try {
			xmc002Response.setSign(Security.getSign(xmc002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new ESBException("SIGN_ERROR", "签名错误！");
		}
		
	}

	@Override
	public void resultException(XMC002Response xmc002Response, String key, String code, String msg) {
		try {
			xmc002Response.setResult_code(code);
			xmc002Response.setResult_desc(msg);
			xmc002Response.setSign(Security.getSign(xmc002Response, key));
		} catch (IllegalArgumentException e) {
			logger.error("签名出错", e);
		} catch (IllegalAccessException e) {
			logger.error("签名出错", e);
		}
	}
}
