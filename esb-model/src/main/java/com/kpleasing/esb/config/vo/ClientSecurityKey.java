package com.kpleasing.esb.config.vo;

import java.io.Serializable;

public class ClientSecurityKey implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -8978345269367423077L;
	
	private int id;
	
	private int securityId;
	
	private String clientCode;
	
	private String clientSecurity;
	
	private String clientSignKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSecurityId() {
		return securityId;
	}

	public void setSecurityId(int securityId) {
		this.securityId = securityId;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getClientSecurity() {
		return clientSecurity;
	}

	public void setClientSecurity(String clientSecurity) {
		this.clientSecurity = clientSecurity;
	}

	public String getClientSignKey() {
		return clientSignKey;
	}

	public void setClientSignKey(String clientSignKey) {
		this.clientSignKey = clientSignKey;
	}
	
	public String toString() {
		return "id=" + this.id + ",securityId=" + this.securityId + ",clientCode=" + this.clientCode
				+ ",clientSecurity=" + this.clientSecurity + ",clientSignKey=" + this.clientSignKey;
	}
}
