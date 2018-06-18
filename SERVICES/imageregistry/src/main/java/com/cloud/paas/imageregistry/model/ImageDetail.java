package com.cloud.paas.imageregistry.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wyj
 * @Description: 镜像详情实体类
 * @Date: Create in 11:19 2017/11/24
 * @Modified by:
 */
public class ImageDetail extends ValueObject {
    /**
     * 镜像编号
     */
    @NotNull(message = "{image.imageid.notblank}",groups = ImageUpdateValidate.class)
    private Long imageId;
    /**
     * 镜像名称
     */
    @NotBlank(message = "{image.imagenamezh.notblank}",groups = {ImageAddValidate.class,ImageUpdateValidate.class})
    private String imageNameZh;
    /**
     * 镜像英文名称
     */
    @NotBlank(message = "{image.imagenameen.notblank}",groups = {ImageAddValidate.class,ImageVersion.ImageVersionLocalAddValidate.class})
    private String imageNameEn;
    /**
     * 镜像描述
     */
    private String imageRemark;
    /**
     * 镜像类型
     */
    @NotNull(message = "{image.imagetype.notblank}",groups = ImageAddValidate.class)
    private Integer imageType;
    /**
     * 镜像大小
     */
    private BigDecimal imageSize;
    /**
     * 镜像图片路径
     */
    private String imagePicturePath;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 最后修改时间
     */
    @JSONField (format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 镜像校验分组
     */
    public interface ImageAddValidate{};
    public interface ImageUpdateValidate{};

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getImageNameZh() {
        return imageNameZh;
    }

    public void setImageNameZh(String imageNameZh) {
        this.imageNameZh = imageNameZh == null ? null : imageNameZh.trim();
    }

    public String getImageNameEn() {
        return imageNameEn;
    }

    public void setImageNameEn(String imageNameEn) {
        this.imageNameEn = imageNameEn == null ? null : imageNameEn.trim();
    }

    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark == null ? null : imageRemark.trim();
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public BigDecimal getImageSize() {
        return imageSize;
    }

    public void setImageSize(BigDecimal imageSize) {
        this.imageSize = imageSize;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImagePicturePath() {
        return imagePicturePath;
    }

    public void setImagePicturePath(String imagePicturePath) {
        this.imagePicturePath = imagePicturePath;
    }
}