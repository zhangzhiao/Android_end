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
 * author : shangrong
 * date : 2019/5/27 6:54 PM
 * description :识别模型选择
 */
public class RecognizeModleTypeAcctivity extends BaseActivity {

    private RadioButton rmtLiveModle;
    private RadioButton rmtIdModle;
    private int one = 1;
    private int two = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizemodletype);

        init();
    }

    public void init() {
        rmtLiveModle = findViewById(R.id.rmt_livemodle);
        rmtIdModle = findViewById(R.id.rmt_idmodle);
        Button rmtSave = findViewById(R.id.rmt_save);


        if (SingleBaseConfig.getBaseConfig().getActiveModel() == one) {
            rmtLiveModle.setChecked(true);

        }
        if (Integer.valueOf(SingleBaseConfig.getBaseConfig().getActiveModel()) == two) {
            rmtIdModle.setChecked(true);

        }


        rmtLiveModle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rmtIdModle.setChecked(false);
            }
        });

        rmtIdModle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rmtLiveModle.setChecked(false);

            }
        });

        rmtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rmtLiveModle.isChecked()) {
                    SingleBaseConfig.getBaseConfig().setActiveModel(one);
                }
                if (rmtIdModle.isChecked()) {
                    SingleBaseConfig.getBaseConfig().setActiveModel(two);
                }
                ConfigUtils.modityJson();
                finish();
            }
        });
    }

}
