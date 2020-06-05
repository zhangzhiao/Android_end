package com.xbzn.Intercom.mvp.model.entity;

public class ModelInfo {
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

    @Override
    public String toString() {
        return "ModelInfo{" +
                "data=" + data +
                ", err_code=" + err_code +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
