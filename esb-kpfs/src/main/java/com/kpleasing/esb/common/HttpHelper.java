package com.kpleasing.esb.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kpleasing.esb.kpfs.exceptions.KPFSBusinessException;


public class HttpHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private static final int CONNECT_TIMEOUT = 180000;
	
	private static final int SOCKET_TIMEOUT = 180000;
	
	private static final String HTTP_URI_CHARSET = "UTF-8";
	
	/** HTTP保留时间 */
	private final static int MAX_HTTP_KEEP_ALIVE = 30 * 1000;

	/** 每个路由最大连接数 */
	private final static int MAX_ROUTE_CONNECTIONS = 400;
	
	/** 最大连接数 */
	private final static int MAX_TOTAL_CONNECTIONS = 800;
	
	private static PoolingHttpClientConnectionManager connManager = null;
	
	private static CloseableHttpClient httpclient = null;
	
	static {
		HttpRequestRetryHandler myRetryHandler = customRetryHandler();
		ConnectionKeepAliveStrategy customKeepAliveHander = customKeepAliveHander();
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		HttpHost localhost = new HttpHost("locahost", 80);
		connManager.setMaxPerRoute(new HttpRoute(localhost), 50);
		httpclient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(customKeepAliveHander)
		        .setRetryHandler(myRetryHandler).build();
	}
	
	/**
	 * 设置HTTP连接保留时间
	 *
	 * @return 保留策略
	 */
	private static ConnectionKeepAliveStrategy customKeepAliveHander() {
		ConnectionKeepAliveStrategy myKeepAliveHander = new ConnectionKeepAliveStrategy() {

			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return MAX_HTTP_KEEP_ALIVE;
			}
		};
		return myKeepAliveHander;
	}

	/**
	 * 是否重试
	 *
	 * @return false，不重试
	 */
	private static HttpRequestRetryHandler customRetryHandler() {
		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				return false;
			}
		};
		return myRetryHandler;
	}
	
	
	
	/**
	 * 图文信息上传内容
	 * @param httpPost
	 * @param bizScene
	 * @param img_path1
	 * @param img_path2
	 * @throws KPFSBusinessException 
	 */
//	private static void setPostContent(HttpPost httpPost, String bizScene, String img_path1, String img_path2) {
//		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//		multipartEntityBuilder.addTextBody("bizScene", bizScene);
//		multipartEntityBuilder.addBinaryBody("content", new File(img_path1), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"), "content.jpg");
//		multipartEntityBuilder.addBinaryBody("content1", new File(img_path2), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"), "content1.jpg");
//		HttpEntity entityBuild = multipartEntityBuilder.build();
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
//		try {
//			entityBuild.writeTo(out);
//			byte[] bytes = out.toByteArray();
//			
//			String signature = CryptoSample.sign(bytes, secret);
//			System.out.println("签名信息：" + signature);
//			
//			// 设置请求头
//			httpPost.setHeader("X-SPDB-Client-ID", "d83fa07c-0b7f-40e5-b53a-8c21f3c0437f");
//			httpPost.setHeader("X-SPDB-SIGNATURE", signature);
//				
//			// 设置请求体
//			httpPost.setEntity(entityBuild);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	
	public static String doHttpPost(String url, String xmlString) throws KPFSBusinessException {
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpPost.setConfig(requestConfig);
		
		LOGGER.info("post 报文信息:"+xmlString);
		
		StringEntity reqEntity = new StringEntity(xmlString, ContentType.TEXT_HTML.withCharset("UTF-8"));
		httpPost.setEntity(reqEntity);
		
		String result = null;
		try {
			response = httpclient.execute(httpPost);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = response.getEntity();

				BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent(), HTTP_URI_CHARSET));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine);
				}
				result = sb.toString();
				LOGGER.info("post 結果：" + result);
				in.close();
			} else {
				HttpEntity httpEntity =  response.getEntity();
				String contentR = EntityUtils.toString(httpEntity);
				LOGGER.info(contentR);
				LOGGER.info("post 状态：" + response.getStatusLine().toString() + response.getStatusLine().getStatusCode());
				throw new KPFSBusinessException("前置机报文发送失败！");
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
			throw new KPFSBusinessException("前置机网络连接异常！");
		}
		finally {
			httpPost.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		return result;
	}
}
