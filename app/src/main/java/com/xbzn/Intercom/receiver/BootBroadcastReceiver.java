package com.xbzn.Intercom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.xbzn.Intercom.mvp.ui.activity.SplashActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "sjft";

    public static final String EXTRA_VOLUME_STATE =
            "android.os.storage.extra.VOLUME_STATE";

    public static final int STATE_UNMOUNTED = 0;
    public static final int STATE_CHECKING = 1;
    public static final int STATE_MOUNTED = 2;
    public static final int STATE_MOUNTED_READ_ONLY = 3;
    public static final int STATE_FORMATTING = 4;
    public static final int STATE_EJECTING = 5;
    public static final int STATE_UNMOUNTABLE = 6;
    public static final int STATE_REMOVED = 7;
    public static final int STATE_BAD_REMOVAL = 8;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (action.equals("android.intent.action.PACKAGE_REPLACED")){
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.v(TAG,"BootBroadcastReceiver packageName:"+packageName);
            if(context.getPackageName().equals(packageName)){
                Intent launchIntent = new Intent(context, SplashActivity.class);//重新启动应用
                //此处如果不想写死启动的Activity，也可以通过如下方法获取默认的启动Activity
                //Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            }
        }
    }
}
