package com.kpleasing.esb.model.crm004;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM004Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 5722871291539707816L;
	
	private String cust_id;

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
}
