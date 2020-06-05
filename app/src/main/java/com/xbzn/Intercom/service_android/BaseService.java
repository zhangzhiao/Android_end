package com.xbzn.Intercom.service_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xbzn.Intercom.utils.SharedUtil;

public class BaseService extends Service {
    SharedUtil sharedUtil;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sharedUtil =new SharedUtil(getApplicationContext());
        return null;
    }
}
