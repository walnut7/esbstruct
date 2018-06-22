package com.kpleasing.esb.wxss.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.ESBException;
import com.kpleasing.esb.model.wxss002.CarInfoRequest;
import com.kpleasing.esb.model.wxss002.CarInfoResponse;
import com.kpleasing.esb.model.wxss002.CarParam;
import com.kpleasing.esb.model.wxss002.WXSS002CarParam;
import com.kpleasing.esb.model.wxss002.WXSS002Request;
import com.kpleasing.esb.model.wxss002.WXSS002Response;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncCarProcess implements Processor {

	private static Logger logger = Logger.getLogger(SyncCarProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		WXSS002Request wxss002Requ = getSyncCarRequestObject((byte[]) in.getBody());

		WXSS002Response wxss002RespBean = generateWXSS002ResponseBean(wxss002Requ);

		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(wxss002RespBean));
		logger.info("ESB接口WXSS002响应报文密文：" + msgResponse);
		in.setBody(msgResponse);
	}

	/**
	 * 生成响应报文
	 * 
	 * @param wxssResp
	 * @return
	 */
	private String generateMsgResponseXML(WXSS002Response wxssResp) {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("<esb><head>")
				.append("<return_code><![CDATA[").append(wxssResp.getReturn_code()).append("]]></return_code>")
				.append("<return_desc><![CDATA[").append(wxssResp.getReturn_desc()).append("]]></return_desc>")
				.append("<req_serial_no><![CDATA[").append(wxssResp.getReq_serial_no()).append("]]></req_serial_no>")
				.append("<req_date><![CDATA[").append(wxssResp.getReq_date()).append("]]></req_date>")
				.append("<res_serial_no><![CDATA[").append(wxssResp.getRes_serial_no()).append("]]></res_serial_no>")
				.append("<res_date><![CDATA[").append(wxssResp.getRes_date()).append("]]></res_date>")
				.append("<sign><![CDATA[").append(wxssResp.getSign()).append("]]></sign>").append("</head><body>")
				.append("<result_code><![CDATA[").append(wxssResp.getResult_code()).append("]]></result_code>")
				.append("<result_desc><![CDATA[").append(wxssResp.getResult_desc()).append("]]></result_desc>")
				.append("</body></esb>");
		logger.info("ESB响应报文明文" + respXml.toString());
		return respXml.toString();
	}

	/**
	 * 生成响应实体
	 * 
	 * @param wxss002Req
	 * @return
	 */
	private WXSS002Response generateWXSS002ResponseBean(WXSS002Request wxss002Req) {
		WXSS002Response wxssResp = new WXSS002Response();
		wxssResp.setReq_serial_no(wxss002Req.getReq_serial_no());
		wxssResp.setReq_date(wxss002Req.getReq_date());
		wxssResp.setRes_serial_no(StringUtil.getSerialNo32());
		wxssResp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		wxssResp.setReturn_code("SUCCESS");
		wxssResp.setReturn_desc("请求成功");

		try {
			ParameterConfig paramConfig = wxss002Req.getParamConfig();
			CarInfoRequest syncCarInfoReq = getDestRequestBean(wxss002Req, paramConfig);

			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(),
					getRequestXml(syncCarInfoReq));

			logger.info("开始发起请求.......");
			logger.info("请求地址：" + paramConfig.getDestSystemUrl());
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("WSXX接口SYNC_CAR_INFO返回明文信息：" + RespXml);

			CarInfoResponse ciResponse = new CarInfoResponse();
			XMLHelper.getBeanFromXML(RespXml, ciResponse);
			if ("SUCCESS".equals(ciResponse.getReturn_code()) && "SUCCESS".equals(ciResponse.getResult_code())) {
				verificationReturnMsg(ciResponse, paramConfig.getSignKey());
			}
			wxssResp.setResult_code(ciResponse.getResult_code());
			wxssResp.setResult_desc(ciResponse.getResult_desc());

			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for (ClientSecurityKey cParam : clientParams) {
				if (wxss002Req.getSecurity_code().equals(cParam.getClientCode())
						&& wxss002Req.getSecurity_value().equals(cParam.getClientSecurity())) {
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
	 * 封装请求实体Bean
	 * 
	 * @param wxssReq
	 * @param param
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private CarInfoRequest getDestRequestBean(WXSS002Request wxssReq, ParameterConfig param)
			throws IllegalArgumentException, IllegalAccessException {
		CarInfoRequest ciReq = new CarInfoRequest();
		ciReq.setApi_code("SYNC_CAR_INFO");
		ciReq.setReq_serial_no(StringUtil.getSerialNo32());
		ciReq.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		ciReq.setSecurity_code(param.getAppCode());
		ciReq.setSecurity_value(param.getAppSecurity());

		logger.info("转换车辆信息");
		ciReq.setModel_id(wxssReq.getModel_id());
		ciReq.setFirst_letter(wxssReq.getFirst_letter());
		ciReq.setBrand(wxssReq.getBrand());
		ciReq.setSeries(wxssReq.getSeries());
		ciReq.setSeries_logo(wxssReq.getSeries_logo());
		ciReq.setModel(wxssReq.getModel());
		ciReq.setSale_status(wxssReq.getSale_status());
		ciReq.setMenufactory(wxssReq.getMenufactory());
		ciReq.setYear(wxssReq.getYear());
		ciReq.setPublic_time(wxssReq.getPublic_time());
		ciReq.setMsrp(wxssReq.getMsrp());
		ciReq.setModel_type(wxssReq.getModel_type());
		ciReq.setEnabled_flag(wxssReq.getEnabled_flag());

		logger.info("转换车辆配置信息");
		List<WXSS002CarParam> cp_list = wxssReq.getCar_params();
		if (cp_list != null && cp_list.size() > 0) {
			List<CarParam> params = new ArrayList<CarParam>();
			for (WXSS002CarParam wcp : cp_list) {
				CarParam scp = new CarParam();
				scp.setParam_name(wcp.getParam_name());
				scp.setParam_value(wcp.getParam_value());
				params.add(scp);
			}
			ciReq.setCar_params(params);
		}

		logger.info("SignKey:" + param.getSignKey());
		ciReq.setSign(Security.getSign(ciReq, param.getSignKey()));

		return ciReq;
	}

	/**
	 * 获得请求XML
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private String getRequestXml(CarInfoRequest ciReq) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<wsxx><head>")
		.append("<api_code><![CDATA[").append(ciReq.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(ciReq.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(ciReq.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(ciReq.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(ciReq.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(ciReq.getSign()).append("]]></sign>")
		.append("</head><body>");

		msgRequest.append(XMLHelper.getXMLFromBean(ciReq));
		
		List<CarParam> param_list = ciReq.getCar_params();
		if (param_list != null && param_list.size() > 0) {
			msgRequest.append("<car_params>");
			for (CarParam scp : param_list) {
				msgRequest.append("<car_param>");
				msgRequest.append(XMLHelper.getXMLFromBean(scp));
				msgRequest.append("</car_param>");
			}
			msgRequest.append("</car_params>");
		}
		msgRequest.append("</body></wsxx>");

		logger.info("WXSS接口SYNC_CAR_INFO请求报文信息：" + msgRequest.toString());
		return msgRequest.toString();
	}

	/**
	 * 验证目标系统返回值
	 * 
	 * @param sciResp
	 * @param key
	 * @throws ESBException
	 */
	private void verificationReturnMsg(CarInfoResponse sciResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = sciResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "WSXX API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}

		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		sciResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(sciResp, key);

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
	private WXSS002Request getSyncCarRequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			WXSS002Request wxss002Request = (WXSS002Request) ois.readObject();

			return wxss002Request;

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
