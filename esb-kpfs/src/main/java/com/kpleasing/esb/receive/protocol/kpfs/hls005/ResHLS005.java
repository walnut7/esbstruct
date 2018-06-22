package com.kpleasing.esb.receive.protocol.kpfs.hls005;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;



public class ResHLS005 {
	
private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS005.class);
	
	private String return_code;
	private String return_desc;
	private String result_code;
	private String result_desc;
	private String req_serial_no;
	private String req_date;
	private String res_serial_no;
	private String res_date;
	private String ols_trans_no;
	private String ols_date;
	private String sign_type;
	private String crd_cont_no;
	private String bill_no;
	private String sign;
	
	
	public static ResHLS005 fromXML(String xml) throws Exception {
		return (ResHLS005) BeanXMLMapping.fromXML(xml, ResHLS005.class);
	}
	
	public String getReturn_code() {
		return return_code;
	}
	
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_desc() {
		return return_desc;
	}

	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_desc() {
		return result_desc;
	}

	public void setResult_desc(String result_desc) {
		this.result_desc = result_desc;
	}

	public String getReq_serial_no() {
		return req_serial_no;
	}

	public void setReq_serial_no(String req_serial_no) {
		this.req_serial_no = req_serial_no;
	}

	public String getReq_date() {
		return req_date;
	}

	public void setReq_date(String req_date) {
		this.req_date = req_date;
	}

	public String getRes_serial_no() {
		return res_serial_no;
	}

	public void setRes_serial_no(String res_serial_no) {
		this.res_serial_no = res_serial_no;
	}

	public String getRes_date() {
		return res_date;
	}

	public void setRes_date(String res_date) {
		this.res_date = res_date;
	}

	public String getOls_trans_no() {
		return ols_trans_no;
	}

	public void setOls_trans_no(String ols_trans_no) {
		this.ols_trans_no = ols_trans_no;
	}

	public String getOls_date() {
		return ols_date;
	}

	public void setOls_date(String ols_date) {
		this.ols_date = ols_date;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getCrd_cont_no() {
		return crd_cont_no;
	}

	public void setCrd_cont_no(String crd_cont_no) {
		this.crd_cont_no = crd_cont_no;
	}

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String toResponseXML() throws KPFSBusinessException {
		try {
			StringBuilder res = new StringBuilder();
			res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
			.append("<esb><response>")
			.append("<return_code>").append(this.return_code).append("</return_code>")
			.append("<return_desc>").append(this.return_desc).append("</return_desc>")
			.append("<result_code>").append(this.result_code).append("</result_code>")
			.append("<result_desc>").append(this.result_desc).append("</result_desc>")
			.append("<req_serial_no>").append(this.req_serial_no).append("</req_serial_no>")
			.append("<req_date>").append(this.req_date).append("</req_date>")   
			.append("<res_serial_no>").append(this.res_serial_no).append("</res_serial_no>")
			.append("<res_date>").append(this.res_date).append("</res_date>")
			.append("<ols_trans_no>").append(this.ols_trans_no).append("</ols_trans_no>")
			.append("<ols_date>").append(this.ols_date).append("</ols_date>")
			.append("<sign_type>").append(this.sign_type).append("</sign_type>")
			.append("<crd_cont_no>").append(this.crd_cont_no).append("</crd_cont_no>")
			.append("<bill_no>").append(this.bill_no).append("</bill_no>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}
}
