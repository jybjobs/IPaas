package com.cloud.paas.systemmanager.service.impl.config;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.dao.SysConfigDao;
import com.cloud.paas.systemmanager.model.SysConfig;
import com.cloud.paas.systemmanager.qo.config.ConfigCondition;
import com.cloud.paas.systemmanager.service.impl.BaseServiceImpl;
import com.cloud.paas.systemmanager.service.config.ConfigService;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("configService")
public class ConfigServiceImpl extends BaseServiceImpl<SysConfig> implements ConfigService  {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
    @Autowired
    private SysConfigDao sysConfigDao;
    @Override
    public BaseDAO<SysConfig> getBaseDAO() {
        return sysConfigDao;
    }

    /**
     * 根据中文名查找配置
     * @param configChName
     * @return
     */
    @Override
    public Result selectByChNameSrv(String configChName)  {
        logger.info("配置中文名为：{}",configChName);
        Result result = null;
        try {
            SysConfig config = sysConfigDao.selectByChName(configChName);
            result = CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_SUCCESS");
            if(config != null){
                result.setData(config);
            }else {
                result.setData("没有相关记录");
            }

        } catch (BusinessException e) {
            logger.error("查询配置失败！");
            e.printStackTrace();
            throw new  BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_FAILURE"));
        }
        return result;
    }

    /**
     *
     * @param configEnName
     * @return
     */
    @Override
    public Result selectByEnNameSrv(String configEnName) {
        logger.debug("配置英文名为：{}",configEnName);
        Result result = null;
        try {
            SysConfig config = sysConfigDao.selectByEnName(configEnName);
            result = CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_SUCCESS");
            if(config != null){
                result.setData(config);
            }else {
                result.setData("没有查到相关记录");
            }
        } catch (Exception e) {
            logger.error("通过英文名查询配置失败！");
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_FAILURE"));
        }
        return result;
    }

    /**
     * 分页条件查询配置列表
     * @param configCondition
     * @return
     */
    @Override
    public Result getAllConfig(ConfigCondition configCondition) throws BusinessException{
        //1.获取分页信息
        int pageNum = configCondition.getPageNum();
        int pageSize = configCondition.getPageSize();
        logger.info("----获取分页信息---pageNum:{},pageSize:{}",pageNum,pageSize);
        //2.设置分页条件
        Page page = PageHelper.startPage(pageNum, pageSize);
        //3.分页条件查询
        List<SysConfig> list;
        try {
            list= sysConfigDao.selectByAll(configCondition);
        }catch (Exception e){
            logger.error("查询配置失败!!!");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_FAILURE"));
        }
        PageInfo pageInfo = PageUtil.getPageInfo(page, list);
        Result result = CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_SUCCESS");
        result.setData(pageInfo);
        return result;
    }

    /**
     * 获取所有配置项，无分页信息
     * @return
     */
    @Override
    public Map getAllConfigList(){
       //1.获取所有配置项
        logger.info("获取所有配置项，无分页信息");
        List<SysConfig> sysConfigList = sysConfigDao.selectByAll(new ConfigCondition());
        //2.封装map
        Map map = new HashMap();
        if(sysConfigList != null){
            logger.info("配置项查询成功！");
            for(SysConfig sysConfig:sysConfigList){
                map.put(sysConfig.getSysConfigEnname(),sysConfig.getSysConfigValue());
            }
            logger.debug("配置map为：{}",String.valueOf(map));
            return map;

        }else {
            logger.error("获取所有配置项（全量查询，无分页信息，供程序调用部分）失败！查询结果为空");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_QUERY_FAILURE"));
        }

    }
    @Override
    public Result insertSelectiveSrv(SysConfig sysConfig) {
        //1.验证英文名是否重复
        logger.info("验证英文名：【{}】是否存在",sysConfig.getSysConfigEnname());
        if(sysConfigDao.selectByEnName(sysConfig.getSysConfigEnname())!= null){
           logger.error("英文名已存在！英文名：【{}】",sysConfig.getSysConfigEnname());
           throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_ADD_FAILURE_EN_USED"));
        }
        //2.验证中文名是否重复
        logger.info("验证中文名：【{}】是否存在",sysConfig.getSysConfigChname());
        if(sysConfigDao.selectByChName(sysConfig.getSysConfigChname())!=null){
            logger.error("中文名已存在！中文名：【{}]",sysConfig.getSysConfigEnname());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_ADD_FAILURE_ZH_USED"));
        }
        //3.插入数据库
        logger.info("-----插入配置到数据库-----");
        if(sysConfigDao.insertSelective(sysConfig) == 1){
            logger.info("插入配置成功！");
            Result result = CodeStatusUtil.resultByCodeEn("CONFIG_ADD_SUCCESS");
            return result;
        }else {
            logger.error("---------插入配置失败-----");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_ADD_FAILURE"));
        }

    }

    /**
     * 更新配置项
     * @param sysConfig
     * @return
     */
    @Override
    public Result updateByPrimaryKeySrv(SysConfig sysConfig) {
        logger.info("更新配置，配置id为：{}",sysConfig.getSysConfigId());
        //1.验证中文名是否重复
        logger.info("验证中文名：【{}】是否存在",sysConfig.getSysConfigChname());
        if(sysConfigDao.selectByChName(sysConfig.getSysConfigChname())!=null){
            logger.error("中文名已存在！中文名：【{}]",sysConfig.getSysConfigEnname());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_ADD_FAILURE_ZH_USED"));
        }
        //2.插入数据库
        logger.info("插入数据库");
        if(sysConfigDao.updateByPrimaryKey(sysConfig) == 1){
            logger.info("更新配置成功");
            return CodeStatusUtil.resultByCodeEn("CONFIG_UPDATE_SUCCESS");
        }else {
            logger.error("配置更新失败");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_UPDATE_FUILURE"));
        }
    }

    /**
     * 删除配置
     * @param sysConfigId
     * @return
     */
    @Override
    public Result deleteByPrimaryKeySrv(int sysConfigId) {
        logger.info("删除配置的id为：{}",sysConfigId);
        if(sysConfigDao.deleteByPrimaryKey(sysConfigId)==1){
            logger.info("删除配置成功！");
            return CodeStatusUtil.resultByCodeEn("CONFIG_DELETE_SUCCESS");
        }else {
            logger.error("配置删除失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("CONFIG_DELETE_FAILURE"));
        }
    }
}
