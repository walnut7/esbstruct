package com.kpleasing.esb.kpfs.exceptions;

public class KPFSException extends Exception {

	/**	 * serialVersionUID	 */
	private static final long serialVersionUID = 9076375435775133703L;
	
	private String code;

	private String description;

	private String req_serial_no;
	
	private String req_date;
	
	public KPFSException() { super(); }
	
	public KPFSException(String serialNo, String receDate, String description) {
		this.setReq_serial_no(serialNo);
		this.setReq_date(receDate);
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
	
	public String getReq_serial_no() {
		return req_serial_no;
	}

	public void setReq_serial_no(String req_serial_no) {
		this.req_serial_no = req_serial_no;
	}

	public String getReq_date() {
		return req_date;
	}

	public void setReq_date(String req_date) {
		this.req_date = req_date;
	}
	
	@Override
	public String getMessage() {
		return "request serial no. is:<" + req_serial_no + ">;\trequest time is:<" + req_date + ">;\tcode:" + code + ";description:" + description;
	}

}
