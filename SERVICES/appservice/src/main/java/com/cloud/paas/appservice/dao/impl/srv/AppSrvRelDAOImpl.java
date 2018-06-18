package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.AppSrvRelDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.AppSrvRel;
import org.springframework.stereotype.Repository;

/**
 * Created by CSS on 2017/12/19.
 */
@Repository("appSrvRelDAO")
public class AppSrvRelDAOImpl extends BaseDAOImpl<AppSrvRel> implements AppSrvRelDAO {
    public static final String NAME_SPACE = "AppSrvRelDAO";
    @Override

    public  String getNameSpace(){
        return AppSrvRelDAOImpl.NAME_SPACE;
    }
    /**
     * 删除SrvId对应的关系记录
     * @param srvId
     * @return
     */
    public int doDeleteBySrvId(int srvId){
        return sqlSessionTemplate.delete(this.getNameSpace()+".deleteBySrvId",srvId);
    }
}
