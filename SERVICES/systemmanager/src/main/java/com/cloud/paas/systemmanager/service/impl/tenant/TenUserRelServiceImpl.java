package com.cloud.paas.systemmanager.service.impl.tenant;

import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.dao.TenUserRelDAO;
import com.cloud.paas.systemmanager.model.TenUserRel;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.service.tenant.TenUserRelService;
import com.cloud.paas.systemmanager.vo.dictory.TenUserRelVO;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("tenUserRelService")
public class TenUserRelServiceImpl extends BaseServiceImpl<TenUserRel> implements TenUserRelService {
    private static final Logger logger = LoggerFactory.getLogger(TenUserRelServiceImpl.class);

    @Override
    public BaseDAO<TenUserRel> getBaseDAO(){return tenUserRelDAO;}

    @Autowired
    private TenUserRelDAO tenUserRelDAO;


    /**
     * 租户人员批量插入
     * @param userid
     * @param tenUserRelList
     * @return
     * @throws Exception
     */
    @Override
    public Result doInsertByTenantid(String userid, TenUserRelVO tenUserRelList) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("TENANTUSER_BUILD_FAILURE");
        int line = 0;
        for(TenUserRel tenUserRel:tenUserRelList.getTenUserRelList()){
            line = tenUserRelDAO.doInsertByBean(tenUserRel);
            line++;
        }
       if(line == tenUserRelList.getTenUserRelList().size()){
            result = CodeStatusUtil.resultByCodeEn("TENANTUSER_BUILD_SUCCESS");
            result.setData(line);
        }
        return result;

    }

    /**
     * 租户人员更新
     * @param userid
     * @param tenUserRelVO
     * @return
     * @throws Exception
     */
    @Override
    public Result updateTenantUsers(String userid, TenUserRelVO tenUserRelVO) throws Exception{
        Result result = CodeStatusUtil.resultByCodeEn("TENANTUSER_MODIFY_FAILURE");
        int line = 0;
        for(TenUserRel tenUserRel:tenUserRelVO.getTenUserRelList()){
            tenUserRelDAO.doUpdateByBean(tenUserRel);
            line += 1;
        }
        if(line == tenUserRelVO.getTenUserRelList().size()){
            result = CodeStatusUtil.resultByCodeEn("TENANTUSER_MODIFY_SUCCESS");
            result.setData(line);
        }
        return result;
    }

}
