package com.kpleasing.esb.config.vo;

import java.io.Serializable;
import java.util.List;

public class ParameterConfig implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -8222087014223551484L;
	
	private int securityId;
	
	private String apiCode;
	
	private String clazz;
	
	private String appCode;
	
	private String appSecurity;
	
	private String destSystemUrl;
	
	private String destSystemPort;
	
	private String desKey;
	
	private String desIv;
	
	private String signKey;
	
	private List<ClientSecurityKey> clientSecurityKey;

	public int getSecurityId() {
		return securityId;
	}

	public void setSecurityId(int securityId) {
		this.securityId = securityId;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppSecurity() {
		return appSecurity;
	}

	public void setAppSecurity(String appSecurity) {
		this.appSecurity = appSecurity;
	}

	public String getDestSystemUrl() {
		return destSystemUrl;
	}

	public void setDestSystemUrl(String destSystemUrl) {
		this.destSystemUrl = destSystemUrl;
	}

	public String getDestSystemPort() {
		return destSystemPort;
	}

	public void setDestSystemPort(String destSystemPort) {
		this.destSystemPort = destSystemPort;
	}

	public String getDesKey() {
		return desKey;
	}

	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}

	public String getDesIv() {
		return desIv;
	}

	public void setDesIv(String desIv) {
		this.desIv = desIv;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public List<ClientSecurityKey> getClientSecurityKey() {
		return clientSecurityKey;
	}

	public void setClientSecurityKey(List<ClientSecurityKey> clientSecurityKey) {
		this.clientSecurityKey = clientSecurityKey;
	}
	
	public String toString() {
		return "securityId=" + this.securityId + ",apiCode=" + this.apiCode + ",clazz=" + this.clazz + ",appCode="
				+ this.appCode + ",appSecurity=" + this.appSecurity + ",destSystemUrl=" + destSystemUrl
				+ ",destSystemPort=" + this.destSystemPort + ",desKey=" + this.desKey + ",desIv=" + this.desIv
				+ ",signKey=" + this.signKey;
	}
}
