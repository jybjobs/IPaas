package com.cloud.paas.imageregistry.service.impl.image;

import com.cloud.paas.imageregistry.model.DockerFileDetail;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.util.RegistryUtil;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.bean.BeanUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: zcy
 * @desc: 镜像版本异步方法
 * @Date: Created in 2018-01-30 19:44
 * @modified By:
 **/
@Service
public class ImageVersionAsyncService {
    private static final Logger logger = LoggerFactory.getLogger(ImageVersionAsyncService.class);
    @Autowired
    private ImageVersionServiceImpl imageVersionService;
    /**
     * 本地上传线程
     * @param userId
     * @param imageVersionVO
     */
    @Async
    public void addByLocalAsync(String userId, ImageVersionVO imageVersionVO, ImageVersion imageVersion) {
        Result result = CodeStatusUtil.resultByCodeEn("IMAGE_BUILD_FAILURE");
        ImageDetail imageDetail = imageVersionVO.getImageDetail();
        String path = Config.UPLOAD_DIRECTORY_IMAGE + imageVersion.getImagePath();
        //tar包推送到镜像仓库
        // 1.获取仓库的信息
        logger.info("获取仓库的信息");
        RegistryUtil registryUtil = new RegistryUtil();
        try {
            BeanUtil.copyBean2Bean(registryUtil, imageVersionService.getRegistryDetail(imageVersion.getRegistryId()));
        } catch (Exception e) {
            logger.error("copyBean2Bean(registryUtil, getRegistryDetail(imageVersion.getRegistryId()))失败:{}",e.getMessage());
            throw new BusinessException(result);
        }
        // 2.获取tag  直接使用加仓库ip的tag
        logger.info("获取镜像tag");
        String registryTag = registryUtil.getRegistryAddress() + imageVersionService.getRegistryTag(imageDetail.getImageNameEn(), imageVersion.getImageVersion());
        // 3.load tar包
        logger.info("loadtar包");
        imageVersionService.load(imageVersion,path,registryTag);
        // 4.tag、push 到镜像仓库
        logger.info("push到镜像仓库");
        imageVersion.setImageStatus(CodeStatusUtil.getStatusByCodeEn("IMAGE_LOCAL_UPLOADING").getCode());
        imageVersionService.updateImageVersion(imageVersion);
        imageVersionService.addLocalPushToRegistry(imageVersion, registryTag, registryUtil);
        //5.删除镜像
        logger.info("删除本地镜像");
        //imageVersionService.removeImage(imageVersion,tagLocal);
        imageVersionService.removeImage(imageVersion,registryTag);
        //6.数据库更新，结束
        imageVersionService.finishLocalAdd(path,imageVersion);
    }

    @Async
    public void buildAndPushToRegistryAsync(ImageVersion imageVersion,ImageVersionVO imageVersionVO){
        // 1.获取dockerfile详细信息
        logger.info("获取dockerfile详细信息");
        DockerFileDetail dockerFileDetail = imageVersionVO.getDockerFileDetail ();
        // 2.获取仓库的信息
        logger.debug("ImageId：" + imageVersionVO.getImageId ());
        RegistryUtil registryUtil = imageVersionService.getRegistryAddressInfo(imageVersion.getRegistryId ());
        String imageId;
        try {
            // 3.将dockerfile build成image 并返回imageId
            logger.info ("将dockerfile build成image 并返回imageId");
            Result result= imageVersionService.prepareToBuild(imageVersion, dockerFileDetail);
            imageId = result.getData ().toString ();
            // 4.更新镜像大小
            logger.info("更新镜像大小");
            imageVersionService.setImageSize(imageVersion,imageId);
        }catch (BusinessException e){
            imageVersion.setImageStatus (CodeStatusUtil.getStatusByCodeEn ("IMAGE_BUILD_FAILURE").getCode ());
            imageVersionService.updateImageVersion(imageVersion);
            throw e;
        }
        try {
            // 5.准备push镜像到仓库
            logger.info("准备push镜像到仓库");
            String Tag= imageVersionService.prepareToPush(registryUtil,imageVersion);
            // 6.push镜像到仓库
            logger.info ("推入仓库");
            String registryTag = registryUtil.getRegistryAddress() + Tag;
            imageVersionService.pushToRegistry(imageId, registryTag, registryUtil);
            // 7.删除本地镜像
            logger.info("删除本地镜像");
            imageVersionService.removeImage(imageVersion,registryTag);
            // 8.更新镜像状态 结束
            imageVersion.setImageStatus (CodeStatusUtil.getStatusByCodeEn ("IMAGE_UPLOADING_SUCCESS").getCode ());
            imageVersionService.updateImageVersion(imageVersion);
        }catch (BusinessException e){
            imageVersion.setImageStatus (CodeStatusUtil.getStatusByCodeEn ("IMAGE_UPLOADING_FAILURE").getCode ());
            imageVersionService.updateImageVersion(imageVersion);
            throw e;
        }

    }
}
