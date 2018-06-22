package com.kpleasing.esb.model.crm010;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class SyncFaceVedioTimeRequest extends CrmRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -7485411632369909429L;
	private String cust_id;
	private String cust_name;
	private String first_date;
	private String second_date;
	
	public String getCust_id() {
		return cust_id;
	}
	
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getFirst_date() {
		return first_date;
	}

	public void setFirst_date(String first_date) {
		this.first_date = first_date;
	}

	public String getSecond_date() {
		return second_date;
	}

	public void setSecond_date(String second_date) {
		this.second_date = second_date;
	}
}
