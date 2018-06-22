package com.kpleasing.esb.model.leasing014;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING014Request extends RequestHeader implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = 2489693038057515353L;
	private String applyno;
	
	public String getApplyno() {
		return applyno;
	}
	
	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}
}
