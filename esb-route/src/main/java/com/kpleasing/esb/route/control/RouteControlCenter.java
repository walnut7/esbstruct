package com.kpleasing.esb.route.control;

import org.apache.log4j.Logger;

import com.kpleasing.esb.business.AbstractRouteFactory;
import com.kpleasing.esb.config.Configure;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.exception.InputNDataParamException;
import com.kpleasing.esb.exception.RouteException;
import com.kpleasing.esb.route.util.ConfigUtil;

public class RouteControlCenter {
	
	private static Logger logger = Logger.getLogger(RouteControlCenter.class);

	/**
	 * 动态路由对象
	 * @param apiCode
	 * @param requestXml
	 * @return
	 */
	public static Object getDynamicsRouteObject(String apiCode, String requestXml) throws RouteException, InputNDataParamException {
		try {
			// 读取参数，动态调用业务处理
			logger.info("读取路由配置信息");
			Configure config = ConfigUtil.getInstance().getConfig();
			ParameterConfig paramConfig = null;
			for(ParameterConfig param : config.getParameterConfig()) {
				if(param.getApiCode().equals(apiCode)) {
					paramConfig = param;	break;
				}
			}
			
			logger.info("根据["+paramConfig.getApiCode()+"]调用：["+paramConfig.getClazz()+"]开始处理");
			AbstractRouteFactory<?> business = (AbstractRouteFactory<?>) Class.forName(paramConfig.getClazz()).newInstance();
			return business.process(requestXml, paramConfig);
			
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RouteException("FAILED", "系统错误：" + e.getMessage());
		}
	}
}
