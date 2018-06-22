package com.kpleasing.esb.model.leasing005;

import java.io.Serializable;

public class QueryAutoTrialResultRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 2794784209153202734L;
	
	private String requestNum;
	
	private String syscode;
	
	private String syspwd;
	
	private String report_number;
	
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

	public String getReport_number() {
		return report_number;
	}

	public void setReport_number(String report_number) {
		this.report_number = report_number;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignContent() {
		return this.getRequestNum() + this.getSyscode() + this.getSyspwd() + this.getReport_number();
	}
}
