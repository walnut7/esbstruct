package com.kpleasing.esb.model.leasing002;

import java.io.Serializable;

public class UserAccountInfo implements Serializable {
	
	/**	 * 	 */
	private static final long serialVersionUID = -6599474739818249992L;

	private String esb_bank_account_id;
	
	private String bank_account_name;
	
	private String deduction_bank_code;
	
	private String bank_full_name;
	
	private String bank_account_num;
	
	private String bank_branch_name;
	
	private String cell_phone;
	
	private String storable_card_no;
	
	private String ind_auth_flag;
	
	private String currency;
	
	private String deduction_flag;
	
	private String deduction_agency_id;
	
	private String enabled_flag;

	public String getEsb_bank_account_id() {
		return esb_bank_account_id;
	}

	public void setEsb_bank_account_id(String esb_bank_account_id) {
		this.esb_bank_account_id = esb_bank_account_id;
	}

	public String getBank_account_name() {
		return bank_account_name;
	}

	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}

	public String getDeduction_bank_code() {
		return deduction_bank_code;
	}

	public void setDeduction_bank_code(String deduction_bank_code) {
		this.deduction_bank_code = deduction_bank_code;
	}

	public String getBank_full_name() {
		return bank_full_name;
	}

	public void setBank_full_name(String bank_full_name) {
		this.bank_full_name = bank_full_name;
	}

	public String getBank_account_num() {
		return bank_account_num;
	}

	public void setBank_account_num(String bank_account_num) {
		this.bank_account_num = bank_account_num;
	}

	public String getBank_branch_name() {
		return bank_branch_name;
	}

	public void setBank_branch_name(String bank_branch_name) {
		this.bank_branch_name = bank_branch_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDeduction_flag() {
		return deduction_flag;
	}

	public void setDeduction_flag(String deduction_flag) {
		this.deduction_flag = deduction_flag;
	}

	public String getDeduction_agency_id() {
		return deduction_agency_id;
	}

	public void setDeduction_agency_id(String deduction_agency_id) {
		this.deduction_agency_id = deduction_agency_id;
	}

	public String getEnabled_flag() {
		return enabled_flag;
	}

	public void setEnabled_flag(String enabled_flag) {
		this.enabled_flag = enabled_flag;
	}

	public String getCell_phone() {
		return cell_phone;
	}

	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}

	public String getStorable_card_no() {
		return storable_card_no;
	}

	public void setStorable_card_no(String storable_card_no) {
		this.storable_card_no = storable_card_no;
	}

	public String getInd_auth_flag() {
		return ind_auth_flag;
	}

	public void setInd_auth_flag(String ind_auth_flag) {
		this.ind_auth_flag = ind_auth_flag;
	}

}
