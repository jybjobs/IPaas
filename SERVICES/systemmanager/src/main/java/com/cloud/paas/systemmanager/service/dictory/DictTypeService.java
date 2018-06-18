package com.cloud.paas.systemmanager.service.dictory;

import com.cloud.paas.systemmanager.model.DictType;
import com.cloud.paas.systemmanager.qo.dictionary.DictCondition;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.util.result.Result;

import java.util.List;

public interface DictTypeService extends BaseService<DictType> {

    public Result insertDict(DictType dictType);

    public Result updateDict(DictType dictType);

    Result listDictType();

    public int disableDictType(int dictTypeId);
    public int enableDictType(int dictTypeId);

    public Result delDictType(int dictTypeId);
    public int recoverDictType(int dictTypeId);

    public DictType doFindByName(String dictTypeName);

    Result getItemByTypeName(List<String> dictTypeNames);

    /**
     * 分页，得到一个id的list
     * @param dictCondition
     * @return
     */
    List<Integer> findDictIdListByCondition(DictCondition dictCondition);
    /**
     * 分页 条件查询字典
     */
    Result findDictIdListByConditionResult(DictCondition dictCondition) ;


        /**
         * 条件查询字典
         * @param condition
         * @return
         */
    Result searchDict(DictCondition condition);
    Result getAllDict();
}
