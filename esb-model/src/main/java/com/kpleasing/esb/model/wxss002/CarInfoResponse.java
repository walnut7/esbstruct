package com.kpleasing.esb.model.wxss002;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class CarInfoResponse extends ResponseHeader implements Serializable {

	/** *  */
	private static final long serialVersionUID = 8955423147528228272L;
	
	private String result_code;
	
	private String result_desc;

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_desc() {
		return result_desc;
	}

	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}

}
