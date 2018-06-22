package com.kpleasing.esb.exception;

public class ESBException extends Exception {

	/**	 * 	 */
	private static final long serialVersionUID = 2247279293121328970L;
	
	private String code;

	private String description;

	public ESBException() { super(); }
	
	public ESBException(String code, String description) {
		this.setCode(code);
		this.setDescription(description);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
