package com.cloud.paas.util.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import javax.xml.bind.DatatypeConverter;
import java.util.Map;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 14:46 2017/11/30
 * @Modified by:
 */
public class RestClient {

	private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static String doGet(String url){
		String responseStr = null;
		try {
			responseStr =  RestTemplateUtils.getSingleInstance().getRestTemplate().getForObject(url, String.class);
		}catch(RestClientException e) {
			logger.error("GET failed, url: {}, error: {}",url, e.getMessage());
			return null;
		}
		return responseStr;
	}

	/**
	 * 有账号密码的Get
	 * @param name 账号
	 * @param pwd 密码
	 * @param url 地址
	 * @return responseStr
	 */
	public static String doGetWIthAuth(String name, String pwd, String url) {
		ResponseEntity<String> responseStr;
		try {
			HttpHeaders headers = new HttpHeaders();
			String encoding = "Basic " + DatatypeConverter.printBase64Binary((name + ":" + pwd).getBytes("UTF-8"));
			headers.set("Authorization", encoding);
			HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
			responseStr = RestTemplateUtils.getSingleInstance().getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, String.class);
		} catch (Exception e) {
			logger.error("GET failed, url: {}, error: {}", url, e.getMessage());
			return null;
		}
		return responseStr.getBody();
	}

	public static ResponseEntity<String> doPostWithAuthAndParameters(String name, String pwd, String url){
		return doPostWithAuthAndParameters(name, pwd, url, null);
	}
	/**
	 * @Description 有账号密码和参数的Post
	 * @param name 账号
	 * @param pwd 密码
	 * @param url 地址
	 * @param params 参数
	 * @return responseStr
	 */
	public static ResponseEntity<String> doPostWithAuthAndParameters(String name, String pwd, String url, MultiValueMap<String, String> params) {
		ResponseEntity<String> responseStr = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			String encoding = "Basic " + DatatypeConverter.printBase64Binary((name + ":" + pwd).getBytes("UTF-8"));
			headers.set("Authorization", encoding);
			HttpEntity<MultiValueMap<String, String>> entity;
			if (params != null){
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				entity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
			} else {
				entity = new HttpEntity<MultiValueMap<String, String>>(null, headers);
			}
			responseStr =  RestTemplateUtils.getSingleInstance().getRestTemplate().postForEntity(url, entity, String.class);
		} catch (Exception e) {
			logger.error("Post failed, url: {}, error: {}",url, e.getMessage());
			return null;
		}
		return responseStr;
	}

	/**
	 * @Description 有账号密码的Post
	 * @param name 账号
	 * @param pwd 密码
	 * @param url 地址
	 * @param xml XML字符串
	 * @return responseStr
	 */
	public static ResponseEntity<String> doPostXMLWithAuth(String name, String pwd, String url, String xml) {
		ResponseEntity<String> responseStr = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			String encoding = "Basic " + DatatypeConverter.printBase64Binary((name + ":" + pwd).getBytes("UTF-8"));
			headers.set("Authorization", encoding);
			HttpEntity<String> entity;
			headers.setContentType(MediaType.APPLICATION_XML);
			entity = new HttpEntity<String>(xml, headers);
			responseStr =  RestTemplateUtils.getSingleInstance().getRestTemplate().postForEntity(url, entity, String.class);
		} catch (Exception e) {
			logger.error("Post failed, url: {}, error: {}",url, e.getMessage());
			return null;
		}
		return responseStr;
	}

	/**
	 * @Description 有账号密码的Post
	 * @param name 账号
	 * @param pwd 密码
	 * @param url 地址
	 * @param xml XML字符串
	 * @return responseStr
	 */
	public static ResponseEntity<String> doPostWithAuth(String name, String pwd, String url, String xml) {
		ResponseEntity<String> responseStr = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			String encoding = "Basic " + DatatypeConverter.printBase64Binary((name + ":" + pwd).getBytes("UTF-8"));
			headers.set("Authorization", encoding);
			HttpEntity<String> entity;
			entity = new HttpEntity<String>(xml, headers);
			responseStr =  RestTemplateUtils.getSingleInstance().getRestTemplate().postForEntity(url, entity, String.class);
		} catch (Exception e) {
			logger.error("Post failed, url: {}, error: {}",url, e.getMessage());
			return null;
		}
		return responseStr;
	}

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static String doPost(String url, String body){
		String responseStr = null;
		try {
			responseStr =  RestTemplateUtils.getSingleInstance().getRestTemplate().postForObject(url, body, String.class);
		}catch(RestClientException e) {
			logger.error("POST failed, url: {}, body: {}, error: {}", url, body, e.getMessage());
            System.out.println("POST failed, url: {}, body: {}, error: {}"+ "----------"+url+"----------"+ body+"----------"+e.getMessage());
			return null;
		}
		return responseStr;
	}

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static ResponseEntity<String> doPostForEntity(String url, String body){
		ResponseEntity<String> responseEntity = null;
		try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<String>(body, headers);
			responseEntity =  RestTemplateUtils.getSingleInstance().getRestTemplate().postForEntity(url,entity,String.class);
		}catch(RestClientException e) {
			logger.error("POST failed, url: {}, body: {}, error: {}", url, body, e.getMessage());
			return null;
		}
		return responseEntity;
	}

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static String doPost(String url){
		return doPost(url, null);
	}

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static boolean doPut(String url, String body){
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(body, headers);
			RestTemplateUtils.getSingleInstance().getRestTemplate().put(url,entity);
		}catch(RestClientException e){
			logger.error("PUT failed, url: {}, body: {}", url, body);
			return false;
		}
		return true;
	}

	/**
	 * @Brief:
	 * @Param:
	 * @Return:
	 */
	public static boolean doDelete(String url, Map<String, String> formParams){
		try{
			if (formParams != null && formParams.size() != 0) {
				 RestTemplateUtils.getSingleInstance().getRestTemplate().delete(url, formParams);
			}
		}catch(RestClientException e){
			logger.error("PUT failed, url: {}", url );
			return false;
		}
		return true;
	}

    /**
     * delete请求
     * @param url
     * @param formParams
     * @return
     */
	public static String doDeleteForEntity(String url,Map<String, String> formParams){
        ResponseEntity<String> responseEntity;
        if (formParams != null && formParams.size() != 0) {
            responseEntity = RestTemplateUtils.getSingleInstance().getRestTemplate().exchange(url, HttpMethod.DELETE,null,String.class,formParams);
        } else {
            responseEntity = RestTemplateUtils.getSingleInstance().getRestTemplate().exchange(url, HttpMethod.DELETE,null,String.class);
        }
        return responseEntity.getBody();
    }
}
