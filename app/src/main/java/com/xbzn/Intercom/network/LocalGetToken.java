package com.xbzn.Intercom.network;

import android.util.Log;

import com.xbzn.Intercom.mvp.model.entity.ModelInfo;
import com.xbzn.Intercom.network.service.CommonService;
import com.xbzn.Intercom.utils.HttpManager;
import com.xbzn.Intercom.utils.SharedPreferencesUtil;
import com.xbzn.Intercom.utils.SharedUtil;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LocalGetToken {
    public static void getLocalToken(String Android_id, SharedUtil sharedUtil){

        HttpManager httpManager =new HttpManager();
        CommonService commonService =   httpManager.getService(CommonService.class);
        String body ="{\"app_id\":\""+Android_id+"\"}";
        Log.i(TAG, "getLocalToken: "+body);
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Call<ModelInfo> rootCall = commonService.Login(requestBody);
        rootCall.enqueue(new Callback<ModelInfo>() {
            @Override
            public void onResponse(Call<ModelInfo> call, Response<ModelInfo> response) {
                if(response.body().getSuccess()){
                    sharedUtil.putString("token",response.body().getData().getToken());
                    Log.i(TAG, "onResponse: "+response.body().getData().getToken());
                    sharedUtil.putString("device_id",response.body().getData().getDevice_id());
                    Log.i(TAG, "onResponse: "+sharedUtil.getString("device_id"));
                    sharedUtil.putString("uptown_id",response.body().getData().getUptown_id());
                }
            }

            @Override
            public void onFailure(Call<ModelInfo> call, Throwable t) {

            }
        });
    }
}
