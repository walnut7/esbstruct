package com.kpleasing.esb.model.leasing014;

import java.io.Serializable;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING014Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -461470510780827228L;
	private String result_code;
	private String result_desc;
	private String byout_total_amount;
	private String byout_amount;
	private String last_rental;
	private String last_penalty;
	private String deposit_amount;
	private String service_amount;
	
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

	public String getByout_total_amount() {
		return byout_total_amount;
	}

	public void setByout_total_amount(String byout_total_amount) {
		this.byout_total_amount = byout_total_amount;
	}

	public String getByout_amount() {
		return byout_amount;
	}

	public void setByout_amount(String byout_amount) {
		this.byout_amount = byout_amount;
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

	public String getDeposit_amount() {
		return deposit_amount;
	}

	public void setDeposit_amount(String deposit_amount) {
		this.deposit_amount = deposit_amount;
	}

	public String getService_amount() {
		return service_amount;
	}

	public void setService_amount(String service_amount) {
		this.service_amount = service_amount;
	}
}
