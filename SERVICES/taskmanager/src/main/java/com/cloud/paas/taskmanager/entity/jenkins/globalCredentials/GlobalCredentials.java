package com.cloud.paas.taskmanager.entity.jenkins.globalCredentials;

import java.util.List;

/**
 * @Author: srf
 * @desc: Credentials对象
 * @Date: Created in 2018-04-28 13:58
 * @Modified By:
 */
public class GlobalCredentials {
    private List<Credential> credentials;

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
