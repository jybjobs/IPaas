package com.cloud.paas.imageregistry.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
/**
 * @Author: yht
 * @Description: 镜像版本实体类
 * @Date: Create in 11:19 2017/11/24
 * @Modified by:
 */
public class ImageVersion extends ValueObject{
    /**
     * 镜像版本编号
     */
    private Integer imageVersionId;
    /**
     * 镜像编号
     */
    @NotNull(message = "{image.imageid.notblank}",groups = ImageVersionAddValidate.class)
    private Long imageId;
    /**
     * 镜像版本
     */
    @NotBlank(message = "image.imageversion.notblank",groups = ImageVersionAddValidate.class)
    private String imageVersion;
    /**
     * 镜像状态
     */
    private Integer imageStatus;
    /**
     * 镜像阶段
     */
    private Integer imageStage;
    /**
     * 镜像版本描述
     */
    private String imageVersionRemark;
    /**
     * 镜像权限
     */
    private Integer imageAuth;
    /**
     * 镜像版本大小
     */
    private Float imageVersionSize;
    /**
     *镜像文件上传方式
     */
    private Integer imageUploadWay;
    /**
     *镜像文件上传名字
     */
    @NotBlank(message = "{image.imageuploadname.notblank}",groups = ImageVersionLocalAddValidate.class)
    private String imageUploadName;
    /**
     * 镜像文件存放路径
     */
    @NotBlank(message = "{image.imagepath.notblank}",groups = ImageVersionLocalAddValidate.class)
    private String imagePath;
    /**
     * dockfile编号
     */
    private Integer dockfileId;
    /**
     * 业务包编号
     */
    private Integer busiPkgVersionId;
    /**
     * 镜像来源
     */
    private String imageSource;
    /**
     * 镜像来源仓库
     */
    private String imageSourceRegistry;
    /**
     * 主机地址
     */
    private String hostIp;
    /**
     * 端口
     */
    private Integer port;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String passwd;

    /**
     * 镜像当前所在仓库
     */
    @NotNull(message = "{image.registryid.notblank}",groups = ImageVersionAddValidate.class)
    private Integer registryId;
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

    //校验分组
    public interface ImageVersionAddValidate{}
    public interface ImageVersionLocalAddValidate{}

    public Integer getImageVersionId() {
        return imageVersionId;
    }
    public void setImageVersionId(Integer imageVersionId) {
        this.imageVersionId = imageVersionId;
    }
    public Long getImageId() {
        return imageId;
    }
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    public String getImageVersion() {
        return imageVersion;
    }
    public void setImageVersion(String imageVersion) {
        this.imageVersion = imageVersion == null ? null : imageVersion.trim();
    }
    public Integer getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(Integer imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Integer getImageStage() {
        return imageStage;
    }

    public void setImageStage(Integer imageStage) {
        this.imageStage = imageStage;
    }

    public Integer getImageAuth() {
        return imageAuth;
    }

    public void setImageAuth(Integer imageAuth) {
        this.imageAuth = imageAuth;
    }

    public Integer getImageUploadWay() {
        return imageUploadWay;
    }

    public void setImageUploadWay(Integer imageUploadWay) {
        this.imageUploadWay = imageUploadWay;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public Integer getDockfileId() {
        return dockfileId;
    }

    public void setDockfileId(Integer dockfileId) {
        this.dockfileId = dockfileId;
    }

    public Integer getBusiPkgVersionId() {
        return busiPkgVersionId;
    }

    public void setBusiPkgVersionId(Integer busiPkgVersionId) {
        this.busiPkgVersionId = busiPkgVersionId;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource == null ? null : imageSource.trim();
    }

    public String getImageSourceRegistry() {
        return imageSourceRegistry;
    }

    public void setImageSourceRegistry(String imageSourceRegistry) {
        this.imageSourceRegistry = imageSourceRegistry == null ? null : imageSourceRegistry.trim();
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp == null ? null : hostIp.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
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

    public String getImageVersionRemark() {
        return imageVersionRemark;
    }

    public void setImageVersionRemark(String imageVersionRemark) {
        this.imageVersionRemark = imageVersionRemark;
    }

    public Float getImageVersionSize() {
        return imageVersionSize;
    }

    public void setImageVersionSize(Float imageVersionSize) {
        this.imageVersionSize = imageVersionSize;
    }

    public String getImageUploadName() {
        return imageUploadName;
    }

    public void setImageUploadName(String imageUploadName) {
        this.imageUploadName = imageUploadName;
    }
    public Integer getRegistryId() {
        return registryId;
    }

    public void setRegistryId(Integer registryId) {
        this.registryId = registryId;
    }
}