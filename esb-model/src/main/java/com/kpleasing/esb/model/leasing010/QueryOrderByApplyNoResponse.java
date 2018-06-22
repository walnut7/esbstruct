package com.kpleasing.esb.model.leasing010;

import java.io.Serializable;
import java.util.List;


public class QueryOrderByApplyNoResponse implements Serializable {
	/**	 * 	 */
	private static final long serialVersionUID = 4684110691645223122L;
	private String deposit;
	private String document_status;
	private String applyno;
	private String repay_day;
	private String plan_session_id;
	private String brand_desc;
	private String series_desc;
	private String model_desc;
	private String sku;
	private String serial_number;
	private String engine;
	private String overdue_status;
	private String lease_channel;
	private String received_principal;
	private String last_interest;
	private String down_payment;
	private String total_interest;
	private String loantenor;
	private String total_principal;
	private String finance_amount;
	private String user_pf_capital_flag;
	private String lease_start_date;
	private String status;
	private String message;
	private List<Repay> repays ;
	private List<Repaid> repaids ;
	
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

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getOverdue_status() {
		return overdue_status;
	}

	public void setOverdue_status(String overdue_status) {
		this.overdue_status = overdue_status;
	}

	public String getLease_channel() {
		return lease_channel;
	}

	public void setLease_channel(String lease_channel) {
		this.lease_channel = lease_channel;
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

	public String getUser_pf_capital_flag() {
		return user_pf_capital_flag;
	}

	public void setUser_pf_capital_flag(String user_pf_capital_flag) {
		this.user_pf_capital_flag = user_pf_capital_flag;
	}

	public String getLease_start_date() {
		return lease_start_date;
	}

	public void setLease_start_date(String lease_start_date) {
		this.lease_start_date = lease_start_date;
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

	public List<Repay> getRepays() {
		return repays;
	}

	public void setRepays(List<Repay> repays) {
		this.repays = repays;
	}

	public List<Repaid> getRepaids() {
		return repaids;
	}

	public void setRepaids(List<Repaid> repaids) {
		this.repaids = repaids;
	}
}
