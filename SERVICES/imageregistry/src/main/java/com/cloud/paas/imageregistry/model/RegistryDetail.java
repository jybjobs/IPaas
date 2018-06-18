package com.cloud.paas.imageregistry.model;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: kaiwen
 * @Description:仓库详细类
 * @Date: Create  2017/11/24
 * @Modified by:
 */
public class RegistryDetail extends ValueObject{

    /**
     * 仓库编号
     */
    @NotNull(message = "{registrydetail.registryid.notnull}",groups = RegistryDetailUpdate.class)
    private Integer registryId;

    /**
     * 仓库名称
     */
    @NotBlank(message = "{registrydetail.registrynamezh.notblank}",groups = RegistryDetailAdd.class)
    private String registryNameZh;
    /**
     * 仓库英文名称
     */
    @NotBlank(message = "{registrydetail.registrynameen.notblank}",groups = RegistryDetailAdd.class)
    private String registryNameEn;
    /**
     * 仓库类型
     */
    @NotNull(message = "{registrydetail.registrytype.notnull}",groups = RegistryDetailAdd.class)
    private Integer registryType;
    /**
     * 是否主库
     */
    private Integer registryPrimaryStandb;
    /**
     * 仓库状态
     */
    private Long registryStatus;
    /**
     * 仓库权限
     */
    @NotNull(message = "{registrydetail.registryauth.notnull}",groups = RegistryDetailAdd.class)
    private Integer registryAuth;
    /**
     * 仓库IP
     */
    @NotBlank(message = "{registrydetail.registryip.notblank}",groups = RegistryDetailAdd.class)
    private String registryIp;
    /**
     * 仓库端口
     */
    @NotNull(message = "{registrydetail.registryport.notnull}",groups = RegistryDetailAdd.class)
    private Integer registryPort;
    /**
     * 仓库存储类型
     */
    @NotNull(message = "{registrydetail.registrystoragetype.notnull}",groups = RegistryDetailAdd.class)
    private Integer registryStorageType;
    /**
     * 仓库配额
     */
    private Integer registryQuota;
    /**
     * 仓库容器id
     */
    private String registryContainerId;
    /**
     *已使用空间
     */
    private BigDecimal imagesUseSize;

    /**
     * 支持主备
     */
//    @NotNull(message = "{registrydetail.supportprimarystandby.notnull}",groups = RegistryDetailAdd.class)
    private Integer supportPrimaryStandby;

    /**
     *关联仓库
     */
    private String relateRegistry;

    /**
     *支持鉴权
     */
    @NotNull(message = "{registrydetail.supportauthentication.notnull}",groups = RegistryDetailAdd.class)
    private Integer supportAuthentication;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passwd;

    /**
     *主机存储路径
     */
    private String hostStoragePath;

    /**
     * 主机存储空间
     */
    private Integer hostStorageSize;

    /**
     *分布式存储路径
     */
    private String distributedStoragePath;

    /**
     *分布式存储空间
     */
    private Integer distributedStorageSize;

    /**
     *文件系统类型
     */
    private Integer fileSystemType;

    /**
     * 描述
     */
    private String remark;

    /**
     *创建者
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
     * 添加验证
     */
    public interface RegistryDetailAdd{}
    public interface RegistryDetailUpdate{}

    /**
     * 一下为get和set方法
     */
    public Integer getRegistryId() {
        return registryId;
    }
    public void setRegistryId(Integer registryId) {
        this.registryId = registryId;
    }
    public String getRegistryNameZh() {
        return registryNameZh;
    }
    public void setRegistryNameZh(String registryNameZh) {
        this.registryNameZh = registryNameZh == null ? null : registryNameZh.trim();
    }
    public String getRegistryNameEn() {
        return registryNameEn;
    }

    public void setRegistryNameEn(String registryNameEn) {
        this.registryNameEn = registryNameEn == null ? null : registryNameEn.trim();
    }

    public Integer getRegistryType() {
        return registryType;
    }

    public void setRegistryType(Integer registryType) {
        this.registryType = registryType;
    }

    public Integer getRegistryPrimaryStandb() {
        return registryPrimaryStandb;
    }

    public void setRegistryPrimaryStandb(Integer registryPrimaryStandb) {
        this.registryPrimaryStandb = registryPrimaryStandb;
    }
    public Long getRegistryStatus() {
        return registryStatus;
    }

    public void setRegistryStatus(Long registryStatus) {
        this.registryStatus = registryStatus;
    }

    public Integer getRegistryAuth() {
        return registryAuth;
    }

    public void setRegistryAuth(Integer registryAuth) {
        this.registryAuth = registryAuth;
    }

    public String getRegistryIp() {
        return registryIp;
    }

    public void setRegistryIp(String registryIp) {
        this.registryIp = registryIp == null ? null : registryIp.trim();
    }

    public Integer getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(Integer registryPort) {
        this.registryPort = registryPort;
    }

    public Integer getRegistryStorageType() {
        return registryStorageType;
    }

    public void setRegistryStorageType(Integer registryStorageType) {
        this.registryStorageType = registryStorageType;
    }

    public Integer getRegistryQuota() {
        return registryQuota;
    }

    public void setRegistryQuota(Integer registryQuota) {
        this.registryQuota = registryQuota;
    }

    public BigDecimal getImagesUseSize() {
        return imagesUseSize;
    }

    public void setImagesUseSize(BigDecimal imagesUseSize) {
        this.imagesUseSize = imagesUseSize;
    }

    public Integer getSupportPrimaryStandby() {
        return supportPrimaryStandby;
    }

    public void setSupportPrimaryStandby(Integer supportPrimaryStandby) {
        this.supportPrimaryStandby = supportPrimaryStandby;
    }

    public String getRelateRegistry() {
        return relateRegistry;
    }

    public void setRelateRegistry(String relateRegistry) {
        this.relateRegistry = relateRegistry == null ? null : relateRegistry.trim();
    }

    public Integer getSupportAuthentication() {
        return supportAuthentication;
    }

    public void setSupportAuthentication(Integer supportAuthentication) {
        this.supportAuthentication = supportAuthentication;
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

    public String getHostStoragePath() {
        return hostStoragePath;
    }

    public void setHostStoragePath(String hostStoragePath) {
        this.hostStoragePath = hostStoragePath == null ? null : hostStoragePath.trim();
    }

    public Integer getHostStorageSize() {
        return hostStorageSize;
    }

    public void setHostStorageSize(Integer hostStorageSize) {
        this.hostStorageSize = hostStorageSize;
    }

    public String getDistributedStoragePath() {
        return distributedStoragePath;
    }

    public void setDistributedStoragePath(String distributedStoragePath) {
        this.distributedStoragePath = distributedStoragePath == null ? null : distributedStoragePath.trim();
    }

    public Integer getDistributedStorageSize() {
        return distributedStorageSize;
    }

    public void setDistributedStorageSize(Integer distributedStorageSize) {
        this.distributedStorageSize = distributedStorageSize;
    }

    public Integer getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(Integer fileSystemType) {
        this.fileSystemType = fileSystemType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getRegistryContainerId() {
        return registryContainerId;
    }

    public void setRegistryContainerId(String registryContainerId) {
        this.registryContainerId = registryContainerId;
    }

    /**
     * 重写toString方法
     * @return
     */
    @Override
    public String toString() {
        return "RegistryDetail{" +
                "registryId=" + registryId +
                ", registryNameZh='" + registryNameZh + '\'' +
                ", registryNameEn='" + registryNameEn + '\'' +
                ", registryType=" + registryType +
                ", registryPrimaryStandb=" + registryPrimaryStandb +
                ", registryStatus=" + registryStatus +
                ", registryAuth=" + registryAuth +
                ", registryIp='" + registryIp + '\'' +
                ", registryPort=" + registryPort +
                ", registryStorageType=" + registryStorageType +
                ", registryQuota=" + registryQuota +
                ", imagesUseSize=" + imagesUseSize +
                ", supportPrimaryStandby=" + supportPrimaryStandby +
                ", relateRegistry='" + relateRegistry + '\'' +
                ", supportAuthentication=" + supportAuthentication +
                ", userName='" + userName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", hostStoragePath='" + hostStoragePath + '\'' +
                ", hostStorageSize=" + hostStorageSize +
                ", distributedStoragePath='" + distributedStoragePath + '\'' +
                ", distributedStorageSize=" + distributedStorageSize +
                ", fileSystemType=" + fileSystemType +
                ", remark='" + remark + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}