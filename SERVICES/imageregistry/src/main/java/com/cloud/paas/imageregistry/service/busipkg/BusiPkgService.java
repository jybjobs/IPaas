package com.cloud.paas.imageregistry.service.busipkg;

import com.cloud.paas.imageregistry.model.BusiPkgDetail;
import com.cloud.paas.imageregistry.qo.BusiPkgExample;
import com.cloud.paas.imageregistry.service.BaseService;
import com.cloud.paas.imageregistry.vo.busipkg.BusiPkgListVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.remoteinfo.RemoteServerInfo;
import com.cloud.paas.util.result.Result;

import java.util.List;

/**
* @Author: xujia
* @Description: 业务包详细信息Service接口
* @Date: 14:08 2017/11/24
*/
public interface BusiPkgService  extends BaseService<BusiPkgDetail> {
    /**
     * ftp上传
     */
  // Result ftpUpLoadBusiPkgFile(String userid,RemoteServerInfo remoteServerInfo,int flag)throws Exception;
    /**
     * ftp下载
     */
    Result ftpDownLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException;
    /**
     * scp上传
     */
    //Result scpUpLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException;

    /**
     * scp下载
     */
    Result scpDownLoadBusiPkgFile(String userId, RemoteServerInfo remoteServerInfo, int flag) throws BusinessException;
    /**
     * 新建业务包
     */
    Result doInsertByBusiPkgDetailBean(BusiPkgDetail busiPkgDetail) throws Exception;
    /**
     * 删除平台上已经存在的业务包
     */
    Result deleteBusiPkgById(int busiPkgId) throws BusinessException;
    /**
     * 修改业务包的属性信息
     */
    Result doUpdateByBusiPkgBean(BusiPkgDetail busiPkgDetail) throws BusinessException;
    /**
     * 重写获取指定用户下的业务包
     */
    Result doFindByBusiPkgId(int busiPkgId) throws BusinessException;
    /**
     * 查询业务包详细信息列表
     * @return 业务包详细信息列表
     */
    public Result listFindByPkgId() throws Exception;
    /**busiPKgId条件业务包详情查询
     * params:busiPkgId
     * return 	List<BusiPkgListVO>
     */
     Result selectVersionVOList(int busiPkgId) throws Exception;
    /**条件业务包详情查询
     * params:condition
     * return 	List<BusiPkgListVO>
     */
     List<BusiPkgListVO> selectVersionVOListByExample(BusiPkgExample busiPkgExample);

    /**
     * 分页
     */
    List<Integer> findBusiPkgIdListByExample(BusiPkgExample busiPkgExample);

    /**
     * 分页查询
     */
     Result findBusiPkgIdListByExampleResult(BusiPkgExample busiPkgExample) throws BusinessException;

    /**
     * 检查业务包中文名是否重复
     * @param busiPkgNameZh
     * @return
     */
    Result checkNameZh(String busiPkgNameZh);

    /**
     * 检查业务包英文名是否重复
     * @param busiPkgNameEn
     * @return
     */
    Result checkNameEn(String busiPkgNameEn);
}
