package com.cloud.paas.imageregistry.dao;

import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;

import java.util.List;

/**
 * @Author: wyj
 * @Description: 镜像信息详情DAO层接口
 * @Date: Create in 11:02 2017/11/22
 * @Modified by:
 */
public interface ImageDetailDAO extends BaseDAO<ImageDetail> {

    /**
     * 获取全量镜像列表
     * @return 镜像列表集合
     */
    List<ImageDetail> listAllImages();

    /**
     * 根据条件进行镜像列表查询
     * @param imageDetail 镜像详细信息（包括镜像名称和镜像类型）
     * @return
     */
    List<ImageDetail> listAllImagesByCondition(ImageDetail imageDetail);

    /**
     * 获取镜像仓库下的镜像列表
     * @param registryId 仓库id
     * @return
     */
    List<ImageListVO> listImageByRegistryId(int registryId);

    ImageDetail findImageNameZh(String imageNameZh);
    ImageDetail findImageNameEn(String imageNameEn);
}
