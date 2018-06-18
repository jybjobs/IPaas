package com.cloud.paas.systemmanager.dao.impl.dictory;

import com.cloud.paas.systemmanager.dao.DictItemDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.DictItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zcy
 * @desc: 字典内容Dao实现
 * @Date: Created in 2017-12-14 14:08
 * @modified By:
 **/

@Repository("dictItemDao")
public class DictItemDAOImpl extends BaseDAOImpl<DictItem> implements DictItemDAO {
    @Override
    public String getNameSpace() {
        return "DictItemDAO";
    }

    /**
     * 设置删除/恢复
     * @param bean
     * @return
     */
    @Override
    public int toggleDelDictItem(DictItem bean) {
        return sqlSessionTemplate.update(this.getNameSpace()+".doChangeDel",bean);
    }

    @Override
    public List<DictItem> doSearchByTypeId(int dictTypeId) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchByTypeId",dictTypeId);
    }

    /**
     * 根据dictItemName查找信息
     * @param dictItem
     * @return
     */
    @Override
    public DictItem findDictItemName(DictItem dictItem) {
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".doFindByDictItemName",dictItem);
    }

    /**
     * 根据dictItemCode查找信息
     * @param dictItem
     * @return
     */
    @Override
    public DictItem findDictItemCode(DictItem dictItem) {
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".doFindByDictItemCode",dictItem);
    }

}
