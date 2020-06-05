package com.xbzn.Intercom.mvp.model.entity;
//设备上线二级bean
public class Data
{
    private String build_id;

    private String build_name;

    private String device_id;

    private String device_name;

    private String device_type;

    private String token;

    private String uptown_id;

    private String uptown_name;

    public void setBuild_id(String build_id){
        this.build_id = build_id;
    }
    public String getBuild_id(){
        return this.build_id;
    }
    public void setBuild_name(String build_name){
        this.build_name = build_name;
    }
    public String getBuild_name(){
        return this.build_name;
    }
    public void setDevice_id(String device_id){
        this.device_id = device_id;
    }
    public String getDevice_id(){
        return this.device_id;
    }
    public void setDevice_name(String device_name){
        this.device_name = device_name;
    }
    public String getDevice_name(){
        return this.device_name;
    }
    public void setDevice_type(String device_type){
        this.device_type = device_type;
    }
    public String getDevice_type(){
        return this.device_type;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
    public void setUptown_id(String uptown_id){
        this.uptown_id = uptown_id;
    }
    public String getUptown_id(){
        return this.uptown_id;
    }
    public void setUptown_name(String uptown_name){
        this.uptown_name = uptown_name;
    }
    public String getUptown_name(){
        return this.uptown_name;
    }

    @Override
    public String toString() {
        return "Data{" +
                "build_id='" + build_id + '\'' +
                ", build_name='" + build_name + '\'' +
                ", device_id='" + device_id + '\'' +
                ", device_name='" + device_name + '\'' +
                ", device_type='" + device_type + '\'' +
                ", token='" + token + '\'' +
                ", uptown_id='" + uptown_id + '\'' +
                ", uptown_name='" + uptown_name + '\'' +
                '}';
    }
}