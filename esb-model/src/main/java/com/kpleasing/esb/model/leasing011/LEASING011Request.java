package com.kpleasing.esb.model.leasing011;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING011Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -5046937176122235702L;
	
	private String applyno;

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}
}
