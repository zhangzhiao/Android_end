package com.xbzn.Intercom.thread;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.xbzn.Intercom.api.FaceApi;
import com.xbzn.Intercom.app.Config;
import com.xbzn.Intercom.db.UserDBManager;
import com.xbzn.Intercom.listener.SdkInitListener;
import com.xbzn.Intercom.manager.FaceSDKManager;
import com.xbzn.Intercom.mvp.model.entity.User;
import com.xbzn.Intercom.mvp.model.entity.UserData;
import com.xbzn.Intercom.network.Api;
import com.xbzn.Intercom.network.service.CommonService;
import com.xbzn.Intercom.utils.BitmapUtils;
import com.xbzn.Intercom.utils.FileUtils;
import com.xbzn.Intercom.utils.HttpManager;
import com.xbzn.Intercom.utils.ImageUtils;
import com.xbzn.Intercom.utils.SharedPreferencesUtil;
import com.xbzn.Intercom.utils.SharedUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jess.arms.utils.PermissionUtil.TAG;

public class BaidufaceThread extends Thread implements SdkInitListener {

    private HttpManager mHttpManager = null;
    private UserDBManager userDBManager;
    private List<UserData> userDataList;
    private String untown_id;
    private Context context;
    private int faceInt;
    private int startFace = 0;
    private SharedUtil sharedUtil;

    public BaidufaceThread(Context context,SharedUtil sharedUtil) {
        this.context = context;
        this.sharedUtil =sharedUtil;
    }

    @Override
    public void run() {
        super.run();
        userDBManager =new UserDBManager(context);
        FaceSDKManager.getInstance().init(context, this);
        untown_id = sharedUtil.getString("uptown_id");
        mHttpManager = new HttpManager();
        userDataList = userDBManager.searchAll();
    }


    // 根据特征抽取的结果 注册人脸
    private void displayCompareResult(String account_id, String uptown_id, String account_name, byte[] faceFeature) {

        String imageName = uptown_id + "-" + account_id + ".jpg";
        boolean isSuccess = false;
        float ret = -1;
        // 检测人脸，提取人脸特征值
        byte[] resetBytes = new byte[512];

        // 压缩、保存人脸图片至300 * 300
        File faceDir = FileUtils.getBatchImportSuccessDirectory();
        File file = new File(faceDir, imageName);
        Log.e("BaiduRegisterService", "文件大小：" + account_name + ":faceFeature:" + faceFeature.length);
        Bitmap rgbBitmap = BitmapUtils.createBitmap(faceFeature, 0);
        ImageUtils.resize(rgbBitmap, file, 300, 300);
        File file_reset = new File(faceDir, imageName);
        Uri imageUri = Uri.fromFile(file_reset);
        Log.e(TAG, "displayCompareResult: 保存路径"+file_reset.getPath() );
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(imageUri);

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            ret = FaceApi.getInstance().getFeature(bitmap, resetBytes,
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO);
            Log.e(TAG, "displayCompareResult: "+ret );
            if (ret < -1) {
                Log.e("BaiduRegisterService", "未检测到人脸，可能原因：人脸太小");
                Thread.sleep(1000);
                displayCompareResult(account_id,uptown_id,account_name,faceFeature);
            }else{
            isSuccess = FaceApi.getInstance().registerUserIntoDBmanager(uptown_id, account_name, imageName,
                    account_id, resetBytes);
                if (isSuccess) {
                    // 关闭摄像头
                    Log.e("BaiduRegisterService", "人脸注册成功：" + account_name);

                } else {
                    Log.e("BaiduRegisterService", "人脸注册失败：" + account_name);
                }

                startRegisterFace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        input.close();
        } catch (FileNotFoundException e) {
            Log.e("BaiduRegisterService", "文件不存在：" + account_name);
        } catch (Exception e) {
            Log.e("BaiduRegisterService", "文件存储异常：" + account_name);
        }


    }


    private void downLoadFile(String account_id, String uptown_id, String account_name, String imgUrl) {
        Log.e(TAG, "开始下载: "+account_name);
        Log.e(TAG, "下载地址: "+Api.imgUrlLocal+imgUrl);
//        mHttpManager.getService(CommonService.class)
//                .getImageFile(Api.imgUrlLocal+imgUrl)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody value) {
//                        byte[] bys = new byte[0];
//                        try {
//                            Log.e("MainActivity", "获取图片成功" + account_name);
//                            bys = value.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
//                            Log.e(TAG, "onNext: "+bys.length );
//                            displayCompareResult(account_id, uptown_id, account_name, bys);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.e("MainActivity", "downLoadFilefail:" + account_name + e.getMessage());
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("MainActivity", "downLoadFileerroe:" + account_name + e.getMessage());
//                        startRegisterFace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.i("MainActivity", "onComplete");
//                    }
//
//
//                });
        CommonService commonService =mHttpManager.getService(CommonService.class);
        commonService.getImageFile(Api.imgUrlLocal+imgUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                byte[] bys = new byte[0];
                try {
                    Log.e("MainActivity", "获取图片成功" + account_name);
                    bys = response.body().bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                    Log.e(TAG, "onNext: "+bys.length );
                    displayCompareResult(account_id, uptown_id, account_name, bys);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "downLoadFilefail:" + account_name + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    @Override
    public void initStart() {
        Log.i("MainActivity", "initStart: ");

    }

    @Override
    public void initLicenseSuccess() {
        Log.i("MainActivity", "initLicenseSuccess: ");
    }

    @Override
    public void initLicenseFail(int errorCode, String msg) {

    }

    @Override
    public void initModelSuccess() {
        startFace = 0;
        startRegisterFace();
    }

    void setFinish() {
        SharedPreferencesUtil.putBoolean(context, Config.isGetFaceData, true);
        // 数据变化，更新内存
        FaceApi.getInstance().initDatabases(true);
    }


    @Override
    public void initModelFail(int errorCode, String msg) {

    }


    //开始注册人脸
    void startRegisterFace() {
        startFace++;
        UserData userData;
        Log.i(TAG, "initModelSuccess: " + Calendar.getInstance().getTimeInMillis());
        if (userDataList.size() > 0) {
            faceInt = startFace;
            if (startFace == userDataList.size() - 1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        setFinish();

                    }
                }).start();
            }
            if (startFace >= userDataList.size()) {
                setFinish();
                return;
            }
            userData = userDataList.get(startFace);
            if (!userData.getFace_img().equals("")) {
                List<User> listUsers = FaceApi.getInstance().getUserCounttByUserName(untown_id, userData.getUser_id());
                if (listUsers != null && listUsers.size() > 0) {
                    startRegisterFace();
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    downLoadFile(userData.getUser_id(), untown_id,userData.getUser_name(), userData.getFace_img());
                }

            } else {
                startRegisterFace();
            }
        }
    }





}
