package com.cloud.paas.systemmanager.controller.tenant;

import com.cloud.paas.systemmanager.model.ResourceInfo;
import com.cloud.paas.systemmanager.service.tenant.ResourceService;
import com.cloud.paas.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: weiwei
 * @desc: 租户的Controller
 * @Date: Created in 2018/1/11
 * @Modified By:
 */

@RestController
@RequestMapping(value = "/res")
public class ResourceController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "获取全量资源列表",notes = "获取全部资源列表")
    @GetMapping(value = "/resource")
    public Result listResources() throws Exception{
        return resourceService.listResources();
    }

    @ApiOperation(value = "创建资源",notes = "创建资源")
    @PostMapping(value = "/{userid}/resource")
    public Result doInsertByBean(@PathVariable("userid") String userid,@RequestBody ResourceInfo resourceInfo) throws Exception{
        return resourceService.doInsertByResourceInfo(userid,resourceInfo);
    }

    /**
     * 修改资源
     * @param userid
     * @param resourceInfo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改资源",notes = "提供修改资源的能力")
    @PutMapping(value = "/{userid}/resource")
    public Result editResource(@PathVariable(value = "userid") String userid,@RequestBody ResourceInfo resourceInfo) throws Exception{
        return resourceService.updateResource(userid,resourceInfo);
    }

    @ApiOperation(value = "获取剩余资源配额",notes = "获取剩余资源配额")
    @GetMapping(value = "/{userid}/restResource")
    public Result getRestResource(@PathVariable(value = "userid") String userid) throws Exception{
        return  resourceService.getRestResource(userid);
    }


}
