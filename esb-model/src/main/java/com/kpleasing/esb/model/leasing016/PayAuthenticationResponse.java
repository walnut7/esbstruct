package com.kpleasing.esb.model.leasing016;

import java.io.Serializable;

public class PayAuthenticationResponse implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = 8089867864871212327L;
	private String status;
	private String message;
	private String response_code;
	private String response_text_message;
	private String token;
	private String storable_pan;
	private String error_code;
	private String error_message;
	
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

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getResponse_text_message() {
		return response_text_message;
	}

	public void setResponse_text_message(String response_text_message) {
		this.response_text_message = response_text_message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStorable_pan() {
		return storable_pan;
	}

	public void setStorable_pan(String storable_pan) {
		this.storable_pan = storable_pan;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
}
