package com.cloud.paas.appservice.util.yaml.common;

/**
 * @Author: srf
 * @desc: Initializers对象
 * @Date: Created in 2018-03-29 10-02
 * @Modified By:
 */
public class Initializers {
    /**

     * 初始化队列
     */
    private String[] array;
    /**
     * 状态
     */
    private Status result;

    public String[] getArray() {
        return array;
    }

    public void setArray(String[] array) {
        this.array = array;
    }

    public Status getResult() {
        return result;
    }

    public void setResult(Status result) {
        this.result = result;
    }
}
