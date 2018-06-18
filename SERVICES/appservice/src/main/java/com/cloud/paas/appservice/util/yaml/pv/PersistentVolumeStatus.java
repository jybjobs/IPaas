package com.cloud.paas.appservice.util.yaml.pv;

/**
 * Created by 17798 on 2018/6/7.
 */
public class PersistentVolumeStatus {

    private String message;

    private String phase;

    private String reason;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
