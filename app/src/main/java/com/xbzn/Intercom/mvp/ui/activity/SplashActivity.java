package com.xbzn.Intercom.mvp.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jld.jldtesttempdemo.servce.SCMSerialServer;
import com.xbzn.Intercom.R;
import com.xbzn.Intercom.listener.SdkInitListener;
import com.xbzn.Intercom.manager.FaceSDKManager;
import com.xbzn.Intercom.network.LocalGetToken;
import com.xbzn.Intercom.network.NetWorkStart;
import com.xbzn.Intercom.network.requst.DeviceLogs;
import com.xbzn.Intercom.service_android.LoadUserDataService;
import com.xbzn.Intercom.thread.BaidufaceThread;
import com.xbzn.Intercom.utils.DialogUtil;
import com.xbzn.Intercom.utils.LoadingDialog;
import com.xbzn.Intercom.utils.TimeUtil;
import com.xbzn.Intercom.utils.ToastUtils;

import java.lang.reflect.Method;

public class SplashActivity extends BaseActivity {
    boolean isBind;
    LoadUserDataService service;
    DialogUtil dialogUtil;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dialogUtil =new DialogUtil(SplashActivity.this);
        hideBottomUIMenu();
        Log.i("TAG", "onCreate: "+getDeviceSN());
        initLicense();
        Intent intent =new Intent(SplashActivity.this,LoadUserDataService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
        handler.post(runnable);
        loadSerial();
         loadingDialog =dialogUtil.loadingDialog("初始化中");
        up();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            LoadUserDataService.MyBinder myBinder = (LoadUserDataService.MyBinder)binder;
            service = myBinder.getService();
            service.getUserDataToDB(sharedUtil);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };
    public static String getDeviceSN() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            Log.i("serial",serial);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }
    private boolean isCall =true;
    Handler handler =new Handler();
    Runnable runnable =new Runnable() {
        @Override
        public void run() {
            if (isCall){
                if(sharedUtil.getString("token").equals("null")&&sharedUtil.getString("device_id").equals("null")){
                    Log.i("zza", "run: null");
                    LocalGetToken.getLocalToken(getDeviceSN(),sharedUtil);
                }else {

                    init();
                }
            }
            handler.postDelayed(runnable,5000);
        }
    };
    private void init() {

        if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_UNACTIVATION) {
            return;
        } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_INIT_FAIL) {
            return;
        } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_INIT_SUCCESS) {
            return;
        } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            loadingDialog.dismiss();
            startActivity(new Intent(SplashActivity.this,inMainActivity.class));
            isCall=false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private void initLicense() {
        LocalGetToken.getLocalToken(getDeviceSN(),sharedUtil);
        BaidufaceThread baidufaceThread =new BaidufaceThread(getApplicationContext(),sharedUtil);
        baidufaceThread.start();
        if (FaceSDKManager.initStatus != FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().init(getApplicationContext(), new SdkInitListener() {
                @Override
                public void initStart() {

                }

                @Override
                public void initLicenseSuccess() {

                }

                @Override
                public void initLicenseFail(int errorCode, String msg) {
                    // 如果授权失败，跳转授权页面
                    ToastUtils.toast(getApplicationContext(), errorCode + msg);
                    startActivity(new Intent(getApplicationContext(), FaceAuthActicity.class));
                }

                @Override
                public void initModelSuccess() {

                }

                @Override
                public void initModelFail(int errorCode, String msg) {

                }
            });
        }
    }
    private void up(){
        NetWorkStart start =new NetWorkStart(sharedUtil);
        DeviceLogs deviceLogs =new DeviceLogs(sharedUtil.getString("device_id"), TimeUtil.getTime(),"null","null","null","null");
        start.sendDevice_logs(deviceLogs);
    }
    public void loadSerial(){
        startService(new Intent(this, SCMSerialServer.class));
    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏

        //for new api versions.
//        Window _window = getWindow();
//        WindowManager.LayoutParams params = _window.getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//        _window.setAttributes(params);
    }
}
