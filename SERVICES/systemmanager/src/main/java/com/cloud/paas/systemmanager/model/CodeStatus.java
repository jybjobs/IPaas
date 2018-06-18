package com.cloud.paas.systemmanager.model;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: wyj
 * @desc: 状态码
 * @Date: Created in 2017-12-26 18:44
 * @Modified By:hzy
 */
public class CodeStatus extends ValueObject {

    /**
     * id
     */
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 状态码

     */
    @NotBlank(message = "状态码不能为空")
    private Integer code;

    /**
     * 状态码描述
     */
    @NotBlank(message = "状态码描述不能为空")
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

    @NotBlank(message = "状态简码不能为空")
    private Integer processCode;
    @NotBlank(message = "缩略信息不能为空")
    private String processZh;

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
