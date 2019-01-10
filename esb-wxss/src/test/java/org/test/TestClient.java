package org.test;

import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;

public class TestClient {
		
	
	public static void main(String[] argc) {
		
		String url = "http://118.194.48.178:8080/esb/route/api";
		
		StringBuilder sbstr1 = new StringBuilder();
		sbstr1.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[WXSS001]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20180312131616]]></req_date>")
		.append("        <security_code><![CDATA[LEASING]]></security_code>")
		.append("        <security_value><![CDATA[123456]]></security_value>")
		.append("        <sign><![CDATA[61B4A704BE7E033FFDF4560742637201]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <bp_id><![CDATA[1541]]></bp_id>")
		.append("        <bp_code><![CDATA[BP000001]]></bp_code>")
		.append("        <bp_name><![CDATA[云南孝尊汽车销售服务有限公司]]></bp_name>")
		.append("        <organization_code><![CDATA[qwertasdfg12321]]></organization_code>")
		.append("        <province><![CDATA[云南省]]></province>")
		.append("        <city><![CDATA[昆明]]></city>")
		.append("        <image_url><![CDATA[asdafasfsa]]></image_url>")
		.append("        <enabled_flag><![CDATA[Y]]></enabled_flag>")
//		.append("		 <sales>")
//		.append("			<sale>")
//		.append("				<sale_id><![CDATA[1304]]></sale_id>")
//		.append("				<sale_phone><![CDATA[15265167005]]></sale_phone>")
//		.append("				<sale_name><![CDATA[刘淑晓]]></sale_name>")
//		.append("				<enabled_flag ><![CDATA[Y]]></enabled_flag>")
//		.append("			</sale>")
//		.append("		 </sales>")
		.append("    </body>")
		.append("</esb>");
		
		StringBuilder sbstr2 = new StringBuilder();
		sbstr2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb>")
		.append("    <head>")
		.append("        <api_code><![CDATA[LEASING007]]></api_code>")
		.append("        <req_serial_no><![CDATA[4fb55adf90414d809593e10d5bc981db]]></req_serial_no>")
		.append("        <req_date><![CDATA[20171205161616]]></req_date>")
		.append("        <security_code><![CDATA[KPXMC]]></security_code>")
		.append("        <security_value><![CDATA[KPXMC]]></security_value>")
		.append("        <sign><![CDATA[ADD6A9ED18576D63B631F2876276C490]]></sign>")
		.append("     </head>")
		.append("     <body>")
		.append("        <cust_id><![CDATA[41]]></cust_id>")
		.append("        <cert_code><![CDATA[411324198306154258]]></cert_code>")
		.append("        <agent_name><![CDATA[上海联通汽车有限公司]]></agent_name>")
		.append("        <finance_plan><![CDATA[717]]></finance_plan>")
		.append("    </body>")
		.append("</esb>");
		
		try {
			String xmlString = EncryptUtil.encrypt("12345678", "abcdefgh", sbstr1.toString());
			
			String result = HttpHelper.doHttpPost(url, xmlString);
			
			String xmlResult = EncryptUtil.decrypt("12345678", "abcdefgh", result);
			
			System.out.println(result);
			
			System.out.println(xmlResult);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
