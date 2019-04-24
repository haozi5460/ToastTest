package com.android.toastlib.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

public class TopCustomizeToastHandler extends BaseCustomToastHandler {
    private static WindowManager mWindowManager = null;
    private static WindowManager.LayoutParams mParams = null;

    public TopCustomizeToastHandler(Context context) {
        super(context);
        initWindowManager(context);
    }

    private void initWindowManager(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        //7.0 以下用TYPE_TOAST
        //7.1.1 需用TYPE_PHONE
        //8.0以上过时，用TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else
            mParams.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 ? WindowManager.LayoutParams.TYPE_PHONE : WindowManager.LayoutParams.TYPE_TOAST;

        mParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mParams.gravity = Gravity.TOP;
        mParams.y = 0;
        mParams.x = 0;
    }

    @Override
    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    @Override
    public WindowManager.LayoutParams getWindowManagerLayoutParams() {
        return mParams;
    }
}
