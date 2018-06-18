package com.cloud.paas.configuration;

import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesConfUtil {

    private static final String CONFIGURATION_ENCODING = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(PropertiesConfUtil.class);
    private static String separator = System.getProperty("file.separator");
    private static String filePath = separator + "resources" + separator + "common-conf.properties";
    private static PropertiesConfUtil pcUtil;
    private static PropertiesConfiguration propertiesConfiguration;


    private PropertiesConfUtil() {
    }

    /**
     * @return
     * @throws BusinessException
     */
    public synchronized static PropertiesConfUtil getInstance() throws BusinessException {
        try {
            if (pcUtil == null) {
                propertiesConfiguration = new PropertiesConfiguration();
                propertiesConfiguration.setEncoding(CONFIGURATION_ENCODING);
                String userDir = System.getProperty("user.dir");
                logger.info("*************userdir: " + userDir);
                propertiesConfiguration.setFileName(filePath);
                propertiesConfiguration.setDelimiterParsingDisabled(true);
                propertiesConfiguration.load();
                pcUtil = new PropertiesConfUtil();
                logger.info("init configuration successfully.");
            }
        } catch (Exception e) {
            logger.error("init configuration failed.", e);
            throw new BusinessException(new Result(0, "", "初始化配置失败！", 1, 1, null));
        }
        return pcUtil;
    }

    /**
     * 根据key获取对应的值
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String result = "";
        if (key != null && !key.equals("")) {
            logger.info("测试getProperty:{}",key);
            Object obj=null;
            try {
                 obj = propertiesConfiguration.getProperty(key);
            }catch (Exception e) {
                logger.error("测试getProperty fail:{}",e.getMessage());
            }
            if (obj != null) {
                //将 obj 对象转换成字符串
                result = String.valueOf(obj);
            }
        }
        return result.trim();
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLongValue(String key, long defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }
}

