package com.xbzn.Intercom.service_android;

public class WarnInfoBean {
    //device_id:设备id
    //warn_type:预警类型(在线，离线)
    //warn_time:预警时间
    //warn_components_type：预警组件类型1摄像头,2锁
    String device_id;
    String warn_type;
    String warn_time;
    String warn_components_type;

    public WarnInfoBean(String device_id, String warn_type, String warn_time, String warn_components_type) {
        this.device_id = device_id;
        this.warn_type = warn_type;
        this.warn_time = warn_time;
        this.warn_components_type = warn_components_type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getWarn_type() {
        return warn_type;
    }

    public void setWarn_type(String warn_type) {
        this.warn_type = warn_type;
    }

    public String getWarn_time() {
        return warn_time;
    }

    public void setWarn_time(String warn_time) {
        this.warn_time = warn_time;
    }

    public String getWarn_components_type() {
        return warn_components_type;
    }

    public void setWarn_components_type(String warn_components_type) {
        this.warn_components_type = warn_components_type;
    }
}
