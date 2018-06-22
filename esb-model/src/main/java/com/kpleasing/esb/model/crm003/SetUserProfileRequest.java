package com.kpleasing.esb.model.crm003;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class SetUserProfileRequest extends CrmRequestHeader implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = -2321061587905219189L;
	
	private String cust_id;
	
	private String phone;
	
	private String cust_name;
	
	private String cert_type;
	
	private String cert_code;

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
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
}
