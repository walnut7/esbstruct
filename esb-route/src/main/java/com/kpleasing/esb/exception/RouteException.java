package com.kpleasing.esb.exception;

public class RouteException extends Exception {

	/**	 * 	 */
	private static final long serialVersionUID = -1251362253279758934L;
	
	private String code;

	private String description;

	public RouteException() { super(); }
	
	public RouteException(String code, String description) {
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
