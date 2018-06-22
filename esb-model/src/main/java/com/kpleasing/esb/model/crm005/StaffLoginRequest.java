package com.kpleasing.esb.model.crm005;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class StaffLoginRequest extends CrmRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -1213371460976438958L;
	
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
