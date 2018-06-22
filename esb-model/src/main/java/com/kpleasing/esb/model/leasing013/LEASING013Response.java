package com.kpleasing.esb.model.leasing013;

import java.io.Serializable;
import java.util.List;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING013Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 764092478949350167L;
	
	private String result_code;
	
	private String result_desc;
	
	private List<LEASING013Repay> repay_list;

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

	public List<LEASING013Repay> getRepay_list() {
		return repay_list;
	}

	public void setRepay_list(List<LEASING013Repay> repay_list) {
		this.repay_list = repay_list;
	}
}
