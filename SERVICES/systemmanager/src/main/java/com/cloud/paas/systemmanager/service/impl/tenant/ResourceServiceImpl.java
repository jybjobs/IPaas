package com.cloud.paas.systemmanager.service.impl.tenant;

import com.cloud.paas.systemmanager.dao.ResourceInfoDAO;
import com.cloud.paas.systemmanager.model.ResourceInfo;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.service.tenant.ResourceService;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cloud.paas.systemmanager.dao.BaseDAO;

import java.util.Date;
import java.util.List;

/**
 * @author: weiwei
 * @desc: 资源service实现
 * @Date: Created in 2018-1-11
 * @modified By:
 **/
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<ResourceInfo> implements ResourceService{
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    @Autowired
    private ResourceInfoDAO resourceDao;
    @Override
    public BaseDAO<ResourceInfo> getBaseDAO(){return resourceDao;}

    @Override
    public Result doInsertByResourceInfo(String userid,ResourceInfo resourceInfo) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("RESOURCE_BUILD_FAILURE");
        resourceInfo.setCreator("admin");
        DateUtil dateUtil = new DateUtil();
        Date currentTime = dateUtil.getCurrentTime();
        resourceInfo.setCreateTime(currentTime);
        int line = resourceDao.doInsertByBean(resourceInfo);
        if(line !=0){
            result = CodeStatusUtil.resultByCodeEn("RESOURCE_BUILD_SUCCESS");
            result.setData(line);
        }
        return  result;
    }

    /**
     * 获取全量资源列表
     * @return
     * @throws Exception
     */
    public Result listResources() throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("RESOURCE_QUERY_FAILURE");
        List<ResourceInfo> resourceList = resourceDao.listResources();
        if(resourceList != null && resourceList.size()>0){
            result = CodeStatusUtil.resultByCodeEn("RESOURCE_QUERY_SUCCESS");
            result.setData(resourceList);
        }
        return result;
    }
    /**
     * 修改资源
     * @param userid
     * @param resourceInfo
     * @return
     */
    @Override
    public Result updateResource(String userid,ResourceInfo resourceInfo) throws Exception {
        Result result = CodeStatusUtil.resultByCodeEn("RESOURCE_MODIFY_FAILURE");
        DateUtil dateUtil = new DateUtil();
        Date currentTime = dateUtil.getCurrentTime();
        resourceInfo.setUpdateTime(currentTime);
        int line = resourceDao.doUpdateByBean(resourceInfo);
        if(line != 0){
            result = CodeStatusUtil.resultByCodeEn("RESOURCE_MODIFY_SUCCESS");
        }
        return result;
    }

    @Override
    public Result getRestResource(String userid) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("RESOURCE_QUERY_FAILURE");
        ResourceInfo restResource = resourceDao.getRestResource();
        if(restResource != null){
            result = CodeStatusUtil.resultByCodeEn("RESOURCE_QUERY_SUCCESS");
            result.setData(restResource);
        }
        return result;
    }

}
