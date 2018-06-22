package com.kpleasing.esb.model.leasing009;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class QueryOrderByCustIdRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 7209340557972755593L;
	
	private String requestNum;
	
	private String syscode;
	
	private String syspwd;
	
	private String cust_id;
	
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

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) 
		+StringUtil.setNullToBlank(this.getCust_id());
	}
}
