package com.cloud.paas.appservice.service.impl.srv;

import com.cloud.paas.appservice.dao.BaseDAO;
import com.cloud.paas.appservice.dao.SrvOperationDAO;
import com.cloud.paas.appservice.model.SrvOperation;
import com.cloud.paas.appservice.qo.SrvOperationExample;
import com.cloud.paas.appservice.service.impl.BaseServiceImpl;
import com.cloud.paas.appservice.service.srv.SrvOperationService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SrvOperationServiceImpl extends BaseServiceImpl<SrvOperation> implements SrvOperationService {

    @Autowired
    private SrvOperationDAO srvOperationDAO;
    private static final Logger logger = LoggerFactory.getLogger(SrvOperationServiceImpl.class);

    @Override
    public BaseDAO<SrvOperation> getBaseDAO() {
        return srvOperationDAO;
    }


    public SrvOperation doFindByExample(int srvId) {
        //启动中：0，启动失败：1，运行中：2
        //停止中：3，停止失败：4，已停止：5
        //
        SrvOperationExample srvOperationExample = new SrvOperationExample();
        srvOperationExample.setSrvId(srvId);
        srvOperationExample.setState(1);
//        return srvOperationDAO.doFindByExample(srvOperationExample);
        return null;
    }


    /**
     * 根据srvId查询服务最新的状态
     *
     * @param srvId
     * @return
     */
    public long getSrvOperationState(Integer srvId) throws Exception {
        return 0;
    }

    /**
     * 通过一组id 得到相应的对象
     *
     * @param list
     * @return
     */
    @Override
    public Result getGroupOfSrvMng(List<Integer> list) throws BusinessException {
        Result result = null;
        if (null != list && list.size() > 0) {
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_SUCCESS");
            List<SrvOperation> listSrvDetail = srvOperationDAO.getGroupOfSrvMng(list);
            result.setData(listSrvDetail);
            return result;
        } else {
            result = CodeStatusUtil.resultByCodeEn("SERVICE_QUERY_FAILURE");
            result.setMessage("无法查询到相应服务的服务状态");
            logger.error("传入的id有误无法查询到相应服务的服务状态");
            return result;
        }
    }
}