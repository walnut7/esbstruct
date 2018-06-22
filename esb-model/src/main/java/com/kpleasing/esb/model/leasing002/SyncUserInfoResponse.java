package com.kpleasing.esb.model.leasing002;

import java.io.Serializable;

public class SyncUserInfoResponse implements Serializable  {

	/**	 * 	 */
	private static final long serialVersionUID = 3798999699616315441L;
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
