package com.kpleasing.esb.model.leasing010;

import java.io.Serializable;

public class Repay implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 4775003009382826914L;
	
	private String due_amount;
	
	private String applyno;
	
	private String times;
	
	private String write_off_flag;
	
	private String due_date;
	
	private String received_amount;
	
	public String getDue_amount() {
		return due_amount;
	}
	
	public void setDue_amount(String due_amount) {
		this.due_amount = due_amount;
	}

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getWrite_off_flag() {
		return write_off_flag;
	}

	public void setWrite_off_flag(String write_off_flag) {
		this.write_off_flag = write_off_flag;
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
}
