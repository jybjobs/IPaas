package com.cloud.paas.appservice.dao.impl.srv;/**
 * @author
 * @create 2017-12-15 10:08
 **/

import com.cloud.paas.appservice.dao.CtnDetailInfoDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.CtnDetailInfo;
import com.cloud.paas.appservice.qo.SrvCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yht
 * @create 2017-12-15 10:08
 **/
@Repository("ctnDetailInfoDAO")
public class CtnDetailInfoDAOImpl extends BaseDAOImpl<CtnDetailInfo> implements CtnDetailInfoDAO {
    /**
     * 容器的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "CtnDetailInfoDAO";

    public String getNameSpace() {
        return NAME_SPACE;
    }

    /**
     * 查询所有容器信息
     *
     * @return
     */
    public List<CtnDetailInfo> listAllCtn() {
        return sqlSessionTemplate.selectList( this.getNameSpace() + ".listAllCtn");
    }


    /**
     * 查询指定用户下所有应用所有服务下所有容器
     */
    public  List<CtnDetailInfo> listUserGivenAppSrvDetailCnts(Integer userId) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".listUserGivenAppSrvDetailCnts",userId);
    }

    /**
     * 查询指定用户下指定应用所有服务下所有容器
     */
    public List<CtnDetailInfo> listCtnUserGivenAppGivenSrv(SrvCondition srvCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".listCtnUserGivenAppGivenSrv", srvCondition);
    }

    /**
     * 查询指定用户下指定应用指定服务下所有容器
     */
    public List<CtnDetailInfo> userGivenAppGivenSrvDetailGivenCnts(SrvCondition srvCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace() + ".userGivenAppGivenSrvDetailGivenCnts", srvCondition);
    }
}
