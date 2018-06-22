package com.kpleasing.esb.kpfs.business;

import java.util.Map;

import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;
import com.kpleasing.esb.kpfs.exceptions.KPFSINDataException;


public interface IProcessService {
	
	public String process(Map<String, Object> map) throws KPFSINDataException, KPFSBusinessException;
}
