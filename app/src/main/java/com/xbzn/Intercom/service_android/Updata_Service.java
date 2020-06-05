package com.xbzn.Intercom.service_android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class Updata_Service extends Service {
    public Updata_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    public class MyBinder extends Binder {
        public Updata_Service getService() {
            return Updata_Service.this;
        }
    }

    //通过binder实现调用者client与Service之间的通信
    private Updata_Service.MyBinder binder = new Updata_Service.MyBinder();
}
