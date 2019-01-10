package com.kpleasing.esb.model.leasing020;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING020Request extends RequestHeader implements Serializable {

	/** *  */
	private static final long serialVersionUID = 470200557043374716L;
	private String cert_type;
	private String cert_code;
	private String spdb_stCard_no;

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

	public String getSpdb_stCard_no() {
		return spdb_stCard_no;
	}

	public void setSpdb_stCard_no(String spdb_stCard_no) {
		this.spdb_stCard_no = spdb_stCard_no;
	}
}
