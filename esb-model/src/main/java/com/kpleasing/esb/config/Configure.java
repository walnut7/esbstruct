package com.kpleasing.esb.config;

import java.io.Serializable;
import java.util.List;

import com.kpleasing.esb.config.vo.ParameterConfig;

public class Configure implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 5256136720556352514L;
	
	public String DES_KEY;
	public String DES_IV;
	public String EWECHAT_URL;
	
	private List<ParameterConfig> paramConfig;


	public List<ParameterConfig> getParameterConfig() {
		return paramConfig;
	}


	public void setParameterConfig(List<ParameterConfig> paramConfig) {
		this.paramConfig = paramConfig;
	}

}
