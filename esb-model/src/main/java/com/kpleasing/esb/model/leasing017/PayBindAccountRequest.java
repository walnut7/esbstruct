package com.kpleasing.esb.model.leasing017;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class PayBindAccountRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 526699863268307007L;
	private String requestNum;
	private String syscode;
	private String syspwd;
	private String external_ref_number;
	private String customer_id;
	private String bank_account_num;
	private String valid_code;
	private String token;
	private String phone;
	private String sign;
	
	public String getRequestNum() {
		return requestNum;
	}
	
	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getSyspwd() {
		return syspwd;
	}

	public void setSyspwd(String syspwd) {
		this.syspwd = syspwd;
	}

	public String getExternal_ref_number() {
		return external_ref_number;
	}

	public void setExternal_ref_number(String external_ref_number) {
		this.external_ref_number = external_ref_number;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getBank_account_num() {
		return bank_account_num;
	}

	public void setBank_account_num(String bank_account_num) {
		this.bank_account_num = bank_account_num;
	}

	public String getValid_code() {
		return valid_code;
	}

	public void setValid_code(String valid_code) {
		this.valid_code = valid_code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) 
		+StringUtil.setNullToBlank(this.getExternal_ref_number())+StringUtil.setNullToBlank(this.getCustomer_id())+StringUtil.setNullToBlank(this.getBank_account_num())
		+StringUtil.setNullToBlank(this.getValid_code())+StringUtil.setNullToBlank(this.getToken())+StringUtil.setNullToBlank(this.getPhone());
	}
}
