package com.cloud.paas.imageregistry.service.impl.image;

import com.cloud.paas.imageregistry.dao.ImageVersionMngDAO;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: zcy
 * @desc: 验证镜像版本
 * @Date: Created in 2018-01-26 14:11
 * @modified By:
 **/
@Service
public class VeryImageVersion {
    @Autowired
    private ImageVersionMngDAO imageVersionMngDAO;

    private static final Logger logger = LoggerFactory.getLogger(VeryImageVersion.class);
    /**
     * 本地上传验证
     * @param imageVersionVO
     * @return
     */
    public void addLocalImageVersion(ImageVersionVO imageVersionVO){
        //验证版本是否重复
        veryVersion(imageVersionVO);
    }

    /**
     * 验证版本是否重复
     * @param imageVersionVO
     */
    public void veryVersion(ImageVersionVO imageVersionVO){
        String version = imageVersionVO.getImageVersion ();
        List<ImageVersion> imageVersions = imageVersionMngDAO.listVersionByImageId (imageVersionVO.getImageId ().intValue ());
        if (imageVersions.size ()>0) {
            for (ImageVersion imageVersion : imageVersions) {
                if (version.equals (imageVersion.getImageVersion ())) {
                    logger.error("有重复镜像版本");
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_VERSION_REPEATE"));
                }
            }
        }
    }
}
