package com.kpleasing.esb.model.leasing009;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING009Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 8500201582301624476L;
	
	private String cust_id;

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

}
