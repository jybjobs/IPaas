package com.cloud.paas.taskmanager.controller;

import com.cloud.paas.taskmanager.entity.jenkins.config.Config;
import com.cloud.paas.taskmanager.service.JenkinsService;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: srf
 * @desc: JenKins使用Controller
 * @Date: Created in 2018-04-19 17:27
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/jenkins")
@CrossOrigin
public class JenkinsController {
    @Autowired
    private JenkinsService jenkinsService;

//    // TODO del
//    @Autowired
//    private TaskConfigDAO taskConfigDAO;
//    /**TODO del
//     * 测试
//     */
//    @ApiOperation(value = "测试", notes = "测试")
//    @GetMapping(value = "/test")
//    public List<Integer> test() {
//        List<Integer> list = taskConfigDAO.doFindAll("docker.jsict.com", 1250);
//        return list;
//    }

    /**
     * 创建Job
     */
    @ApiOperation(value = "创建项目", notes = "创建项目")
    @PostMapping(value = "/{tenantid}/{srvid}/job")
    public Result createJob(@PathVariable int tenantid,
                               @PathVariable int srvid,
                               @RequestBody Config config) {
        return jenkinsService.createJob(tenantid, srvid, config);
    }

    /**
     * 更新Job
     */
    @ApiOperation(value = "更新项目", notes = "更新项目")
    @PutMapping(value = "/{tenantid}/{srvid}/job/{jobid}")
    public Result updateJob(@PathVariable int tenantid,
                               @PathVariable int srvid,
                            @PathVariable int jobid,
                               @RequestBody Config Config) {
        return jenkinsService.updateJob(tenantid, srvid, jobid, Config);
    }
    /**
     * 删除Job
     */
    @ApiOperation(value = "删除项目", notes = "删除项目")
    @DeleteMapping(value = "/{tenantid}/{srvid}/job/{jobid}")
    public Result deleteJob(@PathVariable int tenantid,
                            @PathVariable int srvid,
                            @PathVariable int jobid) {
        return jenkinsService.deleteJob(tenantid, srvid, jobid);
    }
    /**
     * 根据租户名和服务id查询JobId列表
     */
    @ApiOperation(value = "查询项目ID列表", notes = "查询项目ID列表")
    @GetMapping(value = "/{tenantid}/{srvid}/job")
    public Result getJobList(@PathVariable int tenantid,
                             @PathVariable int srvid) {
        return jenkinsService.getJobList(tenantid, srvid);
    }
    /**
     * 根据项目ID查询构建列表
     */
    @ApiOperation(value = "查询构建列表", notes = "查询构建列表")
    @GetMapping(value = "/{tenantid}/{srvid}/job/{jobid}/buildlist")
    public Result getBuildList(@PathVariable int tenantid,
                               @PathVariable int srvid,
                               @PathVariable int jobid) {
        return jenkinsService.getBuildList(tenantid, srvid, jobid);
    }
    /**
     * 构建Job
     */
    @ApiOperation(value = "构建项目", notes = "构建项目")
    @GetMapping(value = "/{tenantid}/{srvid}/job/{jobid}/build")
    public Result createJob(@PathVariable int tenantid,
                            @PathVariable int srvid,
                            @PathVariable int jobid) {
        return jenkinsService.buildJob(tenantid, srvid, jobid);
    }
    /**
     * 终止构建Job
     */
    @ApiOperation(value = "终止构建项目", notes = "终止构建项目")
    @GetMapping(value = "/{tenantid}/{srvid}/job/{jobid}/{buildnum}/abort")
    public Result stopBuildingJob(@PathVariable int tenantid,
                            @PathVariable int srvid,
                            @PathVariable int jobid,
                            @PathVariable int buildnum) {
        return jenkinsService.stopBuildingJob(tenantid, srvid, jobid, buildnum);
    }
    /**
     * 获取镜像名称
     */
    @ApiOperation(value = "获取镜像名称", notes = "获取镜像名称")
    @GetMapping(value = "/{tenantid}/{srvid}/job/{jobid}/{buildnum}/imagetag")
    public Result getImageTag(@PathVariable int tenantid,
                              @PathVariable int srvid,
                              @PathVariable int jobid,
                              @PathVariable int buildnum) {
        return jenkinsService.getImageTag(tenantid, srvid, jobid, buildnum);
    }
    /**
     * 获取构建镜像所需的参数
     */
    @ApiOperation(value = "获取构建镜像所需的参数", notes = "获取构建镜像所需的参数")
    @GetMapping(value = "/{tenantid}/{srvid}/job/{jobid}/imagebuildparams")
    public Result getImageBuildParams(@PathVariable int tenantid,
                               @PathVariable int srvid,
                               @PathVariable int jobid) {
        return jenkinsService.getImageBuildParams(tenantid, srvid, jobid);
    }
    /**
     * 获取Task配置
     */
    @ApiOperation(value = "获取构建镜像所需的参数", notes = "获取构建镜像所需的参数")
    @GetMapping(value = "/job/{jobid}/taskconfig")
    public Result getImageBuildParams(@PathVariable int jobid) {
        return jenkinsService.getJobTaskConfig(jobid);
    }
}
