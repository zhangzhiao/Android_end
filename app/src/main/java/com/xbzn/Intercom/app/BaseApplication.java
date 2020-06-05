package com.xbzn.Intercom.app;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

public class BaseApplication extends com.jess.arms.base.BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        QMUISwipeBackActivityManager.init(this);
    }
}
