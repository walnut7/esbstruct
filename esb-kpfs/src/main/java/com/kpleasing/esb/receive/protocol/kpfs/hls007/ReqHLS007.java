package com.kpleasing.esb.receive.protocol.kpfs.hls007;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS007 {
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String order_no;
	private String order_date;
	private String cert_code;
	private String sign;
	
	
	public String getApi_code() {
		return api_code;
	}
	
	public void setApi_code(String api_code) {
		this.api_code = api_code;
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

	public String getSecurity_code() {
		return security_code;
	}

	public void setSecurity_code(String security_code) {
		this.security_code = security_code;
	}

	public String getSecurity_value() {
		return security_value;
	}

	public void setSecurity_value(String security_value) {
		this.security_value = security_value;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public String getCert_code() {
		return cert_code;
	}

	public void setCert_code(String cert_code) {
		this.cert_code = cert_code;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String toXML() {
		return BeanXMLMapping.toXML("hls010", this);
	}

}
