package com.xbzn.Intercom.service_android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xbzn.Intercom.db.UserDBManager;
import com.xbzn.Intercom.mvp.model.entity.*;
import com.xbzn.Intercom.network.service.CommonService;
import com.xbzn.Intercom.utils.DownloadUtil;
import com.xbzn.Intercom.utils.HttpManager;
import com.xbzn.Intercom.utils.SharedUtil;

import java.io.File;
import java.util.List;

import com.xbzn.Intercom.utils.SilentInstall;
import gnu.trove.TObjectByteHashMap;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jess.arms.utils.PermissionUtil.TAG;

public class LoadUserDataService  extends Service{
    HttpManager httpManager =new HttpManager();
    CommonService commonService =   httpManager.getService(CommonService.class);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public LoadUserDataService getService() {
            return LoadUserDataService.this;
        }
    }

    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();
    public void getUserDataToDB(SharedUtil sharedUtil){
        if(sharedUtil.getString("token").equals("null")){
            Toast.makeText(getApplicationContext(),"token暂未获取到", Toast.LENGTH_LONG).show();
            return;
        }else {
            Call<DataRoot> dataRootCall = commonService.getData(sharedUtil.getString("token"));
            dataRootCall.enqueue(new Callback<DataRoot>() {
                @Override
                public void onResponse(Call<DataRoot> call, Response<DataRoot> response) {
                    if (response.body().getSuccess()){
                        UserDBManager userDBManager =new UserDBManager(getApplicationContext());
                        List<UserData> list =response.body().getData();
                        Log.i(TAG, "onResponse: "+list.toString());
                        for (UserData data :list){
                            Log.i(TAG, "onResponse: "+data.toString());
                            userDBManager.insertOrReplace(data);

                        }
                    }else {
                    onFailure(call,new Throwable("获取用户数据失败 success 为false"));
                    }
                }

                @Override
                public void onFailure(Call<DataRoot> call, Throwable t) {
                    Log.e(BaseConfig.TAG, "onFailure: ",t );
                }
            });

        }

    }
    public void upData_apk(SharedUtil sharedUtil){
        String body ="{\"device_id\":\""+sharedUtil.getString("device_id")+"\"}";
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Call<UpdataRoot> call =commonService.upData(sharedUtil.getString("token"),requestBody);
        call.enqueue(new Callback<UpdataRoot>() {
            @Override
            public void onResponse(Call<UpdataRoot> call, Response<UpdataRoot> response) {
                if(response.body().getSuccess()){
                   UpdataRoot.Data data = response.body().getData();
                   if (Integer.parseInt(data.getApp_version())>packageCode(getApplicationContext())){
                       String apk_url =data.getApk_url();
                       DownloadUtil.get().download(apk_url, Environment.getExternalStorageDirectory().getPath(), data.getApp_version() + ".apk", new DownloadUtil.OnDownloadListener() {
                           @Override
                           public void onDownloadSuccess(File file) {
                               SilentInstall.install(file.getPath());
                           }

                           @Override
                           public void onDownloading(int progress) {

                           }

                           @Override
                           public void onDownloadFailed(Exception e) {
                                e.printStackTrace();
                           }
                       });
                   }
                }else {
                    onFailure(call,new Throwable("数据更新接口请求失败"));
                }
            }

            @Override
            public void onFailure(Call<UpdataRoot> call, Throwable t) {

            }
        });
    }
    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
}
