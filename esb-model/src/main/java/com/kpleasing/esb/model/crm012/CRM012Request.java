package com.kpleasing.esb.model.crm012;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class CRM012Request extends RequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -4313568330475616716L;
	private String cust_id;
	private String account_name;
	private String account;
	private String short_account_no;
	private String bank_phone;
	private String is_yjzf_bind;
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	
	public String getAccount_name() {
		return account_name;
	}
	
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getShort_account_no() {
		return short_account_no;
	}
	
	public void setShort_account_no(String short_account_no) {
		this.short_account_no = short_account_no;
	}
	
	public String getBank_phone() {
		return bank_phone;
	}
	
	public void setBank_phone(String bank_phone) {
		this.bank_phone = bank_phone;
	}
	
	public String getIs_yjzf_bind() {
		return is_yjzf_bind;
	}
	
	public void setIs_yjzf_bind(String is_yjzf_bind) {
		this.is_yjzf_bind = is_yjzf_bind;
	}
}
