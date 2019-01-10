package com.kpleasing.esb.wxss.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.wxss003.FinanceSchemeRequest;
import com.kpleasing.esb.model.wxss003.FinanceSchemeResponse;
import com.kpleasing.esb.model.wxss003.WXSS003Request;
import com.kpleasing.esb.model.wxss003.WXSS003Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncFinanceSchemeProcess implements Processor {

	private static Logger logger = Logger.getLogger(SyncFinanceSchemeProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		WXSS003Request wxss003Requ = getUserInfoRequestObject((byte[]) in.getBody());

		WXSS003Response wxss003Resp = generateWXSS003ResponseBean(wxss003Requ);

		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(wxss003Resp));
		logger.info("ESB接口WXSS003响应报文密文：" + msgResponse);
		in.setBody(msgResponse);
	}

	/**
	 * 生成响应报文
	 * 
	 * @param wxssResp
	 * @return
	 */
	private String generateMsgResponseXML(WXSS003Response wxss003Resp) {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("<esb><head>")
				.append("<return_code><![CDATA[").append(wxss003Resp.getReturn_code()).append("]]></return_code>")
				.append("<return_desc><![CDATA[").append(wxss003Resp.getReturn_desc()).append("]]></return_desc>")
				.append("<req_serial_no><![CDATA[").append(wxss003Resp.getReq_serial_no()).append("]]></req_serial_no>")
				.append("<req_date><![CDATA[").append(wxss003Resp.getReq_date()).append("]]></req_date>")
				.append("<res_serial_no><![CDATA[").append(wxss003Resp.getRes_serial_no()).append("]]></res_serial_no>")
				.append("<res_date><![CDATA[").append(wxss003Resp.getRes_date()).append("]]></res_date>")
				.append("<sign><![CDATA[").append(wxss003Resp.getSign()).append("]]></sign>").append("</head><body>")
				.append("<result_code><![CDATA[").append(wxss003Resp.getResult_code()).append("]]></result_code>")
				.append("<result_desc><![CDATA[").append(wxss003Resp.getResult_desc()).append("]]></result_desc>")
				.append("</body></esb>");
		logger.info("ESB响应报文明文" + respXml.toString());
		return respXml.toString();
	}

	/**
	 * 生成响应实体
	 * 
	 * @param wxss003Req
	 * @return
	 */
	private WXSS003Response generateWXSS003ResponseBean(WXSS003Request wxss003Req) {
		WXSS003Response wxssResp = new WXSS003Response();
		wxssResp.setReq_serial_no(wxss003Req.getReq_serial_no());
		wxssResp.setReq_date(wxss003Req.getReq_date());
		wxssResp.setRes_serial_no(StringUtil.getSerialNo32());
		wxssResp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		wxssResp.setReturn_code("SUCCESS");
		wxssResp.setReturn_desc("请求成功");

		try {
			ParameterConfig paramConfig = wxss003Req.getParamConfig();
			FinanceSchemeRequest finSchemeReq = getDestRequestBean(wxss003Req, paramConfig);

			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(),
					getRequestXml(finSchemeReq));

			logger.info("开始发起请求.......");
			logger.info("请求地址：" + paramConfig.getDestSystemUrl());
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("WSXX接口SYNC_FIN_SCHEME返回明文信息：" + RespXml);

			FinanceSchemeResponse fsResponse = new FinanceSchemeResponse();
			XMLHelper.getBeanFromXML(RespXml, fsResponse);
			if ("SUCCESS".equals(fsResponse.getReturn_code()) && "SUCCESS".equals(fsResponse.getResult_code())) {
				verificationReturnMsg(fsResponse, paramConfig.getSignKey());
			}
			wxssResp.setResult_code(fsResponse.getResult_code());
			wxssResp.setResult_desc(fsResponse.getResult_desc());

			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for (ClientSecurityKey cParam : clientParams) {
				if (wxss003Req.getSecurity_code().equals(cParam.getClientCode())
						&& wxss003Req.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey();
					break;
				}
			}

			wxssResp.setSign(Security.getSign(wxssResp, key));

		} catch (Exception e) {
			wxssResp.setResult_code("FAILED");
			wxssResp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return wxssResp;
	}

	/**
	 * 获得请求XML
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private String getRequestXml(FinanceSchemeRequest finSchemeReq) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<wsxx><head>")
		.append("<api_code><![CDATA[").append(finSchemeReq.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(finSchemeReq.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(finSchemeReq.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(finSchemeReq.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(finSchemeReq.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(finSchemeReq.getSign()).append("]]></sign>")
		.append("</head><body>");
		
		msgRequest.append(XMLHelper.getXMLFromBean(finSchemeReq));
		msgRequest.append("</body></wsxx>");
		
		logger.info("WXSS接口SYNC_FIN_SCHEME请求报文信息：" + msgRequest.toString());
		return msgRequest.toString();
	}

	/**
	 * 封装请求实体Bean
	 * 
	 * @param wxss003Req
	 * @param param
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private FinanceSchemeRequest getDestRequestBean(WXSS003Request wxss003Req, ParameterConfig param)
			throws IllegalArgumentException, IllegalAccessException {
		FinanceSchemeRequest fsReq = new FinanceSchemeRequest();
		fsReq.setApi_code("SYNC_FIN_SCHEME");
		fsReq.setReq_serial_no(StringUtil.getSerialNo32());
		fsReq.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		fsReq.setSecurity_code(param.getAppCode());
		fsReq.setSecurity_value(param.getAppSecurity());

		logger.info("转换融资方案信息");
		fsReq.setPlan_id(wxss003Req.getPlan_id());
		fsReq.setPlan_desc(wxss003Req.getPlan_desc());
		fsReq.setBp_id(wxss003Req.getBp_id());
		fsReq.setModel_id(wxss003Req.getModel_id());
		fsReq.setLease_item_amount(wxss003Req.getLease_item_amount());
		fsReq.setDownpayment_amount(wxss003Req.getDownpayment_amount());
		fsReq.setDeposit_amount(wxss003Req.getDeposit_amount());
		fsReq.setPurchase_tax(wxss003Req.getPurchase_tax());
		fsReq.setInsurance_fee_financing(wxss003Req.getInsurance_fee_financing());
		fsReq.setCar_plate_fee(wxss003Req.getCar_plate_fee());
		fsReq.setGps_fee(wxss003Req.getGps_fee());
		fsReq.setTaxinsurance(wxss003Req.getTaxinsurance());
		fsReq.setPckprice(wxss003Req.getPckprice());
		fsReq.setLease_times(wxss003Req.getLease_times());
		fsReq.setPlan_type(wxss003Req.getPlan_type());
		fsReq.setRental(wxss003Req.getRental());
		fsReq.setInt_rate(wxss003Req.getInt_rate());
		fsReq.setRental_1_12(wxss003Req.getRental_1_12());
		fsReq.setRental_13_48(wxss003Req.getRental_13_48());
		fsReq.setBuyout_amount(wxss003Req.getBuyout_amount());
		fsReq.setValid_date_from(wxss003Req.getValid_date_from());
		fsReq.setValid_date_to(wxss003Req.getValid_date_to());
		fsReq.setPlan_synopsis(wxss003Req.getPlan_synopsis());

		logger.info("SignKey:" + param.getSignKey());
		fsReq.setSign(Security.getSign(fsReq, param.getSignKey()));

		return fsReq;
	}

	/**
	 * 验证目标系统返回值
	 * 
	 * @param sciResp
	 * @param key
	 * @throws ESBException
	 */
	private void verificationReturnMsg(FinanceSchemeResponse fsResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = fsResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "WSXX API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}

		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		fsResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(fsResp, key);

			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "WSXX API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "WSXX API签名出錯!!!");
		}
	}

	/**
	 * 反序列化对象
	 * 
	 * @param bytes
	 * @return
	 */
	private WXSS003Request getUserInfoRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			WXSS003Request wxss003Request = (WXSS003Request) ois.readObject();

			return wxss003Request;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != baiStream) {
				try {
					baiStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != ois) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
