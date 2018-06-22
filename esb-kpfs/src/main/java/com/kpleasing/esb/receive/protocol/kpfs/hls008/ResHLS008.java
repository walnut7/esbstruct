package com.kpleasing.esb.receive.protocol.kpfs.hls008;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS008 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS008.class);
	
	private String return_code;
	private String return_desc;
	private String result_code;
	private String result_desc;
	private String req_serial_no;
	private String req_date;
	private String res_serial_no;
	private String res_date;
	private String ols_tran_no;
	private String ols_date;
	private String pay_serno;
	private String pay_norm_amt;
	private String pay_inte_amt;
	private String loan_bal;
	private String cost_amt;
	private String sop_tran_date;
	private String sop_teller_no;
	private String loan_last_status;
	private String sign;
	
	
	public static ResHLS008 fromXML(String xml) throws Exception {
		return (ResHLS008) BeanXMLMapping.fromXML(xml, ResHLS008.class);
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

	public String getOls_tran_no() {
		return ols_tran_no;
	}

	public void setOls_tran_no(String ols_tran_no) {
		this.ols_tran_no = ols_tran_no;
	}

	public String getOls_date() {
		return ols_date;
	}

	public void setOls_date(String ols_date) {
		this.ols_date = ols_date;
	}

	public String getPay_serno() {
		return pay_serno;
	}

	public void setPay_serno(String pay_serno) {
		this.pay_serno = pay_serno;
	}

	public String getPay_norm_amt() {
		return pay_norm_amt;
	}

	public void setPay_norm_amt(String pay_norm_amt) {
		this.pay_norm_amt = pay_norm_amt;
	}

	public String getPay_inte_amt() {
		return pay_inte_amt;
	}

	public void setPay_inte_amt(String pay_inte_amt) {
		this.pay_inte_amt = pay_inte_amt;
	}

	public String getLoan_bal() {
		return loan_bal;
	}

	public void setLoan_bal(String loan_bal) {
		this.loan_bal = loan_bal;
	}

	public String getCost_amt() {
		return cost_amt;
	}

	public void setCost_amt(String cost_amt) {
		this.cost_amt = cost_amt;
	}

	public String getSop_tran_date() {
		return sop_tran_date;
	}

	public void setSop_tran_date(String sop_tran_date) {
		this.sop_tran_date = sop_tran_date;
	}

	public String getSop_teller_no() {
		return sop_teller_no;
	}

	public void setSop_teller_no(String sop_teller_no) {
		this.sop_teller_no = sop_teller_no;
	}

	public String getLoan_last_status() {
		return loan_last_status;
	}

	public void setLoan_last_status(String loan_last_status) {
		this.loan_last_status = loan_last_status;
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
			.append("<ols_tran_no>").append(this.ols_tran_no).append("</ols_tran_no>")   
			.append("<ols_date>").append(this.ols_date).append("</ols_date>")
			.append("<pay_serno>").append(this.pay_serno).append("</pay_serno>")
			.append("<pay_norm_amt>").append(this.pay_norm_amt).append("</pay_norm_amt>")
			.append("<pay_inte_amt>").append(this.pay_inte_amt).append("</pay_inte_amt>")
			.append("<loan_bal>").append(this.loan_bal).append("</loan_bal>")
			.append("<cost_amt>").append(this.cost_amt).append("</cost_amt>")
			.append("<sop_tran_date>").append(this.sop_tran_date).append("</sop_tran_date>")
			.append("<sop_teller_no>").append(this.sop_teller_no).append("</sop_teller_no>")
			.append("<loan_last_status>").append(this.loan_last_status).append("</loan_last_status>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}
}
