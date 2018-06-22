package com.kpleasing.esb.model.leasing011;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class EarlyRepaymentRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 7655350278552593937L;
	
	private String requestNum;
	
	private String syscode;
	
	private String syspwd;
	
	private String applyno;
	
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

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) 
		+StringUtil.setNullToBlank(this.getApplyno());
	}
}
