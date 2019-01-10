package com.kpleasing.esb.model.leasing019;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING019Response extends ResponseHeader implements Serializable {

	/** *  */
	private static final long serialVersionUID = 6958538924617458271L;
	private String result_code;
	private String result_desc;
	private String spdb_stCard_no;
	
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

	public String getSpdb_stCard_no() {
		return spdb_stCard_no;
	}

	public void setSpdb_stCard_no(String spdb_stCard_no) {
		this.spdb_stCard_no = spdb_stCard_no;
	}
	
}
