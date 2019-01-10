package com.kpleasing.esb.model.leasing020;

import java.io.Serializable;

import com.kpleasing.esb.tools.StringUtil;

public class SavePFStCardNumRequest implements Serializable {

	/** *  */
	private static final long serialVersionUID = 470200557043374716L;
	
	private String request_number;
	
	private String user_account;
	
	private String password;
	
	private String id_card_no;
	
	private String stcardnum;
	
	private String sign;

	public String getRequest_number() {
		return request_number;
	}

	public void setRequest_number(String request_number) {
		this.request_number = request_number;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId_card_no() {
		return id_card_no;
	}

	public void setId_card_no(String id_card_no) {
		this.id_card_no = id_card_no;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getStcardnum() {
		return stcardnum;
	}

	public void setStcardnum(String stcardnum) {
		this.stcardnum = stcardnum;
	}

	public String getSignContent() {
		return StringUtil.setNullToBlank(this.getRequest_number()) + StringUtil.setNullToBlank(this.getUser_account())
		+ StringUtil.setNullToBlank(this.getPassword()) + StringUtil.setNullToBlank(this.getId_card_no()) + StringUtil.setNullToBlank(this.getStcardnum());
	}

}
