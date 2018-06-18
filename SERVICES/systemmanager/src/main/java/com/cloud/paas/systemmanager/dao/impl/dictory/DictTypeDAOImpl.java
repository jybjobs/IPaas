package com.cloud.paas.systemmanager.dao.impl.dictory;

import com.cloud.paas.systemmanager.dao.DictTypeDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.DictType;
import com.cloud.paas.systemmanager.qo.dictionary.DictCondition;
import com.cloud.paas.systemmanager.vo.dictory.Dict;
import com.cloud.paas.systemmanager.vo.dictory.DictTypeItemVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dictTypeDAO")
public class DictTypeDAOImpl extends BaseDAOImpl<DictType> implements DictTypeDAO {
    public static final String NAME_SPACE = "DictTypeDAO";

    @Override
    public  String getNameSpace(){
        return DictTypeDAOImpl.NAME_SPACE;
    }


    @Override
    public List<DictType> listDictType() {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchList");
    }

    /**
     * 设置禁用/启用
     * @param bean
     * @return
     */
    @Override
    public int toggleDisableDictType(DictType bean) {
        return sqlSessionTemplate.update(this.getNameSpace()+".doChangeDisable",bean);
    }

    /**
     * 设置删除/恢复
     * @param bean
     * @return
     */
    @Override
    public int toggleDelDictType(DictType bean) {
        return sqlSessionTemplate.update(this.getNameSpace()+".doChangeDel",bean);
    }


    @Override
    public DictType doFindByName(String dictTypeName) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByName",dictTypeName);
    }

    /**
     * 根据字典类型名获取字典类型和字典内容
     * @param dictTypeNames
     * @return
     */
    @Override
    public List<DictTypeItemVO> getItemByTypeName(List<String> dictTypeNames) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".getItemByTypeName",dictTypeNames);
    }

    /**
     * 分页查找DictIdList
     * @param dictCondition
     * @return
     */
    @Override
    public List<DictType> findDictIdListByCondition(DictCondition dictCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".findDictIdListByCondition",dictCondition);
    }

    /**
     * 分页查询IdList
     */
    @Override
    public List<Dict> findDictIdListByConditionResult(List<Integer> idList){
        return sqlSessionTemplate.selectList(this.getNameSpace()+".findDictIdListByConditionResult",idList);
    }

    /**
     * 根据条件（类型名称/类型标题/内容名称）查询字典
     * @param dictCondition
     * @return
     */
    @Override
    public List<Dict> searchDict(DictCondition dictCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".searchDict",dictCondition);
    }

    /**
     * 获取所有字典
     * @return
     */
    @Override
    public List<Dict> getAllDict() {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".getAllDict");
    }

    @Override
    public DictType doFindByTitle(String title) {
        return sqlSessionTemplate.selectOne (this.getNameSpace ()+".doFindByTitle",title);
    }


}
