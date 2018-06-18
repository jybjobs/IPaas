package com.cloud.paas.appservice.util.yaml.pod;

/**
 * @Author: wyj
 * @desc: 容器的环境变量
 * @Date: Created in 2017-12-14 14:18
 * @Modified By:
 */
public class PodEnv {
    /**
     * 环境变量的名称
     */
    private String name;
    /**
     * 环境变量的值
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
