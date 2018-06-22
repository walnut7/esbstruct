package com.kpleasing.esb.model.leasing005;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING005Request extends RequestHeader implements Serializable  {

	/**	 * 	 */
	private static final long serialVersionUID = -7314755614501561828L;
	
	private String application_no;

	public String getApplication_no() {
		return application_no;
	}

	public void setApplication_no(String application_no) {
		this.application_no = application_no;
	}
}
