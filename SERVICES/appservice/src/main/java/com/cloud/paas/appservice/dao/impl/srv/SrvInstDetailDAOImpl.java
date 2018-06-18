package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvInstDetailDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by 17798 on 2018/4/4.
 */
@Repository
public class SrvInstDetailDAOImpl extends BaseDAOImpl<SrvInstDetail> implements SrvInstDetailDAO {

    /**
     * 服务的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "SrvInstDetailDAO";

    @Override
    public String getNameSpace() {
        return this.NAME_SPACE;
    }


    /**
     * 获取节点端口
     * @return
     */
    @Override
    public String doFindNodePort() {
        return this.sqlSessionTemplate.selectOne(this.getNameSpace() + ".doFindNodePort");
    }

    /**
     * 根据服务实例编号查询服务实例详情
     * @param srvInstDetail
     * @return
     */
    @Override
    public SrvInstDetailVO listSrvInstInfoByInstId(SrvInstDetail srvInstDetail) {
        return this.sqlSessionTemplate.selectOne(this.getNameSpace() + ".listSrvInstInfoByInstId",srvInstDetail);
    }

    /**
     * 根据条件查询服务实例（列表）
     * @param srvInstDetailExample
     * @return
     */
    @Override
    public List<SrvInstDetailVO> listSrvInstByCondition(SrvInstDetailExample srvInstDetailExample) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".listSrvInstByCondition",srvInstDetailExample);
    }

    /**
     * 根据应用名称和服务名称查询服务实例
     * @param srvInstDetailExample
     * @return
     */
    @Override
    public Integer querySrvInstByAppAndSrv(SrvInstDetailExample srvInstDetailExample) {
        return this.sqlSessionTemplate.selectOne(this.getNameSpace() + ".querySrvInstByAppAndSrv",srvInstDetailExample);
    }
}
