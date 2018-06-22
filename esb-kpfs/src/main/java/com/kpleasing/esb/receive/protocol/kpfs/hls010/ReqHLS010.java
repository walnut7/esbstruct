package com.kpleasing.esb.receive.protocol.kpfs.hls010;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS010 {
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String bill_no;
	private String repay_date;
	private String ahead_pay_type;
	private String test_type;
	private String trans_amt;
	private String cost_amt;
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

	public String getRepay_date() {
		return repay_date;
	}

	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}

	public String getAhead_pay_type() {
		return ahead_pay_type;
	}

	public void setAhead_pay_type(String ahead_pay_type) {
		this.ahead_pay_type = ahead_pay_type;
	}

	public String getTest_type() {
		return test_type;
	}

	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}

	public String getTrans_amt() {
		return trans_amt;
	}

	public void setTrans_amt(String trans_amt) {
		this.trans_amt = trans_amt;
	}

	public String getCost_amt() {
		return cost_amt;
	}

	public void setCost_amt(String cost_amt) {
		this.cost_amt = cost_amt;
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
