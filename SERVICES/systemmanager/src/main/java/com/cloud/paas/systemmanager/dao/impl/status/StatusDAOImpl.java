package com.cloud.paas.systemmanager.dao.impl.status;

import com.cloud.paas.systemmanager.dao.StatusDAO;
import com.cloud.paas.systemmanager.dao.impl.BaseDAOImpl;
import com.cloud.paas.systemmanager.model.CodeStatus;
import com.cloud.paas.systemmanager.qo.status.StatusCondition;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wyj
 * @desc:
 * @Date: Created in 2017-12-26 18:34
 * @Modified By: hzy
 */
@Repository("StatusDAO")
public class StatusDAOImpl extends BaseDAOImpl<CodeStatus> implements StatusDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public static final String NAME_SPACE = "StatusDAO";

    @Override
    public  String getNameSpace(){
        return StatusDAOImpl.NAME_SPACE;
    }

    /**
     * 通过状态码查询状态详细信息
     * @param code 状态码
     * @return
     */
    @Override
    public CodeStatus findStatusByCode(int code) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".findStatusByCode",code);
    }

    /**
     * 通过英文描述查询状态详细信息
     * @param codeEn 英文描述
     * @return
     */
    @Override
    public CodeStatus findStatusByCodeEn(String codeEn) {
        return sqlSessionTemplate.selectOne(this.getNameSpace()+".findStatusByCodeEn",codeEn);
    }

    /**
     * 获取所有的状态码
     * @return
     */
    @Override
    public List<CodeStatus> selectAllStatus() {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".selectAllStatus");
    }

    /**
     * 分页查找statusIdList
     * @param statusCondition
     * @return
     */
    @Override
    public List<CodeStatus> findStatusIdListByCondition(StatusCondition statusCondition) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".findStatusIdListByCondition",statusCondition);
    }

    /**
     * 根据id列表，查找状态码对象
     * @param idList
     * @return
     */
    @Override
    public List<CodeStatus> findCodeStatusList(List<Integer> idList) {
        return sqlSessionTemplate.selectList(this.getNameSpace()+".findCodeStatusList",idList);
    }
}
