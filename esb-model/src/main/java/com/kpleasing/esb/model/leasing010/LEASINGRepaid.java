package com.kpleasing.esb.model.leasing010;

import java.io.Serializable;

public class LEASINGRepaid implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 8550015250887098959L;
	private String term;
	private String repaid_type;
	private String due_amount;
	private String due_date;
	private String repaid_amount;
	private String repaid_date;
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}

	public String getRepaid_type() {
		return repaid_type;
	}

	public void setRepaid_type(String repaid_type) {
		this.repaid_type = repaid_type;
	}

	public String getDue_amount() {
		return due_amount;
	}

	public void setDue_amount(String due_amount) {
		this.due_amount = due_amount;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getRepaid_amount() {
		return repaid_amount;
	}

	public void setRepaid_amount(String repaid_amount) {
		this.repaid_amount = repaid_amount;
	}

	public String getRepaid_date() {
		return repaid_date;
	}

	public void setRepaid_date(String repaid_date) {
		this.repaid_date = repaid_date;
	}
}
