package com.cloud.paas.appservice.controller.srv;

import com.cloud.paas.appservice.model.DeployEnv;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.qo.*;
import com.cloud.paas.appservice.service.srv.SrvDeploymentService;
import com.cloud.paas.appservice.service.srv.SrvImplementService;
import com.cloud.paas.appservice.service.srv.SrvMngService;
import com.cloud.paas.appservice.service.srv.SrvOperationService;
import com.cloud.paas.appservice.service.srv.ctn.CtnMngService;
import com.cloud.paas.appservice.vo.srv.SrvDetailVO;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/service")
public class SrvMngController {

    @Autowired
    SrvMngService srvMngService;
    @Autowired
    private CtnMngService ctnMngService;
    @Autowired
    SrvDeploymentService srvDeploymentService;
    @Autowired
    SrvOperationService srvOperationService;

    @Autowired
    private SrvImplementService srvImplementService;

//    @ApiOperation(value = "展示", notes = "")
//    @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String")
//
//    public Result listSrvStatistics(){
//
//    }

    /**
     * 创建服务
     */
    @ApiOperation(value = "创建服务", notes = "为用户提供新建服务能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "SrvDetail", value = "服务详情", required = true, dataType = "SrvDetail")
    })
    @PostMapping(value = "/{userid}/srv/createSrv")
    public Result createService(@PathVariable(value = "userid") String userid,
                                @RequestBody @Validated({SrvDetail.SrvAddValidate.class})SrvDetailVO srvDetailVO) throws BusinessException {
        return srvMngService.doInsertBySrvDetail(srvDetailVO);
    }

    /**
     * 创建服务 devOps
     */
    @ApiOperation(value = "创建服务 devOps", notes = "为devOps平台提供创建服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "SrvDetail", value = "服务详情", required = true, dataType = "SrvDetail")
    })
    @PostMapping(value = "/{userid}/srv/createProject")
    public Result createProject(@PathVariable(value = "userid") String userid,
                                @RequestBody SrvDetailVO srvDetailVO) throws BusinessException {
        return srvMngService.doInsertBySrvDetail(srvDetailVO);
    }

    /**
     * 删除服务
     */
    @ApiOperation(value = "删除服务", notes = "为用户提供删除服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvId", value = "服务详情id", required = true, dataType = "Integer")
    })
    @DeleteMapping(value = "/{userid}/srv/delete/{srvId}")
    public Result deleteService(@PathVariable(value = "userid") String userid,
                                @PathVariable(value = "srvId") Integer srvId) throws BusinessException{
        return srvMngService.doDeleteSrvDetailById(srvId);
    }


    /**
     * 修改服务
     */
    @ApiOperation(value = "修改服务", notes = "为用户提供修改服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvDetailVO", value = "服务详情", required = true, dataType = "SrvDetailVO")
    })
    @PutMapping(value = "/{userid}/srv/updateSrv")
    public Result editService(@PathVariable(value = "userid") String userid,
                              @RequestBody @Validated({SrvDetail.SrvUpdateValidate.class})SrvDetailVO srvDetailVO) throws BusinessException{
        return srvMngService.doUpdateBySrvDetail(srvDetailVO);
    }

    /**
     * 更新服务
     */
    @ApiOperation(value = "更新服务  devOps", notes = "为devOps平台提供修改服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvDetail", value = "服务详情", required = true, dataType = "SrvDetailVO")
    })
    @PutMapping(value = "/{userid}/srv/updateProject")
    public Result updateProject(@PathVariable(value = "userid") String userid,
                              @RequestBody SrvDetailVO srvDetailVO) throws BusinessException{
        return srvMngService.doUpdateProject(srvDetailVO);
    }

    /**
     * 条件查询服务(服务管理)
     */
    @ApiOperation(value = "查询服务", notes = "服务管理页面查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvDetailVO", value = "服务详情VO", required = true, dataType = "SrvDetailVO")
    })
    @PostMapping(value = "/{userid}/srv/list")
    public  Result findSrvByConditions(@PathVariable(value = "userid") Integer userid,
                                    @RequestBody SrvExample srvExample) throws BusinessException{
        return srvMngService.listUserSrvDetail (userid,srvExample);
    }


    /**
     * 查看单个服务详情
     */

    @ApiOperation(value = "查看单个服务详情", notes = "查看单个服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvid", value = "服务编号", required = true, dataType = "Integer"),
    })
    @GetMapping(value = "/{userid}/srv/{srvid}/detail")
    public Result findSrvDetail(@PathVariable(value = "srvid") Integer srvId) throws BusinessException{
        return srvMngService.doFindSrvDetailById (srvId);
    }



    /**
     * 查看单个服务详情
     */

    @ApiOperation(value = "查看单个服务详情", notes = "修改时查看单个服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String"),
    })
    @GetMapping(value = "/{userid}/app/{appname}/srv/{srvid}/detail")
    public Result editService(@PathVariable(value = "srvid") Integer srvId) throws BusinessException{
        return srvMngService.doFindSrvDetailById (srvId);
    }


    /**
     * 修改服务启动顺序
     */
    @ApiOperation(value = "修改服务启动顺序", notes = "页面拖拽修改启动顺序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvname", value = "服务名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "AppSrvRel", value = "服务关系详情", required = true, dataType = "AppSrvRel")
    })
    @PostMapping(value = "/{userid}/app/{appname}/srv/startOrder")
    public Result editStartOrder(@PathVariable(value = "userid") String userid,
                                 @PathVariable(value = "appname") String appname,
                                 @RequestBody List<SrvInstDetail> srvInstDetails) throws BusinessException{
        return srvMngService.doUpdateBySrvInstDetail(srvInstDetails);
    }

    /**
     * 多条件查询服务
     */
    @ApiOperation(value = "查询服务", notes = "页面六个条件查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvDetailVO", value = "服务详情VO", required = true, dataType = "SrvDetailVO")
    })
    @PostMapping(value = "/{userid}/srv/list/condition")
    public  Result findByConditions(@PathVariable(value = "userid") String userid,
                                    @RequestBody SrvExample srvExample) throws BusinessException{
        return srvMngService.listQueryConditions (srvExample);
    }

    /**
     *  根据一组服务id查询相应服务状态
     */
    @ApiOperation(value = "查询服务", notes = "查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "list", value = "服务详情VO的list", required = true, dataType = "List<SrvDetailVO>")
    })
    @PostMapping(value = "/{userid}/srv/list/listId")
    public Result getGroupOfSrvMng(@RequestBody List<Integer> list) throws BusinessException{
            return srvOperationService.getGroupOfSrvMng(list);
    }

    /**
     * 获取所有用户下所有应用的所有服务列表
     */
    @ApiOperation(value = "查询服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParam(value = "srv", required = false, dataType = "String")
    @GetMapping(value = "/app/srv")
    public Result listAppSrvDetail() throws  BusinessException{
        return srvMngService.listUserAppSrvDetail();
    }

    /**
     * 获取指定用户下所有应用的所有服务列表
     */
    @ApiOperation(value = "查询指定用户下所有应用的所有服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParam(value = "userid", required = true, dataType = "Integer")
    @GetMapping(value = "/{userid}/app/srv")
    public Result listUserGivenAppSrvDetail(@PathVariable(value = "userid") Integer userid) throws BusinessException{
        return srvMngService.listUserAppSrvDetail();
    }

    /**
     * 获取指定用户下指定应用的所有服务
     */
    @ApiOperation(value = "查询指定用户下指定应用的所有服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/app/{appname}/srv")
    public Result listUserGivenAppGivenSrvDetail(@PathVariable(value = "userid") Integer userid,
                                                 @PathVariable(value = "appname") String appname) throws BusinessException {
        return srvMngService.listUserGivenAppGivenSrvDetail(userid, appname);
    }

    /**
     * 获取指定用户下指定应用的指定服务
     */
    @ApiOperation(value = "查询指定用户下指定应用的指定服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvname", value = "服务名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/app/{appname}/srv/{srvname}")
    public Result userGivenAppGivenSrvDetailGiven(@PathVariable(value = "userid") Integer userid,
                                                  @PathVariable(value = "appname") String appname,
                                                  @PathVariable(value = "srvname") String srvname) throws BusinessException{
        return srvMngService.userGivenAppGivenSrvDetailGiven(userid, appname, srvname);
    }

    /**
     * 查询所有容器
     *
     * @return
     */
    @ApiOperation(value = "查询所有容器", notes = "为用户提供查询容器的能力")
    @ApiImplicitParam(name = "listAllCtn", value = "", required = false, dataType = "String")
    @GetMapping(value = "/ctns")
    public Result listAllCtn() {
        return ctnMngService.listAllCtn();
    }


    /**
     * 获取指定用户下所有应用的所有服务的所有容器
     */
    @ApiOperation(value = "查询指定用户下所有应用的所有服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParam(value = "userid", required = true, dataType = "Integer")
    @GetMapping(value = "/{userid}/app/ctns")
    public Result listUserGivenAppSrvDetailCnts(@PathVariable(value = "userid") Integer userid) {
        return ctnMngService.listUserGivenAppSrvDetailCnts(userid);
    }

    /**
     * 指定用户下指定应用的所有服务的所有容器
     * {userid}/app/{appname}/ctns
     */
    @ApiOperation(value = "查询容器", notes = "为用户提供启动服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/app/{appname}/ctns")
    public Result listCtnUserGivenAppGivenSrv(@PathVariable(value = "userid") Integer userId,
                                              @PathVariable(value = "appname") String appname) {
        return ctnMngService.listCtnUserGivenAppGivenSrv(userId, appname);
    }

    /**
     * 查询指定用户下指定应用的指定服务的所有容器
     */
    @ApiOperation(value = "查询指定用户下指定应用的指定服务", notes = "为用户提供查询服务的能力")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appname", value = "应用名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvname", value = "服务名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/app/{appname}/srv/{srvname}/ctns")
    public Result userGivenAppGivenSrvDetailGivenCnts(@PathVariable(value = "userid") Integer userid,
                                                      @PathVariable(value = "appname") String appname,
                                                      @PathVariable(value = "srvname") String srvname) {
        return ctnMngService.userGivenAppGivenSrvDetailGivenCnts(userid, appname, srvname);
    }

    /**
     * flag 1:启动服务,2:停止服务，3:服务扩容，4:服务缩容
     */
    @ApiOperation(value = "服务状态的改变", notes = "为用户提供改变服务状态的能力")
    @ApiImplicitParam(value = "serviceName", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/app/{appname}/srv/{srvname}/flag/{flag}")
    public Result srvStatusChanged(@PathVariable(value = "userid") Integer userId,
                                   @PathVariable(value = "appname") String appname,
                                   @PathVariable(value = "srvname") String srvname,
                                   @PathVariable(value = "flag") Long flag) throws BusinessException{
        return srvMngService.SrvStatusChanged(userId, appname, srvname, flag);
    }

    @ApiOperation(value = "根据镜像id查询所有服务", notes = "根据镜像id查询所有服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srvImageVersionId", value = "镜像版本id", required = true, dataType = "String")
    })
    @GetMapping(value = "/app/srv/{srvImageVersionId}/findAll")
    public Result findAll(
            @PathVariable(value = "srvImageVersionId") int srvImageVersionId) throws BusinessException{
        return srvMngService.findAll (srvImageVersionId);
    }

    @ApiOperation(value = "上传服务图片", notes = "将服务图片上传到服务器")
    @ApiImplicitParam(name = "file", value = "服务器图片", required = true, dataType = "MultipartFile")
    @PostMapping(value = "/uploadfile")
    public Result uploadSrvFile(@RequestParam MultipartFile file) throws BusinessException{
        //上传文件并返回文件路径
        return srvMngService.uploadFile(file);
    }

    /**
     * @Author: srf
     * 根据app查询容器名称
     */
    @ApiOperation(value = "根据app查询容器", notes = "为用户提供查询容器名称的能力")
    @GetMapping(value = "/app/{app}/ctns")
    public Result userGivenAppGivenSrvDetailGivenCnts(@PathVariable(value = "app") String app) {
        return ctnMngService.listCtnByApp(app);
    }

    /**
     * @Author: srf
     * 根据Deployment名称查询limits
     */
    @ApiOperation(value = "根据Deployment名称查询limits", notes = "为用户提供查询limits的能力")
    @GetMapping(value = "/app/deploy/{name}")
    public Result getLimitsBydeploy(@PathVariable(value = "name") String name) {
        return ctnMngService.getCtnLimitsByDeployName(name);
    }

    /**
     * @Author: srf
     * 根据容器名称查询容器
     */
    @ApiOperation(value = "根据容器名称查询容器", notes = "为用户提供查询容器的能力")
    @GetMapping(value = "/app/ctn/{name}")
    public Result getPodByName(@PathVariable(value = "name") String name){
        return ctnMngService.getCtnByCtnName(name);
    }

    /**
     * @Author: srf
     * 根据容器名称查询limits
     */
    @ApiOperation(value = "根据容器名称查询limits", notes = "为用户提供查询limits的能力")
    @GetMapping(value = "/app/ctn/{name}/limits")
    public Result getLimitsByCtnName(@PathVariable(value = "name") String name){
        return ctnMngService.getCtnLimitsByCtnName(name);
    }



    /************************************************服务定义**********************************************/

    @ApiOperation(value = "查询服务定义（版本）信息" , notes = "查询服务定义（版本）信息")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/listSrvVersionDetail")
    public Result listSrvVersionDetail(@PathVariable(value = "userid") String userid,
                                       @RequestBody SrvVersionDetailExample srvVersionDetailExample)throws BusinessException{
        return srvMngService.listSrvVersionDetail(userid,srvVersionDetailExample);
    }

    @ApiOperation(value = "创建服务定义版本" , notes = "创建服务定义版本")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/createSrvDef")
    public Result createSrvDef(@PathVariable(value = "userid") String userid,
                               @RequestBody SrvVersionDetailVO srvVersionDetailVO) throws BusinessException{
        return srvMngService.createSrvDef(userid,srvVersionDetailVO);
    }

    @ApiOperation(value = "服务定义版本镜像构建" , notes = "服务定义版本镜像构建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvVersionId", value = "服务版本编号", required = true, dataType = "Integer")
    })
    @PutMapping(value = "/{userid}/srv/def/buildSrvImage/{srvVersionId}")
    public Result buildSrvImage(@PathVariable(value = "userid") String userid,
                               @PathVariable(value = "srvVersionId") Integer srvVersionId) throws BusinessException{
        return srvMngService.buildSrvImage(userid,srvVersionId);
    }

    @ApiOperation(value = "创建服务定义版本并构建" , notes = "创建服务定义版本并构建")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/createAndBuildImage")
    public Result createAndBuild(@PathVariable(value = "userid") String userid,
                               @RequestBody SrvVersionDetailVO srvVersionDetailVO) throws BusinessException{
        return srvMngService.createAndBuild(userid,srvVersionDetailVO);
    }

    @ApiOperation(value = "删除服务定义" , notes = "删除服务定义)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvVersionId", value = "服务定义编号", required = true, dataType = "Integer")
    })
    @DeleteMapping(value = "/{userid}/srv/def/delete/{srvVersionId}")
    public Result deleteSrvDef(@PathVariable(value = "userid") String userid,
                                @PathVariable(value = "srvVersionId") Integer srvVersionId) throws BusinessException{
        return srvMngService.deleteSrvDef(userid,srvVersionId);
    }

    /************************************************服务实例**********************************************/

    @ApiOperation(value = "创建服务实例" , notes = "创建服务实例")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/createSrvInst")
    public Result createSrvInst(@PathVariable(value = "userid") String userid,
                                 @Validated({SrvInstDetail.SrvInstAddValidate.class}) @RequestBody SrvInstDetailVO srvInstDetailVO) throws BusinessException{
        return srvMngService.createSrvInst(userid,srvInstDetailVO);
    }

    @ApiOperation(value = "根据条件查询服务实例（列表）" , notes = "根据条件查询服务实例（列表）")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/inst/listSrvInstByCondition")
    public Result listSrvInstByCondition(@PathVariable(value = "userid") String userid,
                                @RequestBody SrvInstDetailExample srvInstDetailExample) throws BusinessException{
        return srvMngService.listSrvInstByCondition(userid,srvInstDetailExample);
    }

    @ApiOperation(value = "根据主键查询服务实例详情" , notes = "根据主键查询服务实例详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srvInstId", value = "服务实例编号", required = true, dataType = "String")
    })
    @GetMapping(value = "/srv/inst/findSrvInst/{srvInstId}")
    public Result findSrvInst(@PathVariable(value = "srvInstId") Integer srvInstId) throws BusinessException{
        return srvMngService.findSrvInst(srvInstId);
    }

    @ApiOperation(value = "通过服务实例编号获取deployment信息" , notes = "通过服务实例编号获取deployment信息")
    @ApiImplicitParam(name = "srvInstId", value = "服务实例编号", required = true, dataType = "Integer")
    @GetMapping(value = "/srv/deploy/{srvInstId}")
    public Result queryDeploymentInfo(@PathVariable(value = "srvInstId") Integer srvInstId) throws BusinessException{
        return srvMngService.doFindDeploymentInfo(srvInstId);
    }

    @ApiOperation(value = "版本切换" , notes = "版本切换")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/inst/srvVersionChange")
    public Result srvVersionChange(@PathVariable(value = "userid") String userid,
                                         @RequestBody SrvInstDetailExample srvInstDetailExample) throws BusinessException{
        return srvMngService.srvVersionChange(userid,srvInstDetailExample);
    }

    @ApiOperation(value = "一键升级" , notes = "一键升级(根据应用名-服务名构成的服务实例名一键升级)")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/inst/srvVersionUpdate")
    public Result srvVersionUpdate(@PathVariable(value = "userid") String userid,
                                         @RequestBody SrvInstDetailExample srvInstDetailExample) throws BusinessException{
        return srvMngService.srvVersionUpdate(userid,srvInstDetailExample);
    }

    @ApiOperation(value = "jekins调用升级及接口" , notes = "jekins调用升级及接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appNameEn", value = "应用英文名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvNameEn", value = "服务英文名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/srv/inst/srvVersionUpdate/{appNameEn}/{srvNameEn}")
    public Result srvVersionUpdate(@PathVariable(value = "userid") String userid,
                                   @PathVariable(value = "appNameEn") String appNameEn,
                                   @PathVariable(value = "srvNameEn") String srvNameEn) throws BusinessException{
        return srvMngService.jekinsVersionUpdate(userid,appNameEn,srvNameEn);
    }

    @ApiOperation(value = "服务实例启动" , notes = "服务实例启动")
    @ApiImplicitParam(name = "srvInstId", value = "服务实例编号", required = true, dataType = "Integer")
    @PutMapping(value = "/srv/inst/run/{srvInstId}")
    public Result srvInstRun(@PathVariable(value = "srvInstId") Integer srvInstId) throws BusinessException{
        return srvMngService.srvInstRun(srvInstId);
    }

    @ApiOperation(value = "服务实例停止" , notes = "服务实例停止")
    @ApiImplicitParam(name = "srvInstId", value = "服务实例编号", required = true, dataType = "Integer")
    @DeleteMapping(value = "/srv/inst/stop/{srvInstId}")
    public Result srvInstStop(@PathVariable(value = "srvInstId") Integer srvInstId) throws BusinessException{
        return srvMngService.srvInstStop(srvInstId);
    }

    @ApiOperation(value = "修改服务实例" , notes = "修改服务实例")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/srv/inst/editSrvInst")
    public Result editSrvInst(@PathVariable(value = "userid") String userid,
                              @Validated({SrvInstDetail.SrvInstUpdateValidate.class}) @RequestBody SrvInstDetailVO srvInstDetailVO) throws BusinessException{
        return srvMngService.editSrvInst(userid,srvInstDetailVO);
    }

    @ApiOperation(value = "删除实例" , notes = "删除实例)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvInstId", value = "服务实例编号", required = true, dataType = "Integer")
    })
    @DeleteMapping(value = "/{userid}/srv/inst/delete/{srvInstId}")
    public Result deleteSrvInst(@PathVariable(value = "userid") String userid,
                                @PathVariable(value = "srvInstId") Integer srvInstId) throws BusinessException{
        return srvMngService.deleteSrvInst(userid,srvInstId);
    }

    /************************************************DevOps**********************************************/

    @ApiOperation(value = "devops创建镜像版本并创建dockerfile", notes = "devops创建镜像版本并创建dockerfile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvId", value = "服务编号", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "version", value = "版本", required = true, dataType = "String")
    })
    @GetMapping(value = "/{userid}/{srvId}/createImageVersionAndDockerfile/{version}")
    public Result createImageVersionAndDockerfile(@PathVariable("userid") String userid,@PathVariable("srvId") Integer srvId,@PathVariable("version") String version) throws Exception {
        return srvDeploymentService.createImageVersionAndDockerfile(userid, srvId, version);
    }

    @ApiOperation(value = "devops创建服务版本", notes = "devops创建服务版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "srvId", value = "服务编号", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "version", value = "版本", required = true, dataType = "String"),
            @ApiImplicitParam(name = "imageVersionId", value = "镜像版本编号", required = true, dataType = "Integer")
    })
    @GetMapping(value = "/{userid}/{srvId}/createSrvVersion/{version}/{imageVersionId}")
    public Result createSrvVersion(@PathVariable("userid") String userid,@PathVariable("srvId") Integer srvId,@PathVariable("version") String version,@PathVariable("imageVersionId") Integer imageVersionId) throws Exception {
        return srvDeploymentService.createSrvVersion(userid, srvId, version, imageVersionId);
    }

    @ApiOperation(value = "查询部署" , notes = "查询部署")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/deployment/list")
    public Result list(@PathVariable(value = "userid") String userid,
                          @RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        return srvDeploymentService.listSrvDeploymentWithSrvInst(userid, srvDeploymentExample);
    }

    @ApiOperation(value = "部署" , notes = "部署")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/deployment/publish")
    public Result publish(@PathVariable(value = "userid") String userid,
                                   @RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        return srvDeploymentService.publish(userid, srvDeploymentExample);
    }

    @ApiOperation(value = "更新部署" , notes = "更新部署")
    @ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/deployment/update")
    public Result update(@PathVariable(value = "userid") String userid,
                          @RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        return srvDeploymentService.update(userid, srvDeploymentExample);
    }

    @ApiOperation(value = "部署服务实例停止" , notes = "部署服务实例停止")
    @ApiImplicitParam(name = "deploymentId", value = "部署实例编号", required = true, dataType = "Integer")
    @DeleteMapping(value = "/srv/deployment/stop/{deploymentId}")
    public Result deploymentStop(@PathVariable(value = "deploymentId") Integer deploymentId) throws BusinessException{
        return srvDeploymentService.deploymentStop(deploymentId);
    }

    @ApiOperation(value = "部署服务实例启动" , notes = "部署服务实例启动")
    @ApiImplicitParam(name = "srvInstId", value = "部署实例编号", required = true, dataType = "Integer")
    @PutMapping(value = "/srv/deployment/run/{deploymentId}")
    public Result deploymentStart(@PathVariable(value = "deploymentId") Integer deploymentId) throws BusinessException{
        return srvDeploymentService.deploymentStart(deploymentId);
    }

    @ApiOperation(value = "更新部署信息" , notes = "更新部署信息")
    @PutMapping(value = "/srv/deployment/update/")
    public Result deploymentStart(@RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        return srvDeploymentService.update(srvDeploymentExample);
    }

    @ApiOperation(value = "切换版本" , notes = "切换版本")
    @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/srv/deployment/exchangeVersion")
    public Result exchangeVersion(@PathVariable(value = "userid") String userid,
                                  @RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        return srvDeploymentService.exchangeVersion(userid, srvDeploymentExample);
    }

    @ApiOperation(value = "自动部署接口" , notes = "自动部署接口")
    @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/srv/deployment/autoPublish")
    public void autoPublish(@PathVariable(value = "userid") String userid,
                                  @RequestBody SrvDeploymentExample srvDeploymentExample) throws BusinessException{
        srvDeploymentService.autoPublish(userid, srvDeploymentExample);
    }

    @ApiOperation(value = "查询部署环境变量的接口" , notes = "查询部署环境变量的接口")
    @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/deployment/queryDeployEnv")
    public Result queryDeployEnv(@PathVariable(value = "userid") String userid,
                              @RequestBody DeployEnvExample deployEnvExample) throws BusinessException{
        return srvDeploymentService.doFindDeployEnvList(userid, deployEnvExample);
    }

    @ApiOperation(value = "添加/修改部署环境变量的接口" , notes = "添加/修改部署环境变量的接口")
    @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String")
    @PostMapping(value = "/{userid}/srv/deployment/saveDeployEnv")
    public Result saveDeployEnv(@PathVariable(value = "userid") String userid,
                            @RequestBody DeployEnv deployEnv) throws BusinessException{
        return srvDeploymentService.saveDeployEnv(userid, deployEnv);
    }

    @ApiOperation(value = "删除部署环境变量的接口" , notes = "删除部署环境变量的接口")
    @ApiImplicitParam(name = "userid", value = "用户编号", required = true, dataType = "String")
    @DeleteMapping(value = "/{userid}/srv/deployment/deleteDeployEnv/{deployEnvId}")
    public Result deleteDeployEnv(@PathVariable(value = "userid") String userid,
                                  @PathVariable(value = "deployEnvId") Integer deployEnvId) throws BusinessException{
        return srvDeploymentService.deleteDeployEnv(userid, deployEnvId);
    }

    @ApiOperation(value = "查询容器日志" , notes = "查询容器日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "podName", value = "pod名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "namespace", value = "namespace", required = true, dataType = "String")
    })
    @GetMapping(value = "/srv/deployment/queryPodLog/{namespace}/{podName}")
    public Result queryPodLog(@PathVariable(value = "podName") String podName,
                                  @PathVariable(value = "namespace") String namespace) throws BusinessException{
        return srvDeploymentService.queryPodLog(podName, namespace);
    }

}
