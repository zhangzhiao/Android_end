package com.xbzn.Intercom.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.haha.perflib.Main;
import com.xbzn.Intercom.R;
import com.xbzn.Intercom.listener.SdkInitListener;
import com.xbzn.Intercom.manager.FaceSDKManager;
import com.xbzn.Intercom.mvp.model.AutoInstallModel;
import com.xbzn.Intercom.mvp.ui.fragmane.FaceSearchFragment;
import com.xbzn.Intercom.mvp.ui.fragmane.VideoViewFragment;
import com.xbzn.Intercom.utils.DialogUtil;
import com.xbzn.Intercom.utils.LoadingDialog;
import com.xbzn.Intercom.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class inMainActivity extends BaseActivity {
    private LoadingDialog loadingDialog;
    private FragmentManager manager;
    private Button studio;
    private VideoViewFragment fragment;
    private FaceSearchFragment faceSearchFragment;
    @BindView(R.id.call_phone)
    Button phone_call;
    @BindView(R.id.video_onclick)
     Button video_call;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_ui);
        faceSearchFragment =new FaceSearchFragment();
        loadingDialog  =new DialogUtil(inMainActivity.this).loadingDialog("加载ing");
        loadingDialog.show();
        initView();
        initLicense();
        init();
        ButterKnife.bind(this);
    }
    private void initView() {
        studio =findViewById(R.id.studio);
        studio.setOnClickListener(v->{
            showBottomButton();
//            startActivity(new Intent(inMainActivity.this, SettingMainActivity.class));
            startFace();
        });
        studio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(inMainActivity.this, BatchImportActivity.class));
                return false;
            }
        });


    }
    @OnClick(R.id.call_phone)
    public void toPhone(){
        int i= AutoInstallModel.installSilent(getPackageName(), Environment.getExternalStorageDirectory().getPath()+"/debug.apk");
        Log.e("TAG", "toPhone: "+i,null );
    }
    @OnClick(R.id.video_onclick)
    public void toVideo(){
        faceSearchFragment.onDestroy();
        fragment =new VideoViewFragment();
        manager =getSupportFragmentManager();
        transaction =manager.beginTransaction();
        transaction.replace(R.id.heads,fragment);
        transaction.commitAllowingStateLoss();
    }

    Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    manager =getSupportFragmentManager();
                    transaction =manager.beginTransaction();
                    if(fragment!=null){
                        fragment.onDestroy();
                    }
                    if(faceSearchFragment==null){
                        faceSearchFragment=new FaceSearchFragment();
                    }
                    transaction.replace(R.id.heads,faceSearchFragment);
                    transaction.commitAllowingStateLoss();
                    break;
            }
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
           startFace();
        }

    }
    private void initLicense() {
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
    protected void showBottomButton(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    public void startFace(){
        handler.sendEmptyMessageDelayed(10,2000);
        loadingDialog.dismiss();
    }
}
