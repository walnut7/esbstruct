package com.kpleasing.esb.model.xmc001;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class XMC001Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 3587178412904471698L;
	private String cust_id;
	
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
}
