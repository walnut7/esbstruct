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
import com.kpleasing.esb.model.wxss001.SPInfoRequest;
import com.kpleasing.esb.model.wxss001.SPInfoResponse;
import com.kpleasing.esb.model.wxss001.SaleInfo;
import com.kpleasing.esb.model.wxss001.WXSS001Request;
import com.kpleasing.esb.model.wxss001.WXSS001Response;
import com.kpleasing.esb.model.wxss001.WXSS001SaleInfo;
import com.kpleasing.esb.tools.DateUtil;
import com.kpleasing.esb.tools.EncryptUtil;
import com.kpleasing.esb.tools.HttpHelper;
import com.kpleasing.esb.tools.Security;
import com.kpleasing.esb.tools.StringUtil;
import com.kpleasing.esb.tools.XMLHelper;

public class SyncSPInfoProcess implements Processor {
	
	private static Logger logger = Logger.getLogger(SyncSPInfoProcess.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		Message in = exchange.getIn();
		String DESKey = (String) exchange.getIn().getHeader("DESKEY");
		String DESIv = (String) exchange.getIn().getHeader("DESIV");
		WXSS001Request wxss001Requ = getWXSS001RequestObject((byte[]) in.getBody());

		WXSS001Response wxss001RespBean = generateWXSS001Response(wxss001Requ);
		
		String msgResponse = EncryptUtil.encrypt(DESKey, DESIv, generateMsgResponseXML(wxss001RespBean));
		logger.info("ESB接口WXSS001响应报文密文："+msgResponse);
		in.setBody(msgResponse);
	}

	/**
	 * 生成响应报文
	 * @param wxssResp
	 * @return
	 */
	private String generateMsgResponseXML(WXSS001Response wxssResp) {
		StringBuilder respXml = new StringBuilder();
		respXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<esb><head>")
		.append("<return_code><![CDATA[").append(wxssResp.getReturn_code()).append("]]></return_code>")
		.append("<return_desc><![CDATA[").append(wxssResp.getReturn_desc()).append("]]></return_desc>")
		.append("<req_serial_no><![CDATA[").append(wxssResp.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(wxssResp.getReq_date()).append("]]></req_date>")
		.append("<res_serial_no><![CDATA[").append(wxssResp.getRes_serial_no()).append("]]></res_serial_no>")
		.append("<res_date><![CDATA[").append(wxssResp.getRes_date()).append("]]></res_date>")
		.append("<sign><![CDATA[").append(wxssResp.getSign()).append("]]></sign>")
		.append("</head><body>")
		.append("<result_code><![CDATA[").append(wxssResp.getResult_code()).append("]]></result_code>")
		.append("<result_desc><![CDATA[").append(wxssResp.getResult_desc()).append("]]></result_desc>")
		.append("</body></esb>");
		logger.info("ESB响应报文明文"+respXml.toString());
		return respXml.toString();
	}
	
	/**
	 * 生成响应实体
	 * @param wxssRequest
	 * @return
	 */
	private WXSS001Response generateWXSS001Response(WXSS001Request wxssRequest) {
		WXSS001Response wxssResp = new WXSS001Response();
		wxssResp.setReq_serial_no(wxssRequest.getReq_serial_no());
		wxssResp.setReq_date(wxssRequest.getReq_date());
		wxssResp.setRes_serial_no(StringUtil.getSerialNo32());
		wxssResp.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		wxssResp.setReturn_code("SUCCESS");
		wxssResp.setReturn_desc("请求成功");
		
		try {
			ParameterConfig paramConfig = wxssRequest.getParamConfig();
			SPInfoRequest spiRequest = getDestRequestBean(wxssRequest, paramConfig);
			String reqMsg = EncryptUtil.encrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), getRequestXml(spiRequest));
			
			logger.info("开始发起请求.......");
			logger.info("请求地址："+ paramConfig.getDestSystemUrl());
			String respMsg = HttpHelper.doHttpPost(paramConfig.getDestSystemUrl(), reqMsg);
			String RespXml = EncryptUtil.decrypt(paramConfig.getDesKey(), paramConfig.getDesIv(), respMsg);
			logger.info("WSXX接口SYNC_SP_INFO返回明文信息：" + RespXml);
			
			SPInfoResponse spiResponse = new SPInfoResponse();
			XMLHelper.getBeanFromXML(RespXml, spiResponse);
			if("SUCCESS".equals(spiResponse.getReturn_code()) && "SUCCESS".equals(spiResponse.getResult_code())) {
				verificationReturnMsg(spiResponse, paramConfig.getSignKey());
			}
			wxssResp.setResult_code(spiResponse.getResult_code());
			wxssResp.setResult_desc(spiResponse.getResult_desc());
			
			List<ClientSecurityKey> clientParams = paramConfig.getClientSecurityKey();
			String key = null;
			for(ClientSecurityKey cParam : clientParams) {
				if(wxssRequest.getSecurity_code().equals(cParam.getClientCode()) && 
						wxssRequest.getSecurity_value().equals(cParam.getClientSecurity())) {
					key = cParam.getClientSignKey(); break;
				}
			}
			
			wxssResp.setSign(Security.getSign(wxssResp, key));
		} catch (Exception  e) {
			wxssResp.setResult_code("FAILED");
			wxssResp.setResult_desc(e.getMessage());
			logger.error(e.getMessage(), e);
		} 
		return wxssResp;
	}
	
	/**
	 * 封装请求实体Bean
	 * @param wxssReq
	 * @param param
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private SPInfoRequest getDestRequestBean(WXSS001Request wxssReq, ParameterConfig param) throws IllegalArgumentException, IllegalAccessException {
		SPInfoRequest spi = new SPInfoRequest();
		spi.setApi_code("SYNC_SP_INFO");
		spi.setReq_serial_no(StringUtil.getSerialNo32());
		spi.setReq_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
		spi.setSecurity_code(param.getAppCode());
		spi.setSecurity_value(param.getAppSecurity());
		logger.info("转换SP信息");
		spi.setBp_id(wxssReq.getBp_id());
		spi.setBp_code(wxssReq.getBp_code());
		spi.setBp_name(wxssReq.getBp_name());
		spi.setOrganization_code(wxssReq.getOrganization_code());
		spi.setProvince(wxssReq.getProvince());
		spi.setCity(wxssReq.getCity());
		spi.setImage_url(wxssReq.getImage_url());
		spi.setEnabled_flag(wxssReq.getEnabled_flag());
		
		logger.info("转换SP销售信息");
		List<WXSS001SaleInfo> sale_list = wxssReq.getSales();
		if(sale_list != null && sale_list.size()>0){
			List<SaleInfo> sales = new ArrayList<SaleInfo>();
			for(WXSS001SaleInfo si :sale_list){
				SaleInfo sale = new SaleInfo();
				sale.setSale_id(si.getSale_id());
				sale.setSale_name(si.getSale_name());
				sale.setSale_phone(si.getSale_phone());
				sale.setEnabled_flag(si.getEnabled_flag());
				sales.add(sale);
			}
			spi.setSales(sales);
		}
		logger.info("SignKey:"+param.getSignKey());
		spi.setSign(Security.getSign(spi, param.getSignKey()));
		
		return spi;
		
	}
	
	/**
	 * 验证目标系统返回值
	 * @param setProfileResp
	 * @param key
	 * @throws ESBException 
	 */
	private void verificationReturnMsg(SPInfoResponse spInfoResp, String key) throws ESBException {
		// 签名校验
		String signFromAPIResponse = spInfoResp.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new ESBException("FAILED", "WSXX API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		logger.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		spInfoResp.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(spInfoResp, key);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验证不过，表示这个API返回的数据有可能已经被篡改了
				throw new ESBException("FAILED", "WSXX API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new ESBException("FAILED", "WSXX API签名出錯!!!");
		}
	}
	
	/**
	 * 获得请求XML
	 * 
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String getRequestXml(SPInfoRequest spiReq) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder msgRequest = new StringBuilder();
		msgRequest.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")
		.append("<wsxx><head>")
		.append("<api_code><![CDATA[").append(spiReq.getApi_code()).append("]]></api_code>")
		.append("<req_serial_no><![CDATA[").append(spiReq.getReq_serial_no()).append("]]></req_serial_no>")
		.append("<req_date><![CDATA[").append(spiReq.getReq_date()).append("]]></req_date>")
		.append("<security_code><![CDATA[").append(spiReq.getSecurity_code()).append("]]></security_code>")
		.append("<security_value><![CDATA[").append(spiReq.getSecurity_value()).append("]]></security_value>")
		.append("<sign><![CDATA[").append(spiReq.getSign()).append("]]></sign>")
		.append("</head><body>");
		
		msgRequest.append(XMLHelper.getXMLFromBean(spiReq));
		
		List<SaleInfo> sale_list = spiReq.getSales();
		if(sale_list != null && sale_list.size()>0){
			msgRequest.append("<sales>");
			for(SaleInfo si :sale_list){
				msgRequest.append("<sale>");
				msgRequest.append(XMLHelper.getXMLFromBean(si));
				msgRequest.append("</sale>");
			}
			msgRequest.append("</sales>");
		}
		msgRequest.append("</body></wsxx>");
		
		logger.info("WXSS接口SYNC_SP_INFO请求报文信息："+msgRequest.toString());
		return msgRequest.toString();
	}
	
	/**
	 * 反序列化对象
	 * @param bytes
	 * @return
	 */
	private WXSS001Request getWXSS001RequestObject(byte[] bytes) {
		ByteArrayInputStream baiStream = null;
		ObjectInputStream ois = null;
		try {
			baiStream = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(baiStream);
			WXSS001Request wxss001Request = (WXSS001Request) ois.readObject();

			return wxss001Request;

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
