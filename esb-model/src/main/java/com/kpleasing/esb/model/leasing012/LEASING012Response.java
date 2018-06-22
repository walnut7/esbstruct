package com.kpleasing.esb.model.leasing012;

import java.io.Serializable;
import java.util.List;

import com.kpleasing.esb.model.ResponseHeader;

public class LEASING012Response extends ResponseHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -789567799338561048L;
	
	private String result_code;
	
	private String result_desc;
	
	private List<LEASING012OverdueOrder> overdueList;

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

	public List<LEASING012OverdueOrder> getOverdueList() {
		return overdueList;
	}

	public void setOverdueList(List<LEASING012OverdueOrder> overdueList) {
		this.overdueList = overdueList;
	}
}
