package com.cloud.paas.appservice.dao.impl.srv;/**
 * @author
 * @create 2017-12-18 14:50
 **/

import com.cloud.paas.appservice.dao.SrvScaleRelDAO;
import com.cloud.paas.appservice.dao.impl.BaseDAOImpl;
import com.cloud.paas.appservice.model.SrvScaleRel;
import org.springframework.stereotype.Repository;

/**
 *
 * @author
 * @create 2017-12-18 14:50
 **/
@Repository("srvScaleRelDAO")
public class SrvScaleRelDAOImpl extends BaseDAOImpl<SrvScaleRel> implements SrvScaleRelDAO {
    public static final String NAME_SPACE = "SrvScaleRelDAO";
    public String getNameSpace() {
        return NAME_SPACE;
    }
}
