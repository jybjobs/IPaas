package com.cloud.paas.appservice.service.impl.srv;/**
 * 服务创建删除校验
 *
 * @author
 * @create 2017-12-28 22:19
 **/

import com.cloud.paas.appservice.dao.SrvInstDetailDAO;
import com.cloud.paas.appservice.dao.SrvMngDAO;
import com.cloud.paas.appservice.dao.SrvVersionDetailDAO;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.appservice.model.SrvInstDetail;
import com.cloud.paas.appservice.model.SrvVersionDetail;
import com.cloud.paas.appservice.qo.SrvInstDetailExample;
import com.cloud.paas.appservice.vo.srv.SrvDetailVO;
import com.cloud.paas.appservice.vo.srv.SrvInstDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusContant;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.date.DateUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务创建删除校验
 *
 * @author
 * @create 2017-12-28 22:19
 **/
@Service
public class VerifySrv {
    private static final Logger logger = LoggerFactory.getLogger(VerifySrv.class);

    @Autowired
    SrvMngDAO srvMngDAO;
    @Autowired
    SrvInstDetailDAO srvInstDetailDAO;
    @Autowired
    SrvVersionDetailDAO srvVersionDetailDAO;

    DateUtil dateUtil = new DateUtil();

    public void createSrvVerify(SrvDetailVO srvDetailVO) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_BUILD_FAILURE);
        //1.服务中英文名校验
        logger.info("服务中英文名校验");
        String nameEn = srvDetailVO.getSrvNameEn();
        String nameCh = srvDetailVO.getSrvNameCh();
        if ((nameEn != null) && (nameEn != "") && (nameCh != null) && (nameCh != "")) {
            SrvDetail srvDetail = srvMngDAO.findNameEn(nameEn);
            SrvDetail srvDetail1 = srvMngDAO.findNameZh(nameCh);
            if ((srvDetail != null) || (srvDetail1 != null)) {
                result.setMessage("服务已存在");
                throw new BusinessException(result);
            }
        } else {
            result.setMessage("请输入服务名");
            throw new BusinessException(result);
        }

        // 2.判断是否有汉字
        logger.info("判断是否有汉字");
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(nameEn);
        if (m.find()) {
            result.setMessage("英文名称不能含有中文");
            throw new BusinessException(result);
        }
        //2.判断环境变量是否为空
//        logger.info("判断环境变量是否为空");
//        List<SrvEnvRel> srvEnvRels = srvDetailVO.getSrvEnvRels();
//        for (SrvEnvRel srvEnvRel : srvEnvRels) {
//            if (srvEnvRel.getEnvKey() == "" | srvEnvRel.getEnvValue() == "") {
//                result.setMessage("环境变量不能为空");
//                throw new BusinessException(result);
//            }
//        }
        //3.判断nodeport端口是否已存在
//        logger.info("判断nodeport端口是否存在");
//        int nodePort = srvDetailVO.getNodePort();
//        SrvDetail srvDetail2 = srvMngDAO.findNodePort(nodePort);
//        if(srvDetail2 != null){
//            result.setMessage("NodePort已被使用，请重新设定！");
//            throw new BusinessException(result);
//        }
    }

    //更新服务验证
    public void updateSrvVerify(SrvDetail srvDetail) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_FAILURE);
        //1.修改服务中文名校验
        String srvNameCh = srvDetail.getSrvNameCh();
        int srvId = srvDetail.getSrvId();
        if ((srvNameCh != null) && (srvNameCh != "")) {
            logger.info("服务查重");
            SrvDetail srvDetail2 = srvMngDAO.findNameZh(srvNameCh);
            if ((srvDetail2 != null)) {
                int srvId2 = srvDetail2.getSrvId();
                if(srvId != srvId2){
                    result.setMessage("服务中文名已存在");
                    throw new BusinessException(result);
                }
            }
        }
        else{
            result.setMessage("服务中文名不能为空");
            throw new BusinessException(result);
        }
    }

    //服务id验证
    public Result verifySrvId(int srvId) throws BusinessException{
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        if (srvId != 0) {
            result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATED);
            result.setData(srvId);
            return  result;
        }
        else {
            throw new BusinessException(result);
        }
    }

    /**
     * 创建服务实例校验
     * @param srvInstDetailVO
     * @throws BusinessException
     */
    public void createSrvInstVerify(SrvInstDetailVO srvInstDetailVO) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_INSTANCE_CREATE_FAILURE);
        //服务实例重复校验
        logger.info("服务实例重复校验");
        //1.获取服务实例信息
        logger.info("1.获取服务实例信息");
        Integer appId = srvInstDetailVO.getAppId();
        Integer srvVersionId = srvInstDetailVO.getSrvVersionId();
        SrvVersionDetail srvVersionDetail = srvVersionDetailDAO.doFindById(srvVersionId);
        //2.校验关联应用，服务定义
        logger.info("2.校验关联应用，服务定义");
        if(appId == null){
            result.setMessage("未选择关联应用");
            throw new BusinessException(result);
        }else if(srvVersionId == null){
            result.setMessage("未选择服务定义");
            throw new BusinessException(result);
        }else if(srvVersionDetail == null){
            result.setMessage("服务定义不存在");
            throw new BusinessException(result);
        }else{
            //3.校验服务是否存在
            logger.info("3.校验服务是否存在");
            SrvInstDetailExample srvInstDetailExample = new SrvInstDetailExample();
            srvInstDetailExample.setSrvId(srvVersionDetail.getSrvId());
            srvInstDetailExample.setAppId(appId);
            List<SrvInstDetailVO> list = srvInstDetailDAO.listSrvInstByCondition(srvInstDetailExample);
            if(list != null && list.size() > 0){
                result.setMessage("该服务已存在，只能升级或切换版本，无法创建");
                throw new BusinessException(result);
            }
        }
    }

    /**
     * 服务实例状态校验，如果服务实例状态为中间状态，则抛出异常,否则返回状态值
     * 未发布=>2134000 初始状态
     * 创建中=>2133000,启动中=>2130000 中间状态
     * 构建成功=>2131000,失败=>2132000,运行中=>2135000 最终状态
     * @param srvInstDetailExample
     * @return code 返回服务实例状态值
     * @throws BusinessException
     */
    public void srvInstStatusVerify(SrvInstDetailExample srvInstDetailExample) throws BusinessException{
        int status;
        Result result = CodeStatusUtil.resultByCodeEn(CodeStatusContant.SERVICE_MODIFY_FAILURE);
        //1.获取当前服务实例信息
        int srvInstId = srvInstDetailExample.getCurrentSrvInstId();
        SrvInstDetail srvInstDetail = srvInstDetailDAO.doFindById(srvInstId);
        if (srvInstDetail != null) {
            //2.服务实例状态校验
            logger.info("服务实例状态校验");
            status = srvInstDetail.getSrvInstStatus();
            if(status == 2133000 || status == 2130000){
                result.setMessage("当前服务实例属于中间状态,无法升级或切换版本");
                throw new BusinessException(result);
            }
        }else{
            result.setMessage("服务实例不存在");
            throw new BusinessException(result);
        }
        //3.对校验状态返回值分析 校验失败=>0,构建成功=>2131000,失败=>2132000,运行中=>2135000,未发布=>2134000
        if(status == 0){
            result.setMessage("校验失败");
            result.setCode(String.valueOf(status));
            throw new BusinessException(result);
        }else{
            logger.debug("当前服务实例处于初始状态或者最终状态");
            SrvInstDetail curSrvInstDetail = srvInstDetailDAO.doFindById(srvInstDetailExample.getCurrentSrvInstId());
            //4.更新当前服务实例状态为历史
            curSrvInstDetail.setUpdateTime(dateUtil.getCurrentTime());
            curSrvInstDetail.setHistory(1);
            srvInstDetailDAO.doUpdateByBean(curSrvInstDetail);
        }
    }
}