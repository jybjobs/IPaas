package com.cloud.paas.appservice.controller.app;

import com.cloud.paas.appservice.model.AppDetail;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.service.app.AppMngService;
import com.cloud.paas.appservice.service.srv.AppSrvRelService;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.appservice.vo.app.AppSrvListVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/apps")
public class AppMngController {

    @Autowired
    private AppMngService appMngService;
    @Autowired
    private SrvMngService srvMngService;
    @Autowired
    private AppSrvRelService appSrvRelService;

    /**
     * 删除应用
     */
    @ApiOperation(value = "删除应用", notes = "根据应用id删除应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用编号", required = true, dataType = "Integer")
    })
    @DeleteMapping(value = "/{userid}/{appid}")
    public Result deleteAppById(@PathVariable("userid") String userid, @PathVariable("appid") Integer appid) throws BusinessException {
        return appMngService.deleteAppById(userid, appid);
    }

    /**
     * 获取全量应用列表
     */
    @ApiOperation(value = "获取全量应用列表", notes = "获取所有用户下所有应用列表")
    @GetMapping(value = "/apps")
    public Result listApps() throws BusinessException {
        return appMngService.listApps();

    }

    @ApiOperation(value = "获取指定用户下的应用列表", notes = "获取指定用户下的应用列表")
    @GetMapping(value = "/{userid}/apps")
    public Result listAppsByUserId() {
        //TODO: 获取指定用户下的应用列表
        return null;
    }

    @ApiOperation(value = "条件查询应用列表", notes = "服务创建选择应用条件查询应用列表")
    @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/app/condition")
    public Result listByCondition(@PathVariable(value = "userid") String userId, @RequestBody AppExample appExample) throws BusinessException {
        return appMngService.listByCondition(appExample);
    }

    @ApiOperation(value = "指定用户单个应用状态", notes = "获取单个应用状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用编号", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/{userid}/app/{appid}/status")
    public Result getAppStatusByUserIdAppId(@PathVariable(value = "userid") String userid, @PathVariable(value = "appid") Integer appid) throws BusinessException {
        return appMngService.getAppStatusByUserIdAppId(userid, appid);
    }

    @ApiOperation(value = "指定用户全量应用状态", notes = "获取所有应用状态")
    @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String")
    @GetMapping(value = "/{userid}/app/status")
    public Result getAppStatusByUserId(@PathVariable(value = "userid") String userid) {
        //TODO: 指定用户全量应用状态
        return null;
    }

    @ApiOperation(value = "所有用户的所有应用状态", notes = "获取所有应用状态")
    @GetMapping(value = "/app/status/{userid}")
    public Result getAppStatus(@PathVariable(value = "userid") String userid) throws BusinessException {
        return appMngService.getAppStatus(userid);
    }

    /**
     * 创建应用
     */
    @ApiOperation(value = "创建应用", notes = "为用户提供新建应用能力")
    @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/app")
    public Result createApp(@PathVariable(value = "userid") String userid, @RequestBody @Validated AppDetail appDetail) throws BusinessException {
        return appMngService.createApp(userid, appDetail);
    }

    /**
     * 修改应用
     */
    @ApiOperation(value = "修改应用", notes = "为用户提供修改应用的能力")
    @ApiImplicitParam(value = "appInfo", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/app")
    public Result editApp(@PathVariable(value = "userid") String userid, @RequestBody AppDetail appDetail) throws BusinessException {
        return appMngService.updateApp(userid, appDetail);
    }

    /**
     * 根据应用id查询应用详情
     *
     * @param userid
     * @param appid
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "查询应用详情", notes = "根据应用id查询应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用id", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/{userid}/{appid}/detail")
    public Result getAppById(@PathVariable("userid") String userid, @PathVariable("appid") Integer appid) throws BusinessException {
        return appMngService.getAppById(userid, appid);
    }

    /**
     * 查询应用下的服务列表
     *
     * @param userid
     * @param appid
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "查询应用下服务列表", notes = "查询应用下服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用id", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/{userid}/{appid}/srvlist")
    public Result findlistAppSrvListOrderByAppId(@PathVariable("userid") String userid, @PathVariable("appid") Integer appid) throws BusinessException {
        return appMngService.findlistAppSrvListOrderByAppId(appid);
    }

    /**
     * 条件查询应用列表
     * * @param userid
     * @param appExample
     * @return
     * @throws BusinessException
     */
    @ApiOperation(value = "条件查询应用列表", notes = "条件查询应用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appDetail", value = "应用详情", required = true, dataType = "AppDetail")
    })
    @PostMapping(value = "/{userid}/querycondition")
    public Result listAppByCondition(@PathVariable("userid") String userid, @RequestBody AppExample appExample) throws BusinessException {
        Result result = appMngService.listAppSrvListByCondition(appExample);
        //TODO: 应用状态未查询
        return appMngService.listAppSrvListByCondition((List<AppSrvListVO>) result.getData(), appExample);
    }
    @PostMapping(value = "/{userid}/querycondition/limitpages")
    public Result listAppByConditionResult(@PathVariable("userid") String userid, @RequestBody AppExample appExample) throws BusinessException {
        //TODO: 应用状态未查询
        return appMngService.listAppByConditionResult(userid, appExample);
    }

    @ApiOperation(value = "启动|停止应用", notes = "启动停止应用，flag:1 启动应用;flag:2 停止应用;")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用ID", required = true, dataType = "AppDetail"),
            @ApiImplicitParam(name = "flag", value = "操作标记", required = true, dataType = "AppDetail")
    })
    @PostMapping(value = "/{userid}/app/{appid}/flag/{flag}")
    public Result changeApp(@PathVariable("userid") String userId, @PathVariable("appid") Integer appId, @PathVariable("flag") Integer flag) throws BusinessException {
        return appMngService.changeApp(userId, appId, flag);
    }

    @ApiOperation(value = "应用及其服务状态", notes = "返回应用状态及服务状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appid", value = "应用ID", required = true, dataType = "AppDetail")
    })
    @PostMapping(value = "/{userid}/app/state")
    public Result refreshState(@PathVariable("userid") String userId, @RequestBody Integer[] appIds) throws BusinessException {
        return appMngService.refreshState(userId, appIds);
    }

    @ApiOperation(value = "名称检查", notes = "检查应用服务中文名称是否已存在")
    @ApiImplicitParam(name = "nameZh", value = "应用名称", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/appnamezh")
    public Result checkNameZh(@PathVariable("userid") String userId, @RequestBody String appNameZh) {
        return appMngService.checkNameZh(appNameZh);
    }

    @ApiOperation(value = "名称检查", notes = "检查应用服务英文名称是否已存在")
    @ApiImplicitParam(name = "nameEn", value = "应用英文名称", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/appnameen")
    public Result checkNameEn(@PathVariable("userid") String userId, @RequestBody String appNameEn) {
        return appMngService.checkNameEn(appNameEn);
    }

}
