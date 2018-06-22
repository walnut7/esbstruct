package com.kpleasing.esb.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

import com.kpleasing.esb.config.Configure;
import com.kpleasing.esb.route.util.ConfigUtil;
import com.kpleasing.esb.tools.HttpHelper;

public class TimerProcessService implements Processor {
	
	private static Logger logger = Logger.getLogger(TimerProcessService.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("开始推送数据......");
		try {
			Configure config = ConfigUtil.getInstance().getConfig();
			HttpHelper.doHttpPost(config.EWECHAT_URL+"/push", "");
		} catch(Exception e) {
			
		}
	}

}
