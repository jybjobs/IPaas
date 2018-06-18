package com.cloud.paas.util.dns;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class DnsInfo {
    private String dnsserver;
    private int port;
    private String username;
    private String password;


    public String getDnsserver() {
        return dnsserver;
    }

    public void setDnsserver(String dnsserver) {
        this.dnsserver = dnsserver;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
