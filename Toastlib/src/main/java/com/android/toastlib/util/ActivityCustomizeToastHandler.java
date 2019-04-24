package com.android.toastlib.util;

import android.app.Application;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

public class ActivityCustomizeToastHandler extends BaseCustomToastHandler {
    private ActivityLifecycleCallback mWindowHelper;
    private String mPackageName;

    public ActivityCustomizeToastHandler(Application context) {
        super(context);
        init(context);
    }

    private void init(Application application) {
        mPackageName = application.getPackageName();
        mWindowHelper = new ActivityLifecycleCallback(this, application);
    }

    @Override
    public WindowManager getWindowManager() {
        return mWindowHelper.getWindowManager();
    }

    @Override
    public WindowManager.LayoutParams getWindowManagerLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
//            params.windowAnimations = R.style.top_toast_style;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.packageName = mPackageName;
        // 重新初始化位置
        params.gravity = Gravity.TOP;
        params.x = 0;
        params.y = 0;

        return params;
    }
}
