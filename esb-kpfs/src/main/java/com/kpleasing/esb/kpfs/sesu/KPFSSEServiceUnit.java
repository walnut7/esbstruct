package com.kpleasing.esb.kpfs.sesu;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.kpfs.business.KPFSBusinessService;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSException;

public class KPFSSEServiceUnit {

	private static final Logger LOGGER = LoggerFactory.getLogger(KPFSSEServiceUnit.class);

	/**
	 * 异常处理默认返回出错报文
	 * @param resp
	 * @return
	 */
	private String buildFaildResponse(DefaultFailedResponse resp) {
		SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMddHHmmss");
		resp.setReturn_code("FAILED");
		resp.setRes_date(sdft.format(new Date()));
		resp.setRes_serial_no(RandomStringUtils.randomNumeric(32));

		String sign = "";
		try {
			sign = Security.getSign(resp, ConfigUtil.HLS_KEY);
		} catch (Exception e) {
			LOGGER.error("sign error：" + e.getMessage(), e);
		}

		StringBuilder faildBuild = new StringBuilder();
		faildBuild.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		        .append("<esb><response>")
				.append("<return_code>").append(resp.getReturn_code()).append("</return_code>")
				.append("<return_desc>").append(resp.getReturn_desc()).append("</return_desc>")
				.append("<req_serial_no>").append(resp.getReq_serial_no()).append("</req_serial_no>")
				.append("<req_date>").append(resp.getReq_date()).append("</req_date>")
				.append("<res_serial_no>").append(resp.getRes_serial_no()).append("</res_serial_no>")
				.append("<res_date>").append(resp.getRes_date()).append("</res_date>")
				.append("<sign>").append(sign).append("</sign>")
				.append("</response></esb>");
		LOGGER.info("response xml info::" + faildBuild.toString());
		return faildBuild.toString();

	}

	
	public class DefaultFailedResponse {
		private String return_code;
		private String return_desc;
		private String req_serial_no;
		private String req_date;
		private String res_serial_no;
		private String res_date;
		
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
	}
	
	
	public String postKpfsApi(String requestBody) {
		DefaultFailedResponse respError = new DefaultFailedResponse();
		
		String result = "";
		try {
			result = KPFSBusinessService.processKpfsApiCenter(requestBody);
			
		} catch (KPFSException e) {
			LOGGER.error("KPFS error info：" + e.getMessage(), e);
			respError.setReq_serial_no(e.getReq_serial_no());
			respError.setReq_date(e.getReq_date());
			respError.setReturn_desc(e.getDescription());
			return buildFaildResponse(respError);
		} catch(Exception e) {
			LOGGER.error("KPFS error info：" + e.getMessage(), e);
			respError.setReq_serial_no("");
			respError.setReq_date("");
			respError.setReturn_desc(e.getMessage());
			return buildFaildResponse(respError);
		}
		return result;
	}
}