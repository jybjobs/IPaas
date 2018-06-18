package com.cloud.paas.taskmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.paas.taskmanager.dao.TaskConfigDAO;
import com.cloud.paas.taskmanager.entity.ImageBuildParams;
import com.cloud.paas.taskmanager.entity.SrvDetailVO;
import com.cloud.paas.taskmanager.entity.jenkins.config.Config;
import com.cloud.paas.taskmanager.model.SysConfig;
import com.cloud.paas.taskmanager.model.TaskConfig;
import com.cloud.paas.taskmanager.service.JenkinsService;
import com.cloud.paas.taskmanager.util.JenkinsUtil;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: srf
 * @desc: JenkinsServiceImpl对象
 * @Date: Created in 2018-04-20 16:47
 * @Modified By:
 */
@Service("JenkinsService")
public class JenkinsServiceImpl implements JenkinsService {

    private static final Logger logger = LoggerFactory.getLogger(JenkinsServiceImpl.class);

    @Autowired
    private TaskConfigDAO taskConfigDAO;

    /**
     * 创建Job
     */
    public Result createJob(int tenantid, int srvid, Config config) {
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null) {
            return CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_FAILURE");
        }
        //2.将配置存入数据库
        int resultInt = insertTaskConfig(tenantid, srvid, config);
        //3.修改command
        String command = config.getCommand();
        command = command.replace("${JOB_ID}", String.valueOf(resultInt));
        config.setCommand(command);
        //4.在JenKins中创建Job
        if (resultInt != 0) {
            Result result = JenkinsUtil.getInstance().createJob(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                    tenantid + srvDetailVO.getSrvNameEn() + resultInt,
                    srvDetailVO.getUserName(),
                    srvDetailVO.getPassword(),
                    config);
            if (result.getSuccess() == 0) taskConfigDAO.doDeleteById(resultInt);
            return result;
        } else
            return CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_FAILURE");
    }

    /**
     * @param tenantid 租户名
     * @param srvId      服务ID
     * @param config     配置
     * @Description 向数据库插入配置
     * @result 成功则返回jobId，失败则返回1
     */
    private int insertTaskConfig(int tenantid, int srvId, Config config) {
        int resultInt;
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.setSrvId(srvId);
        taskConfig.setTaskConfigJson(JSON.toJSONString(config));
        taskConfig.setCreateTime(new Date());
        taskConfig.setCreator(String.valueOf(tenantid));
        resultInt = taskConfigDAO.doInsertByBean(taskConfig);
        if (resultInt == 0)
            return 0;
        else
            return taskConfig.getTaskConfigId();
    }

    /**
     * 更新Job
     */
    public Result updateJob(int tenantid, int srvid, int jobid, Config config) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_UPDATE_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.更新数据库
        boolean updateResult = updateTaskConfig(jobid, tenantid, srvid, config);
        if (!updateResult)
            return result;
        String command = config.getCommand();
        command = command.replace("${JOB_ID}", String.valueOf(jobid));
        config.setCommand(command);
        return JenkinsUtil.getInstance().updateJob(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                tenantid + srvDetailVO.getSrvNameEn() + jobid,
                srvDetailVO.getUserName(),
                srvDetailVO.getPassword(),
                config);
    }

    /**
     * @param tenantid 租户名
     *  @param jobId 项目ID
     * @param srvId      服务ID
     * @param config     配置
     * @Description 更新数据库
     * @result 更新成功与否
     */
    private boolean updateTaskConfig(int jobId, int tenantid, int srvId, Config config) {
        int resultInt;
        TaskConfig taskConfig = getTaskConfig(jobId);
        taskConfig.setSrvId(srvId);
        taskConfig.setCreator(String.valueOf(tenantid));
        taskConfig.setTaskConfigJson(JSON.toJSONString(config));
        taskConfig.setUpdateTime(new Date());
        resultInt = taskConfigDAO.doUpdateByBean(taskConfig);
        return resultInt == 1;
    }

    /**
     * 删除Job
     */
    public Result deleteJob(int tenantid, int srvid, int jobid) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_DELETE_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.从数据库中删除数据
        boolean deleteResult = deleteTaskConfig(jobid);
        if (!deleteResult) return result;
        return JenkinsUtil.getInstance().deleteJob(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                tenantid + srvDetailVO.getSrvNameEn() + jobid);
    }
    /**
     * @Description 从数据库中删除项目
     * @param jobId 项目ID
     * @result 删除成功与否
     */
    private boolean deleteTaskConfig(int jobId) {
        int resultInt;
        resultInt = taskConfigDAO.doDeleteById(jobId);
        return resultInt == 1;
    }

    /**
     * 查询Job列表
     */
    public Result getJobList(int tenantid, int srvid) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOBS_QUERY_SUCCESS");
        try {
            List<Integer> list = taskConfigDAO.doFindAll(String.valueOf(tenantid), srvid);
            result.setData(list);
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOBS_QUERY_FAILURE");
            throw new BusinessException(result);
        }
        return result;
    }
    /**
     * 根据项目ID查询构建列表
     */
    public Result getBuildList(int tenantid, int srvid, int jobid) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_BUILDLIST_QUERY_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.查询数据库信息
        TaskConfig taskConfig = getTaskConfig(jobid);
        if (taskConfig == null)
            return result;
        //3.返回构建列表
        String jsonStr = taskConfig.getTaskConfigJson();
        try {
            Config config = JSON.parseObject(jsonStr, Config.class);
            result = JenkinsUtil.getInstance().getBuildList(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                    tenantid + srvDetailVO.getSrvNameEn() + jobid, config.getDockerImageName());
            return result;
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_BUILDLIST_QUERY_FAILURE");
            return result;
        }
    }
    /**
     * 构建Job
     */
    public Result buildJob(int tenantid, int srvid, int jobid){
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_CREATE_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.构建Job
        return JenkinsUtil.getInstance().buildJobCreate(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                tenantid + srvDetailVO.getSrvNameEn() + jobid);
    }
    /**
     * 终止构建Job
     */
    public Result stopBuildingJob(int tenantid, int srvid, int jobid, int buildnum) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_ABORT_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.终止构建Job
        return JenkinsUtil.getInstance().stopBuildingJob(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                tenantid + srvDetailVO.getSrvNameEn() + jobid, buildnum);
    }
    /**
     * 获取服务信息
     */
    private SrvDetailVO getSrvDetail(int srvid) {
        Result result;
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_SRV_BY_SRVID);
        url = url.replace("{srvid}", String.valueOf(srvid));
        String response = RestClient.doGet(url);
        try {
            if (response == null)
                return null;
            else {
                result = JSONObject.parseObject(response, Result.class);
                JSONObject jsonObject = (JSONObject) result.getData();
                return JSONObject.toJavaObject(jsonObject, SrvDetailVO.class);
            }
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_FAILURE");
            throw new BusinessException(result);
        }
    }
    /**
     * 获取镜像名称
     */
    public Result getImageTag(int tenantid, int srvid, int jobid, int buildnum) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.查询数据库信息
        TaskConfig taskConfig = getTaskConfig(jobid);
        if (taskConfig == null)
            return result;
        //3.构建Job
        try {
            String jsonStr = taskConfig.getTaskConfigJson();
            Config config = JSON.parseObject(jsonStr, Config.class);
            return JenkinsUtil.getInstance().getImageTag(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                    tenantid + srvDetailVO.getSrvNameEn() + jobid,
                    buildnum,
                    config.getDockerImageName());
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
            return result;
        }
    }

    /**
     * 获取配置
     */
    private TaskConfig getTaskConfig(int taskConfigId) {
        TaskConfig taskConfig = taskConfigDAO.doFindById(taskConfigId);
        return taskConfig;
    }
    /**
     * 获取构建镜像所需的参数
     */
    public Result getImageBuildParams(int tenantid, int srvid, int jobid) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
        //1.获取Service信息
        SrvDetailVO srvDetailVO = getSrvDetail(srvid);
        if (srvDetailVO == null)
            return result;
        //2.查询数据库信息
        TaskConfig taskConfig = getTaskConfig(jobid);
        if (taskConfig == null)
            return result;
        //3.获取参数
        ImageBuildParams imageBuildParams = new ImageBuildParams();
        imageBuildParams.setUserId(1);
        imageBuildParams.setSrvId(taskConfig.getSrvId());
        String buildPath = srvDetailVO.getProjectBuildPath();
        if (buildPath == null) {
            buildPath = "";
        }
        imageBuildParams.setProjectCompress("/" + tenantid + "_" + srvDetailVO.getSrvNameEn() + "/" + tenantid + srvDetailVO.getSrvNameEn() + jobid + "/" + buildPath);
        try {
            String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.QUERY_CONFIG_BY_ENNAME);
            String response = RestClient.doGet(url + "JenkinsWorkspacePath");
            Result responseResult = JSONObject.parseObject(response, Result.class);
            logger.debug("responseResult data：" + responseResult.getData().toString());
            SysConfig sysConfig = JSONObject.parseObject(responseResult.getData().toString(), SysConfig.class);
            logger.debug("sysConfig");
            imageBuildParams.setWorkspacePath(sysConfig.getSysConfigValue());
            String jsonStr = taskConfig.getTaskConfigJson();
            Config config = JSONObject.parseObject(jsonStr, Config.class);
            logger.debug("config");
            result = JenkinsUtil.getInstance().getLastBuildDate(tenantid + "_" + srvDetailVO.getSrvNameEn(),
                    tenantid + srvDetailVO.getSrvNameEn() + jobid);
            if (result.getSuccess() == 0) return result;
            imageBuildParams.setVersion(result.getData().toString());
            imageBuildParams.setImageUrl(config.getImageUrl() + tenantid + "-" + srvDetailVO.getSrvNameEn() + ":" + imageBuildParams.getVersion());
            result.setData(imageBuildParams);
            return result;
        } catch (BusinessException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
            return result;
        }
    }
    /**
     * 获取Task配置
     */
    public Result getJobTaskConfig (int jobId) {
        Result result = CodeStatusUtil.resultByCodeEn("QUERY_TASKCONFIG_SUCCESS");
        TaskConfig taskConfig = getTaskConfig(jobId);
        if (taskConfig != null) {
            String jsonStr = taskConfig.getTaskConfigJson();
            try {
                Config config = JSON.parseObject(jsonStr, Config.class);
                result.setData(config);
                return result;
            } catch (Exception e) {
                result = CodeStatusUtil.resultByCodeEn("QUERY_TASKCONFIG_FAILURE");
                return result;
            }
        } else {
            result = CodeStatusUtil.resultByCodeEn("QUERY_TASKCONFIG_FAILURE");
            return result;
        }
    }
}
