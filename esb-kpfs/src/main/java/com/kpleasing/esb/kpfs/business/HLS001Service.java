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
import com.kpleasing.esb.receive.protocol.kpfs.hls001.ReqHLS001;
import com.kpleasing.esb.receive.protocol.kpfs.hls001.ResHLS001;


public class HLS001Service implements IProcessService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HLS001Service.class);
	
	private void validate(ReqHLS001 req) throws KPFSINDataException {
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
		
		if(StringUtils.isBlank(req.getCert_type())) {
			throw new KPFSINDataException("cert_type is null!");
		}
		
		if(StringUtils.isBlank(req.getCert_code())) {
			throw new KPFSINDataException("cert_code is null!");
		}
		
		if(StringUtils.isBlank(req.getCust_name())) {
			throw new KPFSINDataException("cust_name is null!");
		}
		
		if(StringUtils.isBlank(req.getBiz_sence())) {
			throw new KPFSINDataException("biz_sence is null!");
		}
		
		if(StringUtils.isBlank(req.getOrder_no())) {
			throw new KPFSINDataException("order_no is null!");
		}
		
		if(StringUtils.isBlank(req.getDept_no())) {
			throw new KPFSINDataException("dept_no is null!");
		}
		
		if(StringUtils.isBlank(req.getBusiness_type())) {
			throw new KPFSINDataException("business_type is null!");
		}
		
		if(StringUtils.isBlank(req.getLoan_term())) {
			throw new KPFSINDataException("loan_term is null!");
		}
		
		if(StringUtils.isBlank(req.getInvoice_amt())) {
			throw new KPFSINDataException("invoice_amt is null!");
		}
		
		if(StringUtils.isBlank(req.getAgreed_loan_amt())) {
			throw new KPFSINDataException("agreed_loan_amt is null!");
		}
		
		if(StringUtils.isBlank(req.getSource_dsc())) {
			throw new KPFSINDataException("source_dec is null!");
		}
		
		if(StringUtils.isBlank(req.getEdu_level())) {
			throw new KPFSINDataException("edu_level is null!");
		}
		
		if(StringUtils.isBlank(req.getPosition())) {
			throw new KPFSINDataException("position is null!");
		}
		
		if(StringUtils.isBlank(req.getEntry_year())) {
			throw new KPFSINDataException("entry_year is null!");
		}
		
		if(StringUtils.isBlank(req.getUnit_name())) {
			throw new KPFSINDataException("unit_name is null!");
		}
		
		if(StringUtils.isBlank(req.getUnit_tel())) {
			throw new KPFSINDataException("unit_tel is null!");
		} else if(req.getUnit_tel().length() > 15) {
			throw new KPFSINDataException("unit_tel最大长度为15，unit_tel实际长度为"+req.getUnit_tel().length()+"!");
		}
		
		if(StringUtils.isBlank(req.getMarr_status())) {
			throw new KPFSINDataException("marr_satus is null!");
		}
		
		if(StringUtils.isBlank(req.getLoan_acc_bank())) {
			throw new KPFSINDataException("放款账户开户行行号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getLoan_card_no())) {
			throw new KPFSINDataException("放款卡号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getRepay_acc_bank())) {
			throw new KPFSINDataException("还款账户开户行行号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getRepay_card_no())) {
			throw new KPFSINDataException("还款卡号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_name1())) {
			throw new KPFSINDataException("联系人1姓名不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_relation1())) {
			throw new KPFSINDataException("联系人1关系不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_phone1())) {
			throw new KPFSINDataException("联系人1手机号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_name2())) {
			throw new KPFSINDataException("联系人2姓名不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_relation2())) {
			throw new KPFSINDataException("联系人2关系不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_phone2())) {
			throw new KPFSINDataException("联系人2手机号不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_name())) {
			throw new KPFSINDataException("紧急联系人姓名不能为空!");
		}
		
		if(StringUtils.isBlank(req.getContact_phone())) {
			throw new KPFSINDataException("紧急联系人手机号不能为空!");
		}
	}
	
	/**
	 * 前置机签名校验
	 * @param resHls001
	 * @throws KPFSINDataException 
	 */
	private void validate2(ResHLS001 res) throws KPFSINDataException {
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
	public String process(Map<String, Object> mapHls001) throws KPFSINDataException, KPFSBusinessException {
		ReqHLS001 request = new ReqHLS001();
		try {
			BeanUtils.populate(request, mapHls001);
			
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
			ResHLS001 resHls001 = ResHLS001.fromXML(response);
				
			LOGGER.info("前置机返回报文签名校验："+response);
			validate2(resHls001);
				
			resHls001.setReq_serial_no(request.getReq_serial_no());
			resHls001.setReq_date(request.getReq_date());
			resHls001.setRes_serial_no(StringUtil.getSerialNo32());
			resHls001.setRes_date(DateUtil.getCurrentDate(DateUtil.yyyyMMddHHmmss));
			resHls001.setReturn_code("SUCCESS");
			resHls001.setReturn_desc("成功");
			return resHls001.toResponseXML();
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("HLS001处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getMessage());
		} catch(KPFSINDataException e) {
			LOGGER.error("HLS001处理异常：" + e.getMessage(), e);
			throw new KPFSINDataException(e.getDescription());
		} catch(KPFSBusinessException e) {
			LOGGER.error("HLS001处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getDescription());
		} catch (Exception e) {
			LOGGER.error("HLS001处理异常：" + e.getMessage(), e);
			throw new KPFSBusinessException(e.getMessage());
		}
	}
}
