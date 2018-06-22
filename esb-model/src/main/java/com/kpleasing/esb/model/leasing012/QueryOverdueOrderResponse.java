package com.kpleasing.esb.model.leasing012;

import java.io.Serializable;
import java.util.List;

public class QueryOverdueOrderResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 1475954256132906916L;
	
	private String status;
	
	private String message;
	
	private List<OverdueOrder> overdueList;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<OverdueOrder> getOverdueList() {
		return overdueList;
	}

	public void setOverdueList(List<OverdueOrder> overdueList) {
		this.overdueList = overdueList;
	}
}
