package com.kpleasing.esb.model.leasing015;

import java.io.Serializable;

import com.kpleasing.esb.model.RequestHeader;

public class LEASING015Request extends RequestHeader implements Serializable {

	/**	 *	 */
	private static final long serialVersionUID = 4370735542058675557L;
	
	private String applyno;
	
	private String apply_type;
	
	private String recipients;
	
	private String mail_address;
	
	private String contact_phone;

	public String getApplyno() {
		return applyno;
	}

	public void setApplyno(String applyno) {
		this.applyno = applyno;
	}

	public String getApply_type() {
		return apply_type;
	}

	public void setApply_type(String apply_type) {
		this.apply_type = apply_type;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getMail_address() {
		return mail_address;
	}

	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
}
