package com.kpleasing.esb.receive.protocol.kpfs.hls001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS001 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS001.class);
	
	private String return_code;
	private String return_desc;
	private String result_code;
	private String result_desc;
	private String req_serial_no;
	private String req_date;
	private String res_serial_no;
	private String res_date;
	private String sign;
	private String ols_trans_no;
	private String ols_date;
	private String cert_type;
	private String cert_code;
	private String app_no;
	private String seq_no;
	private String grant_no;     // 征信查询授权书编号
	private String decl_no;      // 贷款用途声明书编号
	
	
	public static ResHLS001 fromXML(String xml) throws Exception {
		return (ResHLS001) BeanXMLMapping.fromXML(xml, ResHLS001.class);
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

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}

	public String getCert_code() {
		return cert_code;
	}

	public void setCert_code(String cert_code) {
		this.cert_code = cert_code;
	}

	public String getApp_no() {
		return app_no;
	}

	public void setApp_no(String app_no) {
		this.app_no = app_no;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}
	
	public String getGrant_no() {
		return grant_no;
	}


	public void setGrant_no(String grant_no) {
		this.grant_no = grant_no;
	}


	public String getDecl_no() {
		return decl_no;
	}


	public void setDecl_no(String decl_no) {
		this.decl_no = decl_no;
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
			.append("<app_no>").append(this.app_no).append("</app_no>")
			.append("<seq_no>").append(this.seq_no).append("</seq_no>")
			.append("<cert_type>").append(this.cert_type).append("</cert_type>")
			.append("<cert_code>").append(this.cert_code).append("</cert_code>")
			.append("<grant_no>").append(this.grant_no).append("</grant_no>")
			.append("<decl_no>").append(this.decl_no).append("</decl_no>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}
}
