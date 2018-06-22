package com.kpleasing.esb.model.leasing006;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING006Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -6868589045231801972L;
	
	private String phone;
	
	private String cert_type;
	
	private String cert_code;
	
	private String applyno;

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

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}
}
