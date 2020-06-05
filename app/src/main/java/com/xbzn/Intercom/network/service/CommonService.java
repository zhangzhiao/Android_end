/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xbzn.Intercom.network.service;

import com.xbzn.Intercom.mvp.model.entity.DataRoot;
import com.xbzn.Intercom.mvp.model.entity.ModelInfo;
import com.xbzn.Intercom.mvp.model.entity.UpdataRoot;
import com.xbzn.Intercom.network.response.ResponseAll;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * ================================================
 * 存放通用的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CommonService {
    //下载图片文件
    //下载图片文件
    @GET
    Call<ResponseBody> getImageFile(@Url String fileUrl);
    //设备上线操作
    @POST("intercom_attest")
    Call<ModelInfo> Login(@Body RequestBody body);
    //获取用户的数据
    @GET("intercom_initialize")
    Call<DataRoot> getData(@Query("token")String token);
//    //下载图片文件
//    @GET
//    Observable<Response<WeatherBean>> getTianqi(@Url String httpUrl, @Query("token") String token);
//
//    //上传投递记录
//    @POST
//    Observable<ResponseBean<DeliverResultBean>> postDelivery(@Url String httpUrl, @Query("token") String token, @Body RequestBody requestBody);
//
//    //上传通用
//    @POST
//    Observable<ResponseBean<DeliverResultBean>> postCommon(@Url String httpUrl, @Query("token") String token, @Body RequestBody requestBody);
//
//    //获取价格
//    @GET
//    Observable<ResponseBean<List<DumpPriceBean>>> getPrice(@Url String httpUrl, @Query("token") String token);
//
//    //上传通用
//    @POST
//    Call<ResponseAll> postCommonObj(@Url String httpUrl, @Query("token")String token,@Body RequestBody requestBody);
    @POST("upload/open_logs")
    Call<ResponseAll> postCommonUp_Open( @Query("token")String token,@Body RequestBody requestBody);
    @POST("upload/device_logs")
    Call<ResponseBody> postCommonUp_Log(@Query("token") String token, @Body RequestBody requestBody);
    @POST("upload/warn_process")
    Call<ResponseAll> postCommonWarn_Log(@Query("token") String token, @Body RequestBody requestBody);
    @POST("intercom_verson_data")
    Call<UpdataRoot> upData(@Query("token") String token, @Body RequestBody requestBody);
}
