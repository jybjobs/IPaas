package com.cloud.paas.imageregistry.vo.busipkg;

import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.model.BusiPkgDetail;

import javax.validation.Valid;

/**
 * @Author: css
 * @desc: 镜像列表详细信息实体类，包含镜像信息和对应的版本信息
 * @Date: Created in 2017-11-23 19:44
 * @Modified By:
 */
public class BusiPkgVersionDetailVO extends BusiPkgVersionDetail {

    @Valid
    private BusiPkgDetail busiPkgDetail;

    public BusiPkgDetail getBusiPkgDetail() {
        return busiPkgDetail;
    }
    public void setBusiPkgDetail(BusiPkgDetail busiPkgDetail) {
        this.busiPkgDetail = busiPkgDetail;
    }
}