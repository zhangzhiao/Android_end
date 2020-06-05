package com.xbzn.Intercom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xbzn.Intercom.mvp.ui.activity.SplashActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 设备一开机就自动启动应用
        if(false){
            log("Boot action received, but auto boot disable and discard.");
            return;
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent it = new Intent(context, SplashActivity.class);
            it.setAction("android.intent.action.MAIN");
            it.addCategory("android.intent.category.LAUNCHER");
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
            log("Boot 开机自动启动");
        } else {
            log("Boot 非开机自动启动");
        }
    }

    private void log(String s) {
        Log.e("zza", s);
    }

}