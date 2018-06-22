package com.kpleasing.esb.model.crm002;

import java.io.Serializable;

import com.kpleasing.esb.model.crm.CrmRequestHeader;

public class GetUserProfileRequest extends CrmRequestHeader implements Serializable {

	/**	 * 	 */
	private static final long serialVersionUID = 5887110880092649087L;
	private String channel_type;
	private String channel_id;

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
