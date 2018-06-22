package com.kpleasing.esb.model.crm005;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM005Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 202798565001947520L;
	
	private String appcode;
	
	private String login_id;
	
	private String password;

	public String getAppcode() {
		return appcode;
	}

	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
