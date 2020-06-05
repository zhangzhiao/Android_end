package com.zza.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UpService extends Service {
    Handler handler =new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
    LocalBinder localBinder =new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Messenger messenger =new Messenger(handler);
        return messenger.getBinder();
    }
    public class LocalBinder extends Binder{
        UpService getService(){
            return UpService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
