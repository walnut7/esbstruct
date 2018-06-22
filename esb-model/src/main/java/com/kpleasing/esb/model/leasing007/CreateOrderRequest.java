package com.kpleasing.esb.model.leasing007;

import java.io.Serializable;

public class CreateOrderRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 3198666926489491173L;
	
	private String request_number;
	
	private String user_account;
	
	private String password;
	
	private String sign;
	
	private String esb_bp_id;
	
	private String id_card_no;
	
	private String plan_session_id;
	
	private String agent_bp_name;
	
	private String sale_id;
	
	private String agent_code;

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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEsb_bp_id() {
		return esb_bp_id;
	}

	public void setEsb_bp_id(String esb_bp_id) {
		this.esb_bp_id = esb_bp_id;
	}

	public String getId_card_no() {
		return id_card_no;
	}

	public void setId_card_no(String id_card_no) {
		this.id_card_no = id_card_no;
	}

	public String getPlan_session_id() {
		return plan_session_id;
	}

	public void setPlan_session_id(String plan_session_id) {
		this.plan_session_id = plan_session_id;
	}

	public String getAgent_bp_name() {
		return agent_bp_name;
	}

	public void setAgent_bp_name(String agent_bp_name) {
		this.agent_bp_name = agent_bp_name;
	}

	public String getSignContent(){
		return this.getRequest_number()+this.getUser_account()+this.getPassword();
	}

	public String getSale_id() {
		return sale_id;
	}

	public void setSale_id(String sale_id) {
		this.sale_id = sale_id;
	}

	public String getAgent_code() {
		return agent_code;
	}

	public void setAgent_code(String agent_code) {
		this.agent_code = agent_code;
	}
	
}
