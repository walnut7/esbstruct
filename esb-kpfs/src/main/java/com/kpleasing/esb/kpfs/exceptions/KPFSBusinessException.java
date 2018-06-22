package com.kpleasing.esb.kpfs.exceptions;

public class KPFSBusinessException extends KPFSException {

	/** * serialVersionUID * */
	private static final long serialVersionUID = 5516487225326614564L;
	
	public KPFSBusinessException(String description) {
		this.setDescription(description);
	}
}
