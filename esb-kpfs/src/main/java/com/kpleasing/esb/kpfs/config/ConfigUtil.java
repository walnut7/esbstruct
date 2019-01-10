package com.kpleasing.esb.kpfs.config;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
	
	// 测试
//	public static String kpfs_server_url = "http://180.168.126.120:9900/kpfs-web/kpfstest/api";
//	public static String KPFS_KEY = "e4d95a567d934b7aa78e0eea1c1b4894";
//	public static String HLS_KEY = "8d60c987bc8e4a31931399286b5d7757";
//	public static String HLS_SYSTEM_NAME = "HLS";
//	public static String HLS_SYSTEM_PWD = "123456";
	
	// 生产
	public static String kpfs_server_url = "http://58.247.134.170:9900/kpfs-web/kpfs/api";
	public static String KPFS_KEY = "3779274adace4003b0a0f80b50df2d29";
	public static String HLS_KEY = "cc2ee6ecd3e94602bb99c780452bb756";
	public static String HLS_SYSTEM_NAME = "HLS";
	public static String HLS_SYSTEM_PWD = "1jPdwXlzTe6";
	
	public static Map<String, String> getProtoMap() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("HLS001", "com.kpleasing.esb.kpfs.business.HLS001Service");
		map.put("HLS002", "com.kpleasing.esb.kpfs.business.HLS002Service");
		map.put("HLS003", "com.kpleasing.esb.kpfs.business.HLS003Service");
		map.put("HLS004", "com.kpleasing.esb.kpfs.business.HLS004Service");
		map.put("HLS005", "com.kpleasing.esb.kpfs.business.HLS005Service");
		map.put("HLS006", "com.kpleasing.esb.kpfs.business.HLS006Service");
		map.put("HLS007", "com.kpleasing.esb.kpfs.business.HLS007Service");
		map.put("HLS008", "com.kpleasing.esb.kpfs.business.HLS008Service");
		map.put("HLS009", "com.kpleasing.esb.kpfs.business.HLS009Service");
		map.put("HLS010", "com.kpleasing.esb.kpfs.business.HLS010Service");
		
		return map;
	}
}
