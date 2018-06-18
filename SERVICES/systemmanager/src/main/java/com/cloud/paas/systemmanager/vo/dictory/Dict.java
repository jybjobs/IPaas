package com.cloud.paas.systemmanager.vo.dictory;

import com.cloud.paas.systemmanager.model.DictItem;
import com.cloud.paas.systemmanager.model.DictType;

import java.util.List;

/**
 * 字典，按条件查询返回值类型
 */

public class Dict extends DictType {
    private List<DictItem> dictItems;

    public List<DictItem> getDictItems() {
        return dictItems;
    }

    public void setDictItems(List<DictItem> dictItems) {
        this.dictItems = dictItems;
    }
}
