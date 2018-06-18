package com.cloud.paas.systemmanager.dao;

import com.cloud.paas.systemmanager.model.DictItem;

import java.util.List;

/**
 * @author: zcy
 * @desc: 键值对dao接口
 * @Date: Created in 2017-12-14 14:04
 * @modified By:
 **/
public interface DictItemDAO extends BaseDAO <DictItem>{
    /**
     * 字典内容删除
     * @param dictitem
     * @return
     */
    int toggleDelDictItem(DictItem dictitem);

    List<DictItem>doSearchByTypeId(int dictTypeId);

    DictItem findDictItemName(DictItem dictItem);

    DictItem findDictItemCode(DictItem dictItem);
}
