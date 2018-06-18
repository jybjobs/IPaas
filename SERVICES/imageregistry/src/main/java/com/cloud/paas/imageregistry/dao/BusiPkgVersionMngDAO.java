package com.cloud.paas.imageregistry.dao;

import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgVersionDetailVO;

import java.util.List;

/**
 * @Author: css
 * @Description:业务版本接口
 * @Date: Create in 10:30 2017/11/22
 * @Modified by:
 */

public interface BusiPkgVersionMngDAO extends BaseDAO<BusiPkgVersionDetail> {

    /**
     * 查询业务包版本不带基本信息
     * @param id
     * @return
     */
    BusiPkgVersionDetail selectByPrimaryKey(int id);
    /**
     * 获取指定业务包下的版本列表
     * @param busiPkgId 业务包Id
     * @return List<BusiPkgVersionDetail>接收BusiPkgVersionDetailVO子类的版本列表
     */
    List<BusiPkgVersionDetail> listFindByPkgId(Integer busiPkgId);//findByPkgId  busiPkgId
    /**
     * 业务包查询条件搜索信息 版本+基本信息结构
     * @param busiPkgExample
     * @return List<BusiPkgVersionDetailVO>业务包查询条件搜索信息
     */
    List<BusiPkgVersionDetailVO> listFindByExample(BusiPkgExample busiPkgExample);

    /**
     * 计算版本个数
     * @param busiPkgId
     * @return
     */
    int countVersion(int busiPkgId);

    /**
     * 指定业务包下版本总大小
     * @param busiPkgId
     * @return
     */
    long getBusiPkgSize(int busiPkgId);

    /**
     * 指定版本的业务包数量
     * @param busiPkgExample
     * @return
     */
    int countByVersion(BusiPkgExample busiPkgExample);

    /**
     * 根据业务包id删除版本
     *
     * @param busiPkgId
     * @return
     */
    int doDeleteByPkgId(int busiPkgId);

    /**
     * 根据业务包Id查询业务包版本
     * @param pkgIds
     * @return
     */
    List<BusiPkgVersionDetail> getStatusByPkgIds(List<Long> pkgIds);


}
