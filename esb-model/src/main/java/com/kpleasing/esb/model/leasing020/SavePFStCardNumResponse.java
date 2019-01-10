package com.kpleasing.esb.model.leasing020;

import java.io.Serializable;

public class SavePFStCardNumResponse implements Serializable {

	/** *  */
	private static final long serialVersionUID = 6958538924617458271L;
	
	private String status;
	
	private String message;

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
	
}
