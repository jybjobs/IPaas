package com.cloud.paas.systemmanager.controller.status;

import com.cloud.paas.systemmanager.model.CodeStatus;
import com.cloud.paas.systemmanager.qo.status.StatusCondition;
import com.cloud.paas.systemmanager.service.status.StatusService;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: wyj
 * @desc: 状态码的controller
 * @Date: Created in 2017-12-26 18:17
 * @Modified By:hzy
 */
@RestController
@RequestMapping(value = "/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @ApiOperation(value = "根据状态码获取详细result", notes = "根据状态码获取result,供util调用")
    @GetMapping(value = "/code/{code}")
    public CodeStatus getStatusByCode(@PathVariable int code){
        return statusService.findStatusByCode(code);
    }

    @ApiOperation(value = "根据状态码描述获取详细result", notes = "根据状态码描述获取result，供util调用")
    @GetMapping(value = "/codeEn/{codeEn}")
    public CodeStatus getStatusByCodeEn(@PathVariable String codeEn){
        return statusService.findStatusByCodeEn(codeEn);
    }

    @ApiOperation(value = "获取所有状态码", notes = "获取所有的状态码")
    @GetMapping(value = "/code/all")
    public Map<Integer,CodeStatus> selectAllStatusByCode(){
        return statusService.selectAllStatus();
    }

    @ApiOperation(value = "获取所有状态码以codeen", notes = "获取所有的状态码")
    @GetMapping(value = "/codeEn/all")
    public Map<String,CodeStatus> selectAllStatusByCodeEn(){
        return statusService.selectAllStatusByCodeEn();
    }

    @ApiOperation(value = "通过code获取指定状态", notes = "获取指定的状态码")
    @GetMapping(value = "/statusByCode/{code}")
    public com.cloud.paas.util.codestatus.CodeStatus getCodeStatusByCode(@PathVariable int code){

        return statusService.getStatusCodeByCode(code);

    }

    @ApiOperation(value = "通过code获取指定状态", notes = "获取指定的状态码")
    @GetMapping(value = "/statusByCodeEn/{codeEn}")
    public com.cloud.paas.util.codestatus.CodeStatus getCodeStatusByCodeEn(@PathVariable String codeEn){

        return statusService.getStatusCodeBtCodeEn(codeEn);

    }

    @ApiOperation(value = "根据条件（状态码/状态信息）查询状态码",notes = "根据条件（状态码/状态信息）查询状态码")
    @PostMapping(value = "/{userid}/list")
    public Result getCodeStatusByCondition(@RequestBody StatusCondition statusCondition) throws Exception {
        return statusService.getCodeStatusByCondition(statusCondition);
    }

    @ApiOperation(value = "插入状态",notes = "插入状态")
    @PostMapping(value = "/{userid}/status")
    public Result doInsertBybean(@RequestBody CodeStatus codeStatus){
        return statusService.doInsertByCodeStatusBean(codeStatus);
    }

    @ApiOperation(value = "修改状态",notes = "修改状态")
    @PutMapping(value = "/{userid}/status")
    public Result doUpdateBybean(@RequestBody CodeStatus codeStatus){
        return statusService.doUpdateByCodeStatusBean(codeStatus);
    }
}
