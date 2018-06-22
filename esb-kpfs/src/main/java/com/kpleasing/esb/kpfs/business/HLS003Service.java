package com.kpleasing.esb.kpfs.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.common.DateUtil;
import com.kpleasing.esb.common.EncryptUtil;
import com.kpleasing.esb.common.HttpHelper;
import com.kpleasing.esb.common.Security;
import com.kpleasing.esb.common.StringUtil;
import com.kpleasing.esb.kpfs.config.ConfigUtil;
import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;
import com.kpleasing.esb.kpfs.exceptions.KPFSINDataException;
import com.kpleasing.esb.receive.protocol.kpfs.hls003.ReqHLS003;
import com.kpleasing.esb.receive.protocol.kpfs.hls003.ResHLS003;


public class HLS003Service implements IProcessService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HLS003Service.class);

	private void validate(ReqHLS003 req) throws KPFSINDataException {
		// 签名校验
		String signFromAPIResponse = req.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new KPFSINDataException("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		LOGGER.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		req.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(req, ConfigUtil.HLS_KEY);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验不过，表示这个API返回的数据有可能已经被篡改了
				throw new KPFSINDataException("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new KPFSINDataException("API签名出錯!!!");
		}

		// 必填项校验
		if(StringUtils.isBlank(req.getReq_serial_no())) {
			throw new KPFSINDataException("参数【req_serial_no】请求流水号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getReq_date())) {
			throw new KPFSINDataException("参数【req_date】请求时间不能为空!");
		}
		
		if(StringUtils.isBlank(req.getSecurity_code()) || StringUtils.isBlank(req.getSecurity_value())) {
			throw new KPFSINDataException("参数【security_code，security_value】请求系统 代码或值不能为空!");
		} else if(!ConfigUtil.HLS_SYSTEM_NAME.equals(req.getSecurity_code()) || !ConfigUtil.HLS_SYSTEM_PWD.equals(req.getSecurity_value())) {
			throw new KPFSINDataException("无操作权限!");
		}
	}
	
	/**
	 * 前置机签名校验
	 * @param resHls001
	 * @throws KPFSINDataException 
	 */
	private void validate2(ResHLS003 res) throws KPFSINDataException {
		// 签名校验
		String signFromAPIResponse = res.getSign();
		if (signFromAPIResponse == "" || signFromAPIResponse == null) {
			throw new KPFSINDataException("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
		}
		
		LOGGER.info("服务器回包里面的签名是:" + signFromAPIResponse);
		// 清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		res.setSign("");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		try {
			String signForAPIResponse = Security.getSign(res, ConfigUtil.KPFS_KEY);
			
			if (!signForAPIResponse.equals(signFromAPIResponse)) {
				// 签名验不过，表示这个API返回的数据有可能已经被篡改了
				throw new KPFSINDataException("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
			}
		} catch (IllegalAccessException e) {
			throw new KPFSINDataException("API签名出錯!!!");
		}
	}
	
	
	@Override
	public String process(Map<String, Object> mapHls003) throws KPFSINDataException, KPFSBusinessException {
		ReqHLS003 request = new ReqHLS003();
		try {
			BeanUtils.populate(request, mapHls003);
			
			// 校验报文数据
			validate(request);
			
			// 前置机报文添加签名
			request.setSign("");
			String sign = Security.getSign(request, ConfigUtil.KPFS_KEY);
			request.setSign(sign);
			
			// 前置机报文加密
			String content = EncryptUtil.encrypt(request.toXML());
			String result = HttpHelper.doHttpPost(ConfigUtil.kpfs_server_url, content);
			
			// 前置机报文解密
			String response = EncryptUtil.decrypt(result);
			LOGGER.info("前置机返回明文信息："+response);
			
			LOGGER.info("报文转换成实体Bean");
			ResHLS003 resHls003 = ResHLS003.fromXML(response);
			
			LOGGER.info("前置机返回报文签名校验："+response);
			validate2(resHls003);
			
			resHls003.setReq_serial_no(request.getReq_serial_no());
			resHls003.setReq_date(request.getReq_date());
			resHls003.setRes_serial_no(StringUtil.getSerialNo32());
			resHls003.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
			resHls003.setReturn_code("SUCCESS");
			resHls003.setReturn_desc("成功");
			
			return resHls003.toResponseXML();
				
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("HLS003处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getMessage());
		} catch(KPFSINDataException e) {
			LOGGER.error("HLS003处理异常：" + e.getMessage(), e);
			throw new KPFSINDataException(e.getDescription());
		} catch(KPFSBusinessException e) {
			LOGGER.error("HLS003处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getDescription());
		} catch (Exception e) {
			LOGGER.error("HLS003处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getMessage());
		}
	}
}
