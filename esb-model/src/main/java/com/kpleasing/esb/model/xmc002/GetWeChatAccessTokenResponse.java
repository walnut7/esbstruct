package com.kpleasing.esb.model.xmc002;

import java.io.Serializable;

import com.kpleasing.esb.model.xmc.XMCResponseHeader;

public class GetWeChatAccessTokenResponse extends XMCResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -1906751829409634414L;
	private String result_code;
	private String result_desc;
	private String access_token;

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_desc() {
		return result_desc;
	}

	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
