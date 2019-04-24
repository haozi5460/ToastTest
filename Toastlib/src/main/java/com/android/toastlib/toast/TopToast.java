package com.android.toastlib.toast;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.toastlib.R;
import com.android.toastlib.ToastUtils;
import com.android.toastlib.util.ScreenUtils;

import java.lang.reflect.Field;

public class TopToast implements IToast {
    private final static String TAG = "TopToast";
    public static TextView messageView = null;
    public Toast toast = null;

    public TopToast(Context context) {
        initToast(context);
    }

    @Override
    public void showToast(CharSequence s) {
        if(messageView != null){
            messageView.setText(s);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void cancel() {
        toast.cancel();
    }

    @Override
    public View getView() {
        return toast.getView();
    }

    private void initToast(Context context) {
        View toastView = LayoutInflater.from(context).inflate(R.layout.common_toast_layout,null);
        messageView = toastView.findViewById(R.id.common_toast_text);
        toastView.setMinimumHeight(ScreenUtils.getActionBarHeight(context)+ ScreenUtils.getStatusHeight(context));
        toastView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toast = new Toast(context);
        toast.setView(toastView);
        toast.setGravity(Gravity.TOP,0,0);
        makeToastSelfViewAnim();
    }

    private void makeToastSelfViewAnim(){
        try {
            Field mTNField = toast.getClass().getDeclaredField("mTN");
            mTNField.setAccessible(true);
            Object mTNObject = mTNField.get(toast);
            Class tnClass = mTNObject.getClass();
            Field paramsField = tnClass.getDeclaredField("mParams");
            /**由于WindowManager.LayoutParams mParams的权限是private*/
            paramsField.setAccessible(true);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
            layoutParams.windowAnimations = R.style.top_toast_style;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }
}
