package com.cloud.paas.appservice.service.srv.ctn;

import com.cloud.paas.appservice.model.CtnDetailInfo;
import com.cloud.paas.appservice.service.BaseService;
import com.cloud.paas.util.result.Result;

/**
 * @author yht
 * @create 2017-12-15 13:58
 **/
public interface CtnMngService extends BaseService<CtnDetailInfo> {
    /**
     * 查询所有容器信息
     *
     * @return
     */
    public Result listAllCtn();


    /**
     * 查询指定用户下所有应用所有服务下所有容器
     */
    public Result listUserGivenAppSrvDetailCnts(Integer userId);

    /**
     * 查询指定用户下指定应用所有服务下所有容器
     */
    public Result listCtnUserGivenAppGivenSrv(Integer userId, String appname);

    /**
     * 查询指定用户下指定应用指定服务下所有容器
     */
    public Result userGivenAppGivenSrvDetailGivenCnts(Integer userId, String appname, String srvname);

    /**
     * @Author: srf
     * 根据app查询容器名称
     */
    public Result listCtnByApp(String app);

    /**
     * @Author: srf
     * 根据Deployment名称查询limits
     */
    public Result getCtnLimitsByDeployName(String name);

    /**
     * @Author: srf
     * 根据容器名称查询容器
     */
    public Result getCtnByCtnName(String name);

    /**
     * @Author: srf
     * 根据容器名称查询limits
     */
    public Result getCtnLimitsByCtnName(String name);
}
