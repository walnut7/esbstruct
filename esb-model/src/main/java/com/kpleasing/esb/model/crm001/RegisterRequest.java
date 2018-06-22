package com.kpleasing.esb.model.crm001;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class RegisterRequest extends CrmRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 8835003491693074799L;
	
	private String phone;
	
	private String channel_type;
	
	private String channel_id;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
}
