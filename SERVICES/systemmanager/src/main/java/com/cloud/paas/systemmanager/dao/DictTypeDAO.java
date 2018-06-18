package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.DictType;
import com.cloud.paas.systemmanager.qo.dictionary.DictCondition;
import com.cloud.paas.systemmanager.vo.dictory.Dict;
import com.cloud.paas.systemmanager.vo.dictory.DictTypeItemVO;

import java.util.List;

public interface DictTypeDAO extends BaseDAO<DictType> {
    /**
     * 获取全部字典类型
     * @return
     */
    public List<DictType> listDictType();

    /**
     * 设置字典类型启用状态
     * @param dictType
     * @return
     */
    public int toggleDisableDictType(DictType dictType);

    /**
     * 设置字典类型删除状态
     * @param dictType
     * @return
     */
    public int toggleDelDictType(DictType dictType);

    /**
     * 根据字典名称查找字典类型
     * @param dictTypeName
     * @return
     */
    public DictType doFindByName(String dictTypeName);

    /**
     * 根据多个字典类型名称列出字典类型及内容
     * @param dictTypeNames
     * @return
     */
    public List<DictTypeItemVO> getItemByTypeName(List<String> dictTypeNames);

    /**
     * 分页
     * @param dictCondition
     * @return
     */
    List<DictType> findDictIdListByCondition(DictCondition dictCondition);
    /**
     * 分页查询IdList
     */
    List<Dict> findDictIdListByConditionResult(List<Integer> idList);


    /**
     * 根据条件（类型名/类型标题/字典名称）查询字典类型及内容
     * @param dictCondition
     * @return
     */
    List<Dict> searchDict(DictCondition dictCondition);

    /**
     * 获取所有字典
     * @return
     */
    List<Dict> getAllDict();
    DictType doFindByTitle(String title);
}
