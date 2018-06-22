package com.kpleasing.esb.model.leasing011;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING011Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -3595302085418871637L;

	private String result_code;
	
	private String result_desc;
	
	private String last_total_amount;
	
	private String last_principal;
	
	private String last_rental;
	
	private String last_penalty;
	
	private String service_amount;
	
	private String deposit_amount;
	
	private String other_amount;


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

	public String getLast_total_amount() {
		return last_total_amount;
	}

	public void setLast_total_amount(String last_total_amount) {
		this.last_total_amount = last_total_amount;
	}

	public String getLast_principal() {
		return last_principal;
	}

	public void setLast_principal(String last_principal) {
		this.last_principal = last_principal;
	}

	public String getLast_rental() {
		return last_rental;
	}

	public void setLast_rental(String last_rental) {
		this.last_rental = last_rental;
	}

	public String getLast_penalty() {
		return last_penalty;
	}

	public void setLast_penalty(String last_penalty) {
		this.last_penalty = last_penalty;
	}

	public String getService_amount() {
		return service_amount;
	}

	public void setService_amount(String service_amount) {
		this.service_amount = service_amount;
	}

	public String getDeposit_amount() {
		return deposit_amount;
	}

	public void setDeposit_amount(String deposit_amount) {
		this.deposit_amount = deposit_amount;
	}

	public String getOther_amount() {
		return other_amount;
	}

	public void setOther_amount(String other_amount) {
		this.other_amount = other_amount;
	}
}
