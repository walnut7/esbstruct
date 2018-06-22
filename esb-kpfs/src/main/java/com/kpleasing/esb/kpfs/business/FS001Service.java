package com.kpleasing.esb.kpfs.business;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;
import com.kpleasing.esb.kpfs.exceptions.KPFSINDataException;
import com.kpleasing.esb.receive.protocol.kpfs.fs001.ReqFS001;


public class FS001Service implements IProcessService {

	@Override
	public String process(Map<String, Object> mapFs001) throws KPFSINDataException, KPFSBusinessException {
		ReqFS001 request = new ReqFS001();
		
		try {
			BeanUtils.populate(request, mapFs001);
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// 校验报文数据
		validate(request);

		// 获取发送的响应报文
		// 1、读取配置文件
		// 2、报文组装
		// 3、报文加密
		// 4、报文发送
		// 5、报文接收
		// 6、报文解析
		// 7、报文返回
		return getDemoResult(request);
	}
	
	private void validate(ReqFS001 req) throws KPFSINDataException {
		
	}
	
	private String getDemoResult(ReqFS001 req) throws KPFSBusinessException {
		return null;
		
	}

}
