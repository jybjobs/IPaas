package com.cloud.paas.util.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.apache.zookeeper.server.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: lly
 * @desc:
 * @Date: Created in 2018-02-24 16:14
 * @Modified By:
 */
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    /**
     * 静态变量map
     */
    public static Map<String, String> configEnNameMap;
    private static ConfigUtil configUtil;

    private ConfigUtil() {
    }

    public synchronized static ConfigUtil getInstance() throws BusinessException {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
            // 获取所有配置项
            try {
                getAllConfigByEnName();
            } catch (Exception e) {
                logger.error("获取所有配置失败:{}", e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_ALL_STATUS_FAILURE"));
            }
        }
        return configUtil;
    }

    private static Map<String, String> getAllConfigByEnName() {
        try {
            configEnNameMap = new LinkedHashMap<String, String>();
            //需要修改
            String result = RestClient.doGet(PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_ALL_CONFIG_VALUE));
            if (result != null) {
                //把Result对象序列化成带格式的JSON字符串
                configEnNameMap = JSONObject.parseObject(result, new TypeReference<Map<String, String>>() {
                });
                if (configEnNameMap != null && configEnNameMap.size() != 0) {
                    return configEnNameMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //需要修改
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("GET_ALL_STATUS_FAILURE"));
        }

        return null;
    }
    public static String getStatusByCodeEn(String configEnName) throws BusinessException{
        if (configEnNameMap == null) {
            return null;
        }
        if (configEnNameMap != null && configEnNameMap.containsKey(configEnName)) {
            return configEnNameMap.get(configEnName);
        }
        return null;
    }
}
