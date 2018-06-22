package com.kpleasing.esb.model.crm008;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM008Request extends RequestHeader implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = 406258656458816116L;
	
	private String cust_id;
	private String cust_name;
	private String gender;
	private String phone;
	private String reserve_time;
	private String reserve_store;
	private String product_title;
	private String product_desc;
	private String memo;

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReserve_time() {
		return reserve_time;
	}

	public void setReserve_time(String reserve_time) {
		this.reserve_time = reserve_time;
	}

	public String getReserve_store() {
		return reserve_store;
	}

	public void setReserve_store(String reserve_store) {
		this.reserve_store = reserve_store;
	}

	public String getProduct_title() {
		return product_title;
	}

	public void setProduct_title(String product_title) {
		this.product_title = product_title;
	}

	public String getProduct_desc() {
		return product_desc;
	}

	public void setProduct_desc(String product_desc) {
		this.product_desc = product_desc;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
