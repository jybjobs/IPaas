package com.cloud.paas.appservice.vo.srv;

import com.cloud.paas.appservice.model.*;

import java.util.*;

/**
 * Created by 17798 on 2018/4/3.
 */
public class SrvInstDetailVO extends SrvInstDetail {

    private List<SrvEnvRel> srvEnvRels;

    private SrvScaleRule srvScaleRule;

    private SrvDetail srvDetail;

    private SrvVersionDetail srvVersionDetail;

    private AppDetail appDetail;

    /**
     * 是否扩缩容 0为否 1为是
     */
    private Integer isScale;

    /**
     * 实例服务名称
     */
    private String srvNameEn;

    /**
     * 镜像
     */
    private String srvImage;

    /**
     * 服务暴露端口
     */
    private Integer srvPort;

    /**
     * 被切换的服务实例编号
     */
    private Integer currentSrvInstId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 部署的环境变量
     */
    private List<DeployEnv> deployEnvs;

    public List<SrvEnvRel> getSrvEnvRels() {
        return srvEnvRels;
    }

    public void setSrvEnvRels(List<SrvEnvRel> srvEnvRels) {
        this.srvEnvRels = srvEnvRels;
    }

    public SrvScaleRule getSrvScaleRule() {
        return srvScaleRule;
    }

    public void setSrvScaleRule(SrvScaleRule srvScaleRule) {
        this.srvScaleRule = srvScaleRule;
    }

    public SrvDetail getSrvDetail() {
        return srvDetail;
    }

    public void setSrvDetail(SrvDetail srvDetail) {
        this.srvDetail = srvDetail;
    }

    public SrvVersionDetail getSrvVersionDetail() {
        return srvVersionDetail;
    }

    public void setSrvVersionDetail(SrvVersionDetail srvVersionDetail) {
        this.srvVersionDetail = srvVersionDetail;
    }

    public AppDetail getAppDetail() {
        return appDetail;
    }

    public void setAppDetail(AppDetail appDetail) {
        this.appDetail = appDetail;
    }

    public Integer getIsScale() {
        return isScale;
    }

    public void setIsScale(Integer isScale) {
        this.isScale = isScale;
    }

    public String getSrvNameEn() {
        return srvNameEn;
    }

    public void setSrvNameEn(String srvNameEn) {
        this.srvNameEn = srvNameEn;
    }

    public String getSrvImage() {
        return srvImage;
    }

    public void setSrvImage(String srvImage) {
        this.srvImage = srvImage;
    }

    public Integer getSrvPort() {
        return srvPort;
    }

    public void setSrvPort(Integer srvPort) {
        this.srvPort = srvPort;
    }

    public Integer getCurrentSrvInstId() {
        return currentSrvInstId;
    }

    public void setCurrentSrvInstId(Integer currentSrvInstId) {
        this.currentSrvInstId = currentSrvInstId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DeployEnv> getDeployEnvs() {
        return deployEnvs;
    }

    public void setDeployEnvs(List<DeployEnv> deployEnvs) {
        this.deployEnvs = deployEnvs;
    }
}
