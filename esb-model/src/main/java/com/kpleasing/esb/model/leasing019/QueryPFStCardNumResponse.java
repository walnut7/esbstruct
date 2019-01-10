package com.kpleasing.esb.model.leasing019;

import java.io.Serializable;

public class QueryPFStCardNumResponse implements Serializable {

	/** *  */
	private static final long serialVersionUID = 6958538924617458271L;
	
	private String status;
	
	private String message;
	
	private String stcardnum;

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

	public String getStcardnum() {
		return stcardnum;
	}

	public void setStcardnum(String stcardnum) {
		this.stcardnum = stcardnum;
	}
	
}
