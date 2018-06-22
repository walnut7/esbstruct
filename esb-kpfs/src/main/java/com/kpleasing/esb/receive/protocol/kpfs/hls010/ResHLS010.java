package com.kpleasing.esb.receive.protocol.kpfs.hls010;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS010 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS010.class);
	
	private String return_code;
	private String return_desc;
	private String result_code;
	private String result_desc;
	private String req_serial_no;
	private String req_date;
	private String res_serial_no;
	private String res_date;
	private String norm_amt;
	private String inte_amt;
	private String cost_amt;
	private String total_amt;
	private String cost_rate;
	private String loan_bal;
	private String repay_list;
	private String sign;
	
	public static ResHLS010 fromXML(String xml) throws Exception {
		return (ResHLS010) BeanXMLMapping.fromXML(xml, ResHLS010.class);
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

	public String getNorm_amt() {
		return norm_amt;
	}

	public void setNorm_amt(String norm_amt) {
		this.norm_amt = norm_amt;
	}

	public String getInte_amt() {
		return inte_amt;
	}

	public void setInte_amt(String inte_amt) {
		this.inte_amt = inte_amt;
	}

	public String getCost_amt() {
		return cost_amt;
	}

	public void setCost_amt(String cost_amt) {
		this.cost_amt = cost_amt;
	}

	public String getTotal_amt() {
		return total_amt;
	}

	public void setTotal_amt(String total_amt) {
		this.total_amt = total_amt;
	}

	public String getCost_rate() {
		return cost_rate;
	}

	public void setCost_rate(String cost_rate) {
		this.cost_rate = cost_rate;
	}

	public String getLoan_bal() {
		return loan_bal;
	}

	public void setLoan_bal(String loan_bal) {
		this.loan_bal = loan_bal;
	}

	public String getRepay_list() {
		return repay_list;
	}

	public void setRepay_list(String repay_list) {
		this.repay_list = repay_list;
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
			.append("<norm_amt>").append(this.norm_amt).append("</norm_amt>")
			.append("<inte_amt>").append(this.inte_amt).append("</inte_amt>")
			.append("<cost_amt>").append(this.cost_amt).append("</cost_amt>")
			.append("<total_amt>").append(this.total_amt).append("</total_amt>")
			.append("<cost_rate>").append(this.cost_rate).append("</cost_rate>")
			.append("<loan_bal>").append(this.loan_bal).append("</loan_bal>")
			.append("<repay_list>").append(this.repay_list).append("</repay_list>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}

}
