package com.kpleasing.esb.receive.protocol.kpfs.hls001;

import com.kpleasing.esb.common.BeanXMLMapping;

public class ReqHLS001 {
	
	private String api_code;
	private String req_serial_no;
	private String req_date;
	private String security_code;
	private String security_value;
	private String cert_type;           // 证件类型            
	private String cert_code;           //证件号码            
	private String cust_name;           //客户姓名            
	private String biz_sence;          //业务场景            
	private String order_no;           //订单编号            
	private String dept_no;            //消费平台编号        
	private String business_type;      //业务类型            
	private String loan_term;           //贷款期限            
	private String invoice_amt;         //开票金额            
	private String agreed_loan_amt;     //协议贷款金额        
	private String source_dsc;          //来源                
	private String edu_level;           //学历                
	private String position;            // 职位                
	private String entry_year;         // 入职年限            
	private String unit_name;          //单位名称            
	private String unit_tel;            //单位电话            
	private String marr_status;         //婚姻状况       
	private String spouse_name;         // 配偶姓名
	private String spouse_cert_type;    // 配偶证件类型
	private String spouse_cert_code;    //配偶身份证          
	private String spouse_income_from;  //配偶收入来源        
	private String spouse_phone;        //配偶手机号          
	private String spouse_annual_income; //配偶年收入          
	private String spouse_work_unit;    //配偶工作单位        
	private String income_status;       //收入状态            
	private String income_from;         //收入来源            
	private String annual_income;       //年收入              
	private String live_status;         //住房状态            
	private String live_addr;           //居住地址            
	private String contact_name1;       //联系人1姓名         
	private String contact_relation1;   //联系人1关系         
	private String contact_phone1;      //联系人1手机号       
	private String contact_name2;       //联系人2姓名         
	private String contact_relation2;   //联系人2关系         
	private String contact_phone2;      //联系人2手机号       
	private String order_create_time;    //订单创建时间        
	private String car_license_date;    //上牌日期        
	private String car_local;           // 车辆所属地
	private String drive_distance;      //行驶里程            
	private String car_deal_amt;        //车辆成交价          
	private String dsc_amt;             //认证车价            
	private String shop_user_name;      //车商用户名          
	private String shop_phone;          //车商电话            
	private String salesman_name;       //地推人员姓名        
	private String license_plate;       //车牌号码            
	private String vin_code;            //VIN码               
	private String brand;               //品牌                
	private String cars;                //车系                
	private String car_type;            //车型                
	private String cust_name_spell;     //姓名拼音            
	private String cert_org;            //发证机关            
	private String cust_type;           //客户类型            
	private String contact_addr;        //联系地址            
	private String contact_zip_code;    //联系地址邮政编码    
	private String email;              // 常用邮箱            
	private String phone;              // 手机号码            
	private String live_zip_code;      // 居住地址邮政编号    
	private String loan_purpose;       // 贷款用途            
	private String loan_acc_bank;      // 放款账户开户行行号  
	private String loan_card_no;       // 放款卡号            
	private String repay_acc_bank;     // 还款账户开户行行号  
	private String repay_card_no;      // 还款卡号            
	private String rel_flag;           // 是否我行关系人      
	private String app_amt;            // 支用金额            
	private String loan_nstd_term;      //支用期限            
	private String loan_term1;          //支用标准期限        
	private String repay_type;          //还款方式            
	private String contact_name;        //紧急联系人姓名      
	private String contact_phone;       //紧急联系人手机号    
	private String work_unit;           //工作单位            
	private String family_addr;         //家庭住址            
	private String resv_tel;            //预留手机 
	private String sign;
	
	public String getApi_code() {
		return api_code;
	}
	
	public void setApi_code(String api_code) {
		this.api_code = api_code;
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

	public String getSecurity_code() {
		return security_code;
	}

	public void setSecurity_code(String security_code) {
		this.security_code = security_code;
	}

	public String getSecurity_value() {
		return security_value;
	}

	public void setSecurity_value(String security_value) {
		this.security_value = security_value;
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

	public String getBiz_sence() {
		return biz_sence;
	}

	public void setBiz_sence(String biz_sence) {
		this.biz_sence = biz_sence;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getDept_no() {
		return dept_no;
	}

	public void setDept_no(String dept_no) {
		this.dept_no = dept_no;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public String getInvoice_amt() {
		return invoice_amt;
	}

	public void setInvoice_amt(String invoice_amt) {
		this.invoice_amt = invoice_amt;
	}

	public String getAgreed_loan_amt() {
		return agreed_loan_amt;
	}

	public void setAgreed_loan_amt(String agreed_loan_amt) {
		this.agreed_loan_amt = agreed_loan_amt;
	}

	public String getSource_dsc() {
		return source_dsc;
	}

	public void setSource_dsc(String source_dsc) {
		this.source_dsc = source_dsc;
	}

	public String getEdu_level() {
		return edu_level;
	}

	public void setEdu_level(String edu_level) {
		this.edu_level = edu_level;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public String getUnit_tel() {
		return unit_tel;
	}

	public void setUnit_tel(String unit_tel) {
		this.unit_tel = unit_tel;
	}

	public String getMarr_status() {
		return marr_status;
	}

	public void setMarr_status(String marr_status) {
		this.marr_status = marr_status;
	}

	public String getSpouse_name() {
		return spouse_name;
	}

	public void setSpouse_name(String spouse_name) {
		this.spouse_name = spouse_name;
	}

	public String getSpouse_cert_type() {
		return spouse_cert_type;
	}

	public void setSpouse_cert_type(String spouse_cert_type) {
		this.spouse_cert_type = spouse_cert_type;
	}

	public String getSpouse_cert_code() {
		return spouse_cert_code;
	}

	public void setSpouse_cert_code(String spouse_cert_code) {
		this.spouse_cert_code = spouse_cert_code;
	}

	public String getSpouse_income_from() {
		return spouse_income_from;
	}

	public void setSpouse_income_from(String spouse_income_from) {
		this.spouse_income_from = spouse_income_from;
	}

	public String getSpouse_phone() {
		return spouse_phone;
	}

	public void setSpouse_phone(String spouse_phone) {
		this.spouse_phone = spouse_phone;
	}

	public String getSpouse_annual_income() {
		return spouse_annual_income;
	}

	public void setSpouse_annual_income(String spouse_annual_income) {
		this.spouse_annual_income = spouse_annual_income;
	}

	public String getSpouse_work_unit() {
		return spouse_work_unit;
	}

	public void setSpouse_work_unit(String spouse_work_unit) {
		this.spouse_work_unit = spouse_work_unit;
	}

	public String getIncome_status() {
		return income_status;
	}

	public void setIncome_status(String income_status) {
		this.income_status = income_status;
	}

	public String getIncome_from() {
		return income_from;
	}

	public void setIncome_from(String income_from) {
		this.income_from = income_from;
	}

	public String getAnnual_income() {
		return annual_income;
	}

	public void setAnnual_income(String annual_income) {
		this.annual_income = annual_income;
	}

	public String getLive_status() {
		return live_status;
	}

	public void setLive_status(String live_status) {
		this.live_status = live_status;
	}

	public String getLive_addr() {
		return live_addr;
	}

	public void setLive_addr(String live_addr) {
		this.live_addr = live_addr;
	}

	public String getContact_name1() {
		return contact_name1;
	}

	public void setContact_name1(String contact_name1) {
		this.contact_name1 = contact_name1;
	}

	public String getContact_relation1() {
		return contact_relation1;
	}

	public void setContact_relation1(String contact_relation1) {
		this.contact_relation1 = contact_relation1;
	}

	public String getContact_phone1() {
		return contact_phone1;
	}

	public void setContact_phone1(String contact_phone1) {
		this.contact_phone1 = contact_phone1;
	}

	public String getContact_name2() {
		return contact_name2;
	}

	public void setContact_name2(String contact_name2) {
		this.contact_name2 = contact_name2;
	}

	public String getContact_relation2() {
		return contact_relation2;
	}

	public void setContact_relation2(String contact_relation2) {
		this.contact_relation2 = contact_relation2;
	}

	public String getContact_phone2() {
		return contact_phone2;
	}

	public void setContact_phone2(String contact_phone2) {
		this.contact_phone2 = contact_phone2;
	}

	public String getOrder_create_time() {
		return order_create_time;
	}

	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}

	public String getCar_license_date() {
		return car_license_date;
	}

	public void setCar_license_date(String car_license_date) {
		this.car_license_date = car_license_date;
	}

	public String getDrive_distance() {
		return drive_distance;
	}

	public void setDrive_distance(String drive_distance) {
		this.drive_distance = drive_distance;
	}

	public String getCar_deal_amt() {
		return car_deal_amt;
	}

	public void setCar_deal_amt(String car_deal_amt) {
		this.car_deal_amt = car_deal_amt;
	}

	public String getDsc_amt() {
		return dsc_amt;
	}

	public void setDsc_amt(String dsc_amt) {
		this.dsc_amt = dsc_amt;
	}

	public String getShop_user_name() {
		return shop_user_name;
	}

	public void setShop_user_name(String shop_user_name) {
		this.shop_user_name = shop_user_name;
	}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getSalesman_name() {
		return salesman_name;
	}

	public void setSalesman_name(String salesman_name) {
		this.salesman_name = salesman_name;
	}

	public String getLicense_plate() {
		return license_plate;
	}

	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}

	public String getCar_local() {
		return car_local;
	}

	public void setCar_local(String car_local) {
		this.car_local = car_local;
	}

	public String getVin_code() {
		return vin_code;
	}

	public void setVin_code(String vin_code) {
		this.vin_code = vin_code;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCars() {
		return cars;
	}

	public void setCars(String cars) {
		this.cars = cars;
	}

	public String getCar_type() {
		return car_type;
	}

	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}

	public String getCust_name_spell() {
		return cust_name_spell;
	}

	public void setCust_name_spell(String cust_name_spell) {
		this.cust_name_spell = cust_name_spell;
	}

	public String getCert_org() {
		return cert_org;
	}

	public void setCert_org(String cert_org) {
		this.cert_org = cert_org;
	}

	public String getCust_type() {
		return cust_type;
	}

	public void setCust_type(String cust_type) {
		this.cust_type = cust_type;
	}

	public String getContact_addr() {
		return contact_addr;
	}

	public void setContact_addr(String contact_addr) {
		this.contact_addr = contact_addr;
	}

	public String getContact_zip_code() {
		return contact_zip_code;
	}

	public void setContact_zip_code(String contact_zip_code) {
		this.contact_zip_code = contact_zip_code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLive_zip_code() {
		return live_zip_code;
	}

	public void setLive_zip_code(String live_zip_code) {
		this.live_zip_code = live_zip_code;
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

	public String getRel_flag() {
		return rel_flag;
	}

	public void setRel_flag(String rel_flag) {
		this.rel_flag = rel_flag;
	}

	public String getApp_amt() {
		return app_amt;
	}

	public void setApp_amt(String app_amt) {
		this.app_amt = app_amt;
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

	public String getRepay_type() {
		return repay_type;
	}

	public void setRepay_type(String repay_type) {
		this.repay_type = repay_type;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getWork_unit() {
		return work_unit;
	}

	public void setWork_unit(String work_unit) {
		this.work_unit = work_unit;
	}

	public String getFamily_addr() {
		return family_addr;
	}

	public void setFamily_addr(String family_addr) {
		this.family_addr = family_addr;
	}

	public String getResv_tel() {
		return resv_tel;
	}

	public void setResv_tel(String resv_tel) {
		this.resv_tel = resv_tel;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEntry_year() {
		return entry_year;
	}

	public void setEntry_year(String entry_year) {
		this.entry_year = entry_year;
	}

	public String getLoan_term1() {
		return loan_term1;
	}

	public void setLoan_term1(String loan_term1) {
		this.loan_term1 = loan_term1;
	}
	
	public String toXML() {
		return BeanXMLMapping.toXML("hls001", this);
	}
}
