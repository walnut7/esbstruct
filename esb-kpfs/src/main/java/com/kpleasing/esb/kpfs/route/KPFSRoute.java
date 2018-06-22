package com.kpleasing.esb.kpfs.route;

import org.apache.camel.builder.RouteBuilder;

import com.kpleasing.esb.kpfs.sesu.KPFSSEServiceUnit;


public class KPFSRoute extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		from("cxfrs:bean:rsServer?bindingStyle=SimpleConsumer")
				.recipientList(simple("direct-vm:${header.operationName}"));

		from("direct-vm:postKpfsApi")
				.bean(KPFSSEServiceUnit.class, "postKpfsApi");
		
	}

}
