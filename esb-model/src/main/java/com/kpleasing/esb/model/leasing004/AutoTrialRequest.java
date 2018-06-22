package com.kpleasing.esb.model.leasing004;

import java.io.Serializable;


public class AutoTrialRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -1648670622386527029L;
	
	private String requestNum;
	
	private String syscode;
	
	private String syspwd;
	
	private String cust_name;
	
	private String phone;
	
	private String cert_type;
	
	private String cert_code;
	
	private String sign;

	public String getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getSyspwd() {
		return syspwd;
	}

	public void setSyspwd(String syspwd) {
		this.syspwd = syspwd;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
	
	public String getSignContent() {
		return this.getRequestNum() + this.getSyscode() + this.getSyspwd() + this.getCust_name() + this.getPhone()
				+ this.getCert_type() + this.getCert_code();
	}
}
