package com.kpleasing.esb.model.wxss002;

import java.io.Serializable;

public class CarParam implements Serializable {

	/** *  */
	private static final long serialVersionUID = -6170779776665688936L;
	
	private String param_name;
	
	private String param_value;

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

}
