package com.cloud.paas.util.rest;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 11:21 2017/10/25
 * @Modified by:
 */
public class RestConstant {

	public static final String REST_CONN_TIMEOUT = "rest.connect.timeout";

	public static final String REST_REQUEST_CONN_TIMEOUT = "rest.request.connect.timeout";

	public static final String REST_SOCKET_TIMEOUT = "rest.socket.timeout";

	public static final String REST_REQUEST_RETRY = "rest.request.retry";

	public static final String REST_SEND_RETRY_FLAG = "rest.send.retry.flag ";

    public static final String QUERY_RS = "query.rs";

	public static final String QUERY_DEPLOYMENT = "query.deployment";

	public static final String QUERY_SERVICE = "query.service";

    public static final String K8S_DEPLOY_APIVERSION = "deploy.apiVersion";

    public static final String QUERY_POD = "query.pod";

    public static final String OP_POD = "op.pod";

    public static final String K8S_SERVICE_APIVERSION = "service.apiVersion";

    public static final String K8S_SERVICE_SESSION_AFFINITY = "service.spec.sessionAffinity";

    public static final String K8S_SCALE_APIVERSION = "scale.apiVersion";

    public static final String K8S_DEPLOYMENT ="k8s.deployment";

    public static final String K8S_SERVICE = "k8s.service";

    public static final String K8S_NAMESPACES = "k8s.namespaces";

    public static final String K8S_DEPLOYMENT_MIN_READY_SECONDS  = "deployment.spec.minReadySeconds";

    public static final String OP_BUSIPKG = "op.busiPkg";

    public static final String OP_IMAGE = "op.image";

    public static final String QUERY_REMOTE_IMAGE ="query.remote.image";

    public static final String QUERY_RULE_IMAGE ="query.rule.image";

    public static final String QUERY_CODE_STATUS ="query.code.status";

    public static final String QUERY_CODE_EN_STATUS = "query.code.en.status";

    public static final String QUERY_CODE_ALL_STATUS ="query.code.all.status";

    public static final String QUERY_ALL_CONFIG_VALUE ="query.all.config.value";

    public static final String QUERY_CODE_EN_ALL_STATUS = "query.code.en.all.status";

    public static final String QUERY_CONFIG_BY_ENNAME = "query.config.by.enname";

    public static final String QUERY_INGRESS = "query.ingress";

    public static final String QUERY_SCALE = "query.scale";

    public static final String K8S_HOST = "k8s.host";

    public static final String K8S_INGRESS = "k8s.ingress";

    public static final String K8S_INGRESS_APIVERSION = "ingress.apiVersion";

    public static final String K8S_SCALE = "k8s.scale";

    public static final String QUERY_BUSIPKG_DETAIL = "query.busipkg.detail";

    public static final String QUERY_IMAGE_INUSE = "query.image.inuse";

    public static final String QUERY_INFLUXDB = "query.influxDB";

    public static final String QUERY_PODNAME_BY_LABELS = "query.podname.by.labels";

    public static final String QUERY_LIMITS_BY_DEPLOYMENT = "query.limits.by.deployment";

    public static final String QUERY_LIMITS_BY_POD = "query.limits.by.pod";

    public static final String QUERY_DEPLOYMENT_INFO = "query.deployment.info";

    public static final String QUERY_K8S_DASHBOARD_POD_LOG = "query.k8s.dashboard.pod.log";

    public static final String QUERY_SRV_BY_SRVID = "query.srv.by.srvid";

    public static final String JENKINS_SERVER = "jenkins.server";

    public static final String JENKINS_USEERNAME = "jenkins.username";

    public static final String JENKINS_PWD = "jenkins.pwd";

    public static final String JENKINS_JOB_CREATE = "jenkins.job.create";

    public static final String JENKINS_JOB_UPDATE = "jenkins.job.update";

    public static final String JENKINS_CREDENTIALS_DELETE = "jenkins.credentials.delete";

    public static final String JENKINS_CREDENTIALS_CREATE = "jenkins.credentials.create";

    public static final String JENKINS_CREDENTIALS_JSON = "jenkins.credentials.json";

    public static final String JENKINS_CREDENTIALS_QUERY = "jenkins.credentials.query";

    public static final String JENKINS_SONAR_JAVAOPTS = "jenkins.sonar.javaopts";

    public static final String JENKINS_ANT_VERSION = "jenkins.ant.version";

    public static final String JENKINS_JOB_BUILD = "jenkins.job.build";

    public static final String JENKINS_JOB_DELETE = "jenkins.job.delete";

    public static final String JENKINS_JOB_IMAGE = "jenkins.job.image";

    public static final String K8S_CEPH_SECRET_NAME = "k8s.ceph.secret.name";

    public static final String K8S_CEPH_SECRET_NAMESPACE = "k8s.ceph.secret.namespace";

    public static final String QUERY_PV = "query.pv";

    public static final String PV_API_VERSION = "pv.apiVersion";

    public static final String K8S_PV = "k8s.pv";

    public static final String QUERY_PVC= "query.pvc";

    public static final String PVC_API_VERSION = "pvc.apiVersion";

    public static final String K8S_PVC = "k8s.pvc";

    public static final String DEPLOY_LOG_DEFAULT_STORAGE = "deploy.log.default.storage";

    public static final String PV_MON_ROOT_DIR = "pv.mon.root.dir";
}
