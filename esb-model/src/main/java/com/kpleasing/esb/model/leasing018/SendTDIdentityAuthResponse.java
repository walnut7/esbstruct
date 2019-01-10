package com.kpleasing.esb.model.leasing018;

import java.io.Serializable;

public class SendTDIdentityAuthResponse implements Serializable {

	/** *  */
	private static final long serialVersionUID = -824196556873730165L;

	private String status;
	
	private String message;
	
	private String auth_status;
	
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

	public String getAuth_status() {
		return auth_status;
	}

	public void setAuth_status(String auth_status) {
		this.auth_status = auth_status;
	}
}
