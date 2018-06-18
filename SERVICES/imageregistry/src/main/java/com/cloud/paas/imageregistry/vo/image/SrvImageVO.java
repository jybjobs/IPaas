package com.cloud.paas.imageregistry.vo.image;

/**
 * Created by 17798 on 2018/3/30.
 */
public class SrvImageVO {

    private String srvNameEn;

    private String srvNameZh;

    private String srvVersion;

    private Integer busiPkgVersionId;

    private Integer srvImageVersionId;

    private Integer srvImageId;

    private Integer srvVersionId;

    public String getSrvNameEn() {
        return srvNameEn;
    }

    public void setSrvNameEn(String srvNameEn) {
        this.srvNameEn = srvNameEn;
    }

    public String getSrvNameZh() {
        return srvNameZh;
    }

    public void setSrvNameZh(String srvNameZh) {
        this.srvNameZh = srvNameZh;
    }

    public String getSrvVersion() {
        return srvVersion;
    }

    public void setSrvVersion(String srvVersion) {
        this.srvVersion = srvVersion;
    }

    public Integer getBusiPkgVersionId() {
        return busiPkgVersionId;
    }

    public void setBusiPkgVersionId(Integer busiPkgVersionId) {
        this.busiPkgVersionId = busiPkgVersionId;
    }

    public Integer getSrvImageVersionId() {
        return srvImageVersionId;
    }

    public void setSrvImageVersionId(Integer srvImageVersionId) {
        this.srvImageVersionId = srvImageVersionId;
    }

    public Integer getSrvImageId() {
        return srvImageId;
    }

    public void setSrvImageId(Integer srvImageId) {
        this.srvImageId = srvImageId;
    }

    public Integer getSrvVersionId() {
        return srvVersionId;
    }

    public void setSrvVersionId(Integer srvVersionId) {
        this.srvVersionId = srvVersionId;
    }

}
