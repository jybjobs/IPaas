package com.cloud.paas.imageregistry.vo.busipkg;

import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;

import java.util.List;

/**
 * Created by CSS on 2017/11/30.
 */
public class BusiPkgListVO  extends BusiPkgDetail {
    /**
     * 业务版本信息集合
     */
    private List<BusiPkgVersionDetail>  listBusiPkgVersion;
    public List<BusiPkgVersionDetail> getListBusiPkgVersion() {
        return listBusiPkgVersion;
    }

    public void setListBusiPkgVersion(List<BusiPkgVersionDetail> listBusiPkgVersion) {
        this.listBusiPkgVersion = listBusiPkgVersion;
    }

}
