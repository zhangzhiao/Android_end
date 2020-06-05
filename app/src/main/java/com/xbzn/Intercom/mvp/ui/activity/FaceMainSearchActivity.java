package com.xbzn.Intercom.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xbzn.Intercom.R;
import com.xbzn.Intercom.mvp.ui.activity.setting.SettingMainActivity;


/**
 * 1：N 人脸检索
 */
public class FaceMainSearchActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    public static final int PAGE_TYPE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        mContext = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化View 相关
     */
    private void initView() {
        findViewById(R.id.ly_search).setOnClickListener(this);
        findViewById(R.id.ly_reigster).setOnClickListener(this);
        findViewById(R.id.ly_user).setOnClickListener(this);
        findViewById(R.id.ly_import).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_search:
                Intent intent = new Intent(mContext, FaceConfigActivity.class);
                intent.putExtra("page_type", "search");
                startActivityForResult(intent, PAGE_TYPE);
                break;
            case R.id.ly_reigster:
                Intent intent2 = new Intent(mContext, FaceConfigActivity.class);
                startActivityForResult(intent2, PAGE_TYPE);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_setting:
                startActivity(new Intent(mContext, SettingMainActivity.class));

                break;
            default:
                break;
        }
    }
}
