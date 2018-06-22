package com.kpleasing.esb.model.crm011;

import java.io.Serializable;
import java.util.List;

import com.kpleasing.esb.model.ResponseHeader;

public class CRM011Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -5334612334092378653L;
	private String result_code;
	private String result_desc;
	private String cust_id;
	private List<CRM011BankAccount> accounts;
	
	public String getResult_code() {
		return result_code;
	}
	
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_desc() {
		return result_desc;
	}

	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public List<CRM011BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<CRM011BankAccount> accounts) {
		this.accounts = accounts;
	}
}
