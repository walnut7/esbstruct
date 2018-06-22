package com.kpleasing.esb.model.wxss001;

import java.io.Serializable;
import java.util.List;

import com.kpleasing.esb.model.RequestHeader;

public class SPInfoRequest extends RequestHeader implements Serializable {

	/** *  */
	private static final long serialVersionUID = 6970834988143467959L;

	private String bp_id;
	
	private String bp_code;
	
	private String bp_name;
	
	private String organization_code;
	
	private String province;
	
	private String city;
	
	private String image_url;
	
	private String enabled_flag;
	
	private List<SaleInfo> sales;

	public String getBp_id() {
		return bp_id;
	}

	public void setBp_id(String bp_id) {
		this.bp_id = bp_id;
	}

	public String getBp_code() {
		return bp_code;
	}

	public void setBp_code(String bp_code) {
		this.bp_code = bp_code;
	}

	public String getBp_name() {
		return bp_name;
	}

	public void setBp_name(String bp_name) {
		this.bp_name = bp_name;
	}

	public String getOrganization_code() {
		return organization_code;
	}

	public void setOrganization_code(String organization_code) {
		this.organization_code = organization_code;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getEnabled_flag() {
		return enabled_flag;
	}

	public void setEnabled_flag(String enabled_flag) {
		this.enabled_flag = enabled_flag;
	}

	public List<SaleInfo> getSales() {
		return sales;
	}

	public void setSales(List<SaleInfo> sales) {
		this.sales = sales;
	}
    
}
