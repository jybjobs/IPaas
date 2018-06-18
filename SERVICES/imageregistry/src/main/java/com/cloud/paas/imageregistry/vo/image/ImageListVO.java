package com.cloud.paas.imageregistry.vo.image;

import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;

import java.util.List;

/**
 * @Author: wyj
 * @desc: 镜像列表详细信息实体类，包含镜像信息和对应的版本信息
 * @Date: Created in 2017-11-23 19:44
 * @Modified By:
 */
public class ImageListVO extends ImageDetail {

    /**
     * 镜像版本信息集合
     */
    private List<ImageVersionVO> imageVersionVO;
    /**
     * 镜像版本信息集合
     */
    private List<ImageVersion> imageVersions;

    /**
     * 仓库id
     */

    public List<ImageVersionVO> getImageVersionVO() {
        return imageVersionVO;
    }

    public void setImageVersionVO(List<ImageVersionVO> imageVersionVO) {
        this.imageVersionVO = imageVersionVO;
    }

    private int registryId;

    public List<ImageVersion> getImageVersions() {
        return imageVersions;
    }

    public void setImageVersions(List<ImageVersion> imageVersions) {
        this.imageVersions = imageVersions;
    }

    public int getRegistryId() {
        return registryId;
    }

    public void setRegistryId(int registryId) {
        this.registryId = registryId;
    }
}
