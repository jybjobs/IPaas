package com.cloud.paas.util.rest;

import com.cloud.paas.configuration.PropertiesConfUtil;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 10:50 2017/11/24
 * @Modified by:
 */
public class RestTemplateUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtils.class);
	private RestTemplate restTemplate;
	private static PropertiesConfUtil pcUtil;

	public static RestTemplateUtils getSingleInstance(){
		return InnerInstance.instance;
	}

	public RestTemplate getRestTemplate(){
		return this.restTemplate;
	}

	/** 私有的静态内部类 */
	private static class InnerInstance{
		private static final RestTemplateUtils instance = new RestTemplateUtils();
	}

	/** 私有的构造方法 */
	private RestTemplateUtils(){
		/** 生成连接池管理器 */
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);// 长连接保持30秒
		pollingConnectionManager.setMaxTotal(500);	 // 总连接数
		pollingConnectionManager.setDefaultMaxPerRoute(500);	// 同路由的并发数


		/** 设置httpclientbuilder */
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(pollingConnectionManager);
		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));// 重试次数，默认是3次，没有开启
		httpClientBuilder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);  // 保持长连接配置，需要在头添加Keep-Alive

		/**  设置http的header	 */
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		headers.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
		headers.add(new BasicHeader("Connection", "keep-alive"));
		httpClientBuilder.setDefaultHeaders(headers);

		/** 生成httpclient对象, httpClient连接配置，底层是配置RequestConfig */
		HttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		clientHttpRequestFactory.setConnectTimeout(5000);     // 连接超时
		clientHttpRequestFactory.setReadTimeout(5000);        // 数据读取超时时间，即SocketTimeout
		clientHttpRequestFactory.setConnectionRequestTimeout(200);// 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
		clientHttpRequestFactory.setBufferRequestBody(false);   // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。

		/** 添加内容转换器  */
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		messageConverters.add(new FormHttpMessageConverter());
//		messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		messageConverters.add(new ByteArrayHttpMessageConverter());

		/** 生成rest句柄 */
		restTemplate = new RestTemplate(messageConverters);
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

		LOGGER.info("RestClient初始化完成");
	}

	static {

	}
}
