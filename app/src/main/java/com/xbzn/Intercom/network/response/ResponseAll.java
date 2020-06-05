package com.xbzn.Intercom.network.response;

public class ResponseAll {
    public String err_code;
    public String message;
    public boolean success;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseAll() {
    }

    public ResponseAll(String err_code, String message, boolean success) {
        this.err_code = err_code;
        this.message = message;
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseAll{" +
                "err_code='" + err_code + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
