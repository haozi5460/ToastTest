package com.android.toastlib;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.android.toastlib.toast.ActivityCustomizeToast;
import com.android.toastlib.toast.ActivityToast;
import com.android.toastlib.toast.IToast;
import com.android.toastlib.toast.SystemToast;
import com.android.toastlib.toast.TopCustomizeToast;
import com.android.toastlib.toast.TopToast;
import com.android.toastlib.util.ToastHandler;

public class ToastUtils {
    /**
     * 顶部下拉回弹样式
     */
    public final static int TOP_TOAST_STYLE = 0x1023;
    /**
     *系统透明度样式
     */
    public final static int SYSTEM_TOAST_STYLE = 0x1024;
    public final static String TAG = "ToastUtils";
    private static IToast topToast = null;
    private static IToast systemToast = null;
    private static ToastHandler topToastHandler = null;
    private static ToastHandler systemToastHandler = null;

    /**
     * Should init in Application.java
     * @param application
     */
    public static void init(Application application){
        //init TopToast 大于7.1.1 使用ActivityToast,否则使用TopToast
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
            topToast = new ActivityCustomizeToast(application);
        }else{
            topToast = new TopCustomizeToast(application);
        }

        //init SystemToast
        systemToast = new SystemToast(application);
        topToastHandler = new ToastHandler(topToast);
        systemToastHandler = new ToastHandler(systemToast);
    }

    /**
     * Show Toast use the default TOP_TOAST_STYLE
     * @param id
     */
    public static void show(int id){
        show(id,TOP_TOAST_STYLE);
    }

    /**
     * Show Toast use the default TOP_TOAST_STYLE
     * @param message
     */
    public static void show(CharSequence message){
        show(message,TOP_TOAST_STYLE);
    }

    /**
     * Show Toast with the Toast style you choosed
     * @param id
     * @param toastStyle
     */
    public static void show(int id,int toastStyle){
        checkToastUtils();
        IToast toast = null;
        try {
            toast = toastStyle == TOP_TOAST_STYLE ? topToast :systemToast;
            show(toast.getView().getContext().getResources().getText(id),toastStyle);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    /**
     * Show Toast with the Toast style you choosed
     * @param message
     * @param toastStyle
     */
    public static void show(CharSequence message,int toastStyle) {
        checkToastUtils();
        ToastHandler toastHandler = null;
        try {
            toastHandler = toastStyle == TOP_TOAST_STYLE ? topToastHandler : systemToastHandler;
            toastHandler.add(message);
            toastHandler.show();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    /**
     * 取消吐司的显示
     */
    public static void cancel() {
        checkToastUtils();
        topToastHandler.cancel();
        systemToastHandler.cancel();
    }

    private static void checkToastUtils() {
        if(topToast == null || systemToast == null
            || topToastHandler == null || systemToastHandler == null){
            throw new IllegalStateException("ToastUtils has not been initialized");
        }
    }
}
