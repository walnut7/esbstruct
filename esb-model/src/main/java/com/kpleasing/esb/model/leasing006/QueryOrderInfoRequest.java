package com.kpleasing.esb.model.leasing006;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class QueryOrderInfoRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -3312009198371127558L;
	
	private String requestNum;
	
	private String phone;
	
	private String certType;
	
	private String certCode;
	
	private String applyno;
	
	private String syscode;
	
	private String syspwd;
	
	private String sign;

	public String getRequestNum() {
		return requestNum;
	}

	public void setRequestNum(String requestNum) {
		this.requestNum = requestNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertCode() {
		return certCode;
	}

	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
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
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) + StringUtil.setNullToBlank(this.getPhone()) + StringUtil.setNullToBlank(this.getCertType())
				+ StringUtil.setNullToBlank(this.getCertCode())+StringUtil.setNullToBlank(this.getApplyno());
	}
}
