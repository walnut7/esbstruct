package com.kpleasing.esb.model.leasing013;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING013Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -911339335218519359L;
	
	private String repay_date;

	public String getRepay_date() {
		return repay_date;
	}

	public void setRepay_date(String repay_date) {
		this.repay_date = repay_date;
	}
}
