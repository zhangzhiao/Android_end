package com.xbzn.Intercom.network;

import android.util.Log;

import com.google.gson.*;
import com.xbzn.Intercom.network.requst.DeviceLogs;
import com.xbzn.Intercom.network.requst.OpenData;
import com.xbzn.Intercom.network.response.ResponseAll;
import com.xbzn.Intercom.network.service.CommonService;
import com.xbzn.Intercom.service_android.WarnInfoBean;
import com.xbzn.Intercom.utils.HttpManager;
import com.xbzn.Intercom.utils.SharedUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Base64;

import static com.jess.arms.utils.PermissionUtil.TAG;

//网络相关类
public class NetWorkStart {
    SharedUtil sharedUtil;

    public NetWorkStart(SharedUtil sharedUtil) {
        this.sharedUtil = sharedUtil;
    }

    private HttpManager httpManager =new HttpManager();
    private CommonService commonService = httpManager.getService(CommonService.class);
    //上报设备信息
    public void sendDevice_logs(DeviceLogs deviceLogs){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String body ="{\"DeviceLogs\":"+gson.toJson(deviceLogs)+"}";
        Log.i(TAG, "sendDevice_logs: "+body);
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Log.i(TAG, "sendDevice_logs: token"+sharedUtil.getString("token"));
        Call<ResponseBody> call = commonService.postCommonUp_Log(sharedUtil.getString("token"),requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                    Log.i(TAG, "onResponse: 数据上报OK"+response.body().toString()+"request"+ call.request().body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });
    }
    //上报开门信息
    public void sendOpen_logs(OpenData openData){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        openData.setImgurl(sharedUtil.getString("img"));
        JsonObject jsonObject =new JsonObject();
        JsonElement jsonElement =new JsonParser().parse(gson.toJson(openData));
        jsonObject.add("OpenLogs",jsonElement);
        String body =gson.toJson(jsonObject);
        Log.i(TAG, "sendDevice_logs: Resild_id "+openData.getResild_id());
        Log.i(TAG, "sendDevice_logs: "+body);
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Log.i(TAG, "sendDevice_logs: token"+sharedUtil.getString("token"));
        Call<ResponseAll> call =commonService.postCommonUp_Open(sharedUtil.getString("token"),requestBody);

        call.enqueue(new Callback<ResponseAll>() {
            @Override
            public void onResponse(Call<ResponseAll> call, Response<ResponseAll> response) {
                if(response.body().getSuccess()){
                    Log.i(TAG, "onResponse: 开门数据上报OK"+response.body().toString()+"request"+ call.request().body().toString());
                }else {
                    onFailure(call,new Throwable("数据上报失败"));
                }
            }

            @Override
            public void onFailure(Call<ResponseAll> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });
    }
    public void sendWarn_logs(WarnInfoBean warnInfoBean){
        GsonBuilder gsonBuilder = new GsonBuilder();
        warnInfoBean.setDevice_id(sharedUtil.getString("device_id"));
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        JsonObject jsonObject =new JsonObject();
        JsonElement jsonElement =new JsonParser().parse(gson.toJson(warnInfoBean));
        jsonObject.add("WarnLog",jsonElement);
        String body =gson.toJson(jsonObject);
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Log.i(TAG, "sendDevice_logs: token"+sharedUtil.getString("token"));
        Call<ResponseAll> call =commonService.postCommonWarn_Log(sharedUtil.getString("token"),requestBody);

        call.enqueue(new Callback<ResponseAll>() {
            @Override
            public void onResponse(Call<ResponseAll> call, Response<ResponseAll> response) {
                if(response.body().getSuccess()){
                    Log.i(TAG, "onResponse: 摄像头预警"+response.body().toString()+"request"+ call.request().body().toString());
                }else {
                    onFailure(call,new Throwable("摄像头预警 数据上报失败"));
                }
            }

            @Override
            public void onFailure(Call<ResponseAll> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });
    }
}
