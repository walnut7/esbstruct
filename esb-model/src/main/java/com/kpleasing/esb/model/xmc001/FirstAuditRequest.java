package com.kpleasing.esb.model.xmc001;

import java.io.Serializable;

import com.kpleasing.esb.model.xmc.XMCRequestHeader;

public class FirstAuditRequest extends XMCRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -2814879761523881461L;
	private String cust_id;
	
	public String getCust_id() {
		return cust_id;
	}
	
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	
}
