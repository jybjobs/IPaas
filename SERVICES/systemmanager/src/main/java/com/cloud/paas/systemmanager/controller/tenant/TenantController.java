package com.cloud.paas.systemmanager.controller.tenant;
import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.qo.tenant.TenantCondition;
import com.cloud.paas.systemmanager.service.tenant.TenUserRelService;
import com.cloud.paas.systemmanager.service.tenant.TenantService;
import com.cloud.paas.systemmanager.vo.dictory.TenUserRelVO;
import com.cloud.paas.exception.BusinessException;
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
@RequestMapping(value = "/ten")
public class TenantController {
    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);
    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenUserRelService tenUserRelService;

    @ApiOperation(value = "获取全量租户列表",notes = "获取全部租户列表")
    @PostMapping(value = "/{userId}/tenantsQueryCondition")
    public Result listTenants(@PathVariable("userId") String userId, @RequestBody TenantCondition tenantCondition) throws BusinessException{
        return tenantService.listTenants(tenantCondition);
    }

    @ApiOperation(value = "创建租户",notes = "创建租户")
    @PostMapping(value = "/{userid}/tenants")
    public Result doInsertByBean(@PathVariable("userid") String userid, @RequestBody TenantInfo tenantInfo) throws Exception{
        return tenantService.doInsertByTenantInfo(userid,tenantInfo);
    }

    @ApiOperation(value = "指定租户人员",notes = "租户管理员指定租户人员")
    @PostMapping(value = "/{userid}/tenantlist")
    public Result doInsertListByTenantid(@PathVariable("userid") String userid, @RequestBody TenUserRelVO tenUserRelVO) throws Exception{
        return  tenUserRelService.doInsertByTenantid(userid,tenUserRelVO);
    }

    @ApiOperation(value = "修改租户人员",notes = "租户管理员修改租户人员")
    @PutMapping(value = "/{userid}/modifyusers")
    public Result doUpdateTenantUsers(@PathVariable("userid") String userid, @RequestBody TenUserRelVO tenUserRelVO) throws Exception{
        return  tenUserRelService.updateTenantUsers(userid,tenUserRelVO);
    }


    /**
     * 修改租户
     * @param userid
     * @param tenantInfo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改租户",notes = "提供修改租户的能力")
    @PutMapping(value = "/{userid}/edittenant")
    public Result editTenant(@PathVariable(value = "userid") String userid,@RequestBody TenantInfo tenantInfo) throws Exception{
        return tenantService.updateTenant(userid,tenantInfo);
    }


    @ApiOperation(value = "查询租户详情",notes = "提供查询租户的能力")
    @GetMapping(value = "/{managerUserId}/tenantInfo")
    public Result getTenantDetailInfo(@PathVariable("managerUserId") Integer managerUserId)throws Exception{
        return tenantService.getTenantDetailInfo(managerUserId);
    }

    @ApiOperation(value = "根据租户名查询租户详情",notes = "提供查询租户的能力")
    @GetMapping(value = "/{userid}/{tenantName}/tenantInfo")
    public Result getTenantDetailInfo(@PathVariable("userid") String userid,@PathVariable("tenantName") String tenantName)throws Exception{
        return tenantService.getTenantInfobyTenantName(userid,tenantName);
    }

    @ApiOperation(value = "删除租户", notes = "删除租户")
    @DeleteMapping(value = "/{userid}/deltenant/{tenantid}")
    public Result deleteByPrimaryKeySrv(@PathVariable(value="userid") String userid, @PathVariable(value="tenantid") Integer tenantid) throws Exception{

        return tenantService.deleteTenantById(userid,tenantid);

    }



}
