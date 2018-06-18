package com.cloud.paas.appservice.service.srv;

import com.cloud.paas.appservice.model.AppSrvRel;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.qo.SrvExample;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.qo.SrvVersionDetailExample;
import com.cloud.paas.appservice.service.BaseService;
import com.cloud.paas.appservice.vo.srv.SrvDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvVersionDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface SrvMngService extends BaseService<SrvDetail> {
    /**
     * 创建服务
     *
     * @param srvDetailVO
     * @return
     */
    public Result doInsertBySrvDetail(SrvDetailVO srvDetailVO) throws BusinessException;

    /**
     * 更新服务
     *
     * @param srvDetailVO
     * @return
     */
    public Result doUpdateBySrvDetail(SrvDetailVO srvDetailVO) throws BusinessException;

    /**
     * 更新服务 devops
     *
     * @param srvDetailVO
     * @return
     */
    public Result doUpdateProject(SrvDetailVO srvDetailVO) throws BusinessException;

    /**
     * 更新服务启动顺序
     *
     * @param appSrvRels
     * @return
     */
    public Result doUpdateByAppSrvRel(List<AppSrvRel> appSrvRels) throws  BusinessException;

    /**
     * 删除服务
     *
     * @param srvId
     * @return
     */
    public Result doDeleteSrvDetailById(Integer srvId) throws  BusinessException;

    /**
     * 通过服务id 查询服务
     *
     * @param srvId
     * @return
     */
    public Result doFindSrvDetailById(Integer srvId) throws  BusinessException;

    /**
     * 所有用户下的所有应用的所有服务
     *
     * @return
     */
    public Result listUserAppSrvDetail() throws  BusinessException;

    /**
     * 指定用户的指定应用下的所有服务列表
     *
     * @param userId, appname
     * @return
     */
    public Result listUserGivenAppGivenSrvDetail(Integer userId, String appname) throws BusinessException;

    /**
     * 指定用户的指定应用下的指定服务列表
     *
     * @param :userId, appname，srvname
     * @return:SrvDetail
     */
    public Result userGivenAppGivenSrvDetailGiven(Integer userId, String appname, String srvname) throws BusinessException;

    /**
     * 改变服务的状态：1：开启，2：停止，3：扩容，4：缩容
     */
    public Result SrvStatusChanged(Integer userId, String appname, String srvname, Long flag)throws BusinessException;

    public Result listUserSrvDetail(int userId, SrvExample srvExample) throws BusinessException;

    public Result listQueryConditions(SrvExample srvExample) throws  BusinessException;

    /**
     * 获取服务的实时状态
     * @param
     * @return
     * @throws Exception
     */
   /* public long getSrvRealtimeState(Integer id) throws Exception;*/

    public Result findAll(int srvImageVersionId);

    /**
     * 上传服务图片
     *
     * @param file   服务图片
     * @return 文件存放路劲
     */
    Result uploadFile(MultipartFile file) throws BusinessException;

    /*********************************************服务定义*************************************************/

    /**
     * 查询服务定义信息
     * @param list
     * @return
     * @throws BusinessException
     */
    Result listSrvVersionDetailByIds(List<Integer> list)throws BusinessException;

    /**
     * 查询服务定义信息
     * @param userid
     * @param srvVersionDetailExample
     * @return
     * @throws BusinessException
     */
    Result listSrvVersionDetail(String userid, SrvVersionDetailExample srvVersionDetailExample)throws BusinessException;



    /**
     * 创建服务定义
     * @param userid
     * @param srvVersionDetailVO
     * @return
     * @throws BusinessException
     */
    Result createSrvDef(String userid, SrvVersionDetailVO srvVersionDetailVO)throws BusinessException;

    /**
     * 创建并构建镜像并推入仓库
     * @param userid
     * @param srvVersionDetailVO
     * @return 是否成功
     */
    Result createAndBuild(String userid, SrvVersionDetailVO srvVersionDetailVO)throws BusinessException;

    /**
     * 调用rest接口，构建镜像并且推入仓库
     */
    Result createAndBuildSrvImage(String userid,int srvVersionId,String url)throws BusinessException;

    /**
     * 同步服务版本镜像状态
     * @return
     * @throws BusinessException
     */
    void syncSrvVersionImageStatus() throws BusinessException;

    /**
     * 删除服务定义
     * @param userid
     * @param srvVersionId
     * @return
     */
    Result deleteSrvDef(String userid,Integer srvVersionId);

    /**
     * 构建服务镜像并推入仓库
     * @param userid
     * @param srvVersionId
     * @return
     */
    Result buildSrvImage(String userid, Integer srvVersionId);

    /*********************************************服务实例*************************************************/

    /**
     * 根据主键查询服务实例详情
     * @param srvInstId
     * @return
     * @throws BusinessException
     */
    Result findSrvInst(Integer srvInstId)throws BusinessException;

    /**
     * 创建服务实例
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    int doInsertSrvInst(SrvInstDetailVO srvInstDetailVO)throws BusinessException;

    /**
     * 创建服务实例并启动
     * @param userid
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    Result createSrvInst(String userid,SrvInstDetailVO srvInstDetailVO)throws BusinessException;

    /**
     * 根据条件查询服务实例（列表）
     * @param srvInstDetailExample
     * @param userid
     * @return
     */
    Result listSrvInstByCondition(String userid,SrvInstDetailExample srvInstDetailExample);

    /**
     * 通过服务实例编号获取deployment信息
     * @param srvInstId
     * @return
     */
    Result doFindDeploymentInfo(Integer srvInstId);

    /**
     * 版本切换
     * @param userid
     * @param srvInstDetailExample
     * @return
     */
    Result srvVersionChange(String userid,SrvInstDetailExample srvInstDetailExample);

    /**
     * 一键升级
     * @param userid
     * @param srvInstDetailExample
     * @return
     */
    Result srvVersionUpdate(String userid,SrvInstDetailExample srvInstDetailExample);

    /**
     * jekins调用升级接口
     * @param userid
     * @param appNameEn
     * @param srvNameEn
     * @return
     */
    Result jekinsVersionUpdate(String userid,String appNameEn,String srvNameEn);

    /**
     * 轮询业务包状态信息
     * @return
     */
    Result pollingBusiPkgStatus();

    /**
     * 删除服务实例
     * @param userid
     * @param srvInstId
     * @return
     */
    Result deleteSrvInst(String userid,Integer srvInstId);

    /**
     * 修改服务实例
     * @param userid
     * @param srvInstDetailVO
     * @return
     */
    Result editSrvInst(String userid,SrvInstDetailVO srvInstDetailVO);

    /**
     * 修改服务实例
     * @param srvInstDetailVO
     * @return
     * @throws BusinessException
     */
    int doUpdateSrvInst(SrvInstDetailVO srvInstDetailVO);

    /**
     * 启动服务
     * @param srvInstId
     * @return
     */
    Result srvInstRun(Integer srvInstId);

    /**
     * 停止服务
     * @param srvInstId
     * @return
     */
    Result srvInstStop(Integer srvInstId);

    /**
     * 轮询容器日志
     * @param srvInstId
     * @return
     */
    Result rollingContainerLog(Integer srvInstId);

    /**
     * 服务编排顺序
     * @param srvInstDetails
     * @return
     */
    Result doUpdateBySrvInstDetail(List<SrvInstDetail> srvInstDetails);
}
