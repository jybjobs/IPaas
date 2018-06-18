package com.cloud.paas.systemmanager.service.status;

import com.cloud.paas.systemmanager.model.CodeStatus;
import com.cloud.paas.systemmanager.qo.status.StatusCondition;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.util.result.Result;

import java.util.List;
import java.util.Map;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-26 18:23
 * @Modified By:
 */
public interface StatusService extends BaseService<CodeStatus> {
    /**
     * 修改状态
     */
    Result doUpdateByCodeStatusBean(CodeStatus codeStatus);

    /**
     * 插入状态
     */
    Result doInsertByCodeStatusBean(CodeStatus codeStatus);

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
     * 获取所有的状态码
     * @return
     */
    Map<Integer,CodeStatus> selectAllStatus();

    /**
     * 获取所有的状态码
     * @return
     */
    Map<String,CodeStatus> selectAllStatusByCodeEn();

    /**
     * 通过code获取指定状态
     * @param code
     * @return
     */
    com.cloud.paas.util.codestatus.CodeStatus getStatusCodeByCode(int code);

    /**
     * 通过codeen获取指定状态
     * @param codeEn
     * @return
     */
    com.cloud.paas.util.codestatus.CodeStatus getStatusCodeBtCodeEn(String codeEn);

    /**
     * 分页，得到一个id的list
     * @param statusCondition
     * @return
     */
    List<Integer> findStatusIdListByCondition(StatusCondition statusCondition);

    /**
     * 根据条件（状态码/状态信息）查询状态码
     * @param statusCondition
     * @return
     */
    Result getCodeStatusByCondition(StatusCondition statusCondition);
}
