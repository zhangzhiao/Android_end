package com.xbzn.Intercom.mvp.ui.fragmane;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.liantian.ac.F;
import com.google.gson.*;
import com.xbzn.Intercom.R;
import com.xbzn.Intercom.callback.CameraDataCallback;
import com.xbzn.Intercom.callback.FaceDetectCallBack;
import com.xbzn.Intercom.camera.AutoTexturePreviewView;
import com.xbzn.Intercom.camera.CameraPreviewManager;
import com.xbzn.Intercom.db.OpenDoorDBManager;
import com.xbzn.Intercom.db.UserDBManager;
import com.xbzn.Intercom.manager.FaceSDKManager;
import com.xbzn.Intercom.mvp.model.LivenessModel;
import com.xbzn.Intercom.mvp.model.entity.SingleBaseConfig;
import com.xbzn.Intercom.mvp.model.entity.User;
import com.xbzn.Intercom.mvp.model.entity.UserData;
import com.xbzn.Intercom.network.NetWorkStart;
import com.xbzn.Intercom.network.requst.OpenData;
import com.xbzn.Intercom.serial.SerialConfig;
import com.xbzn.Intercom.serial.utils.GPIOUtils;
import com.xbzn.Intercom.utils.BitmapUtils;
import com.xbzn.Intercom.utils.FaceOnDrawTexturViewUtil;
import com.xbzn.Intercom.utils.FileUtils;
import com.xbzn.Intercom.utils.SharedUtil;
import com.xbzn.Intercom.utils.TextureVideoViewOutlineProvider;
import com.xbzn.Intercom.utils.TimeUtil;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import static com.jess.arms.utils.PermissionUtil.TAG;


public class FaceSearchFragment extends Fragment {
    // 图片越大，性能消耗越大，也可以选择640*480， 1280*720
    private static final int PREFER_WIDTH = SingleBaseConfig.getBaseConfig().getRgbAndNirWidth();
    private static final int PERFER_HEIGH = SingleBaseConfig.getBaseConfig().getRgbAndNirHeight();

    private Context mContext;

    // 关闭Debug 模式
    private AutoTexturePreviewView mAutoCameraPreviewView;
    private TextView mDetectText;
    private TextView mTrackText;
    private TextureView mFaceDetectImageView;
    private Paint paint;
    private RectF rectF;
    private int mLiveType;
    private float mRgbLiveScore;
    private SharedUtil sharedUtil;

    // 包含适配屏幕后后的人脸的x坐标，y坐标，和width
    private float[] pointXY = new float[3];
    private boolean requestToInner = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View views = inflater.inflate(R.layout.fragment_face, container,false);
        mContext =getContext();
        Toast.makeText(mContext,"start",Toast.LENGTH_LONG).show();
        sharedUtil =new SharedUtil(getContext());
        return views;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View view1 = getView();
        userDBManager =new UserDBManager(getContext());
        initView(view1);
    }

    private void initView(View view) {
        // 活体状态
        mLiveType = SingleBaseConfig.getBaseConfig().getType();
        // 活体阈值
        mRgbLiveScore = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
        // 画人脸框
        paint = new Paint();
        rectF = new RectF();
        mFaceDetectImageView = view.findViewById(R.id.draw_detect_face_view);
        mFaceDetectImageView.setOpaque(false);
        mFaceDetectImageView.setKeepScreenOn(true);

        // 单目摄像头RGB 图像预览
        mAutoCameraPreviewView = view.findViewById(R.id.auto_camera_preview_view);
        mAutoCameraPreviewView.setOutlineProvider(new TextureVideoViewOutlineProvider());
        mFaceDetectImageView.setOutlineProvider(new TextureVideoViewOutlineProvider());
        mAutoCameraPreviewView.setClipToOutline(true);
        mFaceDetectImageView.setClipToOutline(true);
        mFaceDetectImageView.setVisibility(View.VISIBLE);
        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
        mDetectText = view.findViewById(R.id.detect_text);
        mDetectText.setTextColor(0xffffffff);
        mTrackText = view.findViewById(R.id.track_txt);

    }

    @Override
    public void onResume() {
        super.onResume();
        startTestCloseDebugRegisterFunction();
    }
    private void startTestCloseDebugRegisterFunction() {
        // TODO ： 临时放置
        CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_USB);
        CameraPreviewManager.getInstance().startPreview(getContext(), mAutoCameraPreviewView,
                PREFER_WIDTH, PERFER_HEIGH, new CameraDataCallback() {
                    @Override
                    public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
                        // 摄像头预览数据进行人脸检测
                        FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                height, width, mLiveType, new FaceDetectCallBack() {
                                    @Override
                                    public void onFaceDetectCallback(LivenessModel livenessModel) {

                                        if (SingleBaseConfig.getBaseConfig().getDetectFrame().equals("fixedarea")) {
                                            isInserLimit(livenessModel);
                                            // 输出结果
                                            checkCloseResult(livenessModel);
                                        }

                                        if (SingleBaseConfig.getBaseConfig().getDetectFrame().equals("wireframe")) {
                                            checkCloseResult(livenessModel);
                                        }
                                    }

                                    @Override
                                    public void onTip(int code, String msg) {
                                        displayTip(code, msg);
                                    }

                                    @Override
                                    public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
                                        if (SingleBaseConfig.getBaseConfig().getDetectFrame().equals("wireframe")) {
                                            showFrame(livenessModel);
                                        }

                                    }
                                });
                    }
                });
    }
    private void displayTip(final int code, final String tip) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (code == 0) {
                    mTrackText.setVisibility(View.GONE);
                } else {
                    mTrackText.setVisibility(View.VISIBLE);
                    mTrackText.setText("识别失败");
                    mTrackText.setBackgroundColor(Color.RED);
                }
                mDetectText.setText(tip);
            }
        });
    }
    /**
     * 绘制人脸框。
     */
    private void showFrame(final LivenessModel model) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = mFaceDetectImageView.lockCanvas();
                if (canvas == null) {
                    mFaceDetectImageView.unlockCanvasAndPost(canvas);
                    return;
                }
                if (model == null) {
                    // 清空canvas
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mFaceDetectImageView.unlockCanvasAndPost(canvas);
                    return;
                }
                FaceInfo[] faceInfos = model.getTrackFaceInfo();
                if (faceInfos == null || faceInfos.length == 0) {
                    // 清空canvas
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mFaceDetectImageView.unlockCanvasAndPost(canvas);
                    return;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                FaceInfo faceInfo = faceInfos[0];

                rectF.set(FaceOnDrawTexturViewUtil.getFaceRectTwo(faceInfo));
                // 检测图片的坐标和显示的坐标不一样，需要转换。
                FaceOnDrawTexturViewUtil.mapFromOriginalRect(rectF,
                        mAutoCameraPreviewView, model.getBdFaceImageInstance());
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                // 绘制框
                canvas.drawRect(rectF, paint);
                mFaceDetectImageView.unlockCanvasAndPost(canvas);

            }
        });
    }
    UserDBManager userDBManager;
    // 判断人脸是否在园内
    private void isInserLimit(final LivenessModel livenessModel) {
        if (livenessModel == null) {
            requestToInner = false;
            return;
        }
        FaceInfo[] faceInfos = livenessModel.getTrackFaceInfo();
        if (faceInfos == null || faceInfos.length == 0) {
            requestToInner = false;
            return;
        }

        pointXY[0] = livenessModel.getFaceInfo().centerX;
        pointXY[1] = livenessModel.getFaceInfo().centerY;
        pointXY[2] = livenessModel.getFaceInfo().width;
        FaceOnDrawTexturViewUtil.converttPointXY(pointXY, mAutoCameraPreviewView,
                livenessModel.getBdFaceImageInstance(), livenessModel.getFaceInfo().width);
        float lfetLimitX = AutoTexturePreviewView.circleX - AutoTexturePreviewView.circleRadius;
        float rightLimitX = AutoTexturePreviewView.circleX + AutoTexturePreviewView.circleRadius;
        float topLimitY = AutoTexturePreviewView.circleY - AutoTexturePreviewView.circleRadius;
        float bottomLimitY = AutoTexturePreviewView.circleY + AutoTexturePreviewView.circleRadius;

        if (pointXY[0] - pointXY[2] / 2 < lfetLimitX
                || pointXY[0] + pointXY[2] / 2 > rightLimitX
                || pointXY[1] - pointXY[2] / 2 < topLimitY
                || pointXY[1] + pointXY[2] / 2 > bottomLimitY) {
            //如果人脸在检测范围之内
            requestToInner = true;
        } else {
            requestToInner = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFaceDetectImageView.setVisibility(View.GONE);
        mAutoCameraPreviewView.setVisibility(View.GONE);
    }
    boolean isdia =true;
    private void checkCloseResult(final LivenessModel livenessModel) {
        // 当未检测到人脸UI显示
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (livenessModel == null || livenessModel.getFaceInfo() == null) {
                    mTrackText.setVisibility(View.GONE);
                    mDetectText.setText("未检测到人脸");
//                    mFaceDetectImageView.setVisibility(View.INVISIBLE);
//                    mAutoCameraPreviewView.setVisibility(View.INVISIBLE);
                    return;
                } else {
                    mFaceDetectImageView.setVisibility(View.VISIBLE);
                    mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                    if (requestToInner) {

                        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                        mFaceDetectImageView.setVisibility(View.VISIBLE);
                        mTrackText.setVisibility(View.VISIBLE);
                        mTrackText.setText("预览区域内人脸不全");
                        mTrackText.setBackgroundColor(Color.RED);
                        mDetectText.setText("");
                        mDetectText.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (mLiveType == 1) {
                        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                        mFaceDetectImageView.setVisibility(View.VISIBLE);
                        User user = livenessModel.getUser();
                        if (user == null) {
                            mDetectText.setTextColor(0xffffffff);
                            mDetectText.setText("搜索不到用户");
                            mDetectText.setVisibility(View.VISIBLE);
                        } else {
                            mFaceDetectImageView.setVisibility(View.VISIBLE);
                            String absolutePath = FileUtils.getBatchImportSuccessDirectory()
                                    + "/" + user.getImageName();
                            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                            BDFaceImageInstance image = livenessModel.getBdFaceImageInstance();
                            Bitmap faceBitmap = BitmapUtils.getInstaceBmp(image);
                            String faceBitmapStr = BitmapUtils.bitmapToBase64(faceBitmap);

                            mTrackText.setText("识别成功");
                          if(isdia){


                              UserData userData = userDBManager.searchByWhere(user.getUserInfo());
                              sharedUtil.putString("img",faceBitmapStr);
                              Log.i("TAG", "run: "+userData.toString());
                              OpenData openData =new OpenData(sharedUtil.getString("device_id"),userData.getUser_id(),sharedUtil.getString("uptown_id"),userData.getBuild_id(),userData.getRoom_id(), TimeUtil.getTime(),"1",
                                      faceBitmapStr,"1");
                              OpenDoorDBManager manager =new OpenDoorDBManager(getContext());
                              //保存到数据库里面的开门记录
                              manager.insertOrReplace(openData);
                              new Thread(new Runnable() {
                                  @Override
                                  public void run() {
                                      GPIOUtils.gpio_open0(SerialConfig.ON_GREEN);
                                      try {
                                          Thread.sleep(2000);
                                          GPIOUtils.gpio_open0(SerialConfig.OFF_GREEN);
                                      } catch (InterruptedException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }).start();
//                              NetWorkStart netWorkStart =new NetWorkStart(sharedUtil);
//                              netWorkStart.sendOpen_logs(new OpenData(sharedUtil.getString("device_id"),userData.getUser_id(),sharedUtil.getString("uptown_id"),userData.getBuild_id(),userData.getRoom_id(), TimeUtil.getTime(),"1",
//                                      "","1"));
                              messageDialog("",user.getUserName(),getActivity(),bitmap);
                              isdia=false;
                          }

                            mDetectText.setText("欢迎您， " + user.getUserName());
                        }
                    } else {
                        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                        mFaceDetectImageView.setVisibility(View.VISIBLE);
                        float rgbLivenessScore = livenessModel.getRgbLivenessScore();
                        if (rgbLivenessScore < mRgbLiveScore) {
                            mTrackText.setVisibility(View.VISIBLE);
                            mTrackText.setText("识别失败");
                            mTrackText.setBackgroundColor(Color.RED);
                            mDetectText.setText("活体检测未通过");
                        } else {
                            mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                            mFaceDetectImageView.setVisibility(View.VISIBLE);
                            User user = livenessModel.getUser();
                            if (user == null) {
                                mTrackText.setVisibility(View.VISIBLE);
                                mTrackText.setText("识别失败");
                                mTrackText.setBackgroundColor(Color.RED);
                                mDetectText.setText("搜索不到用户");
                                mDetectText.setVisibility(View.VISIBLE);
                            } else {
                                mAutoCameraPreviewView.setVisibility(View.VISIBLE);
                                mFaceDetectImageView.setVisibility(View.VISIBLE);
                                String absolutePath = FileUtils.getBatchImportSuccessDirectory()
                                        + "/" + user.getImageName();
                                Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                                mTrackText.setVisibility(View.VISIBLE);
                                mTrackText.setText("识别成功");
                                mTrackText.setBackgroundColor(Color.rgb(66, 147, 136));
                                mDetectText.setText("欢迎您， " + user.getUserName());
                            }
                        }
                    }

                }
            }
        });
    }
    public void messageDialog(String title,String name,Context context,Bitmap bitmap){
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(context);
       AlertDialog alertDialog1 = alertDialog.create();
       View view =LayoutInflater.from(getContext()).inflate(R.layout.dialog_view,null);
       alertDialog1.setView(view);
        ImageView imageView =view.findViewById(R.id.dia_img);
        TextView textView =view.findViewById(R.id.dia_text);
        Button button =view.findViewById(R.id.dia_btn);
        imageView.setImageBitmap(bitmap);
        textView.setText("欢迎您："+name);
        alertDialog1.show();

        WindowManager.LayoutParams params = alertDialog1.getWindow().getAttributes();
        params.width = 400;
        params.height = 500 ;
        alertDialog1.getWindow().setAttributes(params);
        button.setOnClickListener(v-> {
            isdia=true;
            alertDialog1.dismiss();
        });
    }
}
