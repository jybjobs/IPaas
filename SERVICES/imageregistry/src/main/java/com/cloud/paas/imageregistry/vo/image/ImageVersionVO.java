package com.cloud.paas.imageregistry.vo.image;

import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.imageregistry.model.DockerFileDetail;
import com.cloud.paas.imageregistry.model.ImageDetail;
import com.cloud.paas.imageregistry.model.ImageVersion;
import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.vo.dockerfile.DockerFileDetailVO;
import com.cloud.paas.imageregistry.model.ImageVersionRule;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: yht
 * @desc: 镜像版本实体类，包含镜像公共信息的内容
 * @Date: Created in 2017-11-23 19:44
 * @Modified By:
 */
public class ImageVersionVO extends ImageVersion {
    /**
     * dockerfileVO对象
     */
    private  String condition;
    /**
     * 业务包阶段
     */
    private Integer busiPkgStage;
    private DockerFileDetailVO dockerFileDetailVO;

    private DockerFileDetail dockerFileDetail;

    private JSONObject dockerfileJson;

    private JSONObject imageVersionRuleJson;

    private ImageVersionRule imageVersionRule;

    /**
     * 版本公共信息对象
     */
    @NotNull(message = "{image.imagedetail.notblank}",groups = ImageVersionLocalAddValidate.class)
    @Valid
    private ImageDetail imageDetail;

    /**
     * 仓库信息
     */
    private RegistryDetail registryDetail;

    public List<Integer> getImageVersionIdList() {
        return imageVersionIdList;
    }

    public void setImageVersionIdList(List<Integer> imageVersionIdList) {
        this.imageVersionIdList = imageVersionIdList;
    }

    private List<Integer> imageVersionIdList;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public DockerFileDetail getDockerFileDetail() {
        return dockerFileDetail;
    }

    public void setDockerFileDetail(DockerFileDetail dockerFileDetail) {
        this.dockerFileDetail = dockerFileDetail;
    }

    public JSONObject getDockerfileJson() {
        return dockerfileJson;
    }

    public void setDockerfileJson(JSONObject dockerfileJson) {
        this.dockerfileJson = dockerfileJson;
    }

    /**
     * 获取仓库信息
     */
    public RegistryDetail getRegistryDetail() {
        return registryDetail;
    }

    /**
     * 设置仓库信息
     * @param registryDetail
     */
    public void setRegistryDetail(RegistryDetail registryDetail) {
        this.registryDetail = registryDetail;
    }
    /**
     * 获取版本公共信息的内容
     *
     * @return
     */
    public ImageDetail getImageDetail() {
        return imageDetail;
    }
    /**
     * 设置版本公共信息内容
     *
     * @param imageDetail 版本的详细对象
     */
    public void setImageDetail(ImageDetail imageDetail) {
        this.imageDetail = imageDetail;
    }
    public DockerFileDetailVO getDockerFileDetailVO() {
        return dockerFileDetailVO;
    }

    public void setDockerFileDetailVO(DockerFileDetailVO dockerFileDetailVO) {
        this.dockerFileDetailVO = dockerFileDetailVO;
    }

    public Integer getBusiPkgStage() {
        return busiPkgStage;
    }

    public void setBusiPkgStage(Integer busiPkgStage) {
        this.busiPkgStage = busiPkgStage;
    }

    public JSONObject getImageVersionRuleJson() {
        return imageVersionRuleJson;
    }

    public void setImageVersionRuleJson(JSONObject imageVersionRuleJson) {
        this.imageVersionRuleJson = imageVersionRuleJson;
    }

    public ImageVersionRule getImageVersionRule() {
        return imageVersionRule;
    }

    public void setImageVersionRule(ImageVersionRule imageVersionRule) {
        this.imageVersionRule = imageVersionRule;
    }
}
