package com.kpleasing.esb.tools;

public class TestClient {
		
	public static void main(String[] argc) {
		
		String url = "http://kpxmc-uat.e-autofinance.net:8080/esb/route/api";
		
		StringBuilder sbstr1 = new StringBuilder();
		sbstr1.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[CRM003]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20170731161616]]></req_date>")
		.append("        <security_code><![CDATA[KPXMC]]></security_code>")
		.append("        <security_value><![CDATA[KPXMC]]></security_value>")
		.append("        <sign><![CDATA[61ACCD02AD66D52647A2267CDCBF9770]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <cust_id><![CDATA[4301]]></cust_id>")
		.append("        <phone><![CDATA[13389120121556]]></phone>")
		.append("        <cust_name><![CDATA[李晓燕]]></cust_name>")
		.append("        <cert_type><![CDATA[ID_CARD]]></cert_type>")
		.append("        <cert_code><![CDATA[11111111111111]]></cert_code>")
		.append("    </body>")
		.append("</esb>");
		
		
		StringBuilder sbstra = new StringBuilder();
		sbstra.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[CRM005]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20170731161616]]></req_date>")
		.append("        <security_code><![CDATA[KPXMC]]></security_code>")
		.append("        <security_value><![CDATA[KPXMC]]></security_value>")
		.append("        <sign><![CDATA[F33F4251F026D90183F127A5D21BBF21]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <appcode><![CDATA[KPXMC]]></appcode>")
		.append("        <login_id><![CDATA[]]></login_id>")
		.append("        <password><![CDATA[e10adc3949ba59abbe56e057f20f883e]]></password>")
		.append("    </body>")
		.append("</esb>");
		
		
		StringBuilder sbstr2 = new StringBuilder();
		sbstr2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[LEASING001]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20170731161616]]></req_date>")
		.append("        <security_code><![CDATA[KPXMC]]></security_code>")
		.append("        <security_value><![CDATA[KPXMC123]]></security_value>")
		.append("        <sign><![CDATA[63C03649E5369B479F7674E36A3A7EFD]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <phone><![CDATA[13601973894]]></phone>")
		.append("        <content><![CDATA[测试【坤鹏租赁】]]></content>")
		.append("    </body>")
		.append("</esb>");
		
		
		StringBuilder sbstr3 = new StringBuilder();
		sbstr3.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[LEASING006]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20170731161616]]></req_date>")
		.append("        <security_code><![CDATA[KPXMC]]></security_code>")
		.append("        <security_value><![CDATA[KPXMC]]></security_value>")
		.append("        <sign><![CDATA[63C03649E5369B479F7674E36A3A7EFD]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <phone><![CDATA[18858346531]]></phone>")
		.append("        <cert_type><![CDATA[ID_CARD]]></cert_type>")
		.append("        <cert_code><![CDATA[420115198602016611]]></cert_code>")
		.append("    </body>")
		.append("</esb>");
		
	
		
		StringBuilder sbstr4 = new StringBuilder();
		sbstr4.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append(" <head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>LEASING004</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>BA49D40BE5DD0FB354FC47AAEBA63D3C</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_name>sdf</cust_name>")
		.append("<phone>17717222557</phone>")
		.append("<cert_type>0</cert_type>")
		.append("<cert_code>341282199101132498</cert_code>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr5 = new StringBuilder();
		sbstr5.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>LEASING005</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>5C877B8F466F07E948433FEE3C78D389</sign>")
		.append("</head>")
		.append("<body>")
		.append("<application_no>201711281657010012</application_no>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr6 = new StringBuilder();
		sbstr6.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>CRM007</api_code>")
		.append("<req_serial_no>ecaisoAj8kZYsD9yzWcjd77sbedmQeZm</req_serial_no>")
		.append("<req_date>20171211125508</req_date>")
		.append("<sign>536D071542CBD2DE74A0BD65A6F65D37</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>41</cust_id>")
		.append("<cust_name>托尔斯泰</cust_name>")
		.append("<cert_type>ID_CARD</cert_type>")
		.append("<cert_code>341282199101132498</cert_code>")
		.append("<phone>15856838232</phone>")
		.append("<cust_name_spell>xiaoming</cust_name_spell>")
		.append("<nation>汉</nation>")
		.append("<drive_model>A1</drive_model>")
		.append("<annual_income>10000</annual_income>")
		.append("<income_from>1</income_from>")
		.append("<income_status>1</income_status>")
		.append("<entry_year>11</entry_year>")
		.append("<live_status>1</live_status>")
		.append("<work_unit>test</work_unit>")
		.append("<work_addr>test</work_addr>")
		.append("<position>13333333333</position>")
		.append("<edu_level>1</edu_level>")
		.append("<marr_status>UNMARRIED</marr_status>")
		.append("<spouse_name></spouse_name>")
		.append("<spouse_cert_type>ID_CARD</spouse_cert_type>")
		.append("<spouse_cert_code>341282199101132498</spouse_cert_code>")
		.append("<spouse_phone>15856838232</spouse_phone>")
		.append("<spouse_income_from>1</spouse_income_from>")
		.append("<spouse_annual_income>10000</spouse_annual_income>")
		.append("<spouse_work_unit>test</spouse_work_unit>")
		.append("<unit_tel>13333333333</unit_tel>")
		.append("<email>asdf@flsdk.cn</email>")
		.append("<cert_org>上海</cert_org>")
		.append("<zip_code>200070</zip_code>")
		.append("<family_tel>15856838232</family_tel>")
		.append("<live_addr>test</live_addr>")
		.append("<accounts>")
		.append("  <account>")
		.append("    <acc_name>托尔斯泰</acc_name>")
		.append("   <acc_no>64352341234124124</acc_no>")
		.append("    <bank_code>SPDB</bank_code>")
		.append("    <bank_full_name>上海浦东发展银行</bank_full_name>")
		.append("    <currency>CNY</currency>")
		.append("  </account>")
		.append("</accounts>")
		.append("<contacts>")
		.append(" <contact>")
		.append("   <contact_name>test</contact_name>")
		.append("    <contact_cert_type>1</contact_cert_type>")
		.append("   <contact_cert_code>132</contact_cert_code>")
		.append("   <relation>1</relation>")
		.append("   <contact_phone>13222222222</contact_phone>")
		.append(" </contact>")
		.append(" <contact>")
		.append("   <contact_name>test</contact_name>")
		.append("   <contact_cert_type>1</contact_cert_type>")
		.append("   <contact_cert_code>312</contact_cert_code>")
		.append("    <relation>1</relation>")
		.append("    <contact_phone>13222222222</contact_phone>")
		.append(" </contact>")
		.append(" </contacts>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr7 = new StringBuilder();
		sbstr7.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append(" <head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>CRM004</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>A28FD1B5E2ED854D954D1AFA4091E042</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>20</cust_id>")
		.append("</body>")
		.append("</esb>");
		
		StringBuilder sbstr8 = new StringBuilder();
		sbstr8.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append(" <head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>PUB001</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>A28FD1B5E2ED854D954D1AFA4091E042</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>20</cust_id>")
		.append("</body>")
		.append("</esb>");
		
		StringBuilder sbstr9 = new StringBuilder();
		sbstr9.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append(" <head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>LEASING008</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>A28FD1B5E2ED854D954D1AFA4091E042</sign>")
		.append("</head>")
		.append("<body>")
		.append("<applyno>CON12312321321321</applyno>")
		.append("<reason>不想买</reason>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr10 = new StringBuilder();
		sbstr10.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>LEASING</security_code>")
		.append("<security_value>123456</security_value>")
		.append("<api_code>XMC001</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>8707A4D680A5400BC075E6920F548611</sign>")
		.append("</head>")
		.append("<body>")
		.append("<applyno>CON201709110031</applyno>")
		.append("<status>0</status>")
		.append("</body>")
		.append("</esb>");
		
		StringBuilder sbstr11 = new StringBuilder();
		sbstr11.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>LEASING013</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>D3BB7CB838CE3D8C652206D285FA5EEF</sign>")
		.append("</head>")
		.append("<body>")
		.append("<repay_date>20171221</repay_date>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr12 = new StringBuilder();
		sbstr12.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>LEASING007</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>7E0B0376F80D78BAF3D82032A95316F2</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>38408</cust_id>")
		.append("<cert_code>340304197505081048</cert_code>")
		.append("<agent_name>苏州诚隆汽车销售服务有限公司</agent_name>")
		.append("<finance_plan>602</finance_plan>")
		.append("</body>")
		.append("</esb>");
		

		
		StringBuilder sbstr13 = new StringBuilder();
		sbstr13.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>WXSS</security_code>")
		.append("<security_value>WXSS</security_value>")
		.append("<api_code>XMC001</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>3075AA2A94CDC0D153C32B9172CC31D9</sign>")
		.append("</head>")
		.append("<body>")
		.append("<redirect_url><![CDATA[http://test.e-autofinance.net:8080/kp_test/tesila/car_v3/carList.html]]></redirect_url>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr15 = new StringBuilder();
		sbstr15.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>KPXMC</security_code>")
		.append("<security_value>KPXMC</security_value>")
		.append("<api_code>CRM011</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>2E63CC5DB687B374E9395679BD3437A0</sign>")
		.append("</head>")
		.append("<body>")
		.append("<external_no>123456789</external_no>")
		.append("<cust_id>38408</cust_id>")
		.append("<cust_name>于成学</cust_name>")
		.append("<repay_card_no>1234565432</repay_card_no>")
		.append("<cert_type>0</cert_type>")
		.append("<cert_code>430381198805016335</cert_code>")
		.append("<bank_phone>13892876711</bank_phone>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr16 = new StringBuilder();
		sbstr16.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>LEASING</security_code>")
		.append("<security_value>123456</security_value>")
		.append("<api_code>CRM012</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>42C28726931AD75A093BEEC618D57BEF</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>37151</cust_id>")
		.append("<account_name>杨建利</account_name>")
		.append("<account>6210812430030609552</account>")
		.append("<short_account_no>62108552</short_account_no>")
		.append("<bank_phone>13234438455</bank_phone>")
		.append("<is_yjzf_bind>1</is_yjzf_bind>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr17 = new StringBuilder();
		sbstr17.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("<head>")
		.append("<security_code>WXSS</security_code>")
		.append("<security_value>WXSS</security_value>")
		.append("<api_code>XMC001</api_code>")
		.append("<req_serial_no>Fp9WOHV2OxL9Nys74Jescb3SOo7ld8UN</req_serial_no>")
		.append("<req_date>20171129175041</req_date>")
		.append("<sign>46D454A3F2338438A30BD1111FE3D1E7</sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id>37151</cust_id>")
		.append("</body>")
		.append("</esb>");
		
		
		StringBuilder sbstr18 = new StringBuilder();
		sbstr18.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		.append("<esb>")
		.append("<head>")
		.append("<api_code><![CDATA[XMC001]]></api_code>")
		.append("<req_serial_no><![CDATA[277021b0a6b143fc9e123523e310229e]]></req_serial_no>")
		.append("<req_date><![CDATA[20180608112207]]></req_date>")
		.append("<security_code><![CDATA[WXSS]]></security_code>")
		.append("<security_value><![CDATA[123456]]></security_value>")
		.append("<sign><![CDATA[BD1567B637C4E10DB3B00AA7882B955A]]></sign>")
		.append("</head>")
		.append("<body>")
		.append("<cust_id><![CDATA[38520]]></cust_id>")
		.append("</body>")
		.append("</esb>");
		
		StringBuilder sbstr19 = new StringBuilder();
		sbstr19.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
		.append("<esb>")
		.append("<head>")
		.append("<api_code><![CDATA[XMC002]]></api_code>")
		.append("<req_serial_no><![CDATA[277021b0a6b143fc9e123523e310229e]]></req_serial_no>")
		.append("<req_date><![CDATA[20180608112207]]></req_date>")
		.append("<security_code><![CDATA[CRM]]></security_code>")
		.append("<security_value><![CDATA[c4a39782]]></security_value>")
		.append("<sign><![CDATA[EDBAD9CE27898A6C17D3EF28B7AD15ED]]></sign>")
		.append("</head>")
		.append("<body>")
		.append("</body>")
		.append("</esb>");

		try {
			System.out.println(sbstr19.toString());
			String xmlString = EncryptUtil.encrypt("12345678", "abcdefgh", sbstr19.toString());
			System.out.println(xmlString);
			String result = HttpHelper.doHttpPost(url, xmlString);
			
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
