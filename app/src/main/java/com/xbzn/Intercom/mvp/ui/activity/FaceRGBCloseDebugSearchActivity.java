package com.xbzn.Intercom.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.FaceInfo;
import com.xbzn.Intercom.R;
import com.xbzn.Intercom.callback.CameraDataCallback;
import com.xbzn.Intercom.callback.FaceDetectCallBack;
import com.xbzn.Intercom.camera.AutoTexturePreviewView;
import com.xbzn.Intercom.camera.CameraPreviewManager;
import com.xbzn.Intercom.manager.FaceSDKManager;
import com.xbzn.Intercom.mvp.model.LivenessModel;
import com.xbzn.Intercom.mvp.model.entity.SingleBaseConfig;
import com.xbzn.Intercom.mvp.model.entity.User;
import com.xbzn.Intercom.mvp.ui.activity.setting.SettingMainActivity;
import com.xbzn.Intercom.utils.DensityUtils;
import com.xbzn.Intercom.utils.FaceOnDrawTexturViewUtil;
import com.xbzn.Intercom.utils.FileUtils;
import com.xbzn.Intercom.utils.TextureVideoViewOutlineProvider;

import static com.xbzn.Intercom.mvp.ui.activity.FaceMainSearchActivity.PAGE_TYPE;


/**
 * @Time 2019/06/02
 * @Author v_shishuaifeng
 * @Description RGB关闭Debug页面
 */
public class FaceRGBCloseDebugSearchActivity extends BaseActivity implements View.OnClickListener {

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
    private RelativeLayout relativeLayout;
    private int mLiveType;
    private float mRgbLiveScore;

    // 包含适配屏幕后后的人脸的x坐标，y坐标，和width
    private float[] pointXY = new float[3];
    private boolean requestToInner = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_rgb_close_debug);
        mContext = this;
        initView();

        // 屏幕的宽
        int displayWidth = DensityUtils.getDisplayWidth(mContext);
        // 屏幕的高
        int displayHeight = DensityUtils.getDisplayHeight(mContext);
        // 当屏幕的宽大于屏幕宽时
        if (displayHeight < displayWidth) {
            // 获取高
            int height = displayHeight;
            // 获取宽
            int width = (int) (displayHeight * ((9.0f / 16.0f)));
            // 设置布局的宽和高
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            // 设置布局居中
            params.gravity = Gravity.CENTER;
            relativeLayout.setLayoutParams(params);
        }
    }

    /***
     *  关闭Debug 模式view
     */

    private void initView() {
        // 活体状态
        mLiveType = SingleBaseConfig.getBaseConfig().getType();
        // 活体阈值
        mRgbLiveScore = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
        // 获取整个布局
        relativeLayout = findViewById(R.id.all_relative);
        // 画人脸框
        paint = new Paint();
        rectF = new RectF();
        mFaceDetectImageView = findViewById(R.id.draw_detect_face_view);
        mFaceDetectImageView.setOpaque(false);
        mFaceDetectImageView.setKeepScreenOn(true);

        // 返回
        Button mButReturn = findViewById(R.id.btn_back);
        mButReturn.setOnClickListener(this);
        // 设置
        Button mBtSetting = findViewById(R.id.btn_setting);
        mBtSetting.setOnClickListener(this);

        // 单目摄像头RGB 图像预览
        mAutoCameraPreviewView = findViewById(R.id.auto_camera_preview_view);
//        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
        mAutoCameraPreviewView.setOutlineProvider(new TextureVideoViewOutlineProvider());
        mFaceDetectImageView.setOutlineProvider(new TextureVideoViewOutlineProvider());
        mAutoCameraPreviewView.setClipToOutline(true);
        mFaceDetectImageView.setClipToOutline(true);
        mFaceDetectImageView.setVisibility(View.VISIBLE);
        mAutoCameraPreviewView.setVisibility(View.VISIBLE);
        mDetectText = findViewById(R.id.detect_text);
        mTrackText = findViewById(R.id.track_txt);

        // 调试按钮
//        findViewById(R.id.debug_btn).setOnClickListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        startTestCloseDebugRegisterFunction();
    }

    private void startTestCloseDebugRegisterFunction() {
        // TODO ： 临时放置
        CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_USB);
        CameraPreviewManager.getInstance().startPreview(this, mAutoCameraPreviewView,
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (code == 0) {
                    mTrackText.setVisibility(View.GONE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    mTrackText.setVisibility(View.VISIBLE);
                    mTrackText.setText("识别失败");
                    mTrackText.setBackgroundColor(Color.RED);
                }
                mDetectText.setText(tip);
            }
        });
    }
    boolean f =true;
    public void inin(View view){
        if(f){
            mFaceDetectImageView.setVisibility(View.VISIBLE);
            mAutoCameraPreviewView.setVisibility(View.VISIBLE);
            f=false;
        }else {
            mFaceDetectImageView.setVisibility(View.INVISIBLE);
            mAutoCameraPreviewView.setVisibility(View.INVISIBLE);
            f=true;
        }
        }

    private void checkCloseResult(final LivenessModel livenessModel) {
        // 当未检测到人脸UI显示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (livenessModel == null || livenessModel.getFaceInfo() == null) {
                    mTrackText.setVisibility(View.GONE);
                    mDetectText.setText("未检测到人脸");
                    mFaceDetectImageView.setVisibility(View.INVISIBLE);
                    mAutoCameraPreviewView.setVisibility(View.INVISIBLE);
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
                        relativeLayout.setVisibility(View.VISIBLE);
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
                            relativeLayout.setVisibility(View.VISIBLE);
                            mFaceDetectImageView.setVisibility(View.VISIBLE);
                            String absolutePath = FileUtils.getBatchImportSuccessDirectory()
                                    + "/" + user.getImageName();
                            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                            mTrackText.setVisibility(View.VISIBLE);
                            mTrackText.setText("识别成功");
                            mTrackText.setBackgroundColor(Color.rgb(66, 147, 136));
                            mDetectText.setText("欢迎您， " + user.getUserName());
                        }
                    } else {
                        relativeLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_setting:
                Intent intent = new Intent(mContext, SettingMainActivity.class);
                intent.putExtra("page_type", "search");
                startActivityForResult(intent, PAGE_TYPE);
                finish();
                break;
            default:
                break;
        }


    }

    /**
     * 绘制人脸框。
     */
    private void showFrame(final LivenessModel model) {
        runOnUiThread(new Runnable() {
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

}
