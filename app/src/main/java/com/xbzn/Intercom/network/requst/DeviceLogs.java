package com.xbzn.Intercom.network.requst;

public class DeviceLogs {
    private String device_id;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLogout_code() {
        return logout_code;
    }

    public void setLogout_code(String logout_code) {
        this.logout_code = logout_code;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public DeviceLogs(String device_id, String login_time, String logout_time, String reason, String logout_code, String device_code) {
        this.device_id = device_id;
        this.login_time = login_time;
        this.logout_time = logout_time;
        this.reason = reason;
        this.logout_code = logout_code;
        this.device_code = device_code;
    }

    private String login_time;
    private String logout_time;
    private String reason;
    private String logout_code;
    private String device_code;

}
