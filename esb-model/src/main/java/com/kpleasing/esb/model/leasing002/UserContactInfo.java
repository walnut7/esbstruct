package com.kpleasing.esb.model.leasing002;

import java.io.Serializable;

public class UserContactInfo implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = -4982583412209940351L;

	private String esb_contact_id;
	
	private String contact_person;
	
	private String emergent_person_flag;
	
	private String relation;
	
	private String id_type;
	
	private String id_card_no;
	
	private String cell_phone;
	
	private String phone;
	
	private String fax;
	
	private String email;
	
	private String contact_info;
	
	private String work_unit;
	
	private String send_sms_flag;

	public String getEsb_contact_id() {
		return esb_contact_id;
	}

	public void setEsb_contact_id(String esb_contact_id) {
		this.esb_contact_id = esb_contact_id;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getEmergent_person_flag() {
		return emergent_person_flag;
	}

	public void setEmergent_person_flag(String emergent_person_flag) {
		this.emergent_person_flag = emergent_person_flag;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
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

	public String getCell_phone() {
		return cell_phone;
	}

	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact_info() {
		return contact_info;
	}

	public void setContact_info(String contact_info) {
		this.contact_info = contact_info;
	}

	public String getWork_unit() {
		return work_unit;
	}

	public void setWork_unit(String work_unit) {
		this.work_unit = work_unit;
	}

	public String getSend_sms_flag() {
		return send_sms_flag;
	}

	public void setSend_sms_flag(String send_sms_flag) {
		this.send_sms_flag = send_sms_flag;
	}

}
