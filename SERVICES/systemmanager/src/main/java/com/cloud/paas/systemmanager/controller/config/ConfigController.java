package com.cloud.paas.systemmanager.controller.config;

import com.cloud.paas.systemmanager.model.SysConfig;
import com.cloud.paas.systemmanager.qo.config.ConfigCondition;
import com.cloud.paas.systemmanager.service.config.ConfigService;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/config")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @ApiOperation(value = "根据中文名字查询配置详情", notes = "根据中文名字查询配置详情")
    @GetMapping(value = "/{userid}/zh/{configChName}")
    public Result selectByChNameCtr(@PathVariable(value="userid") int userid,
                                     @PathVariable(value="configChName") String configChName) {

        return configService.selectByChNameSrv(configChName);

    }
    @ApiOperation(value = "条件查询列表", notes = "条件查询列表")
    @PostMapping(value = "/{userid}/query")
    public Result getAllConfigSrv(@PathVariable(value = "userid") String userId, @RequestBody ConfigCondition configCondition) {

        return configService.getAllConfig(configCondition);

    }

    @ApiOperation(value = "查询所有配置，封装成map", notes = "查询所有配置，封装成map")
    @GetMapping(value = "/all")
    public Map getAllConfig(){
        return configService.getAllConfigList();
    }


    @ApiOperation(value = "根据英文名字查询配置详情", notes = "根据英文名字查询配置详情")
    @GetMapping(value = "/{userid}/en/{configEnName}")
    public Result selectByEnNameCtr(@PathVariable(value="userid") int userid,
                                    @PathVariable(value="configEnName") String configEnName) {

        return configService.selectByEnNameSrv(configEnName);

    }
    @ApiOperation(value = "插入配置", notes = "插入配置")
    @PostMapping(value = "/{userid}/add")
    public Result insertSelectiveCtr(@RequestBody @Validated(SysConfig.SysConfigAdd.class) SysConfig sysConfig ) {

        return configService.insertSelectiveSrv(sysConfig);

    }

    @ApiOperation(value = "更新配置", notes = "更新配置")
    @PutMapping(value = "/{userid}/update")
    public Result updateByPrimaryKeyCtr(@RequestBody @Validated(SysConfig.SysConfigUpdate.class) SysConfig sysConfig ) {

        return configService.updateByPrimaryKeySrv(sysConfig);

    }
    @ApiOperation(value = "删除配置", notes = "删除配置")
    @DeleteMapping(value = "/{userid}/delete/{configid}")
    public Result deleteByPrimaryKeySrv(@PathVariable(value="userid") int userid,
                                        @PathVariable(value="configid") int sysConfigId ) {

        return configService.deleteByPrimaryKeySrv(sysConfigId);

    }
}
