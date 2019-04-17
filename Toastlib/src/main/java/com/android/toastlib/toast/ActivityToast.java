package com.android.toastlib.toast;

import android.app.Application;

import com.android.toastlib.util.ActivityToastHelper;

public class ActivityToast extends TopToast {
    // 吐司弹窗显示辅助类
    private final ActivityToastHelper mToastHelper;

    public ActivityToast(Application application) {
        super(application);
        mToastHelper = new ActivityToastHelper(toast, application);
    }

    @Override
    public void cancel() {
        // 取消显示
        mToastHelper.cancel();
    }

    @Override
    public void showToast() {
        mToastHelper.show();
    }
}
