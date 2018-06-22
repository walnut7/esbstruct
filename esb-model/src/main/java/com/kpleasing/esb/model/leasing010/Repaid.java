package com.kpleasing.esb.model.leasing010;

import java.io.Serializable;

public class Repaid implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -1659550365359867532L;
	private String times;
	private String cf_item;
	private String due_amount;
	private String due_date;
	private String received_amount;
	private String received_date;
	
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getCf_item() {
		return cf_item;
	}

	public void setCf_item(String cf_item) {
		this.cf_item = cf_item;
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

	public String getReceived_amount() {
		return received_amount;
	}

	public void setReceived_amount(String received_amount) {
		this.received_amount = received_amount;
	}

	public String getReceived_date() {
		return received_date;
	}

	public void setReceived_date(String received_date) {
		this.received_date = received_date;
	}
}
