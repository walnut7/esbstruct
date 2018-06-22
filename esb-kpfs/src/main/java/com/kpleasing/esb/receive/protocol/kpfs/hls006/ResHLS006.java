package com.kpleasing.esb.receive.protocol.kpfs.hls006;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS006 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS006.class);
	
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
	private String bill_no;
	private String app_no;
	private String cert_type;
	private String cert_code;
	private String cust_name;
	private String loan_purpose;
	private String repay_type;
	private String loan_amt;
	private String loan_bal;
	private String exec_rate;
	private String loan_nstd_term;
	private String loan_term;
	private String loan_start_date;
	private String loan_end_date;
	private String loan_status;
	private String repay_acc_bank;
	private String repay_card_no;
	private String sign;
	
	
	public static ResHLS006 fromXML(String xml) throws Exception {
		return (ResHLS006) BeanXMLMapping.fromXML(xml, ResHLS006.class);
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

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getApp_no() {
		return app_no;
	}

	public void setApp_no(String app_no) {
		this.app_no = app_no;
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

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getLoan_purpose() {
		return loan_purpose;
	}

	public void setLoan_purpose(String loan_purpose) {
		this.loan_purpose = loan_purpose;
	}

	public String getRepay_type() {
		return repay_type;
	}

	public void setRepay_type(String repay_type) {
		this.repay_type = repay_type;
	}

	public String getLoan_amt() {
		return loan_amt;
	}

	public void setLoan_amt(String loan_amt) {
		this.loan_amt = loan_amt;
	}

	public String getLoan_bal() {
		return loan_bal;
	}

	public void setLoan_bal(String loan_bal) {
		this.loan_bal = loan_bal;
	}

	public String getExec_rate() {
		return exec_rate;
	}

	public void setExec_rate(String exec_rate) {
		this.exec_rate = exec_rate;
	}

	public String getLoan_nstd_term() {
		return loan_nstd_term;
	}

	public void setLoan_nstd_term(String loan_nstd_term) {
		this.loan_nstd_term = loan_nstd_term;
	}

	public String getLoan_term() {
		return loan_term;
	}

	public void setLoan_term(String loan_term) {
		this.loan_term = loan_term;
	}

	public String getLoan_start_date() {
		return loan_start_date;
	}

	public void setLoan_start_date(String loan_start_date) {
		this.loan_start_date = loan_start_date;
	}

	public String getLoan_end_date() {
		return loan_end_date;
	}

	public void setLoan_end_date(String loan_end_date) {
		this.loan_end_date = loan_end_date;
	}

	public String getLoan_status() {
		return loan_status;
	}

	public void setLoan_status(String loan_status) {
		this.loan_status = loan_status;
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
			.append("<bill_no>").append(this.bill_no).append("</bill_no>")
			.append("<app_no>").append(this.app_no).append("</app_no>")
			.append("<cert_type>").append(this.cert_type).append("</cert_type>")
			.append("<cert_code>").append(this.cert_code).append("</cert_code>")
			.append("<cust_name>").append(this.cust_name).append("</cust_name>")
			.append("<loan_purpose>").append(this.loan_purpose).append("</loan_purpose>")
			.append("<repay_type>").append(this.repay_type).append("</repay_type>")
			.append("<loan_amt>").append(this.loan_amt).append("</loan_amt>")
			.append("<loan_bal>").append(this.loan_bal).append("</loan_bal>")
			.append("<exec_rate>").append(this.exec_rate).append("</exec_rate>")
			.append("<loan_nstd_term>").append(this.loan_nstd_term).append("</loan_nstd_term>")
			.append("<loan_term>").append(this.loan_term).append("</loan_term>")
			.append("<loan_start_date>").append(this.loan_start_date).append("</loan_start_date>")
			.append("<loan_end_date>").append(this.loan_end_date).append("</loan_end_date>")
			.append("<loan_status>").append(this.loan_status).append("</loan_status>")
			.append("<repay_acc_bank>").append(this.repay_acc_bank).append("</repay_acc_bank>")
			.append("<repay_card_no>").append(this.repay_card_no).append("</repay_card_no>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}
}
