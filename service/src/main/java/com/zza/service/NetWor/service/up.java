package com.zza.service.NetWor.service;

import com.zza.service.bean.DataRoot;
import com.zza.service.bean.ModelInfo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface up {
    @POST("intercom_attest")
    Call<ModelInfo> Login(@Body RequestBody body);
    @GET("intercom_initialize")
    Call<DataRoot> getData(@Query("token")String token);
}
