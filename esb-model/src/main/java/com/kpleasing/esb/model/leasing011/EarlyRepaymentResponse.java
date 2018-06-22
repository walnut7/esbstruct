package com.kpleasing.esb.model.leasing011;

import java.io.Serializable;

public class EarlyRepaymentResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 8502368477954584237L;
	
	private String status;
	
	private String message;

	private String et_amount;
	
	private String last_principal;
	
	private String last_rental;
	
	private String last_penalty;
	
	private String charge_amount;
	
	private String deposit_amount;
	
	private String other_amount;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEt_amount() {
		return et_amount;
	}

	public void setEt_amount(String et_amount) {
		this.et_amount = et_amount;
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

	public String getCharge_amount() {
		return charge_amount;
	}

	public void setCharge_amount(String charge_amount) {
		this.charge_amount = charge_amount;
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