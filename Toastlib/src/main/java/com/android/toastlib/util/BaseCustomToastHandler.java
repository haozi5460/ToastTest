package com.android.toastlib.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.toastlib.R;


public abstract class BaseCustomToastHandler extends Handler {
    public static final int SHORT_DURATION_TIMEOUT = 2000; // 短吐司显示的时长
    public static final int LONG_DURATION_TIMEOUT = 3000; // 长吐司显示的时长
    private final static int SHOW_TOAST = 0x123;
    private final static int HIDE_TOAST = 0x456;
    public final static int MAX_CANCEL_TIEM = 1000;
    public final static int OTHER_HEIGHT = 40;
    private static final String TAG = "BaseCustomToastHandler";
    private static int viewHeight = -1;
    private boolean isCancelToast = false;
    private int cancelToastTime = 0;
    private TextView messageView;
    View toastView;
    private boolean isShowing = false;

    public BaseCustomToastHandler(Context context) {
        super(Looper.getMainLooper());
        init(context);
    }

    private void init(Context context) {
        initToast(context);
    }

    private void initToast(Context context) {
        toastView = LayoutInflater.from(context).inflate(R.layout.common_toast_layout,null);
        messageView = toastView.findViewById(R.id.common_toast_text);
        viewHeight = ScreenUtils.getActionBarHeight(context)+ ScreenUtils.getStatusHeight(context)+OTHER_HEIGHT;
        toastView.setMinimumHeight(viewHeight);
        toastView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void handleMessage(Message msg) {
        CharSequence message = (CharSequence) msg.obj;
        switch (msg.what) {
            case SHOW_TOAST:
                showToast(message);
                break;
            case HIDE_TOAST:
                hideToast(message);
                break;
            default:
                break;
        }
    }

    public void sendShowToastMessage(CharSequence msg) {
        if (!isShowing) {
            Message message = Message.obtain();
            message.what = SHOW_TOAST;
            message.obj = msg;
            sendMessage(message);
        }
    }

    public void sendHideToastMessage(CharSequence messageText) {
        try {
            removeMessages(HIDE_TOAST);
            Message message = Message.obtain();
            message.what = HIDE_TOAST;
            message.obj = messageText;
            sendMessageDelayed(message, getToastDuration(messageText));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void showToast(CharSequence message) {
        try {
            isShowing = true;
            toastView.setVisibility(View.VISIBLE);
            getWindowManager().addView(toastView, getWindowManagerLayoutParams());
            AnimatorSet set = AnimationUtils.getShowAnimation(toastView, AnimationUtils.ANIMATION_PULL, viewHeight);
            set.addListener(new ToastAnimatorListener(true,message));
            set.start();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            removeOtherToast(message);
        }
    }

    private void removeOtherToast(final CharSequence message) {
        if (!isCancelToast) {
//            ToastUtils.cancle();
            isCancelToast = true;
        }
        isShowing = false;
        if (cancelToastTime < MAX_CANCEL_TIEM) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    isShowing = false;
                    showToast(message);
                }
            }, cancelToastTime += 100);
        }
    }

    public void hideToast(CharSequence message) {
        AnimatorSet set = AnimationUtils.getShowAnimation(toastView, AnimationUtils.ANIMATION_HIDE, viewHeight);
        set.addListener(new ToastAnimatorListener(false, message));
        set.start();
    }

    public void removeView() {
        try {
            removeMessages(HIDE_TOAST);
            removeToast();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void removeToast() {
        try {
            isShowing = false;
            toastView.setVisibility(View.INVISIBLE);
            getWindowManager().removeView(toastView);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public View getView() {
        return toastView;
    }

    public void setMessage(CharSequence s) {
        messageView.setText(s);
    }

    public class ToastAnimatorListener implements Animator.AnimatorListener{
        private boolean isShowToast = false;
        private CharSequence message;

        public ToastAnimatorListener(boolean isShowToast, CharSequence message){
            this.isShowToast = isShowToast;
            this.message = message;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            //Do nothing
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            try {
                if(isShowToast){
                    isCancelToast = false;
                    cancelToastTime = 0;
                    sendHideToastMessage(message);
                }else {
                    removeToast();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            //Do nothing
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            //Do nothing
        }
    }

    private int getToastDuration (CharSequence text) {
        // 如果显示的文字超过了10个就显示长吐司，否则显示短吐司
        int durationTime = text.length() > 40 ? LONG_DURATION_TIMEOUT : SHORT_DURATION_TIMEOUT;
        return durationTime;
    }

    public boolean isToastShowing(){
        return isShowing;
    }

    public abstract WindowManager getWindowManager();
    public abstract WindowManager.LayoutParams getWindowManagerLayoutParams();
}
