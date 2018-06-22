package com.kpleasing.esb.receive.protocol.kpfs.hls002;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS002 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS002.class);
	
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
	private String app_no;
	private String app_type;
	private String app_date;
	private String appr_pass_time;
	private String app_nstr_term;
	private String app_term;
	private String app_amt;
	private String appr_amt;
	private String loan_purpose;
	private String loan_acc_bank;
	private String loan_card_no;
	private String repay_acc_bank;
	private String repay_card_no;
	private String manage_status;
	private String repay_type;

	private String sign;
	
	public static ResHLS002 fromXML(String xml) throws Exception {
		return (ResHLS002) BeanXMLMapping.fromXML(xml, ResHLS002.class);
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getApp_no() {
		return app_no;
	}

	public void setApp_no(String app_no) {
		this.app_no = app_no;
	}

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public String getApp_date() {
		return app_date;
	}

	public void setApp_date(String app_date) {
		this.app_date = app_date;
	}

	public String getAppr_pass_time() {
		return appr_pass_time;
	}

	public void setAppr_pass_time(String appr_pass_time) {
		this.appr_pass_time = appr_pass_time;
	}

	public String getApp_nstr_term() {
		return app_nstr_term;
	}

	public void setApp_nstr_term(String app_nstr_term) {
		this.app_nstr_term = app_nstr_term;
	}

	public String getApp_term() {
		return app_term;
	}

	public void setApp_term(String app_term) {
		this.app_term = app_term;
	}

	public String getApp_amt() {
		return app_amt;
	}

	public void setApp_amt(String app_amt) {
		this.app_amt = app_amt;
	}

	public String getAppr_amt() {
		return appr_amt;
	}

	public void setAppr_amt(String appr_amt) {
		this.appr_amt = appr_amt;
	}

	public String getLoan_purpose() {
		return loan_purpose;
	}

	public void setLoan_purpose(String loan_purpose) {
		this.loan_purpose = loan_purpose;
	}

	public String getLoan_acc_bank() {
		return loan_acc_bank;
	}

	public void setLoan_acc_bank(String loan_acc_bank) {
		this.loan_acc_bank = loan_acc_bank;
	}

	public String getLoan_card_no() {
		return loan_card_no;
	}

	public void setLoan_card_no(String loan_card_no) {
		this.loan_card_no = loan_card_no;
	}

	public String getRepay_acc_bank() {
		return repay_acc_bank;
	}

	public void setRepay_acc_bank(String repay_acc_bank) {
		this.repay_acc_bank = repay_acc_bank;
	}

	public String getRepay_card_no() {
		return repay_card_no;
	}

	public void setRepay_card_no(String repay_card_no) {
		this.repay_card_no = repay_card_no;
	}

	public String getManage_status() {
		return manage_status;
	}

	public void setManage_status(String manage_status) {
		this.manage_status = manage_status;
	}

	public String getRepay_type() {
		return repay_type;
	}

	public void setRepay_type(String repay_type) {
		this.repay_type = repay_type;
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
			.append("<app_type>").append(this.app_type).append("</app_type>")
			.append("<app_date>").append(this.app_date).append("</app_date>")
			.append("<appr_pass_time>").append(this.appr_pass_time).append("</appr_pass_time>")
			.append("<app_nstr_term>").append(this.app_nstr_term).append("</app_nstr_term>")
			.append("<app_term>").append(this.app_term).append("</app_term>")
			.append("<app_amt>").append(this.app_amt).append("</app_amt>")
			.append("<appr_amt>").append(this.appr_amt).append("</appr_amt>")
			.append("<loan_purpose>").append(this.loan_purpose).append("</loan_purpose>")
			.append("<loan_acc_bank>").append(this.loan_acc_bank).append("</loan_acc_bank>")
			.append("<loan_card_no>").append(this.loan_card_no).append("</loan_card_no>")
			.append("<repay_acc_bank>").append(this.repay_acc_bank).append("</repay_acc_bank>")
			.append("<repay_card_no>").append(this.repay_card_no).append("</repay_card_no>")
			.append("<manage_status>").append(this.manage_status).append("</manage_status>")
			.append("<repay_type>").append(this.repay_type).append("</repay_type>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}
}
