package com.kpleasing.esb.model.leasing015;

import java.io.Serializable;

public class GeneratePstRentTodoResponse implements Serializable {
	
	/**	 * 	 */
	private static final long serialVersionUID = -9063465628599570873L;
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
