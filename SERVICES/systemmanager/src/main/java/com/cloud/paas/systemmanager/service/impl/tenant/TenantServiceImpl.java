package com.cloud.paas.systemmanager.service.impl.tenant;

import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.dao.ResourceInfoDAO;
import com.cloud.paas.systemmanager.dao.TenantInfoDAO;
import com.cloud.paas.systemmanager.model.TenantInfo;
import com.cloud.paas.systemmanager.qo.tenant.TenantCondition;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.service.tenant.TenantService;
import com.cloud.paas.systemmanager.vo.dictory.TenantInfoVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: weiwei
 * @desc: 资源service实现
 * @Date: Created in 2018-1-11
 * @modified By:
 **/
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("tenantService")
public class TenantServiceImpl extends BaseServiceImpl<TenantInfo> implements TenantService {

    private static final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);
    @Autowired
    private TenantInfoDAO tenantDao;

    @Autowired
    private ResourceInfoDAO resourceDao;

    @Override
    public BaseDAO<TenantInfo> getBaseDAO(){return tenantDao;}

    /**
     * 创建租户
     * @param userid
     * @param tenantInfo
     * @return
     * @throws Exception
     */
    @Override
    public Result doInsertByTenantInfo(String userid,TenantInfo tenantInfo) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_BUILD_FAILURE");

        //判断关键字段是否为空
        if(tenantInfo.getTenantName().equals("") || tenantInfo.getCpuQuota().equals("")||tenantInfo.getMemQuota().equals("")||tenantInfo.getDiskQuota().equals("")){
            return result;
        }
        //验证租户名是否重复
        if(tenantDao.doFindByTenantName(tenantInfo.getTenantName())!=null){
            return result;
        }
        //验证资源配额是否超额
        if(tenantInfo.getCpuQuota() > resourceDao.getRestResource().getCpu() || tenantInfo.getMemQuota()>resourceDao.getRestResource().getMem() || tenantInfo.getDiskQuota()>resourceDao.getRestResource().getDisk()){
            return result;
        }
        tenantInfo.setCreator("admin");
        DateUtil dateUtil = new DateUtil();
        Date currentTime = dateUtil.getCurrentTime();
        tenantInfo.setCreateTime(currentTime);
        int line = tenantDao.doInsertByBean(tenantInfo);
        if(line != 0){
            result = CodeStatusUtil.resultByCodeEn("TENANT_BUILD_SUCCESS");
            result.setData(line);
        }
        return result;
    }




    /**
     * 获取全量租户列表
     * @return
     * @throws Exception
     */
    public Result listTenants(TenantCondition tenantCondition) throws BusinessException{
        //1.获取分页信息
        logger.info("---------获取分页信息--------");
        int pageNum = tenantCondition.getPageNum();
        int pageSize = tenantCondition.getPageSize();
        logger.debug("------pageNum:{},pageSize:{}",pageNum,pageSize);
        //2.设置分页查询条件并查询
        logger.info("------------------设置分页查询条件并查询----------");
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<TenantInfoVO> list = tenantDao.listTenants(tenantCondition);
        PageInfo pageInfo = PageUtil.getPageInfo(page, list);
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_QUERY_SUCCESS");
        result.setData(pageInfo);
        return result;
    }

    /**
     * 修改租户
     * @param userid
     * @param tenantInfo
     * @return
     */
    public Result updateTenant(String userid,TenantInfo tenantInfo) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_MODIFY_FAILURE");
        //验证租户名是否重复
        if(tenantDao.doFindByTenantName(tenantInfo.getTenantName())!=null){
            return result;
        }
        //验证资源配额是否超额
        if(tenantInfo.getCpuQuota() > resourceDao.getRestResource().getCpu() || tenantInfo.getMemQuota()>resourceDao.getRestResource().getMem() || tenantInfo.getDiskQuota()>resourceDao.getRestResource().getDisk()){
            return result;
        }
        DateUtil dateUtil = new DateUtil();
        Date currentTime = dateUtil.getCurrentTime();
        tenantInfo.setUpdateTime(currentTime);
        int line = tenantDao.doUpdateByBean(tenantInfo);
        if(line != 0){
            result = CodeStatusUtil.resultByCodeEn("TENANT_MODIFY_SUCCESS");
        }
        return result;
    }


    /**
     * 获取指定租户信息
     * @param managerUserId
     * @return
     * @throws Exception
     */
    public Result getTenantDetailInfo(Integer managerUserId) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_QUERY_FAILURE");
        TenantInfoVO tenantInfo = tenantDao.getTenantDetailInfo(managerUserId);
        if(tenantInfo != null){
            result = CodeStatusUtil.resultByCodeEn("TENANT_QUERY_SUCCESS");
            result.setData(tenantInfo);
        }
        return result;
    }

    @Override
    public Result getTenantInfobyTenantName(String userid, String tenantName) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_QUERY_FAILURE");
        TenantInfo tenantInfo = tenantDao.doFindByTenantName(tenantName);
        if(tenantInfo != null){
            result = CodeStatusUtil.resultByCodeEn("TENANT_QUERY_SUCCESS");
            result.setData(tenantInfo);
        }
        return result;
    }
    @Override
    public Result deleteTenantById(String userId, Integer tenantId) {
        Result result = CodeStatusUtil.resultByCodeEn("TENANT_DELETE_FAILURE");
        if(tenantDao.doDeleteById(tenantId) == 1 ){
            result = CodeStatusUtil.resultByCodeEn("TENANT_DELETE_SUCCESS");
        }

        return result;
    }
}
