package com.cloud.paas.imageregistry.service.impl.image;

import com.cloud.paas.imageregistry.dao.ImageDetailDAO;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zcy
 * @desc: 验证镜像
 * @Date: Created in 2018-01-25 15:25
 * @modified By:
 **/
@Service
public class VerifyImage {
    @Autowired
    ImageDetailDAO imageDetailDAO;

    /**
     * 创建镜像验证
     * @param imageDetail
     * @return
     */
    public void createImageDetail(ImageDetail imageDetail){
        String imageNameEn=imageDetail.getImageNameEn();
        if (imageDetailDAO.findImageNameEn(imageNameEn)!=null){
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_ENNAME_REPEATE"));
        }
        String imageNameZh=imageDetail.getImageNameZh();
        if (imageDetailDAO.findImageNameZh(imageNameZh)!=null){
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_ZHNAME_REPEATE"));
        }
    }

    /**
     * 修改镜像验证
     * @param imageDetail
     * @return
     */
    public void updateImageDetail(ImageDetail imageDetail){
        Long imageId=imageDetail.getImageId();
        String imageNameZh=imageDetail.getImageNameZh();
        //如果中文名未修改则不检测是否重复
        if (!imageDetailDAO.doFindById(imageId.intValue()).getImageNameZh().equals(imageNameZh)){
            if (imageDetailDAO.findImageNameZh(imageNameZh)!=null){
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("IMAGE_ZHNAME_REPEATE"));
            }
        }
    }

}
