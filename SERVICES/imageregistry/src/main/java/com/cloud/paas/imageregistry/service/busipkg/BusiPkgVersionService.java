package com.cloud.paas.imageregistry.service.busipkg;

import com.alibaba.fastjson.JSONArray;
import com.cloud.paas.imageregistry.model.BusiPkgVersionDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.service.BaseService;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgVersionDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: css
 * @Description:业务版本服务接口
 * @Date: Create in 10:30 2017/11/22
 * @Modified by:
 */
public interface BusiPkgVersionService extends BaseService<BusiPkgVersionDetail> {
    /**
     * 删除指定业务版本
     */
    Result doDeleteByBusiPkgVersionId(int busiPkgVersionId) throws BusinessException;

    /**
     * 更新业务版本
     */
    Result doUpdateByBusiPkgVersionBean(String userid, BusiPkgVersionDetail busiPkgVersionDetail) throws BusinessException;

    /**
     * 获取单表业务版本信息
     */
    Result doFindByBusiPkgVersionId(int busiPkgVersionId) throws BusinessException;

    /**
     * 根据业务包编号集合查询业务包当前状态，并返回已上传的服务版本编号集合
     * @param srvVersionInfos
     * @return
     * @throws BusinessException
     */
    Result findStatusByIdsReturnSrvVersionIds(JSONArray srvVersionInfos)throws BusinessException;

    /**
     * 新增版本
     *
     * @param busiPkgVersionDetail
     * @param userId
     * @return
     */
    Result doInsertByBean(BusiPkgVersionDetail busiPkgVersionDetail, String userId) throws BusinessException;

    /**
     * 自动生成业务包版本
     *
     * @param busiPkgId
     * @return
     * @throws Exception
     */
    public Result insertVersionByBusiPkgId(String userid,Integer busiPkgId) throws BusinessException;

    /**
     * 获取指定业务包下的版本列表
     *
     * @param pkgId
     * @return List<BusiPkgVersionDetail>接收BusiPkgVersionDetailVO子类的版本列表
     */
    Result listFindByPkgId(int pkgId) throws BusinessException;

    /**
     * 业务包查询条件搜索信息 版本+基本信息结构
     *
     * @param busiPkgExample
     * @return List<BusiPkgVersionDetailVO>业务包查询条件搜索信息
     */
    Result listFindByExample(BusiPkgExample busiPkgExample) throws BusinessException;

    /**
     * 上传业务包
     *
     * @param file   本地业务包文件
     * @param userid 用户id
     * @return 文件存放路劲
     */
    Result uploadFile(MultipartFile file, String userid) throws BusinessException;

    /**
     * 上传图片
     *
     * @param img    本地图片
     * @param userid 用户id
     * @return 图片存放路径
     */
    Result uploadImg(MultipartFile img, String userid) throws BusinessException;

    /**
     * 直接新建业务包和业务版本
     *
     * @param userid                 用户id
     * @param busiPkgVersionDetailVO 业务版本+业务信息结构的VO
     * @return 记录改变数目
     */
    Result insertPkgAndVersion(String userid, BusiPkgVersionDetailVO busiPkgVersionDetailVO) throws BusinessException;

    /**
     * 计算业务包下版本个数
     *
     * @param busiPkgId
     * @return
     */
    Result countVersion(int busiPkgId);

    /**
     * 校验业务包版本是否重复
     *
     * @param busiPkgExample
     * @param
     */
    Result checkVersion(BusiPkgExample busiPkgExample);

    /**
     * 获取刷新状态
     * @param userid
     * @param pkgIds
     * @return
     */
    public Result refreshStatus(String userid, List<Long> pkgIds);

}
