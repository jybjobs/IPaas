package com.cloud.paas.taskmanager.entity.jenkins.job.builders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Author: srf
 * @desc: Shell对象
 * @Date: Created in 2018-04-23 16:37
 * @Modified By:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Shell {
    @XmlElement
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
