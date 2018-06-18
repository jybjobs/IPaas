package com.cloud.paas.taskmanager.service;

import com.cloud.paas.taskmanager.entity.jenkins.config.Config;
import com.cloud.paas.util.result.Result;

/**
 * @Author: srf
 * @desc: JenkinsService接口
 * @Date: Created in 2018-04-20 16:46
 * @Modified By:
 */
public interface JenkinsService {
    /**
     * 创建Job
     */
    public Result createJob(int tenantid, int srvid, Config config);
    /**
     * 更新SVNJob
     */
    public Result updateJob(int tenantid, int srvid, int jobid, Config config);
    /**
     * 删除Job
     */
    public Result deleteJob(int tenantid, int srvid, int jobid);
    /**
     * 根据租户名和服务id查询JobId列表
     */
    public Result getJobList(int tenantid, int srvid);
    /**
     * 根据项目ID查询构建列表
     */
    public Result getBuildList(int tenantid, int srvid, int jobid);
    /**
     * 构建Job
     */
    public Result buildJob(int tenantid, int srvid, int jobid);
    /**
     * 终止构建Job
     */
    public Result stopBuildingJob(int tenantid, int srvid, int jobid, int buildnum);
    /**
     * 获取镜像名称
     */
    public Result getImageTag(int tenantid, int srvid, int jobid, int buildnum);
    /**
     * 获取构建镜像所需的参数
     */
    public Result getImageBuildParams(int tenantid, int srvid, int jobid);
    /**
     * 获取Task配置
     */
    public Result getJobTaskConfig (int jobId);
}
