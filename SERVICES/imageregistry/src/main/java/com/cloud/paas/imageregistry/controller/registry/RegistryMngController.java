package com.cloud.paas.imageregistry.controller.registry;

import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.qo.ConditionQuery;
import com.cloud.paas.imageregistry.service.registry.RegistryService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: kaiwen
 * @desc: 仓库的Controller
 * @Date: Created in 2017/11/25
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/registry")
public class RegistryMngController {

    /**
     * 注入registryService对象
     */
    @Autowired
    private RegistryService registryService;

    @ApiOperation(value = "查询仓库", notes = "根据仓库ID，查询仓库，不输入默认查询全部")
    @ApiImplicitParam(name = "registryid", value = "仓库ID", required = false, defaultValue = "0", dataType = "Integer")
    @GetMapping(value = "/{userId}/{registryId}/detail")
    public Result doFindById(@PathVariable("userId") String userId, @PathVariable("registryId") Integer registryId) throws BusinessException {
        return registryService.doFindById(registryId);
    }

    @ApiOperation(value = "查询仓库中镜像版本", notes = "根据仓库ID，查询仓库中镜像的版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "registryid", value = "仓库ID", required = false, defaultValue = "0", dataType = "Integer"),
            @ApiImplicitParam(name = "registryDetail", value = "仓库信息", required = false, defaultValue = "0", dataType = "RegistryDetail")
    })
    @ApiImplicitParam(name = "registryid", value = "仓库ID", required = false, defaultValue = "0", dataType = "Integer")
    @PostMapping(value = "/{userId}/{registryId}/registryDetail")
    public Result listImageByRegistryId(@PathVariable("userId") String userId, @PathVariable("registryId") Integer registryId,@RequestBody RegistryDetail registryDetail) throws Exception {
        registryDetail.setRegistryId(registryId);
        return registryService.listImageByRegistryId(registryDetail);
    }
    @ApiOperation(value = "查询仓库", notes = "根据仓库中文名称，查询仓库，不输入默认查询全部")
    @ApiImplicitParam(name = "registryNameZh", value = "仓库ID", required = false, defaultValue = "0", dataType = "String")
    @GetMapping(value = "/{userId}/{registryNameZh}/detailZH")
    public Result doFindByName(@PathVariable("userId") String userId, @PathVariable("registryNameZh") String registryNameZh) throws Exception {
        return registryService.doFindByName(registryNameZh);
    }

    @ApiOperation(value = "查询仓库", notes = "根据仓库中文名称，查询仓库，不输入默认查询全部")
    @ApiImplicitParam(name = "registryNameEn", value = "仓库ID", required = false, defaultValue = "0", dataType = "String")
    @GetMapping(value = "/{userId}/{registryNameEn}/detailEN")
    public Result doFindByNameEn(@PathVariable("userId") String userId, @PathVariable("registryNameEn") String registryNameEn) throws Exception {
        return registryService.doFindByNameEn(registryNameEn);
    }

    @ApiOperation(value = "多条件查询仓库", notes = "根据仓库多条件查询，查询仓库")
    @ApiImplicitParam(name = "registryDetail", value = "", required = false, defaultValue = "0", dataType = "String")
    @PostMapping(value = "/MultiConditionQuery/{userid}/registryQueryCondition")
    public Result doFindByMultiConditionQuery(@RequestBody ConditionQuery condition) throws BusinessException {
        return registryService.doFindByMultiConditionQuery(condition);
    }

    @ApiOperation(value = "查询仓库", notes = "查询仓库，显示全部仓库列表")
    @ApiImplicitParam(name = "doFindAllList", value = "仓库信息", required = false, defaultValue = "0", dataType = "Integer")
    @GetMapping(value = "/all/{userid}")
    public Result listRegistryDetail() throws Exception {
        return registryService.listRegistryDetail();
    }

    @ApiOperation(value = "查询仓库", notes = "查询仓库，显示指定用户名下的仓库列表")
    @ApiImplicitParam(name = "doFindAll", value = "仓库信息", required = false, defaultValue = "0", dataType = "Integer")
    @GetMapping(value = "/user/{userid}")
    public Result listRegistryDetailUser() throws Exception {
        return registryService.listRegistryDetail();
    }

    @ApiOperation(value = "创建仓库", notes = "创建仓库，只有管理员或者管理员授权的用户可以创建")
    @ApiImplicitParam(name = "registryDetail", value = "仓库信息", required = false, defaultValue = "0", dataType = "String")
    @PostMapping(value = "/{userid}/add")
    public Result doInsertByBean(@RequestBody @Validated(RegistryDetail.RegistryDetailAdd.class) RegistryDetail registryDetail) throws BusinessException {
        return registryService.doInsertByRegistry(registryDetail);
    }

    @ApiOperation(value = "校验路径", notes = "创建仓库，只有管理员或者管理员授权的用户可以创建")
    @ApiImplicitParam(name = "registryDetail", value = "仓库信息", required = false, defaultValue = "0", dataType = "String")
    @PostMapping(value = "/{userid}/judageUrlExit")
    public Result JudageUrlExit(@RequestBody RegistryDetail registryDetail) throws Exception {
        return registryService.JudageUrlExit(registryDetail);
    }

    @ApiOperation(value = "修改仓库", notes = "修改仓库，只有管理员或者管理员授权的用户可以修改")
    @ApiImplicitParam(name = "registryDetail", value = "仓库信息", required = true, dataType = "String")
    @PutMapping(value = "/{userid}/update")
    public Result doUpdateByBean(@RequestBody @Validated(RegistryDetail.RegistryDetailUpdate.class) RegistryDetail registryDetail) throws BusinessException {
        return registryService.doUpdateByRegistry(registryDetail);
    }

    @ApiOperation(value = "删除仓库", notes = "删除仓库，只有管理员或者管理员授权的用户可以删除")
    @ApiImplicitParam(name = "registryid", value = "仓库ID", required = true, dataType = "String")
    @DeleteMapping(value = "/delete/{userid}/{registryId}")
    public Result doDeleteById(@PathVariable("registryId") Integer registryId) throws BusinessException {
        return registryService.doDeleteById(registryId);
    }

    @ApiOperation(value = "启动仓库", notes = "启动仓库，只有管理员或者管理员授权的用户可以操作")
    @ApiImplicitParam(name = "registryId", value = "仓库ID", required = true, dataType = "String")
    @PostMapping(value = "/{registryId}/start")
    public Result startRegistry(@PathVariable("registryId") Integer registryId) throws BusinessException {
        return registryService.startRegistry(registryId);
    }

    @ApiOperation(value = "停止仓库", notes = "停止仓库，只有管理员或者管理员授权的用户可以操作")
    @ApiImplicitParam(name = "registryId", value = "仓库ID", required = true)
    @PostMapping(value = "/{registryId}/stop")
    public Result stopRegistry(@PathVariable("registryId") int registryId) throws BusinessException {
        return registryService.stopRegistry(registryId);
    }
}
