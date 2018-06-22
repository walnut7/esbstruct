package com.kpleasing.esb.model.leasing001;

import java.io.Serializable;

public class SendSMSRequest implements Serializable {
	
	/**	 * 	 */
	private static final long serialVersionUID = 2524158976201387831L;

	private String requestNum;
	
	private String phone;
	
	private String content;
	
	private String syscode;
	
	private String syspwd;
	
	private String sign;

	public String getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}
	
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

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getSyspwd() {
		return syspwd;
	}

	public void setSyspwd(String syspwd) {
		this.syspwd = syspwd;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return this.requestNum+this.content+this.syscode+this.syspwd+this.phone;
	}
}
