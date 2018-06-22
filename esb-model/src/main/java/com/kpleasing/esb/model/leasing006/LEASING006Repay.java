package com.kpleasing.esb.model.leasing006;

import java.io.Serializable;

public class LEASING006Repay implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = -3102999749937076748L;
	private String term;
	private String due_date;
	private String due_amount;
	private String receive_amount;
	private String receive_status;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getDue_amount() {
		return due_amount;
	}

	public void setDue_amount(String due_amount) {
		this.due_amount = due_amount;
	}

	public String getReceive_amount() {
		return receive_amount;
	}

	public void setReceive_amount(String receive_amount) {
		this.receive_amount = receive_amount;
	}

	public String getReceive_status() {
		return receive_status;
	}

	public void setReceive_status(String receive_status) {
		this.receive_status = receive_status;
	}
}
