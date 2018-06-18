package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.CodeStatus;
import com.cloud.paas.systemmanager.qo.status.StatusCondition;

import java.util.List;

/**
 * @Author: wyj
 * @desc: 状态dao
 * @Date: Created in 2017-12-26 18:31
 * @Modified By:hzy
 */
public interface StatusDAO extends BaseDAO<CodeStatus>{

    /**
     * 通过code查找详细状态
     * @param code 状态码
     * @return result对象
     */
    CodeStatus findStatusByCode(int code);

    /**
     * 通过code描述查找详细状态
     * @param codeEn 英文描述
     * @return  result对象
     */
    CodeStatus findStatusByCodeEn(String codeEn);

    /**
     * 获取所有状态码
     * @return
     */
    List<CodeStatus> selectAllStatus();

    /**
     * 分页查找statusIdList
     * @param statusCondition
     * @return
     */
    List<CodeStatus> findStatusIdListByCondition(StatusCondition statusCondition);

    /**
     * 根据id列表，查找状态码对象
     * @param idList
     * @return
     */
    List<CodeStatus> findCodeStatusList(List<Integer> idList);
}
