package com.cloud.paas.taskmanager.util;

import com.alibaba.fastjson.JSON;
import com.cloud.paas.taskmanager.entity.jenkins.config.Config;
import com.cloud.paas.taskmanager.entity.jenkins.globalCredentials.Credential;
import com.cloud.paas.taskmanager.entity.jenkins.globalCredentials.GlobalCredentials;
import com.cloud.paas.taskmanager.entity.jenkins.job.Project;
import com.cloud.paas.taskmanager.entity.jenkins.job.builders.*;
import com.cloud.paas.taskmanager.entity.jenkins.job.properties.BuildDiscarderProperty;
import com.cloud.paas.taskmanager.entity.jenkins.job.properties.Properties;
import com.cloud.paas.taskmanager.entity.jenkins.job.properties.Strategy;
import com.cloud.paas.taskmanager.entity.jenkins.job.scm.*;
import com.cloud.paas.taskmanager.entity.jenkins.job.triggers.SCMTrigger;
import com.cloud.paas.taskmanager.entity.jenkins.job.triggers.Triggers;
import com.cloud.paas.taskmanager.entity.jenkins.taskBuild.TaskBuildDetails;
import com.cloud.paas.configuration.PropertiesConfUtil;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.rest.RestClient;
import com.cloud.paas.util.rest.RestConstant;
import com.cloud.paas.util.result.Result;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: srf
 * @desc: JenkinsUtil对象
 * @Date: Created in 2018-04-21 15:45
 * @Modified By:
 */
public class JenkinsUtil {
    private volatile static JenkinsUtil jenkinsUtil = null;
    private static JenkinsServer jenkinsServer;
    private static String loginUserName = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_USEERNAME);
    private static String password = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_PWD);

    public static JenkinsUtil getInstance() {
        synchronized (JenkinsUtil.class) {
            if (jenkinsUtil == null) {
                jenkinsUtil = new JenkinsUtil();
            }
        }
        return jenkinsUtil;
    }

    private JenkinsUtil() {
    }

    /**
     * 连接JenKins服务器
     *
     * @return 连接信息
     */
    private Result connectToServer() {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_SUCCESS");
        synchronized (JenkinsUtil.class) {
            if (jenkinsServer == null) {
                String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_SERVER);
                try {
                    jenkinsServer = new JenkinsServer(new URI(url), loginUserName, password);
                } catch (URISyntaxException e) {
                    result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 创建Job
     *
     * @param folderName 文件夹名
     * @param jobName Job名
     * @param userName 账号
     * @param pwd 密码
     * @param config     配置信息
     * @return Job创建信息
     */
    public Result createJob(String folderName, String jobName,String userName, String pwd, Config config) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.确保文件夹存在
        result = makeFolderExist(folderName);
        if (result.getSuccess() == 0) return result;
        //3.判断Job是否已经存在
        result = jobIsExist(folderName, jobName);
        if (result.getSuccess() == 0) return result;
        boolean jobIsExist = (boolean) result.getData();
        if (jobIsExist) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_ALREADYEXISTS");
            return result;
        }
        //4.删除再创建credential
        result = createOrUpdareCredentials(userName, pwd, jobName);
        if (result.getSuccess() == 0) return result;
        //5.配置Project
        result = setProject(config, jobName);
        if (result.getSuccess() == 0) return result;
        //6.生成XML文件
        String xml = JAXBUtils.modelToStringXML(result.getData());
        //7.在JenKins中创建Job
        result = jenkinsCreateJob(folderName, jobName, xml);
        return result;
    }

    /**
     * 调用JenKins创建Job接口
     *
     * @param folderName 服务名
     * @param jobName    Job名
     * @param xml        xml字符串
     * @return 创建信息
     */
    private Result jenkinsCreateJob(String folderName, String jobName, String xml) {
        Result result;
        try {
            Job job = jenkinsServer.getJob(folderName);
            FolderJob folderJob = jenkinsServer.getFolderJob(job).orNull();
            jenkinsServer.createJob(folderJob, jobName, xml);
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_SUCCESS");
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_FAILURE");
            return result;
        }
//        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_JOB_CREATE);
//        url = url.replace("{folder}", folderName) + jobName;
//        ResponseEntity<String> response = RestClient.doPostXMLWithAuth(loginUserName, password, url, xml);
//        if (response != null) {
//            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_SUCCESS");
//            result.setData(response);
//        } else {
//            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_FAILURE");
//        }
        return result;
    }

    /**
     * 更新Job
     *
     * @param folderName 文件夹名
     * @param jobName Job名
     * @param userName 账号
     * @param pwd 密码
     * @return 更新信息
     */
    public Result updateJob(String folderName, String jobName,String userName, String pwd, Config config) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.判断文件夹是否已经存在
        result = checkFolderExist(folderName);
        if (result.getSuccess() == 0) return result;
        boolean folderIsExist = (boolean) result.getData();
        if (!folderIsExist) {
            result.setSuccess(0);
            return result;
        }
        //3.判断Job是否已经存在
        result = jobIsExist(folderName, jobName);
        if (result.getSuccess() == 0) return result;
        boolean jobIsExist = (boolean) result.getData();
        if (!jobIsExist) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_NOTEXISTS");
            return result;
        }
        //5.删除再创建credential
        result = createOrUpdareCredentials(userName, pwd, jobName);
        if (result.getSuccess() == 0) return result;
        //6.配置Project
        result = setProject(config, jobName);
        if (result.getSuccess() == 0) return result;
        //7.生成XML文件
        String xml = JAXBUtils.modelToStringXML(result.getData());
        //8.在JenKins中更新Job
        result = jenkinsUpdateJob(folderName, jobName, xml);
        return result;
    }

    private Result jenkinsUpdateJob(String folderName, String jobName, String xml) {
        Result result;
//        try {
//            Job job = jenkinsServer.getJob(folderName);
//            FolderJob folderJob = jenkinsServer.getFolderJob(job).orNull();
//            jenkinsServer.updateJob(folderJob, jobName, xml, true);
//            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_UPDATE_SUCCESS");
//        } catch (IOException e) {
//            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_UPDATE_FAILURE");
//            return result;
//        }
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_JOB_UPDATE);
        url = url.replace("{folder}", folderName).replace("jobname", jobName);
        ResponseEntity<String> response = RestClient.doPostWithAuth(loginUserName, password, url, xml);
        if (response != null) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_UPDATE_SUCCESS");
            result.setData(response);
        } else {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_UPDATE_FAILURE");
        }
        return result;
    }

    /**
     * 检查并确保文件夹存在
     *
     * @param folderName 文件夹名称
     * @return 文件夹创建信息
     */
    private Result makeFolderExist(String folderName) {
        Result result = checkFolderExist(folderName);
        if (result.getSuccess() == 0) return result;
        boolean exist = (boolean) result.getData();
        if (!exist) {
            try {
                jenkinsServer.createFolder(folderName);
            } catch (IOException e) {
                result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_CREATE_FAILURE");
                return result;
            }
        }
        return result;
    }

    /**
     * 检查文件夹存在性
     *
     * @param folderName 文件夹名称
     * @return 文件夹存在信息
     */
    private Result checkFolderExist(String folderName) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_QUERY_SUCCESS");
        boolean exist = false;
        result.setData(exist);
        try {
            //1.获取所有文件夹和项目Job
            Map<String, Job> jobMap = jenkinsServer.getJobs();
            for (String key : jobMap.keySet()) {
                Job job = jobMap.get(key);
                FolderJob folderJob = jenkinsServer.getFolderJob(job).orNull();
                //2.判断是项目Job则略过
                if (folderJob == null) continue;
                //3.检测到目标文件夹则置true
                if (folderJob.getDisplayName().equals(folderName)) exist = true;
                result.setData(exist);
            }
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_QUERY_FAILURE");
            return result;
        }
        return result;
    }

    /**
     * 判断Job是否已经存在
     *
     * @param folderName 文件夹名称
     * @param jobName    Job名称
     * @return Job存在信息
     */
    private Result jobIsExist(String folderName, String jobName) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_SUCCESS");
        try {
            FolderJob folderJob = jenkinsServer.getFolderJob(jenkinsServer.getJob(folderName)).orNull();
            if (folderJob == null) {
                result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_CREATE_FAILURE");
                return result;
            }
            Job job = jenkinsServer.getJob(folderJob, jobName);
            if (job != null) {
                result.setData(true);
            } else {
                result.setData(false);
            }
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
            return result;
        }
        return result;
    }

    /**
     * 判断账号是否存在，存在则删了重新创建，否则直接创建
     *
     * @param name          账号
     * @param pwd           密码
     * @param credentialsId 账号ID
     * @return 账号创建信息
     */
    private Result createOrUpdareCredentials(String name, String pwd, String credentialsId) {
        //1.判断账号是否存在
        Result result = checkCredentialExist(credentialsId);
        if (result.getSuccess() == 0) return result;
        boolean isExist = (boolean) result.getData();
        //2.存在则先删除
        if (isExist) {
            ResponseEntity<String> responseEntity = deleteCredentials(credentialsId);
            if (responseEntity == null) return CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_CREATE_FAILURE");
        }
        //3.创建账号
        ResponseEntity<String> responseEntity = createCredentials(name, pwd, credentialsId);
        if (responseEntity == null)
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_CREATE_FAILURE");
        else
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_CREATE_SUCCESS");
        return result;
    }

    /**
     * 检查账号是否存在
     *
     * @param credentialId 账号ID
     * @return 账号存在信息
     */
    private Result checkCredentialExist(String credentialId) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_QUERY_FAILURE");
        //1.获取账号列表
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_CREDENTIALS_QUERY);
        String response = RestClient.doGetWIthAuth(loginUserName, password, url);
        //2.判断获取是否成功
        if (response == null) return result;
        try {
            //3.遍历账号列表
            GlobalCredentials globalCredentials = JSON.parseObject(response, GlobalCredentials.class);
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_QUERY_SUCCESS");
            result.setData(false);
            List<Credential> credentials = globalCredentials.getCredentials();
            for (Credential credential : credentials) {
                //4.检测到目标账号则置true
                if (credential.getId().equals(credentialId)) result.setData(true);
            }
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CREDENTIALS_QUERY_FAILURE");
            return result;
        }
        return result;
    }

    /**
     * 删除账号
     *
     * @param credentialsId 账号ID
     * @return 账号删除信息
     */
    private ResponseEntity<String> deleteCredentials(String credentialsId) {
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_CREDENTIALS_DELETE);
        url = url.replace("{creentialsId}", credentialsId);
        return RestClient.doPostWithAuthAndParameters(loginUserName, password, url);
    }

    /**
     * 创建账号
     *
     * @param name          账号
     * @param pwd           密码
     * @param credentialsId 账号ID
     * @return 账号创建信息
     */
    private ResponseEntity<String> createCredentials(String name, String pwd, String credentialsId) {
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_CREDENTIALS_CREATE);
        String credentialsJson = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_CREDENTIALS_JSON);
        credentialsJson = credentialsJson.replace("{credentialsId}", credentialsId);
        credentialsJson = credentialsJson.replace("{username}", name);
        credentialsJson = credentialsJson.replace("{password}", pwd);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("json", credentialsJson);
        return RestClient.doPostWithAuthAndParameters(loginUserName, password, url, params);
    }

    /**
     * 配置Peoject
     *
     * @param config        配置信息
     * @param credentialId        账号ID
     * @return Project设置信息
     */
    private Result setProject(Config config, String credentialId) {
        Result result;
        Project project = new Project();
        //配置propeties
        if (config.getStrategy() != null) project.setProperties(getProperties(config.getStrategy()));
        //配置jdk
        project.setJdk(config.getJdk());
        //配置triggers
        if (config.getScmTriggerSpec() != null) project.setTriggers(getTriggers(config.getScmTriggerSpec()));
        //配置builders
        result = getBuilders(config);
        if (result.getSuccess() == 0) return result;
        project.setBuilders((Builders) result.getData());
        //配置VC
        switch (config.getVersionControlInfo().getType()) {
            case git: {
                result = setGit(config, credentialId);
                project.setScm((Scm) result.getData());
            }
            break;
            default: {
                result = setSVN(config, credentialId);
                project.setScm((Scm) result.getData());
            }
        }
        result.setData(project);
        return result;
    }

    /**
     * 配置Properties
     *
     * @param strategy 构建保持策略
     * @return 返回属性
     */
    private Properties getProperties(Strategy strategy) {
        Properties properties = new Properties();
        BuildDiscarderProperty buildDiscarderProperty = new BuildDiscarderProperty();
        strategy.setStrategyClass("hudson.tasks.LogRotator");
        buildDiscarderProperty.setStrategy(strategy);
        properties.setBuildDiscarderProperty(buildDiscarderProperty);
        return properties;
    }

    /**
     * 配置定时任务
     *
     * @param scmTriggerSpec SVN定时器
     * @return 定时任务
     */
    private Triggers getTriggers(String scmTriggerSpec) {
        Triggers triggers = new Triggers();
        SCMTrigger scmTrigger = new SCMTrigger();
        scmTrigger.setSpec(scmTriggerSpec);
        triggers.setScmTrigger(scmTrigger);
        return triggers;
    }

    /**
     * 配置构建
     *
     * @param config 配置
     * @return 构建
     */
    private Result getBuilders(Config config) {
        Builders builders = new Builders();
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_CREATE_SUCCESS");
        if (config.getSonarProperties() != null) {
            SonarRunnerBuilder sonarRunnerBuilder = new SonarRunnerBuilder();
            result = getVersion("sonar");
            if (result.getSuccess() == 0) return result;
            sonarRunnerBuilder.setPlugin("sonar@" + result.getData());
            sonarRunnerBuilder.setProperties("sonar.projectKey=" + config.getSonarProperties().getProjectKey() + "\n" +
                    "sonar.projectName=" + config.getSonarProperties().getProjectName() + "\n" +
                    "sonar.projectVersion=" + config.getSonarProperties().getProjectVersion() + "\n" +
                    "sonar.sources=.\n" +
                    "sonar.java.binaries=.\n" +
                    "sonar.language=java\n" +
                    "sonar.sourceEncoding=UTF-8");
            sonarRunnerBuilder.setJavaOpts(PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_SONAR_JAVAOPTS));
            builders.setSonarRunnerBuilder(sonarRunnerBuilder);
        }
        if (config.existAnt()) {
            Ant ant = new Ant();
            ant.setAntName(PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_ANT_VERSION));
            result = getVersion("ant");
            if (result.getSuccess() == 0) return result;
            ant.setPlugin("ant@" + result.getData());
            builders.setAnt(ant);
        }
        if (config.getMavenInfo() != null) {
            Maven maven = new Maven();
            maven.setTargets(config.getMavenInfo().getMavenCommand());
            maven.setPom(config.getMavenInfo().getPath());
            builders.setMaven(maven);
        }
        Shell shell = new Shell();
        shell.setCommand(config.getCommand());
        builders.setShell(shell);
        result.setData(builders);
        return result;
    }

    /**
     * 配置SVN的具体操作
     *
     * @param config 配置信息
     * @param credentialId 账号ID
     * @return SVN
     */
    private Result setSVN(Config config, String credentialId) {
        Result result = getVersion("subversion");
        if (result.getSuccess() == 0) return result;
        Scm scm = new Scm();
        scm.setPlugin("subversion@" + result.getData());
        scm.setScmClass("hudson.scm.SubversionSCM");
        Locations locations = new Locations();
        ModuleLocation moduleLocation = new ModuleLocation();
        String path = config.getVersionControlInfo().getPath();
//        try {
//            String decoderPath = URLDecoder.decode(path, "UTF-8");
//            String encoderPath = URLEncoder.encode(decoderPath, "UTF-8");
//            moduleLocation.setRemote(encoderPath);
//        } catch (UnsupportedEncodingException e) {
//            result = CodeStatusUtil.resultByCodeEn("ENCODING_FAILURE");
//            return result;
//        }
        moduleLocation.setRemote(path);
        moduleLocation.setCredentialsId(credentialId);
        moduleLocation.setLocal(".");
        moduleLocation.setDepthOption(DepthOption.infinity);
        moduleLocation.setCancelProcessOnExternalsFail(true);
        moduleLocation.setIgnoreExternalsOption(true);
        locations.setModuleLocation(moduleLocation);
        scm.setLocations(locations);
        scm.setQuietOperation(true);
        result.setData(scm);
        return result;
    }

    /**
     * @Description
     * @param config 配置信息
     * @param credentialId 账号ID
     * @result Git
     */
    private Result setGit(Config config, String credentialId) {
        Result result = getVersion("git");
        if (result.getSuccess() == 0) return result;
        Scm scm = new Scm();
        scm.setPlugin("git@" + result.getData());
        scm.setScmClass("hudson.plugins.git.GitSCM");
        UserRemoteConfigs userRemoteConfigs = new UserRemoteConfigs();
        UserRemoteConfig userRemoteConfig = new UserRemoteConfig();
        String path = config.getVersionControlInfo().getPath();
//        try {
//            String decoderPath = URLDecoder.decode(path, "UTF-8");
//            String encoderPath = URLEncoder.encode(decoderPath, "UTF-8");
//            userRemoteConfig.setUrl(encoderPath);
//        } catch (UnsupportedEncodingException e) {
//            result = CodeStatusUtil.resultByCodeEn("ENCODING_FAILURE");
//            return result;
//        }
        userRemoteConfig.setUrl(path);
        userRemoteConfig.setCredentialsId(credentialId);
        userRemoteConfigs.setUserRemoteConfig(userRemoteConfig);
        Branches branches = new Branches();
        BranchSpec branchSpec = new BranchSpec();
        branchSpec.setName(config.getVersionControlInfo().getBranch());
        branches.setBranchSpec(branchSpec);
        scm.setConfigVersion(2);
        scm.setUserRemoteConfigs(userRemoteConfigs);
        scm.setBranches(branches);
        result.setData(scm);
        return result;
    }

    /**
     * 获取插件版本
     *
     * @param shortname 插件名称
     * @return 插件版本信息
     */
    private Result getVersion(String shortname) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_GET_VERSION_FAILURE");
        try {
            PluginManager pluginManager = jenkinsServer.getPluginManager();
            List<Plugin> list = pluginManager.getPlugins();
            for (Plugin plugin : list) {
                if (plugin.getShortName().equals(shortname)) {
                    result = CodeStatusUtil.resultByCodeEn("JENKINS_GET_VERSION_SUCCESS");
                    result.setData(plugin.getVersion());
                }
            }
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_GET_VERSION_FAILURE");
            return result;
        }
        return result;
    }
    /**
     * @Description 查询构建列表
     * @param folderName 文件夹名
     * @param jobName Job名
     * @result 构建列表
     */
    public Result getBuildList(String folderName, String jobName, String imagePre) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.从JenKins获取构建列表
        return getBuildListFromJenkins(folderName, jobName, imagePre);
    }
    private Result getBuildListFromJenkins(String folderName, String jobName, String imagePre) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_BUILDLIST_QUERY_SUCCESS");
        try {
            Job fJob = jenkinsServer.getJob(folderName);
            FolderJob folderJob = jenkinsServer.getFolderJob(fJob).orNull();
            Job job = jenkinsServer.getJob(folderJob, jobName);
            List<Build> buildList = job.details().getBuilds();
            List<TaskBuildDetails> taskBuildDetailsList = new ArrayList<>();
            for (Build build: buildList) {
                BuildWithDetails buildWithDetails = build.details();
                TaskBuildDetails taskBuildDetails = new TaskBuildDetails();
                taskBuildDetails.setIndex(Integer.parseInt(buildWithDetails.getId()));
                taskBuildDetails.setBuildStatus(buildWithDetails.getResult());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                taskBuildDetails.setDate(format.format(new Date(buildWithDetails.getTimestamp())));
                format = new SimpleDateFormat("yyyyMMddHHmm");
                taskBuildDetails.setImageTag(imagePre + format.format(new Date(buildWithDetails.getTimestamp())));
                taskBuildDetails.setLog(buildWithDetails.getConsoleOutputText());
                taskBuildDetailsList.add(taskBuildDetails);
            }
            result.setData(taskBuildDetailsList);
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_BUILDLIST_QUERY_FAILURE");
            return result;
        }
        return result;
    }

    /**
     * 构建Job开始
     *
     * @param folderName 文件夹名称
     * @param jobname    Job名称
     * @return 构建Job开始信息
     */
    public Result buildJobCreate(String folderName, String jobname) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.调用构建接口
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_JOB_BUILD);
        url = url.replace("{folder}", folderName);
        url = url.replace("{jobname}", jobname);
        ResponseEntity<String> responseEntity = RestClient.doPostWithAuthAndParameters(loginUserName, password, url);
        if (responseEntity.getStatusCodeValue() == 201) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_CREATE_SUCCESS");
        } else {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_CREATE_FAILURE");
            return result;
        }
        return result;
    }
    /**
     * @Description 构建中止
     * @param folderName 文件夹名
     * @param jobName Job名
     * @param buildNum 构建数
     * @result 构建信息
     */
    public Result stopBuildingJob (String folderName, String jobName, int buildNum) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.获取构建
        result = queryBuildByNum(folderName, jobName, buildNum);
        if (result.getSuccess() == 0) return result;
        //3.中止构建
        Build build = (Build) result.getData();
        try {
            build.Stop();
            Thread.sleep(300);
            result = queryBuildByNum(folderName, jobName, buildNum);
            if (result.getSuccess() == 0) return result;
            build = (Build) result.getData();
            if (build.details().getResult() == BuildResult.ABORTED)
                result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_ABORT_SUCCESS");
            else
                result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_ABORT_FAILURE");
            return result;
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_BUILD_ABORT_FAILURE");
            return result;
        }
    }

    /**
     * 删除Job
     *
     * @param folderName 服务名称
     * @param jobname    Job名称
     * @return 删除Job信息
     */
    public Result deleteJob(String folderName, String jobname) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.删除账号
        ResponseEntity<String> delResponseEntity = deleteCredentials(jobname);
        if (delResponseEntity == null){
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_DELETE_FAILURE");
            return result;
        }
        //3.调用删除接口
        String url = PropertiesConfUtil.getInstance().getProperty(RestConstant.JENKINS_JOB_DELETE);
        url = url.replace("{folder}", folderName);
        url = url.replace("{jobname}", jobname);
        ResponseEntity<String> responseEntity = RestClient.doPostWithAuthAndParameters(loginUserName, password, url);
        if (responseEntity == null) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_DELETE_FAILURE");
            return result;
        } else {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOB_DELETE_SUCCESS");
        }
        return result;
    }

    /**
     * 查询Job列表
     *
     * @param srvname 服务名称
     * @return Job列表
     */
    public Result getJobList(String srvname) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.判断文件夹是否存在
        result = queryFolder(srvname);
        if (result.getSuccess() == 0) return result;
        FolderJob folderJob = (FolderJob) result.getData();
        //3.查询Jobs列表
        result = queryJobList(folderJob);
        if (result.getSuccess() == 1) {
            Map<String, Job> jobMaps = (Map<String, Job>) result.getData();
            List<String> jobList = new ArrayList<>();
            for (Job job: jobMaps.values()) {
                jobList.add(job.getName());
            }
            result.setData(jobList);
        }
        return result;
    }

    /**
     * @param folderName 文件夹名
     * @Description 查询文件夹
     * @result 文件夹
     */
    private Result queryFolder(String folderName) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_QUERY_FAILURE");
        try {
            //1.获取所有文件夹和项目Job
            Map<String, Job> jobMap = jenkinsServer.getJobs();
            for (String key : jobMap.keySet()) {
                Job job = jobMap.get(key);
                FolderJob folderJob = jenkinsServer.getFolderJob(job).orNull();
                //2.判断是项目Job则略过
                if (folderJob == null) continue;
                //3.检测到目标文件夹则置true
                if (folderJob.getDisplayName().equals(folderName)) {
                    result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_QUERY_SUCCESS");
                    result.setData(folderJob);
                    break;
                }
            }
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_FOLDER_QUERY_FAILURE");
            return result;
        }
        return result;
    }

    /**
     * @param folderJob 文件夹
     * @Description 向JenKins请求文件夹下的项目列表
     * @result 文件夹下的项目列表
     */
    private Result queryJobList(FolderJob folderJob) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_JOBS_QUERY_SUCCESS");
        try {
            Map<String, Job> jobMaps = jenkinsServer.getJobs(folderJob);
            result.setData(jobMaps);
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_JOBS_QUERY_FAILURE");
            return result;
        }
        return result;
    }
    /**
     * @Description 获取镜像名
     * @param folderName 文件夹名
     * @param jobName Job名
     * @param buildNum 构建数
     * @param imagePre 镜像前缀
     * @result 镜像名
     */
    public Result getImageTag(String folderName, String jobName, int buildNum, String imagePre) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.获取构建
        result = queryBuildByNum(folderName, jobName, buildNum);
        if (result.getSuccess() == 0) return result;
        //3.获取镜像名
        Build build = (Build) result.getData();
        result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_SUCCESS");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            result.setData(imagePre + format.format(new Date(build.details().getTimestamp())));
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
        }
        return result;
    }
    /**
     * @Description 根据构建数获取构建
     * @param folderName 文件夹名
     * @param jobName Job名
     * @param buildNum 构建数
     * @result 构建
     */
    private Result queryBuildByNum(String folderName, String jobName, int buildNum) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_SUCCESS");
        try {
            Job fJob = jenkinsServer.getJob(folderName);
            FolderJob folderJob = jenkinsServer.getFolderJob(fJob).orNull();
            Job job = jenkinsServer.getJob(folderJob, jobName);
            Build build = job.details().getBuildByNumber(buildNum);
            if (build != null) {
                result.setData(build);
            } else {
                result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
            }
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
            return result;
        }
        return result;
    }
    public Result getLastBuildDate(String folderName, String jobName) {
        //1.连接服务器
        Result result = connectToServer();
        if (result.getSuccess() == 0) return result;
        //2.获取构建
        result = queryLastBuild(folderName, jobName);
        if (result.getSuccess() == 0) return result;
        Build build = (Build) result.getData();
        result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_SUCCESS");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            result.setData(format.format(new Date(build.details().getTimestamp())));
        } catch (IOException e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_IMAGE_QUERY_FAILURE");
            return result;
        }
        return result;
    }
    /**
     * @Description 获取最后一次构建
     * @param folderName 文件夹名
     * @param jobName Job名
     * @result 构建
     */
    private Result queryLastBuild(String folderName, String jobName) {
        Result result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_SUCCESS");
        try {
            Job fJob = jenkinsServer.getJob(folderName);
            FolderJob folderJob = jenkinsServer.getFolderJob(fJob).orNull();
            Job job = jenkinsServer.getJob(folderJob, jobName);
            Build build = job.details().getLastBuild();
            if (build != null) {
                result.setData(build);
            } else {
                result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
            }
        } catch (Exception e) {
            result = CodeStatusUtil.resultByCodeEn("JENKINS_CONNECT_FAILURE");
            return result;
        }
        return result;
    }
}
