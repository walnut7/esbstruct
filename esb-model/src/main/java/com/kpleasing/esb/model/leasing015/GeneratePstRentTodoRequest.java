package com.kpleasing.esb.model.leasing015;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class GeneratePstRentTodoRequest implements Serializable {
	
	/**	 * 	 */
	private static final long serialVersionUID = -6424050902887643159L;
	private String requestNum;
	private String syscode;
	private String syspwd;
	private String applyno;
	private String applyType;
	private String name;
	private String deliveryAddr;
	private String phone;
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

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequestNum()) + StringUtil.setNullToBlank(this.getSyscode()) + StringUtil.setNullToBlank(this.getSyspwd()) 
		+StringUtil.setNullToBlank(this.getApplyno())+StringUtil.setNullToBlank(this.getApplyType())+StringUtil.setNullToBlank(this.getName())
		+StringUtil.setNullToBlank(this.getDeliveryAddr())+StringUtil.setNullToBlank(this.getPhone());
	}
}
