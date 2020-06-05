package com.zza.service;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zza.service.NetWor.service.up;
import com.zza.service.bean.Data;
import com.zza.service.bean.DataRoot;
import com.zza.service.bean.ModelInfo;
import com.zza.service.bean.UserData;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    SUtil sUtil;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Data da = (Data) msg.obj;
                    sUtil.putString("token",da.getToken());
                    texts.setText(da.toString());
                    getData(sUtil.getString("token"));
                    Log.i("77777", "handleMessage: "+sUtil.getString("token"));
                    break;
                case 4:
                    UserData data = (UserData) msg.obj;
                    texts.append(data.toString());
                    break;
            }
        }
    };
    Retrofit retrofit =new Retrofit.Builder()
            .baseUrl("http://192.168.1.201:8111/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    TextView texts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texts =findViewById(R.id.texts);
        sUtil =new SUtil(getApplicationContext());
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        login(ANDROID_ID);
        Log.i("TAG", "onCreate: "+ANDROID_ID);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        UserData userData =new UserData("123","123","123","123","123","123","123","123","123","123");
        Log.i("togson", "onCreate: "+ gson.toJson(userData));
    }
    public void login(String android_id){

        up p = retrofit.create(up.class);
        String body ="{\"app_id\":\""+android_id+"\"}";
        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),body);
        Call<ModelInfo> call = p.Login(requestBody);
        call.enqueue(new Callback<ModelInfo>() {
            @Override
            public void onResponse(Call<ModelInfo> call, Response<ModelInfo> response) {
                if(response.body().getSuccess()){
                    handler.sendEmptyMessage(2);
                    Message message =new Message();
                    message.what=3;
                    message.obj=response.body().getData();
                    handler.sendMessage(message);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<ModelInfo> call, Throwable t) {
                t.printStackTrace();
                Log.e("zzzzz", "onFailure: ",t );
            }
        });
    }
    public void getData(String token){
     up p =retrofit.create(up.class);
     Call<DataRoot> call= p.getData(token);
     call.enqueue(new Callback<DataRoot>() {
         @Override
         public void onResponse(Call<DataRoot> call, Response<DataRoot> response) {
                 Log.i("zzaeee", "onResponse: "+response.body().getData().toString());
         }

         @Override
         public void onFailure(Call<DataRoot> call, Throwable t) {
             Log.e("zzz", "onFailure: ",t );
         }
     });
    }


}
