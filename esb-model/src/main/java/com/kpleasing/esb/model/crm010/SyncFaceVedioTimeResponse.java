package com.kpleasing.esb.model.crm010;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmResponseHeader;

public class SyncFaceVedioTimeResponse extends CrmResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -6508439130124927126L;
	private String result_code;
	private String result_desc;
	
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
}
