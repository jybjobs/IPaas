package com.cloud.paas.appservice.dao;

import com.cloud.paas.appservice.model.SrvOperation;
import com.cloud.paas.appservice.qo.AppExample;
import com.cloud.paas.appservice.qo.SrvCondition;
import com.cloud.paas.appservice.vo.srv.SrvDetailOrderVO;
import com.cloud.paas.exception.BusinessException;

import java.util.List;

/**
 * @Author: xujia
 * @Description:
 * @Date: Create in 19:18 2017/11/20
 * @Modified by:
 */
public interface SrvOperationDAO extends BaseDAO<SrvOperation> {

    /**
     * 根据查询条件查询服务状态、启动时间、停止时间
     * @param appExample
     * @return
     */
    List<SrvDetailOrderVO> listByExample(AppExample appExample);

    /**
     * 根据一组应用id查询服务状态、启动时间、停止时间
     * @param appIds
     * @return
     */
    List<SrvDetailOrderVO> listByIds(Integer[] appIds);

    public Integer doSetState(SrvCondition srvCondition);

    /**
     * 获取服务的最新状态
     * @param srvId
     * @return
     * @throws Exception
     */
    long getSrvOperationState(Integer srvId) throws BusinessException;
    /**
     * 通过一组id 得到相应的对象
     * @param list
     * @return
     */
    List<SrvOperation> getGroupOfSrvMng(List<Integer> list) throws BusinessException;

    /**
     * 根据状态码查询最新的服务id集合
     * @param state
     * @return
     * @throws Exception
     */
    List<Integer> getSrvIdByCodeStatus(Integer state) throws Exception;

}
