package com.xbzn.Intercom.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

//用户数据
@Entity
public class UserData {

    private String build_id;

    private String build_name;

    private String face_img;

    private String phone_no;

    private String room_id;

    private String room_number;

    private String uptown_id;

    private String uptown_name;
    @Index(unique = true)
    private String user_id;

    private String user_name;
//“sex”:性别，
//“ic_card”：ic卡号
//“user_type”：用户类型：
   private String sex;
   private String ic_card;
   private String user_type;
@Generated(hash = 1466202132)
public UserData(String build_id, String build_name, String face_img,
        String phone_no, String room_id, String room_number, String uptown_id,
        String uptown_name, String user_id, String user_name, String sex,
        String ic_card, String user_type) {
    this.build_id = build_id;
    this.build_name = build_name;
    this.face_img = face_img;
    this.phone_no = phone_no;
    this.room_id = room_id;
    this.room_number = room_number;
    this.uptown_id = uptown_id;
    this.uptown_name = uptown_name;
    this.user_id = user_id;
    this.user_name = user_name;
    this.sex = sex;
    this.ic_card = ic_card;
    this.user_type = user_type;
}
@Generated(hash = 1838565001)
public UserData() {
}
    @Override
    public String toString() {
        return "UserData{" +
                "build_id='" + build_id + '\'' +
                ", build_name='" + build_name + '\'' +
                ", face_img='" + face_img + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", room_id='" + room_id + '\'' +
                ", room_number='" + room_number + '\'' +
                ", uptown_id='" + uptown_id + '\'' +
                ", uptown_name='" + uptown_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
    public String getBuild_id() {
        return this.build_id;
    }
    public void setBuild_id(String build_id) {
        this.build_id = build_id;
    }
    public String getBuild_name() {
        return this.build_name;
    }
    public void setBuild_name(String build_name) {
        this.build_name = build_name;
    }
    public String getFace_img() {
        return this.face_img;
    }
    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }
    public String getPhone_no() {
        return this.phone_no;
    }
    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
    public String getRoom_id() {
        return this.room_id;
    }
    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
    public String getRoom_number() {
        return this.room_number;
    }
    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }
    public String getUptown_id() {
        return this.uptown_id;
    }
    public void setUptown_id(String uptown_id) {
        this.uptown_id = uptown_id;
    }
    public String getUptown_name() {
        return this.uptown_name;
    }
    public void setUptown_name(String uptown_name) {
        this.uptown_name = uptown_name;
    }
    public String getUser_id() {
        return this.user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getIc_card() {
        return this.ic_card;
    }
    public void setIc_card(String ic_card) {
        this.ic_card = ic_card;
    }
    public String getUser_type() {
        return this.user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
