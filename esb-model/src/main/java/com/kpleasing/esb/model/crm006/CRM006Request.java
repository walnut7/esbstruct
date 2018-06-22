package com.kpleasing.esb.model.crm006;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM006Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -3792868910367085821L;
	
	private String login_id;
	
	private String password;
	
	private String new_password;

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

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
}
