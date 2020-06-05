package com.xbzn.Intercom.mvp.model.entity;

import java.util.List;
//用户数据bean
public class DataRoot
{
    private List<UserData> data;

    private int err_code;

    private String message;

    private boolean success;

    public void setData(List<UserData> data){
        this.data = data;
    }
    public List<UserData> getData(){
        return this.data;
    }
    public void setErr_code(int err_code){
        this.err_code = err_code;
    }
    public int getErr_code(){
        return this.err_code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
}