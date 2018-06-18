package com.cloud.paas.imageregistry.vo.registry;

import com.cloud.paas.imageregistry.model.RegistryDetail;

/**
 * @Author: kaiwen
 * @desc: RegistryDetailVO包含仓库信息
 * @Date: Created in 2017/11/29 18:09
 * @Modified By:孙开文
 */
public class RegistryDetailVO  extends RegistryDetail{

    private Integer count;
    private Integer imageSize;

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
