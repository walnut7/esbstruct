package com.kpleasing.esb.model.leasing001;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING001Request extends RequestHeader implements Serializable {
	
	/**	 * */	
	private static final long serialVersionUID = -3803025978899976656L;

	private String phone;
	
	private String content;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
