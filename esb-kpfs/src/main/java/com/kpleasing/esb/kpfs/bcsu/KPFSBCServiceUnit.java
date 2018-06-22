package com.kpleasing.esb.kpfs.bcsu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(name = "HttpServiceServlet", urlPatterns = { "/kpfs/*" }, loadOnStartup = 1)
public class KPFSBCServiceUnit extends HttpServlet {
	
	/**	 * 	 */
	private static final long serialVersionUID = -9131986402849230151L;

	private static final Logger LOGGER = LoggerFactory.getLogger(KPFSBCServiceUnit.class);
	
	@Resource(name = "java:jboss/camel/context/spring-context")
	private CamelContext camelContext;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			StringBuilder requestBody = new StringBuilder();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				requestBody.append(line);
			}

			LOGGER.info("receive xml info::" + requestBody.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			ServletOutputStream out = resp.getOutputStream();
			ProducerTemplate producer = camelContext.createProducerTemplate();
			String result = producer.requestBody("direct:start", requestBody.toString(), String.class);
			LOGGER.info("response xml info::" + result);
			out.write(result.getBytes("UTF-8"));
			
		} catch (IOException e) {
			LOGGER.error("error:", e);
		}
	}
}
