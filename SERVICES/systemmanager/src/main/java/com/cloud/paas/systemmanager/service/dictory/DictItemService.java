package com.cloud.paas.systemmanager.service.dictory;

import com.cloud.paas.systemmanager.model.DictItem;
import com.cloud.paas.systemmanager.service.BaseService;
import com.cloud.paas.systemmanager.vo.dictory.ItemSort;
import com.cloud.paas.util.result.Result;

import java.util.List;

/**
 *DictItemService
 */
public interface DictItemService extends BaseService<DictItem>{
    /**
     * 删除字典内容
     * @param dictItemId
     * @return
     */
    Result delDictItem(int dictItemId);

    Result insertDictItem(DictItem dictItem);

    Result updateDictItem(DictItem dictItem);

    /**
     *编辑字典内容排列顺序
     * @param itemSorts
     * @return
     */
    Result sortDictItem(List<ItemSort> itemSorts);

    Result doSearchByTypeId(int dictTypeId);
}
