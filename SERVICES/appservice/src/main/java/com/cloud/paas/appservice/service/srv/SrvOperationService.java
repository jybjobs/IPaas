package com.cloud.paas.appservice.service.srv;

import com.cloud.paas.appservice.model.SrvOperation;
import com.cloud.paas.appservice.service.BaseService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;

import java.util.List;


public interface SrvOperationService extends BaseService<SrvOperation>{

    /**
     * 根据服务ID查找创建时间最新的记录
     * @param srvId
     * @return
     */
    SrvOperation doFindByExample(int srvId);

    /**
     * 根据srvId查询服务最新的状态
     * @param srvId
     * @return
     */
    long getSrvOperationState(Integer srvId) throws Exception;

    /**
     * 通过一组id 得到相应的对象
     * @param list
     * @return
     */
    public Result getGroupOfSrvMng(List<Integer> list) throws BusinessException;

}
