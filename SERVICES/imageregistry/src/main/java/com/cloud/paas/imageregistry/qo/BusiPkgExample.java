package com.cloud.paas.imageregistry.qo;

import com.cloud.paas.imageregistry.model.ValueObject;

import java.util.List;

/**
 * Created by CSS on 2017/11/29.
 */
public class BusiPkgExample extends ValueObject {
    /**
     * 业务包id
     */
    private Integer busiPkgId;
    /**
     * 业务包名称
     */
    private String busiPkgNameZh;
    /**
     * 业务包英文名
     */
    private String busiPkgNameEn;
    /**
     * 业务包类型
     */
    private Integer busiPkgType;
    /**
     * 业务包版本
     */
    private String busiPkgVersion;
    /**
     * 业务包状态
     */
    private Integer busiPkgStatus;
    /**
     * 业务包阶段
     */
    private Integer busiPkgStage;
    /**
     * 创建者
     */
    private String creator;
    /**
     *分页需要的IdList
     */
    private List<Integer> idList;

    /**
     * 多选框 名称 版本
     */
    private String condition;

    public Integer getBusiPkgId() {
        return busiPkgId;
    }

    public void setBusiPkgId(Integer busiPkgId) {
        this.busiPkgId = busiPkgId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }



    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }



    public String getBusiPkgNameZh() {
        return busiPkgNameZh;
    }

    public void setBusiPkgNameZh(String busiPkgNameZh) {
        this.busiPkgNameZh = busiPkgNameZh;
    }

    public String getBusiPkgNameEn() {
        return busiPkgNameEn;
    }

    public void setBusiPkgNameEn(String busiPkgNameEn) {
        this.busiPkgNameEn = busiPkgNameEn;
    }

    public Integer getBusiPkgType() {
        return busiPkgType;
    }

    public void setBusiPkgType(Integer busiPkgType) {
        this.busiPkgType = busiPkgType;
    }

    public String getBusiPkgVersion() {
        return busiPkgVersion;
    }

    public void setBusiPkgVersion(String busiPkgVersion) {
        this.busiPkgVersion = busiPkgVersion;
    }

    public Integer getBusiPkgStatus() {
        return busiPkgStatus;
    }

    public void setBusiPkgStatus(Integer busiPkgStatus) {
        this.busiPkgStatus = busiPkgStatus;
    }

    public Integer getBusiPkgStage() {
        return busiPkgStage;
    }

    public void setBusiPkgStage(Integer busiPkgStage) {
        this.busiPkgStage = busiPkgStage;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
