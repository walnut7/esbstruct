package com.kpleasing.esb.model.leasing018;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING018Request extends RequestHeader implements Serializable {

	/** *  */
	private static final long serialVersionUID = -1367773286979397157L;
	private String name;
	private String phone;
	private String cert_type;
	private String cert_code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

}
