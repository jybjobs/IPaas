package com.cloud.paas.appservice.dao.impl.srv;

import com.cloud.paas.appservice.dao.SrvVersionDetailDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvVersionDetail;
import com.cloud.paas.appservice.qo.SrvVersionDetailExample;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 17798 on 2018/3/28.
 */
@Repository
public class SrvVersionDetailDAOImpl extends BaseDAOImpl<SrvVersionDetail> implements SrvVersionDetailDAO {

    /**
     * 服务操作的mapper.xml的namespace
     */
    public static final String NAME_SPACE = "com.cloud.paas.appservice.dao.SrvVersionDetailDAO";

    @Override
    public String getNameSpace() {
        return this.NAME_SPACE;
    }

    @Override
    public List<SrvVersionDetailVO> listSrvVersionDetail(SrvVersionDetailExample srvVersionDetailExample) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".listSrvVersionDetail",srvVersionDetailExample);
    }

    @Override
    public List<SrvVersionDetailVO> doFindProcessingImageVersionIds() {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".doFindProcessingImageVersionIds");
    }

    @Override
    public List<SrvVersionDetail> listSrvVersionDetailByIds(List<Integer> list) {
        return this.sqlSessionTemplate.selectList(this.getNameSpace() + ".listSrvVersionDetailByIds",list);
    }
}
