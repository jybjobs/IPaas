package com.cloud.paas.util.codestatus;

/**
 * @Author: wyj
 * @desc: 状态码状态
 * @Date: Created in 2017-12-26 20:55
 * @Modified By:
 */
public class CodeStatus {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码描述
     */
    private String codeEn;

    /**
     * messgae
     */
    private String msg;
    /**
     * 业务级别
     */
    private Integer level;
    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 是否成功
     * 0失败 1成功
     */
    private Integer success;

    private Integer processCode;

    private String processZh;

    public CodeStatus(Integer code, String codeEn, String msg, Integer level, Integer type, Integer success) {
        this.code = code;
        this.codeEn = codeEn;
        this.msg = msg;
        this.level = level;
        this.type = type;
        this.success = success;
    }

    public CodeStatus() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public void setCodeEn(String codeEn) {
        this.codeEn = codeEn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getProcessCode() {
        return processCode;
    }

    public void setProcessCode(Integer processCode) {
        this.processCode = processCode;
    }

    public String getProcessZh() {
        return processZh;
    }

    public void setProcessZh(String processZh) {
        this.processZh = processZh;
    }
}
