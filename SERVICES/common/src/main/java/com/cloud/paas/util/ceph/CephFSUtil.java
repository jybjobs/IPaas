package com.cloud.paas.util.ceph;

import com.ceph.fs.CephMount;
import com.cloud.paas.configuration.PropertiesConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * CephFS工具类
 * Created by 17798 on 2018/6/6.
 */
public class CephFSUtil {

    private final static Logger logger = LoggerFactory.getLogger(CephFSUtil.class);

    public final static int READ_AND_WRITE = 0777;

    private static CephFSUtil cephFSUtil;

    private static CephMount mount;

    public static CephFSUtil getInstance() {
        if (cephFSUtil == null) {
            synchronized (CephFSUtil.class) {
                if (cephFSUtil == null) {
                    cephFSUtil = new CephFSUtil();
                }
            }
        }
        return cephFSUtil;
    }

    /**
     * 获取CephMount
     * @return
     */
    public CephMount achieveCephMount () {
        if (mount == null) {
            synchronized (CephFSUtil.class) {
                if (mount == null) {
                    mount = new CephMount(PropertiesConfUtil.getInstance().getProperty(CephConstant.CEPH_USER));
                    mount.conf_set("mon_host", PropertiesConfUtil.getInstance().getProperty(CephConstant.CEPH_MON_HOST));
                    mount.conf_set("key", PropertiesConfUtil.getInstance().getProperty(CephConstant.CEPH_CLIENT_ADMIN_KEYRING_KEY));
                }
            }
        }
        return mount;
    }

    /**
     * 创建文件夹
     * @param mountDir 挂载目录
     * @param newDir 新建目录
     * @param mode 模式（可读可写）
     * @return dir 共享文件夹路径
     */
    public String mkdir (String mountDir, String newDir, int mode) throws IOException{
        logger.debug("创建文件夹");
        CephMount mount = CephFSUtil.getInstance().achieveCephMount();
        mount.mount(mountDir);
        mount.mkdirs(newDir,mode);
        mount.unmount();
        return mountDir + newDir;
    }

}
