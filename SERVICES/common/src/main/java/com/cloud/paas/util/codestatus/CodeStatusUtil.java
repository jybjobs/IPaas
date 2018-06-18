package com.cloud.paas.util.codestatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-27 10:25
 * @Modified By:
 */
public class CodeStatusUtil {

    private static final Logger logger = LoggerFactory.getLogger(CodeStatusUtil.class);

    /**
     * 静态变量map
     */
    public static Map<Integer, CodeStatus> statusCodeMap;

    /**
     * 静态变量map
     */
    public static Map<String, CodeStatus> statusCodeEnMap;

    private static CodeStatusUtil codeStatusUtil;


    private CodeStatusUtil() {
    }


    public synchronized static CodeStatusUtil getInstance() throws BusinessException{
        if (codeStatusUtil == null) {
            codeStatusUtil = new CodeStatusUtil();
            // 获取所有验证码
            try {
                getAllStatusByCode();
                getAllStatusByCodeEn();
            } catch (Exception e) {
                logger.error("获取所有验证码失败:{}",e.getMessage());
                //throw new BusinessException(new Result(0,"500","获取所有验证码失败",0,0,null));
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_ALL_STATUS_FAILURE"));
            }
        }
        return codeStatusUtil;
    }

    /**
     * 通过调接口获取单个CodeStatus
     *
     * @param code
     * @return
     */
    private static CodeStatus getResultByCode(int code) {
        if (code != 0) {
            String result = null;
            try {
                result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_CODE_STATUS) + code);
                if (result != null) {
                    CodeStatus codeStatus = JSONObject.parseObject(result, CodeStatus.class);
                    if (codeStatus != null) {
                        return codeStatus;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("STATUS_QUERY_FAILURE"));
            }
        }
        return null;
    }

    /**
     * 通过调接口获取单个CodeStatus
     *
     * @param codeEn
     * @return
     */
    private static CodeStatus getResultByCodeEn(String codeEn) {
        if (codeEn != null) {
            String result = null;
            try {
                //获取配置文件的内容，得到接口的URL
                result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_CODE_EN_STATUS) + codeEn);
                if (result != null) {
                    CodeStatus codeStatus = JSONObject.parseObject(result, CodeStatus.class);
                    if (codeStatus != null) {
                        return codeStatus;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("STATUS_QUERY_FAILURE"));
            }
        }
        return null;
    }

    /**
     * 获取所有状态码
     *
     * @return 状态码集合
     */
    private static Map<Integer, CodeStatus> getAllStatusByCode() {
        try {
            statusCodeMap = new LinkedHashMap<Integer, CodeStatus>();
            //doGet(url)
            String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_CODE_ALL_STATUS));
            if (result != null) {
                //把Result对象序列化成带格式的JSON字符串
                statusCodeMap = JSONObject.parseObject(result, new TypeReference<Map<Integer, CodeStatus>>() {
                });
                if (statusCodeMap != null && statusCodeMap.size() != 0) {
                    return statusCodeMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_ALL_STATUS_FAILURE"));
        }

        return null;
    }

    /**
     * 获取所有状态码
     *
     * @return 状态码集合
     */
    private static Map<String, CodeStatus> getAllStatusByCodeEn() {
        try {
            statusCodeEnMap = new LinkedHashMap<String, CodeStatus>();
            String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_CODE_EN_ALL_STATUS));
            if (result != null) {
                statusCodeEnMap = JSONObject.parseObject(result, new TypeReference<Map<String, CodeStatus>>() {
                });
                if (statusCodeEnMap != null && statusCodeEnMap.size() != 0) {
                    return statusCodeEnMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_ALL_STATUS_FAILURE"));
        }

        return null;
    }

    /**
     * 通过code调内存获取获取状态码
     *
     * @param code 状态码
     * @return
     */
    public static CodeStatus getStatusByCode(int code) throws BusinessException {
        if (code == 0) {
            return null;
        }
        if (statusCodeMap != null && statusCodeMap.containsKey(code)) {
            return statusCodeMap.get(code);
        }
        return getResultByCode(code);
    }

    /**
     * 通过codeEn调内存获取状态码
     *
     * @param codeEn 英文描述
     * @return
     */
    public static CodeStatus getStatusByCodeEn(String codeEn) throws BusinessException{
        if (codeEn == null) {
            return null;
        }
        if (statusCodeEnMap != null && statusCodeEnMap.containsKey(codeEn)) {
            return statusCodeEnMap.get(codeEn);
        }
        return getResultByCodeEn(codeEn);
    }

    /**
     * 通过code获取Result对象
     *
     * @param code
     * @return
     * @throws Exception
     */
    public static Result resultByCode(int code) throws BusinessException {
        CodeStatus codeStatus = getStatusByCode(code);
        if (codeStatus == null){
            CodeStatus status = getStatusByCodeEn(CodeStatusContant.NO_CODE_STATUS);
            throw new BusinessException(bean2Bean(status));
        } else {
            return bean2Bean(codeStatus);
        }
    }

    /**
     * 通过codeEn获取Result对象
     *
     * @param codeEn
     * @return
     * @throws Exception
     */
    public static Result resultByCodeEn(String codeEn) throws BusinessException {
        CodeStatus codeStatus = getStatusByCodeEn(codeEn);
        if (codeStatus == null){
            CodeStatus status = getStatusByCodeEn(CodeStatusContant.NO_CODE_STATUS);
            throw new BusinessException(bean2Bean(status));
        } else {
            return bean2Bean(codeStatus);
        }
    }

    /**
     * codestatus转换成Result
     *
     * @param codeStatus
     * @return
     */
    private static Result bean2Bean(CodeStatus codeStatus) throws BusinessException{
        return new Result(codeStatus.getSuccess(), String.valueOf(codeStatus.getCode()), codeStatus.getMsg(), codeStatus.getLevel(), codeStatus.getProcessCode(), null);
    }
}
