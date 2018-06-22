package com.kpleasing.esb.model.leasing007;

import java.io.Serializable;

public class CreateOrderResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -2475560379223721940L;
	
	private String applyno;
	
	private String result_code;
	
	private String result_desc;

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

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
