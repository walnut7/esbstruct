package com.kpleasing.esb.route;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.Configure;
import com.kpleasing.esb.route.util.ConfigUtil;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.StringUtil;

public class ErrorProcessService implements Processor {
	
	private static Logger logger = Logger.getLogger(ErrorProcessService.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String rtnCode = (String) in.getHeader("ReturnCode");
		String rtnDesc = (String) in.getHeader("ReturnDesc");
		String rtnSeriNo = (String) in.getHeader("ReqSerialNo");
		String rtnDate = (String) in.getHeader("ReqDate");
		
		StringBuilder respStr = new StringBuilder();
		respStr.append("<?xml version=\"1.0\"?><esb><head>");
		if(null == rtnCode || null == rtnDesc) {
			respStr.append("<return_code>FAILED</return_code>");
			respStr.append("<return_desc>未知错误</return_desc>");
		} else if(null == rtnSeriNo || null == rtnDate) {
			respStr.append("<return_code>").append(rtnCode).append("</return_code>");
			respStr.append("<return_desc>").append(rtnDesc).append("</return_desc>");
		} else {
			respStr.append("<req_serial_no>").append(rtnSeriNo).append("</req_serial_no>");
			respStr.append("<req_date>").append(rtnDate).append("</req_date>");
			respStr.append("<return_code>").append(rtnCode).append("</return_code>");
			respStr.append("<return_desc>").append(rtnDesc).append("</return_desc>");
		
		}
		respStr.append("<res_serial_no>").append(StringUtil.getSerialNo32()).append("</res_serial_no>");
		respStr.append("<res_date>").append(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss)).append("</res_date>");
		respStr.append("</head><body/></esb>");
		logger.info("系统处理异常响应明文："+respStr);
		
		Configure config = ConfigUtil.getInstance().getConfig();
		String respResult = EncryptUtil.encrypt(config.DES_KEY, config.DES_IV, respStr.toString());
		logger.info("系统处理异常响应密文："+respResult);
		exchange.getOut().setBody(respResult);
	}
}
