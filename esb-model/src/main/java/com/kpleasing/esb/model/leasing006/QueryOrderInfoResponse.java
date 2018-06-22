package com.kpleasing.esb.model.leasing006;

import java.io.Serializable;
import java.util.List;

public class QueryOrderInfoResponse implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -516792720432630035L;
	
	private String deposit;
	
	private String document_status;
	
	private String applyno;
	
	private String repay_day;
	
	private String plan_session_id;
	
	private String brand_desc;
	
	private String series_desc;
	
	private String model_desc;
	
	private String sku;
	
	private String received_principal;
	
	private String last_interest;
	
	private String down_payment;
	
	private String total_interest;
	
	private String loantenor;
	
	private String total_principal;
	
	private String finance_amount;
	
	private String status;
	
	private String message;
	
	private List<QueryOrderInfoRepay> repayList ;

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getDocument_status() {
		return document_status;
	}

	public void setDocument_status(String document_status) {
		this.document_status = document_status;
	}

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getRepay_day() {
		return repay_day;
	}

	public void setRepay_day(String repay_day) {
		this.repay_day = repay_day;
	}

	public String getPlan_session_id() {
		return plan_session_id;
	}

	public void setPlan_session_id(String plan_session_id) {
		this.plan_session_id = plan_session_id;
	}

	public String getBrand_desc() {
		return brand_desc;
	}

	public void setBrand_desc(String brand_desc) {
		this.brand_desc = brand_desc;
	}

	public String getSeries_desc() {
		return series_desc;
	}

	public void setSeries_desc(String series_desc) {
		this.series_desc = series_desc;
	}

	public String getModel_desc() {
		return model_desc;
	}

	public void setModel_desc(String model_desc) {
		this.model_desc = model_desc;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getReceived_principal() {
		return received_principal;
	}

	public void setReceived_principal(String received_principal) {
		this.received_principal = received_principal;
	}

	public String getLast_interest() {
		return last_interest;
	}

	public void setLast_interest(String last_interest) {
		this.last_interest = last_interest;
	}

	public String getDown_payment() {
		return down_payment;
	}

	public void setDown_payment(String down_payment) {
		this.down_payment = down_payment;
	}

	public String getTotal_interest() {
		return total_interest;
	}

	public void setTotal_interest(String total_interest) {
		this.total_interest = total_interest;
	}

	public String getLoantenor() {
		return loantenor;
	}

	public void setLoantenor(String loantenor) {
		this.loantenor = loantenor;
	}

	public String getTotal_principal() {
		return total_principal;
	}

	public void setTotal_principal(String total_principal) {
		this.total_principal = total_principal;
	}

	public String getFinance_amount() {
		return finance_amount;
	}

	public void setFinance_amount(String finance_amount) {
		this.finance_amount = finance_amount;
	}

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

	public List<QueryOrderInfoRepay> getRepayList() {
		return repayList;
	}

	public void setRepayList(List<QueryOrderInfoRepay> repayList) {
		this.repayList = repayList;
	}
}
