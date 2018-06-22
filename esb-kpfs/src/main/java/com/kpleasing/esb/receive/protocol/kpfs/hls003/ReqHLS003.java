package com.kpleasing.esb.receive.protocol.kpfs.hls003;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS003 {
	
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String seq_no;
	private String app_no;
	private String crd_amt;
	private String loan_amt;
	private String loan_nstd_term;
	private String loan_term;
	private String loan_purpose;
	private String repay_type;
	private String sign_type;

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

	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getApp_no() {
		return app_no;
	}

	public void setApp_no(String app_no) {
		this.app_no = app_no;
	}

	public String getCrd_amt() {
		return crd_amt;
	}

	public void setCrd_amt(String crd_amt) {
		this.crd_amt = crd_amt;
	}

	public String getLoan_amt() {
		return loan_amt;
	}

	public void setLoan_amt(String loan_amt) {
		this.loan_amt = loan_amt;
	}

	public String getLoan_nstd_term() {
		return loan_nstd_term;
	}

	public void setLoan_nstd_term(String loan_nstd_term) {
		this.loan_nstd_term = loan_nstd_term;
	}

	public String getLoan_term() {
		return loan_term;
	}

	public void setLoan_term(String loan_term) {
		this.loan_term = loan_term;
	}

	public String getLoan_purpose() {
		return loan_purpose;
	}

	public void setLoan_purpose(String loan_purpose) {
		this.loan_purpose = loan_purpose;
	}

	public String getRepay_type() {
		return repay_type;
	}

	public void setRepay_type(String repay_type) {
		this.repay_type = repay_type;
	}
	
	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public String toXML() {
		return BeanXMLMapping.toXML("hls003", this);
	}
}
