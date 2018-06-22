package com.kpleasing.esb.model.leasing005;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING005Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -681653328311005400L;
	
	private String result_code;
	
	private String result_desc;
	
	private String credit_code;
	
	private String credit_note;
	
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

	public String getCredit_code() {
		return credit_code;
	}

	public void setCredit_code(String credit_code) {
		this.credit_code = credit_code;
	}

	public String getCredit_note() {
		return credit_note;
	}

	public void setCredit_note(String credit_note) {
		this.credit_note = credit_note;
	}

}
