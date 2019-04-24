package com.android.toastlib.toast;

import android.app.Application;
import android.view.View;

import com.android.toastlib.util.ActivityCustomizeToastHandler;

public class ActivityCustomizeToast implements IToast{
    // 吐司弹窗显示辅助类
    private final ActivityCustomizeToastHandler mToastHelper;

    public ActivityCustomizeToast(Application application) {
        mToastHelper = new ActivityCustomizeToastHandler(application);
    }

    @Override
    public void cancel() {
        // 取消显示
        mToastHelper.removeView();
    }

    @Override
    public View getView() {
        return mToastHelper.getView();
    }

    @Override
    public void showToast(CharSequence s) {
        mToastHelper.setMessage(s);
        if(mToastHelper.isToastShowing())
            mToastHelper.sendHideToastMessage(s);
        else
            mToastHelper.sendShowToastMessage(s);
    }
}
