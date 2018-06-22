package com.kpleasing.esb.model.crm011;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM011Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -5810670316602223038L;
	private String cust_id;
	private String account;
	
	public String getCust_id() {
		return cust_id;
	}
	
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
