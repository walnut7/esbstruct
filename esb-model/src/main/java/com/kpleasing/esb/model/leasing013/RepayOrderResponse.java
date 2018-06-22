package com.kpleasing.esb.model.leasing013;

import java.io.Serializable;
import java.util.List;

public class RepayOrderResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -6515124251321447514L;
	
	private String status;
	
	private String message;
	
	private List<RepayOrder> repayOrders;

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

	public List<RepayOrder> getRepayOrders() {
		return repayOrders;
	}

	public void setRepayOrders(List<RepayOrder> repayOrders) {
		this.repayOrders = repayOrders;
	}
}
