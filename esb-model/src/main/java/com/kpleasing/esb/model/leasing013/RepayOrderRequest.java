package com.kpleasing.esb.model.leasing013;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class RepayOrderRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 5509313227089299191L;
	
	private String requestNum;
	
	private String syscode;
	
	private String syspwd;
	
	private String due_date;
	
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
	
	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) + StringUtil.setNullToBlank(this.getDue_date());
	}
}
