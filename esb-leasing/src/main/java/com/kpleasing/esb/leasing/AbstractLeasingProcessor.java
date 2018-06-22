package com.kpleasing.esb.leasing;

import com.kpleasing.esb.base.AbstractBaseProcessor;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.tools.HttpHelper;

public abstract class AbstractLeasingProcessor<T, E, M, N> extends AbstractBaseProcessor<T, E, M, N> {
	
	@Override
	public String sendHttpRequest(ParameterConfig config, String reqXml) throws ESBException {
		return HttpHelper.doHttpPost(config.getDestSystemUrl(), reqXml, HttpHelper.SOAP);
	}

	
	@Override
	public void verifyResponse(N n, ParameterConfig config) throws ESBException {
		// TODO Auto-generated method stub
		
	}
	
}
