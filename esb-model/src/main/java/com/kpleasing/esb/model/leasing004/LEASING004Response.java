package com.kpleasing.esb.model.leasing004;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING004Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 3382694933183224652L;
	
	private String result_code;
	
	private String result_desc;
	
	private String application_no;

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

	public String getApplication_no() {
		return application_no;
	}

	public void setApplication_no(String application_no) {
		this.application_no = application_no;
	}
}
