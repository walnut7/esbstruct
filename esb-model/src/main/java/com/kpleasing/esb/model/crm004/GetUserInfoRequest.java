package com.kpleasing.esb.model.crm004;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class GetUserInfoRequest extends CrmRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -8707317549317438313L;
	
	private String cust_id;

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
}
