package com.kpleasing.esb.model.leasing002;

import java.io.Serializable;
import java.util.List;

public class SyncUserInfoRequest implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -278506850469967227L;

	private String request_number;
	
	private String user_account;
	
	private String password;
	
	private String sign;
	
	private String esb_bp_id;
	
	private String wxFlag;
	
	private String bp_name;
	
	private String bp_name_spell;
	
	private String id_type;
	
	private String id_card_no;
	
	private String local_person_flag;
	
	private String gender;
	
	private String date_of_birth;
	
	private String academic_background;
	
	private String marital_status;
	
	private String entire_period;
	
	private String licensing_organization;
	
	private String deposit_certificate;
	
	private String nationality;
	
	private String driver_license;
	
	private String annual_income;
	
	private String income_from;
	
	private String income_status;
	
	private String working_duration;
	
	private String working_place;
	
	private String office_address;
	
	private String position;
	
	private String industry;
	
	private String credit_limit;
	
	private String phone;
	
	private String cell_phone;
	
	private String email;
	
	private String postcode;
	
	private String home_phone;
	
	private String live_status;
	
	private String id_address;
	
	private String contact_address;
	
	private String blacklist_flag;
	
	private String blacklist_note;
	
	private String enabled_flag;
	
	private String note;
	
	private String bp_name_sp;
	
	private String id_type_sp;
	
	private String id_card_no_sp;
	
	private String spouse_phone;
	
	private String spouse_income_from;
	
	private String spouse_annual_income;
	
	private String spouse_work_unit;
	
	private String address_sp;
	
	private List<UserContactInfo> contactInfo;
	
	private List<UserAccountInfo> accountInfo;
	
	private List<UserAttachmentInfo> attachs;

	public String getRequest_number() {
		return request_number;
	}

	public void setRequest_number(String request_number) {
		this.request_number = request_number;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEsb_bp_id() {
		return esb_bp_id;
	}

	public void setEsb_bp_id(String esb_bp_id) {
		this.esb_bp_id = esb_bp_id;
	}

	public String getBp_name() {
		return bp_name;
	}

	public void setBp_name(String bp_name) {
		this.bp_name = bp_name;
	}
	
	public String getWxFlag() {
		return wxFlag;
	}

	public void setWxFlag(String wxFlag) {
		this.wxFlag = wxFlag;
	}

	public String getBp_name_spell() {
		return bp_name_spell;
	}

	public void setBp_name_spell(String bp_name_spell) {
		this.bp_name_spell = bp_name_spell;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getId_card_no() {
		return id_card_no;
	}

	public void setId_card_no(String id_card_no) {
		this.id_card_no = id_card_no;
	}

	public String getLocal_person_flag() {
		return local_person_flag;
	}

	public void setLocal_person_flag(String local_person_flag) {
		this.local_person_flag = local_person_flag;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getAcademic_background() {
		return academic_background;
	}

	public void setAcademic_background(String academic_background) {
		this.academic_background = academic_background;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public String getEntire_period() {
		return entire_period;
	}

	public void setEntire_period(String entire_period) {
		this.entire_period = entire_period;
	}

	public String getLicensing_organization() {
		return licensing_organization;
	}

	public void setLicensing_organization(String licensing_organization) {
		this.licensing_organization = licensing_organization;
	}

	public String getDeposit_certificate() {
		return deposit_certificate;
	}

	public void setDeposit_certificate(String deposit_certificate) {
		this.deposit_certificate = deposit_certificate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDriver_license() {
		return driver_license;
	}

	public void setDriver_license(String driver_license) {
		this.driver_license = driver_license;
	}

	public String getAnnual_income() {
		return annual_income;
	}

	public void setAnnual_income(String annual_income) {
		this.annual_income = annual_income;
	}

	public String getIncome_from() {
		return income_from;
	}

	public void setIncome_from(String income_from) {
		this.income_from = income_from;
	}

	public String getIncome_status() {
		return income_status;
	}

	public void setIncome_status(String income_status) {
		this.income_status = income_status;
	}

	public String getWorking_duration() {
		return working_duration;
	}

	public void setWorking_duration(String working_duration) {
		this.working_duration = working_duration;
	}

	public String getWorking_place() {
		return working_place;
	}

	public void setWorking_place(String working_place) {
		this.working_place = working_place;
	}

	public String getOffice_address() {
		return office_address;
	}

	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getCredit_limit() {
		return credit_limit;
	}

	public void setCredit_limit(String credit_limit) {
		this.credit_limit = credit_limit;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCell_phone() {
		return cell_phone;
	}

	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getHome_phone() {
		return home_phone;
	}

	public void setHome_phone(String home_phone) {
		this.home_phone = home_phone;
	}

	public String getLive_status() {
		return live_status;
	}

	public void setLive_status(String live_status) {
		this.live_status = live_status;
	}

	public String getId_address() {
		return id_address;
	}

	public void setId_address(String id_address) {
		this.id_address = id_address;
	}

	public String getContact_address() {
		return contact_address;
	}

	public void setContact_address(String contact_address) {
		this.contact_address = contact_address;
	}

	public String getBlacklist_flag() {
		return blacklist_flag;
	}

	public void setBlacklist_flag(String blacklist_flag) {
		this.blacklist_flag = blacklist_flag;
	}

	public String getBlacklist_note() {
		return blacklist_note;
	}

	public void setBlacklist_note(String blacklist_note) {
		this.blacklist_note = blacklist_note;
	}

	public String getEnabled_flag() {
		return enabled_flag;
	}

	public void setEnabled_flag(String enabled_flag) {
		this.enabled_flag = enabled_flag;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getBp_name_sp() {
		return bp_name_sp;
	}

	public void setBp_name_sp(String bp_name_sp) {
		this.bp_name_sp = bp_name_sp;
	}

	public String getId_type_sp() {
		return id_type_sp;
	}

	public void setId_type_sp(String id_type_sp) {
		this.id_type_sp = id_type_sp;
	}

	public String getId_card_no_sp() {
		return id_card_no_sp;
	}

	public void setId_card_no_sp(String id_card_no_sp) {
		this.id_card_no_sp = id_card_no_sp;
	}

	public String getSpouse_phone() {
		return spouse_phone;
	}

	public void setSpouse_phone(String spouse_phone) {
		this.spouse_phone = spouse_phone;
	}

	public String getSpouse_income_from() {
		return spouse_income_from;
	}

	public void setSpouse_income_from(String spouse_income_from) {
		this.spouse_income_from = spouse_income_from;
	}

	public String getSpouse_annual_income() {
		return spouse_annual_income;
	}

	public void setSpouse_annual_income(String spouse_annual_income) {
		this.spouse_annual_income = spouse_annual_income;
	}

	public String getSpouse_work_unit() {
		return spouse_work_unit;
	}

	public void setSpouse_work_unit(String spouse_work_unit) {
		this.spouse_work_unit = spouse_work_unit;
	}

	public String getAddress_sp() {
		return address_sp;
	}

	public void setAddress_sp(String address_sp) {
		this.address_sp = address_sp;
	}
	
	public List<UserContactInfo> getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(List<UserContactInfo> contactInfo) {
		this.contactInfo = contactInfo;
	}

	public List<UserAccountInfo> getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(List<UserAccountInfo> accountInfo) {
		this.accountInfo = accountInfo;
	}
	
	public List<UserAttachmentInfo> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<UserAttachmentInfo> attachs) {
		this.attachs = attachs;
	}

	public String getSignContent(){
		return this.getRequest_number()+this.getUser_account()+this.getPassword();
	}

}
