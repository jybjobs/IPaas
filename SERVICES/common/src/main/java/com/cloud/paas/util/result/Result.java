package com.cloud.paas.util.result;

public class Result {

    /**
     * 是否成功
     * 0失败 1成功
     */
    private Integer success;
    /**
     * 业务状态码
     * 编码规则：模块名 + 功能 + 编号
     * 模块名：2位
     * 功能：2位
     * 编号：4位
     */
    private String code;
    /**
     * 操作信息
     */
    private String message;
    /**
     * 业务级别
     */
    private Integer level;
    /**
     * 操作类型
     */
    private Integer type;
    /**
     * 数据
     */
    private Object data;

    public Result(Integer success, String code, String message, Integer level, Integer type, Object data) {
        this.setData(data);
        this.setSuccess(success);
        this.setCode(code);
        this.setMessage(message);
        this.setLevel(level);
        this.setType(type);
    }

    private Result(){}

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
