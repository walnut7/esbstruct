package com.kpleasing.esb.model.leasing008;

import java.io.Serializable;

public class CancelBusinessOrderRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -4333183483498526589L;
	
	private String requestNum;
	
	private String applyno;
	
	private String reason;
	
	private String syscode;
	
	private String syspwd;
	
	private String sign;

	public String getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return  this.getRequestNum()+this.getSyscode()+this.getSyspwd()+this.getApplyno()+this.getReason();
	}
}
