package com.cloud.paas.util.httpclient;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 10:23 2017/10/25
 * @Modified by:
 */
public class HttpProcessException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -2749168865492921426L;

	public HttpProcessException(Exception e){
		super(e);
	}

	/**
	 * @param message
	 */
	public HttpProcessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param e
	 */
	public HttpProcessException(String message, Exception e) {
		super(message, e);
	}
}
