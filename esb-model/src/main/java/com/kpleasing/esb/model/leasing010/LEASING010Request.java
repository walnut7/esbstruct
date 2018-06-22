package com.kpleasing.esb.model.leasing010;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING010Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -4083716916088206163L;
	
	private String applyno;

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

}
