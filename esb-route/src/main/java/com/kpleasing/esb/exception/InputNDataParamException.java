package com.kpleasing.esb.exception;

public class InputNDataParamException extends RouteException {

	/**	 * 	 */
	private static final long serialVersionUID = 3828347026773383108L;
	
    private String req_serial_no;
	
	private String req_date;
	
	public InputNDataParamException(String desc, String req_serial_no, String req_date) {
		super.setCode("FAILED");
		super.setDescription(desc);
		this.setReq_serial_no(req_serial_no);
		this.setReq_date(req_date);
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
}
