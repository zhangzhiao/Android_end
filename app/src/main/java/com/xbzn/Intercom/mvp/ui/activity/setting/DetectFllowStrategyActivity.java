package com.xbzn.Intercom.mvp.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.xbzn.Intercom.R;
import com.xbzn.Intercom.mvp.model.entity.SingleBaseConfig;
import com.xbzn.Intercom.mvp.ui.activity.BaseActivity;
import com.xbzn.Intercom.utils.ConfigUtils;

/**
 * author : baidu
 * date : 2019/6/10 4:17 PM
 * description :检测跟踪策略
 */
public class DetectFllowStrategyActivity extends BaseActivity {
    private RadioButton dfsFullLineDetect;
    private RadioButton dfsSpeciallArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectfllowstrategy);

        init();
    }

    public void init() {
        dfsFullLineDetect = findViewById(R.id.dfs_full_line_detect);
        dfsSpeciallArea = findViewById(R.id.dfs_speciall_area);
        Button dfsSave = findViewById(R.id.dfs_save);

        if (SingleBaseConfig.getBaseConfig().getDetectFrame().equals("wireframe")) {
            dfsFullLineDetect.setChecked(true);

        }
        if (SingleBaseConfig.getBaseConfig().getDetectFrame().equals("fixedarea")) {
            dfsSpeciallArea.setChecked(true);
        }

        dfsFullLineDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dfsFullLineDetect.setChecked(true);
                dfsSpeciallArea.setChecked(false);
            }
        });

        dfsSpeciallArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dfsSpeciallArea.setChecked(true);
                dfsFullLineDetect.setChecked(false);
            }
        });

        dfsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dfsFullLineDetect.isChecked()) {
                    SingleBaseConfig.getBaseConfig().setDetectFrame("wireframe");
                }
                if (dfsSpeciallArea.isChecked()) {
                    SingleBaseConfig.getBaseConfig().setDetectFrame("fixedarea");
                }
                ConfigUtils.modityJson();
                finish();
            }
        });

    }
}
