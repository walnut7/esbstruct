package com.kpleasing.esb.route.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.kpleasing.esb.config.Configure;
import com.kpleasing.esb.config.vo.ClientSecurityKey;
import com.kpleasing.esb.config.vo.ParameterConfig;
import com.kpleasing.esb.spring.context.SpringContextHolder;

public class ConfigUtil {

	private static Logger logger = Logger.getLogger(ConfigUtil.class);
	private static volatile ConfigUtil instance = null;
	private Configure config;

	private ConfigUtil() {
	}

	public static ConfigUtil getInstance() {
		if (instance == null) {
			synchronized (ConfigUtil.class) {
				if (instance == null) {
					instance = new ConfigUtil();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 系统键值对参数
	 * @return
	 */
	public Map<String, String> getSystemParam() {
		JdbcTemplate jdbc = (JdbcTemplate) SpringContextHolder.getBean("jdbcTemplate");
		List<Map<String, Object>> list = jdbc.queryForList("SELECT sys_key, sys_value FROM TB_SYSTEM_CONFIG");
		
		Map<String, String> map = new HashMap<String, String>();
		for(Map<String, Object> m : list) {
			logger.info("sys_key = " + m.get("sys_key") + "   sys_value = " + m.get("sys_value"));
			map.put((String)m.get("sys_key"), (String)m.get("sys_value"));
		}
		
		return map;
	}
	

	/**
	 * 外部系统安全验证参数
	 * @return
	 */
	private List<ParameterConfig> getParamConfigList() {
		JdbcTemplate jdbc = (JdbcTemplate) SpringContextHolder.getBean("jdbcTemplate");
		List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM TB_SECURITY");
		List<ParameterConfig> paraConfigList = new  ArrayList<ParameterConfig>();
		for(Map<String, Object> map : list) {
			ParameterConfig paraConfig = new ParameterConfig();
			paraConfig.setSecurityId((int)map.get("security_id"));
			paraConfig.setApiCode((String)map.get("api_code"));
			paraConfig.setClazz((String)map.get("clazz"));
			paraConfig.setAppCode((String)map.get("app_code"));
			paraConfig.setAppSecurity((String)map.get("app_security"));
			paraConfig.setDestSystemUrl((String)map.get("dest_system_url"));
			paraConfig.setDestSystemPort((String)map.get("dest_system_port"));
			paraConfig.setDesKey((String)map.get("des_key"));
			paraConfig.setDesIv((String)map.get("des_iv"));
			paraConfig.setSignKey((String)map.get("sign_key"));
			paraConfig.setClientSecurityKey(getClientSecurityKeyList(paraConfig.getSecurityId()));
			
			logger.info(paraConfig.toString());
			paraConfigList.add(paraConfig);
		}
		return paraConfigList;
	}

	
	/**
	 * ESB终端系统安全参数
	 * @param security
	 */
	private List<ClientSecurityKey> getClientSecurityKeyList(int securityId) {
		JdbcTemplate jdbc = (JdbcTemplate) SpringContextHolder.getBean("jdbcTemplate");
		List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM TB_CLIENT_SECURITY WHERE security_id = ?", new Object[] { securityId });
		
		List<ClientSecurityKey> clientKeyList = new  ArrayList<ClientSecurityKey>();
		for(Map<String, Object> map : list) {
			ClientSecurityKey clientKeys = new ClientSecurityKey();
			clientKeys.setId((int)map.get("id"));
			clientKeys.setSecurityId((int)map.get("security_id"));
			clientKeys.setClientCode((String)map.get("client_code"));
			clientKeys.setClientSecurity((String)map.get("client_security"));
			clientKeys.setClientSignKey((String)map.get("client_sign_key"));
			
			logger.info(clientKeys.toString());
			clientKeyList.add(clientKeys);
		}
		return clientKeyList;
	}

	
	/**
	 * 
	 * @return
	 */
	public Configure getConfig() {
		if (null == config) {
			config = new Configure();
			config.setParameterConfig(getParamConfigList());
			
			Map<String, String> mapParam = getSystemParam();
			config.DES_KEY = mapParam.get("DES_KEY");
			config.DES_IV = mapParam.get("DES_IV");
			config.EWECHAT_URL = mapParam.get("EWECHAT_URL");
		}
		return config;
	}

}
