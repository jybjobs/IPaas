package com.cloud.paas.imageregistry.dao;


import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.vo.image.ImageListVO;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;

import java.math.BigDecimal;
import java.util.List;
/**
 * @Author: yht
 * @Description: 镜像版本信息DAO层接口
 * @Date: Create in 11:02 2017/11/22
 * @Modified by:
 */
public interface ImageVersionMngDAO extends BaseDAO<ImageVersion>{
    /**
     * 根据imageId查出镜像版本的信息
     * @param imageId 版本id
     * @return 返回版本信息列表
     */
    List<ImageVersion> listVersionByImageId(int imageId);
    /**
     * 根据imageVersionVO查出镜像的信息
     * @param imageVersionVO
     * @return 返回版本信息VO类列表
     */
    List<ImageListVO> listImageDetail(ImageVersionVO imageVersionVO);

    List<ImageVersionVO> doFindSrvPage(ImageVersionVO imageVersionVO);

    /**
     * 根据imageversionId查出一条版本信息的详细内容
     * @param imageversionId 版本信息id
     * @return 返回一条版本信息的详细内容
     */
    ImageVersionVO doFindDetailByImageId(int imageversionId);
    /**
     * 查出版本信息的详细内容
     * @param
     * @return 返回版本信息的详细内容
     */
    List<ImageVersionVO> doFindDetail(ImageVersionVO imageVersionVO);

    /**
     * 根据仓库id获取镜像版本
     * @param registryId
     * @return
     */
    List<ImageVersion> listByRegistryId(Integer registryId);

    /**
     * 通过仓库的id,查询仓库中已经存在的镜像版本
     * @param registryId
     * @return
     */
    List<ImageVersionVO> listImageByRegistryId(Integer registryId);
    List<ImageDetail> findPageByIdList(ImageVersionVO imageVersionVO);
    int confirmByDelete(int imageId);
    BigDecimal sumImageSize(long imageId);
    int doInsertByBean(ImageVersion imageVersion);

    List<ImageVersion> getStatusByImageIds(List<Long> imageIds);
}