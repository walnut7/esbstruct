package com.kpleasing.esb.receive.protocol.kpfs.hls004;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS004 {
	
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String query_type;
	private String crd_cont_no;
	private String bill_no;
	private String cert_type;
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

	public String getQuery_type() {
		return query_type;
	}

	public void setQuery_type(String query_type) {
		this.query_type = query_type;
	}

	public String getCrd_cont_no() {
		return crd_cont_no;
	}

	public void setCrd_cont_no(String crd_cont_no) {
		this.crd_cont_no = crd_cont_no;
	}

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
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
		return BeanXMLMapping.toXML("hls004", this);
	}

}
