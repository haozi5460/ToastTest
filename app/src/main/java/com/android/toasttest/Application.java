package com.android.toasttest;

import com.android.toastlib.ToastUtils;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
