package com.kpleasing.esb.receive.protocol.kpfs.hls009;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.BeanXMLMapping;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class ResHLS009 {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResHLS009.class);
	
	private String return_code;
	private String return_desc;
	private String result_code;
	private String result_desc;
	private String req_serial_no;
	private String req_date;
	private String res_serial_no;
	private String res_date;
	private String bill_no;
	private String start_date;
	private String end_date;
	private String cust_name;
	private String interest_day;
	private String due_day;
	private String loan_amt;
	private String loan_bal;
	private String repay_acc;
	private String rate;
	private String real_rate;
	private String query_num;
	private String query_list;
	private String sign;
	
	public static ResHLS009 fromXML(String xml) throws Exception {
		return (ResHLS009) BeanXMLMapping.fromXML(xml, ResHLS009.class);
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

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getInterest_day() {
		return interest_day;
	}

	public void setInterest_day(String interest_day) {
		this.interest_day = interest_day;
	}

	public String getDue_day() {
		return due_day;
	}

	public void setDue_day(String due_day) {
		this.due_day = due_day;
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

	public String getRepay_acc() {
		return repay_acc;
	}

	public void setRepay_acc(String repay_acc) {
		this.repay_acc = repay_acc;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getReal_rate() {
		return real_rate;
	}

	public void setReal_rate(String real_rate) {
		this.real_rate = real_rate;
	}

	public String getQuery_num() {
		return query_num;
	}

	public void setQuery_num(String query_num) {
		this.query_num = query_num;
	}

	public String getQuery_list() {
		return query_list;
	}

	public void setQuery_list(String query_list) {
		this.query_list = query_list;
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
			.append("<bill_no>").append(this.bill_no).append("</bill_no>")   
			.append("<start_date>").append(this.start_date).append("</start_date>")
			.append("<end_date>").append(this.end_date).append("</end_date>")
			.append("<cust_name>").append(this.cust_name).append("</cust_name>")   
			.append("<interest_day>").append(this.interest_day).append("</interest_day>")
			.append("<due_day>").append(this.due_day).append("</due_day>")
			.append("<loan_amt>").append(this.loan_amt).append("</loan_amt>")
			.append("<loan_bal>").append(this.loan_bal).append("</loan_bal>")
			.append("<repay_acc>").append(this.repay_acc).append("</repay_acc>")
			.append("<rate>").append(this.rate).append("</rate>")
			.append("<real_rate>").append(this.real_rate).append("</real_rate>")
			.append("<query_num>").append(this.query_num).append("</query_num>")
			.append("<query_list>").append(this.query_list).append("</query_list>")
			.append("<sign>").append(Security.getSign(this, ConfigUtil.HLS_KEY)).append("</sign>")
			.append("</response></esb>");
			LOGGER.info("response xml info::" + res.toString());
			return res.toString();
		} catch (IllegalAccessException e) {
			throw new KPFSBusinessException("Response内部签名出错！");
		}
	}

}
