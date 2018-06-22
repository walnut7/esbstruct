package com.kpleasing.esb.common;

import java.util.UUID;

public class StringUtil {
	
	
	public static String getSerialNo32() {
		return UUID.randomUUID().toString().replace("-","");
	}
	
	public static void main(String[] argc) {
		System.out.println(getSerialNo32());;
	}

}
