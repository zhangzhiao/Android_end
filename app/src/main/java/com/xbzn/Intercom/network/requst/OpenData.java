package com.xbzn.Intercom.network.requst;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Base64;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class OpenData {
    String device_id;
    String resild_id;
    String uptown_id;
    String build_id;
    String room_id;
    String visit_time;
    String unlock_type;
    String imgurl;
    String preson_type;
    @Generated(hash = 1369925641)
    public OpenData(String device_id, String resild_id, String uptown_id,
            String build_id, String room_id, String visit_time, String unlock_type,
            String imgurl, String preson_type) {
        this.device_id = device_id;
        this.resild_id = resild_id;
        this.uptown_id = uptown_id;
        this.build_id = build_id;
        this.room_id = room_id;
        this.visit_time = visit_time;
        this.unlock_type = unlock_type;
        this.imgurl = imgurl;
        this.preson_type = preson_type;
    }
    @Generated(hash = 740343850)
    public OpenData() {
    }
    public String getDevice_id() {
        return this.device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public String getResild_id() {
        return this.resild_id;
    }
    public void setResild_id(String resild_id) {
        this.resild_id = resild_id;
    }
    public String getUptown_id() {
        return this.uptown_id;
    }
    public void setUptown_id(String uptown_id) {
        this.uptown_id = uptown_id;
    }
    public String getBuild_id() {
        return this.build_id;
    }
    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }
    public String getRoom_id() {
        return this.room_id;
    }
    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
    public String getVisit_time() {
        return this.visit_time;
    }
    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }
    public String getUnlock_type() {
        return this.unlock_type;
    }
    public void setUnlock_type(String unlock_type) {
        this.unlock_type = unlock_type;
    }
    public String getImgurl() {
        return this.imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public String getPreson_type() {
        return this.preson_type;
    }
    public void setPreson_type(String preson_type) {
        this.preson_type = preson_type;
    }

   
}
