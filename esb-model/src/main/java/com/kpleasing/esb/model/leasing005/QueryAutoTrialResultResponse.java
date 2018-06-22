package com.kpleasing.esb.model.leasing005;

import java.io.Serializable;

public class QueryAutoTrialResultResponse implements Serializable {
	
	/**	 * 	 */
	private static final long serialVersionUID = 8705395460176465753L;

	private String status;
	
	private String message;
	
	private String credit_result;
	
	private String credit_note;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCredit_result() {
		return credit_result;
	}

	public void setCredit_result(String credit_result) {
		this.credit_result = credit_result;
	}

	public String getCredit_note() {
		return credit_note;
	}

	public void setCredit_note(String credit_note) {
		this.credit_note = credit_note;
	}
}
