package com.kpleasing.esb.model.leasing004;

import java.io.Serializable;


public class AutoTrialResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -7711434780050013781L;
	
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
