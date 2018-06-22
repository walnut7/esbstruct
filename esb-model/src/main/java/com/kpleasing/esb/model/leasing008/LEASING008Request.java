package com.kpleasing.esb.model.leasing008;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING008Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -1276487567874657962L;
	
	private String applyno;
	
	private String reason;

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
