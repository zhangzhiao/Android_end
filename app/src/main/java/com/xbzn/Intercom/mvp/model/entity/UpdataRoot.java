package com.xbzn.Intercom.mvp.model.entity;

/**
 * Created by Enzo Cotter on 2020/6/4.
 */
public class UpdataRoot
{
    private Data data;

    private int err_code;

    private String message;

    private boolean success;

    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
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
    public class Data
    {
        private String apk_url;

        private String app_version;

        private String create_time;

        private int device_type;

        private String info;

        public void setApk_url(String apk_url){
            this.apk_url = apk_url;
        }
        public String getApk_url(){
            return this.apk_url;
        }
        public void setApp_version(String app_version){
            this.app_version = app_version;
        }
        public String getApp_version(){
            return this.app_version;
        }
        public void setCreate_time(String create_time){
            this.create_time = create_time;
        }
        public String getCreate_time(){
            return this.create_time;
        }
        public void setDevice_type(int device_type){
            this.device_type = device_type;
        }
        public int getDevice_type(){
            return this.device_type;
        }
        public void setInfo(String info){
            this.info = info;
        }
        public String getInfo(){
            return this.info;
        }
    }

}