package com.kpleasing.esb.receive.protocol.kpfs.hls008;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS008 {
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String bill_no;
	private String ahead_pay_model;
	private String pay_date;
	private String pay_norm_amt;
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

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getAhead_pay_model() {
		return ahead_pay_model;
	}

	public void setAhead_pay_model(String ahead_pay_model) {
		this.ahead_pay_model = ahead_pay_model;
	}
	
	public String getPay_date() {
		return pay_date;
	}

	public void setPay_date(String pay_date) {
		this.pay_date = pay_date;
	}

	public String getPay_norm_amt() {
		return pay_norm_amt;
	}

	public void setPay_norm_amt(String pay_norm_amt) {
		this.pay_norm_amt = pay_norm_amt;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String toXML() {
		return BeanXMLMapping.toXML("hls008", this);
	}

}
