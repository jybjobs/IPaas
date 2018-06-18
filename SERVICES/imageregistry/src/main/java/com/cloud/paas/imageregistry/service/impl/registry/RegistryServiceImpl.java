package com.cloud.paas.imageregistry.service.impl.registry;

import com.cloud.paas.imageregistry.qo.ConditionQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cloud.paas.imageregistry.dao.BaseDAO;
import com.cloud.paas.imageregistry.dao.ImageVersionMngDAO;
import com.cloud.paas.imageregistry.dao.RegistryDetailDAO;
import com.cloud.paas.imageregistry.model.RegistryDetail;
import com.cloud.paas.imageregistry.service.impl.BaseServiceImpl;
import com.cloud.paas.imageregistry.service.registry.RegistryService;
import com.cloud.paas.imageregistry.util.Config;
import com.cloud.paas.imageregistry.util.DockerUtil;
import com.cloud.paas.imageregistry.vo.image.ImageVersionVO;
import com.cloud.paas.imageregistry.vo.registry.RegistryDetailVO;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.page.PageUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestTemplateUtils;
import com.cloud.paas.util.result.Result;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;


@SuppressWarnings({"serial", "rawtypes", "unchecked"})
@Service("registryService")
public class RegistryServiceImpl extends BaseServiceImpl<RegistryDetail> implements RegistryService {

    /**
     * 注入registryDetailDAO对象
     */
    @Autowired
    private RegistryDetailDAO registryDetailDAO;

    @Autowired
    private ImageVersionMngDAO imageVersionMngDAO;


    /**
     * 定义logger,以便用于打印日志内容
     */
    private static final Logger logger = LoggerFactory.getLogger(RegistryServiceImpl.class);

    /**
     * 实现父类的抽象方法
     *
     * @return registryDetailDAO
     */
    @Override
    public BaseDAO<RegistryDetail> getBaseDAO() {
        return registryDetailDAO;
    }

    /**
     * 通过ID查询
     *
     * @param registryId
     * @return
     */

    public Result doFindById(Integer registryId) throws BusinessException {
        logger.info("查询仓库详情！仓库id【{}】", registryId);
        RegistryDetailVO registryDetail = null;
        try {
            registryDetail = registryDetailDAO.doFindById(registryId);
            if (null == registryDetail.getRegistryNameZh()) {
                logger.error("仓库表中无该id:" + registryId + "对应的记录");
                return CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
            } else {
                logger.info("仓库表中找到id为" + registryId + "的对应的记录");
                Result result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
                result.setData(registryDetail);
                return result;
            }
        } catch (Exception e) {
            logger.error("查找仓库id号为：" + registryId + "的记录出错", e);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    /**
     * 通过名称判断仓库是否存在
     *
     * @param registryDetail
     * @return
     * @throws Exception
     */
    public Result JudageRegistryExit(RegistryDetail registryDetail) throws Exception {
        Result result = null;
        if (null == registryDetail.getRegistryNameZh()) {
            logger.info("仓库表中无该name:" + "对应的记录");
            result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
            return result;
        } else {
            logger.info("仓库表存在该name:" + "对应的记录");
            result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
            result.setData(registryDetail);
            return result;
        }
    }

    /**
     * 通过名称查询
     */
    @Override
    public Result doFindByName(String name) throws Exception {
        RegistryDetail registryDetail = null;
        Result result = null;
        if (null == name || "".equals(name)) {
            logger.info("输入的名称格式错误" + name + "的对应的记录");
            result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
            result.setMessage("输入的名称格式错误");
            return result;
        }
        try {
            registryDetail = registryDetailDAO.doFindByName(name);
            result = JudageRegistryExit(registryDetail);
            return result;
        } catch (Exception e) {
            logger.error("查询仓库出错", e);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    /**
     * 通过英文名称查询
     */
    @Override
    public Result doFindByNameEn(String name) throws Exception {
        RegistryDetail registryDetail = null;
        Result result = null;
        if (null == name || "".equals(name)) {
            logger.info("输入的名称格式错误" + name + "的对应的记录");
            result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
            return result;
        }
        try {
            registryDetail = registryDetailDAO.doFindByNameEn(name);
            result = JudageRegistryExit(registryDetail);
            return result;
        } catch (Exception e) {
            logger.error("查询仓库出错", e);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    /**
     * 实现查询所有的仓库，
     *
     * @return 仓库列表
     */
    @Override
    public Result listRegistryDetail() throws Exception {
        List<RegistryDetail> list = null;
        Result result = null;
        try {
            list = registryDetailDAO.listRegistryDetail();
            result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
            result.setData(list);
            return result;
        } catch (Exception e) {
            logger.error("查询所有的仓库", e);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    /**
     * 多条件混合查询
     *
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public Result doFindByMultiConditionQuery(ConditionQuery condition) throws BusinessException {
        //1.获取分页信息
        logger.info("-----------获取仓库查询分页信息------------");
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        logger.debug("pageNum" + pageNum + " pageSize:" + pageSize);
        //2.设置分页查询条件并查询
        logger.info("------------------设置分页查询条件并查询----------");
        Page page = PageHelper.startPage(pageNum, pageSize);
        try {
            List<RegistryDetailVO> list = registryDetailDAO.doFindByMultiConditionQuery(condition);
            logger.debug("分页查询成功！");
            PageInfo pageInfo = PageUtil.getPageInfo(page, list);
            Result result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
            result.setData(pageInfo);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("分页查询仓库失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    public boolean JudgeTheExistenceOfPath(String path) {
        File file = null;
        try {
            file = new File(path);
        } catch (Exception e) {
            logger.error("目录格式错误");
        }
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建
     *
     * @param registryDetail
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Result doInsertByRegistry(RegistryDetail registryDetail) throws BusinessException {
        //1.校验仓库英文名、ip:port等是否可用
        logger.info("校验英文名【{}】、ip:port【{}:{}】等是否可用", registryDetail.getRegistryNameEn(), registryDetail.getRegistryIp(), registryDetail.getRegistryPort());
        checkLegality(registryDetail);
        //检验仓库中文名是否可用
        logger.info("校验中文名【名称：{}】", registryDetail.getRegistryNameZh());
        checkRegistryNameZh(registryDetail);
        //创建仓库
        logger.info("创建仓库容器！");
        registryDetail = createRegistry(registryDetail);
        //插入数据库
        logger.info("插入数据库！");
        insertRegistry(registryDetail);
        //返回信息
        logger.info("仓库构建成功");
        return CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_SUCCESS");
    }

    /**
     * 创建仓库容器
     *
     * @param registryDetail
     * @return
     */
    private RegistryDetail createRegistry(RegistryDetail registryDetail) {

        //判断是否支持鉴权
        if (registryDetail.getSupportAuthentication() == 0) {
            //TODO 鉴权操作(尚未完成）
        }
        // 连接docker daemon
        String registryIp = registryDetail.getRegistryIp();
        String uri = "http://" + registryIp + ":" + Config.REGISTRY_DD_PORT;
        logger.debug("连接docker daemon的地址为：【uri：{}】", uri);
        DockerClient dockerClient = null;
        try {
            dockerClient = DockerUtil.getDocker(uri);
        } catch (DockerCertificateException e) {
            e.printStackTrace();
            logger.error("连接docker daemon【uri：{}】异常，异常信息：{}", uri, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_CONNECT"));
        }
        //配置仓库容器的端口映射、挂载目录
        ContainerConfig registryCtnConfig = setRegistryCtnConfig(registryDetail);
        //创建仓库容器
        logger.info("创建仓库容器");
        ContainerCreation container = null;
        try {
            container = dockerClient.createContainer(registryCtnConfig, registryDetail.getRegistryNameEn());
        } catch (DockerException e) {
            logger.error("-----------------------创建仓库容器失败！失败信息：【{}】", e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE"));

        } catch (InterruptedException e) {
            logger.error("------------------中断异常！信息：【{}】", e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE"));
        }
        //创建成功！返回信息
        String ctnId = container.id();
        logger.debug("仓库容器创建成功！容器id：【id：{}】", ctnId);
        registryDetail.setRegistryContainerId(ctnId);
        return registryDetail;
    }


    /**
     * 插入数据库
     *
     * @param registryDetail
     */
    private void insertRegistry(RegistryDetail registryDetail) {
        if (registryDetailDAO.doInsertByBean(registryDetail) > 0) {
            logger.info("插入数据库成功！");
        } else {
            logger.error("插入数据库失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE"));
        }
    }

    /**
     * 检验中文名是否可用
     *
     * @param registryDetail
     */

    private void checkRegistryNameZh(RegistryDetail registryDetail) throws BusinessException {
        RegistryDetail registry;
        try {
            registry= registryDetailDAO.doFindByName(registryDetail.getRegistryNameZh());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }
        if (null != registry) {
            logger.error("中文名已使用！【名称：{}】", registryDetail.getRegistryNameZh());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_NAMEZH_USED"));
        }
    }

    /**
     * 判断英文名，端口是否被占用
     *
     * @param registryDetail
     */
    private void checkLegality(RegistryDetail registryDetail) {
        //根据ip获取仓库列表
        List<RegistryDetail> registryDetailList = registryDetailDAO.doFindByRegistryIp(registryDetail.getRegistryIp());
        //判断是否存在符合条件的仓库
        if (registryDetailList != null) {
            for (int i = 0; i < registryDetailList.size(); i++) {
                //获取ip;port
                String uri = registryDetailList.get(i).getRegistryIp() + ":" + String.valueOf(registryDetailList.get(i).getRegistryPort());
                //获取英文名
                String nameEn = registryDetailList.get(i).getRegistryNameEn();
                //判断uri是否被使用
                if ((registryDetail.getRegistryIp() + ":" + String.valueOf(registryDetail.getRegistryPort())).equals(uri)) {
                    logger.error("端口已使用！【uri：{}】", uri);
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_PORT_USED"));
                }
                //判断英文名是否被使用
                if (registryDetail.getRegistryNameEn().equals(nameEn)) {
                    logger.error("英文名已使用【nameEn：{}】", nameEn);
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_NAMEEN_USED"));
                }
                //判断路径是否可用
                if (registryDetail.getHostStoragePath().equals(registryDetailList.get(i).getHostStoragePath())) {
                    logger.error("路径已被使用！【path：{}】", registryDetail.getDistributedStoragePath());
                    throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_PATH_USED"));
                }
            }
        }
    }

    /**
     * 判断路径是否已经存在
     *
     * @return
     * @throws Exception
     */
    public Result JudageUrlExit(RegistryDetail registryDetail) throws Exception {
        RegistryDetail registry = null;
        Result result = null;
        String registryDetailIp = registryDetail.getRegistryIp();
        Integer registryDetailPort = registryDetail.getRegistryPort();
        String urlDetail = registryDetailIp + "" + registryDetailPort;
        List<String> url = getRegistryDetailUrl();
        //遍历比较
        for (int i = 0; i < url.size(); i++) {
            if (urlDetail.equals(url.get(i))) {//说明该URL已经存在了
                logger.debug("该仓库路径已经存在");
                result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
                result.setData(url.get(i));
                return result;
            } else {
                result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
                result.setMessage("该路径不存在");
                return result;
            }
        }
        result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE");
        result.setMessage("该仓库路径不存在");
        return result;
    }

    /**
     * 遍历仓库表，得到所有参考的路径
     *
     * @return
     */
    public List<String> getRegistryDetailUrl() {
        //将仓库数据表中的仓库的ip+port全部拿出来，与新插入的数据的ip+port进行比较，不能一样。
        List<RegistryDetail> listRegistry = registryDetailDAO.listRegistryDetail();
        List<String> url = new ArrayList<>();
        //将仓库数据表中的仓库的ip+port全部拿出来，
        String registryIp = "";
        Integer registryPort = 0;
        //得到所有的url
        for (int i = 0; i < listRegistry.size(); i++) {
            registryIp = listRegistry.get(i).getRegistryIp();
            registryPort = listRegistry.get(i).getRegistryPort();
            url.add(registryIp + "" + registryPort);
        }
        return url;
    }

    /**
     * 通过仓库id 查询仓库中已经存在的镜像版本列表
     *
     * @param registryDetail
     * @return
     * @throws Exception
     */
    public Result listImageByRegistryId(RegistryDetail registryDetail) throws Exception {

        Integer registryID = registryDetail.getRegistryId();
        logger.info("查询仓库【id：{}】里的镜像列表",registryID);
        try {
            int pageNum = registryDetail.getPageNum();
            int pageSize = registryDetail.getPageSize();
            logger.debug("查询分页信息：pageNum" + pageNum + " pageSize:" + pageSize);
            Page page = PageHelper.startPage(pageNum, pageSize);
            PageInfo pageInfo = null;
            List<ImageVersionVO> imageVersions = imageVersionMngDAO.listImageByRegistryId(registryID);
            pageInfo = PageUtil.getPageInfo(page, imageVersions);
            pageInfo.setList(imageVersions);
            Result result = CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_SUCCESS");
            result.setData(pageInfo);
            return result;
        } catch (Exception e) {
            logger.error("查询仓库中的镜像版本出错,错误信息【{}】", e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_ERROR"));
        }
    }

    /**
     * 修改
     *
     * @param registryDetail
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public Result doUpdateByRegistry(RegistryDetail registryDetail) throws BusinessException {
        //1.判断仓库状态,状态停止成功、启动失败、构建成功时才能修改
        logger.info("获取仓库信息【id；{}】", registryDetail.getRegistryId());
        getRegistryStatus(registryDetail);
        //2.检验修改仓库中文名是否重复
        logger.info("校验中文名【名称：{}】", registryDetail.getRegistryNameZh());
        checkRegistryNameZh(registryDetail);
        //3.更新数据库
        logger.info("更新数据库");
        if (registryDetailDAO.doUpdateByBean(registryDetail) > 0) {
            logger.debug("更新数据库成功");
        } else {
            logger.error("更新数据库失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_MODIFY_FAILURE"));
        }
        return CodeStatusUtil.resultByCodeEn("REGISTRY_MODIFY_SUCCESS");

    }

    /**
     * 判断仓库状态
     *
     * @param registryDetail
     */
    private void getRegistryStatus(RegistryDetail registryDetail) {
        logger.debug("数据库查询");
        RegistryDetail registry = registryDetailDAO.doFindByRegistryId(registryDetail.getRegistryId());
        if (registry == null) {
            logger.error("---------------仓库信息查询失败！---------【id：{}】", registryDetail.getRegistryId());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }
        //获取仓库状态
        Long status = registry.getRegistryStatus();
        //判断状态
        if (status == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_SUCCESS").getCode().longValue() ||
                status == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_FAILURE").getCode().longValue() ||
                status == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_BUILD_SUCCESS").getCode().longValue()) {
            logger.info("仓库可以修改");
        }else{
            logger.error("仓库运行中，不能修改！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_MODIFY_FAILURE_RUNNING"));
        }
    }

    /**
     * 删除
     *
     * @param registryId
     * @return TODO 容器删除成功，但是数据库更新失败怎么办？？？？
     */
    @Transactional
    public Result doDeleteById(Integer registryId) throws BusinessException {
        //1.判断仓库状态， 状态不为启动成功，停止失败时就能删除
        logger.info("仓库状态判断");
        RegistryDetail registryDetail;
        registryDetail = checkRegistryStatus(registryId);
        //2.删除仓库容器
        logger.info("删除仓库容器");
        removeRegistryContainer(registryDetail);
        //3.更新数据库
        logger.info("更新数据库");
        updateDeleteRegistry(registryId);
        return CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_SUCCESS");
    }

    /**
     * 删除数据库里面的仓库记录
     *
     * @param registryId
     */
    private void updateDeleteRegistry(Integer registryId) {
        if (registryDetailDAO.doDeleteById(registryId) > 0) {
            logger.info("删除仓库成功");
        } else {
            logger.error("删除容器成功！数据库更新失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_UPDATE_FAILURE"));
        }

    }

    /**
     * 判断仓库状态
     *
     * @param registryId
     * @return
     */

    private RegistryDetail checkRegistryStatus(Integer registryId) {
        RegistryDetail registryDetail = null;
        logger.info("查询仓库信息");
        try {
            registryDetail = registryDetailDAO.doFindById(registryId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("仓库查询失败！仓库id【{}】");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }
        //判断仓库是否存在
        if(null == registryDetail.getRegistryId() || null == registryDetail){
            logger.error("仓库不存在！【id：{}】",registryId);
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_NOT_EXIST"));
        }
        logger.info("获取仓库状态");
        Long state = registryDetail.getRegistryStatus();
        if (state == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_SUCCESS").getCode().longValue() ||
                state == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_FAILURE").getCode().longValue()) {
            logger.error("正在运行，无法删除", registryDetail.getRegistryNameEn());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE_RUNNING"));
        } else {
            List<ImageVersionVO> imageVersions = imageVersionMngDAO.listImageByRegistryId(registryId);
            if (imageVersions != null && imageVersions.size() > 0) {
                logger.error("仓库中有镜像，无法删除");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE_CONTAIN_IMA"));
            }
            return registryDetail;
        }
    }

    /**
     * @param registryDetail
     * @return
     * @throws DockerCertificateException
     * @brief 删除仓库容器
     */
    public void removeRegistryContainer(RegistryDetail registryDetail) {
        //1。获取仓库容器id
        logger.info("获取仓库容器id");
        String ctnId = registryDetail.getRegistryContainerId();
        if (StringUtils.isBlank(ctnId)) {
            logger.error("未获得仓库容器id");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_START_FAILURE_CTNID_NULL"));
        }

        //2.连接docker
        logger.info("连接docker daemon");
        String registryIp = registryDetail.getRegistryIp();
        String uri = "http://" + registryIp + ":" + Config.REGISTRY_DD_PORT;
        logger.debug("仓库所在的 docker daemon uri: {}", uri);
        DockerClient dockerClient = null;
        try {
            dockerClient = DockerUtil.getDocker(uri);
        } catch (DockerCertificateException e) {
            logger.error("连接{}异常,异常信息{}", uri, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_CONNECT"));
        }

        //3查看仓库容器是否存在
        try {
            dockerClient.inspectContainer(ctnId);
        } catch (ContainerNotFoundException e) {
            logger.error("仓库容器不存在！");
            logger.error("Inspect registry 【{}】 failed, container was not found,error info: {}", Config.REGISTRY_IMAGE_INFO, e.getMessage());
            registryDetail.setRegistryContainerId("");
            return;
        } catch (DockerException e) {
            logger.error("-----docker异常----------");
            logger.error("Inspect registry 【{}】 failed, a server error occurred,error info: {}", Config.REGISTRY_IMAGE_INFO, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE"));
        } catch (InterruptedException e) {
            logger.error("-------中断异常---------");
            logger.error("Inspect registry 【{}】 failed, the thread is interrupted,error info: {}", Config.REGISTRY_IMAGE_INFO, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE"));
        }


        //4.删除仓库容器
        logger.info("删除仓库容器registry containerid is : {}", ctnId);
        try {
            dockerClient.removeContainer(ctnId);
        } catch (DockerException e) {
            logger.error("-----docker异常----------");
            logger.error("remove registry 【{}】,ctnid: 【{}】 failed, error info: {}", registryDetail.getRegistryNameEn(), ctnId, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE"));
        } catch (InterruptedException e) {
            logger.error("-------中断异常---------");
            logger.error("remove registry 【{}】,ctnid:【{}】 failed, error info: {}", registryDetail.getRegistryNameEn(), ctnId, e.getMessage());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_DELETE_FAILURE"));
        }
        logger.info("registry 【{}】 remove complete, containerid is : {}", registryDetail.getRegistryNameEn(), ctnId);
    }

    /**
     * 删除仓库中镜像
     *
     * @return
     */
    public void removeRegistryImage(String registryIp, String registryPort, String busiPkgNameEn, String busiPkgVersion) {
        Result result = CodeStatusUtil.resultByCodeEn("REGISTRY_IMAGE_DELETE_FAILURE");
        if (StringUtils.isNotBlank(registryIp) && StringUtils.isNotBlank(registryPort) && StringUtils.isNotBlank(busiPkgNameEn) && StringUtils.isNotBlank(busiPkgVersion)) {
            //1、获取仓库中镜像的digest
            String url = "http://" + registryIp + ":" + registryPort;
            logger.debug("仓库url:{}", url);
            RestTemplate restTemplate = RestTemplateUtils.getSingleInstance().getRestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            //版本是2.3或者更高版本,通过HEAD或者GET方式获取digest时候需要在请求头header中加如下请求头
            requestHeaders.add("Accept", "application/vnd.docker.distribution.manifest.v2+json");
            HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
            ResponseEntity<String> response = null;
            try {
                response = restTemplate.exchange(url + "/v2/{busiPkgNameEn}/manifests/{busiPkgVersion}", HttpMethod.GET, requestEntity, String.class, busiPkgNameEn, busiPkgVersion);
            } catch (RestClientException e) {
                logger.error("获取仓库镜像{}:{}失败，镜像不存在，失败信息:{}", busiPkgNameEn, busiPkgVersion, e.getMessage());
                result.setMessage("获取仓库镜像失败,镜像不存在");
                throw new BusinessException(result);
            }
            //获取digest的值
            String etag = response.getHeaders().getETag();
            String digest = etag.substring(1, etag.length() - 1);
            //2、删除仓库中的镜像
            try {
                restTemplate.delete(url + "/v2/{busiPkgNameEn}/manifests/{digest}", busiPkgNameEn, digest);
            } catch (RestClientException e) {
                logger.error("删除仓库镜像{}:{}失败，失败信息:{}", busiPkgNameEn, busiPkgVersion, e.getMessage());
                throw new BusinessException(result);
            }
            logger.info("删除仓库中镜像{}:{}成功", busiPkgNameEn, busiPkgVersion);
            return;
        }
        logger.error("仓库ip、仓库端口、镜像名称、镜像版本不能为空");
        throw new BusinessException(result);
    }

    /**
     * 启动仓库
     *
     * @param registryID
     * @return
     */
    @Override
    public Result startRegistry(Integer registryID) throws BusinessException {
        //1.获取仓库详细信息
        logger.info("获取仓库详细信息【id：{}】", registryID);
        RegistryDetail registryDetail = null;
        try {
            registryDetail = registryDetailDAO.doFindByRegistryId(registryID);
            //判断仓库是否存在
            if(null == registryDetail){
                logger.error("仓库不存在！【id：{}】",registryID);
               return CodeStatusUtil.resultByCodeEn("REGISTRY_NOT_EXIST");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询仓库信息失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }


        //2.判断仓库的状态，如果处于启动成功或停止失败状态，就直接返回
        logger.info("判断仓库状态");
        Long status = registryDetail.getRegistryStatus();
        if (status == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_SUCCESS").getCode().longValue() ||
                status == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_FAILURE").getCode().longValue()) {
            logger.error("启动处于启动状态，仓库名字：{},仓库IP: {}, 仓库端口: {}", registryDetail.getRegistryNameEn(), registryDetail.getRegistryIp(), registryDetail.getRegistryPort());
            return CodeStatusUtil.resultByCodeEn("REGISTRY_START_SUCCESS");
        } else {

            //3.启动仓库容器
            logger.info("启动仓库容器");
            //3.1获取仓库容器id
            String ctnId = registryDetail.getRegistryContainerId();
            if (ctnId == null) {
                logger.error("启动失败！仓库容器为空！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_START_FAILURE_CTNID_NULL"));
            }
            //3.2获取地址
            String uri = "http://" + registryDetail.getRegistryIp() + ":" + Config.REGISTRY_DD_PORT;
            //连接docker daemon
            logger.info("连接docker daemon，地址为：【uri：{}】", uri);
            DockerClient dockerClient = null;
            try {
                dockerClient = DockerUtil.getDocker(uri);
            } catch (DockerCertificateException e) {
                logger.error("连接docker daemon【uri：{}】异常，异常信息：{}", uri, e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_CONNECT"));
            }
            //3.3启动容器
            logger.info("----启动容器");
            try {
                dockerClient.startContainer(ctnId);
            } catch (DockerException e) {
                e.printStackTrace();
                logger.error("启动容器异常！错误信息：【{}】", e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_START_FAILURE"));
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("docker 连接中断异常，错误信息：【{}】", e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_START_FAILURE"));
            }

            //4.更新数据库状态
            logger.info("更新数据库仓库状态为：【{}】", CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_SUCCESS").getCode().longValue());
            registryDetail.setRegistryStatus(CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_SUCCESS").getCode().longValue());
            if (registryDetailDAO.doUpdateByBean(registryDetail) > 0) {
                logger.info("更新数据库仓库状态成功！");
                return CodeStatusUtil.resultByCodeEn("REGISTRY_START_SUCCESS");
            } else {
                logger.error("更新数据库仓库状态失败！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_START_FAILURE_UPDATE_DB"));
            }
        }
    }


    /**
     * 设置仓库配置
     *
     * @param registryDetail
     * @return
     */
    private ContainerConfig setRegistryCtnConfig(RegistryDetail registryDetail) {
        ContainerConfig registryCtnConfig = null;
        String registryNameZh = registryDetail.getRegistryNameZh();
        // 绑定仓库容器端口到主机端口
        final String ctnPort = Config.REGISTRY_CONTAINER_PORT;
        final Map<String, List<PortBinding>> portBindings = new HashMap<>();
        if (registryDetail.getRegistryPort() != null) {
            final String[] ports = {Long.toString(registryDetail.getRegistryPort())};
            logger.debug("仓库-{}的容器端口:{}，端口映射:{}", registryNameZh, ctnPort, ArrayUtils.toString(ports));
            for (String port : ports) {
                List<PortBinding> hostPorts = new ArrayList<>();
                hostPorts.add(PortBinding.of("0.0.0.0", port));
                portBindings.put(ctnPort, hostPorts);
            }
        }

        // 主机仓库配置文件挂载位置
        final String hostStoragePath = Config.REGISTRY_HOST_STORAGE_PATH + registryDetail.getRegistryNameEn() + "/" + registryDetail.getHostStoragePath();
        // 仓库容器配置文件位置
        final String ctnStoragePath = Config.REGISTRY_CONTAINER_STORAGE_PATH;
        //映射仓库容器端口，挂载仓库配置文件，挂载仓库存储
        final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings)
                .appendBinds(hostStoragePath + ":" + ctnStoragePath)
                .build();

        registryCtnConfig = ContainerConfig.builder().image(Config.REGISTRY_IMAGE_INFO).hostConfig(hostConfig).exposedPorts(ctnPort).build();
        return registryCtnConfig;
    }

    /**
     * 停止
     *
     * @param registryID
     * @return
     */
    public Result stopRegistry(Integer registryID) throws BusinessException {
        //1.获取仓库详细信息
        logger.info("获取仓库详细信息【id：{}】", registryID);
        RegistryDetail registryDetail = null;
        try {
            registryDetail = registryDetailDAO.doFindByRegistryId(registryID);
            if(null == registryDetail){
                logger.error("仓库不存在！【id：{}】",registryID);
                return CodeStatusUtil.resultByCodeEn("REGISTRY_NOT_EXIST");
            }
        } catch (Exception e) {
            logger.error("查询仓库信息失败！");
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_QUERY_FAILURE"));
        }
        //2.判断仓库的状态，处于停止，启动失败，创建成功状态的直接返回
        Long state = registryDetail.getRegistryStatus();
        logger.info("判断仓库状态！仓库状态码为：【status：{}】", state);

        if (state == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_SUCCESS").getCode().longValue() ||
                state == CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_START_FAILURE").getCode().longValue() ||
                state == CodeStatusUtil.getInstance().getStatusByCodeEn("APP_BUILD_SUCCESS").getCode().longValue()) {
            logger.info("停止仓库失败，因为其没有处于启动状态，仓库名字：{},仓库IP: {}, 仓库端口: {}", registryDetail.getRegistryNameEn(), registryDetail.getRegistryIp(), registryDetail.getRegistryPort());
            throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_FAILURE"));
        } else {
            //3.停止仓库容器
            logger.info("停止仓库容器");
            //3.1获取仓库容器id
            String ctnId = registryDetail.getRegistryContainerId();
            if (ctnId == null) {
                logger.error("停止失败！仓库容器为空！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_FAILURE_CTNID_NULL"));
            }
            //3.2获取地址
            String uri = "http://" + registryDetail.getRegistryIp() + ":" + Config.REGISTRY_DD_PORT;
            //连接docker daemon
            logger.info("连接docker daemon，地址为：【uri：{}】", uri);
            DockerClient dockerClient = null;
            try {
                dockerClient = DockerUtil.getDocker(uri);
            } catch (DockerCertificateException e) {
                logger.error("连接docker daemon【uri：{}】异常，异常信息：{}", uri, e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_BUILD_FAILURE_CONNECT"));
            }
            //3.3停止容器
            logger.info("----停止容器");
            try {
                dockerClient.stopContainer(ctnId, 1);
            } catch (DockerException e) {
                e.printStackTrace();
                logger.error("停止容器异常！错误信息：【{}】", e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_FAILURE"));
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("docker 连接中断异常，错误信息：【{}】", e.getMessage());
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_FAILURE"));
            }

            //4.更新数据库状态
            logger.info("更新数据库仓库状态为：【{}】", CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_SUCCESS").getCode().longValue());
            registryDetail.setRegistryStatus(CodeStatusUtil.getInstance().getStatusByCodeEn("REGISTRY_STOP_SUCCESS").getCode().longValue());
            if (registryDetailDAO.doUpdateByBean(registryDetail) > 0) {
                logger.info("更新数据库仓库状态成功！");
                return CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_SUCCESS");
            } else {
                logger.error("更新数据库仓库状态失败！");
                throw new BusinessException(CodeStatusUtil.resultByCodeEn("REGISTRY_STOP_SUCCESS_UPDATE_FAILURE"));
            }
        }
    }
}