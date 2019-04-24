package com.android.toastlib.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.toastlib.toast.IToast;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/ToastUtils
 *    time   : 2018/11/12
 *    desc   : Toast 显示处理类
 */
public class ToastHandler extends Handler {
    public static final int CONTINUE_SHORT_DURATION_TIMEOUT = 600; // 短吐司显示的时长
    public static final int CONTINUE_LONG_DURATION_TIMEOUT = 1000; // 长吐司显示的时长
    private static final long MAX_CONTROL_INTERVAL = 500;

    private static final int TYPE_SHOW = 1; // 显示吐司
    private static final int TYPE_CONTINUE = 2; // 继续显示
    private static final int TYPE_CANCEL = 3; // 取消显示

    // 队列最大容量
    private static final int MAX_TOAST_CAPACITY = 10;

    // 吐司队列
    private volatile Queue<CharSequence> mQueue;

    // 当前是否正在执行显示操作
    private volatile boolean isShow;

    // 吐司对象
    private final IToast mToast;
    private long lastAddMsgTime = 0;
    private CharSequence currentToastString = null;

    public ToastHandler(IToast toast) {
        super(Looper.getMainLooper());
        mToast = toast;
        mQueue = new ArrayBlockingQueue<>(MAX_TOAST_CAPACITY);
    }

    public void add(CharSequence s) {
//        if (mQueue.isEmpty() || !mQueue.contains(s)){
//            || isNeedAddMessage()) {
//            lastAddMsgTime = System.currentTimeMillis();

//            if(mQueue.offer(s)){// 成功添加一个元素并返回true，如果队列已满，则返回false

//            }else{
//                // 移除队列头部元素并添加一个新的元素
//                mQueue.poll();
//                mQueue.offer(s);
//            }
//        }

        if(!mQueue.contains(s) || isNeedAddMessage(s)){
            if(mQueue.offer(s))
                lastAddMsgTime = System.currentTimeMillis();
        }
    }

    private boolean isNeedAddMessage(CharSequence addString) {
        if((System.currentTimeMillis() - lastAddMsgTime > MAX_CONTROL_INTERVAL)
                && currentToastString != null
                    && addString.toString().equalsIgnoreCase(currentToastString.toString())){
            return true;
        }

        return false;
    }

    public void show() {
        if (!isShow) {
            isShow = true;
            sendEmptyMessage(TYPE_SHOW);
        }
    }

    public void cancel() {
        if (isShow) {
            isShow = false;
            sendEmptyMessage(TYPE_CANCEL);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TYPE_SHOW:
                // 返回队列头部的元素，如果队列为空，则返回null
                CharSequence text = mQueue.peek();
                if (text != null) {
                    currentToastString = text.toString();
                    mToast.showToast(text);
                    // 等这个 Toast 显示完后再继续显示，要加上一些延迟，不然在某些手机上 Toast 可能会来不及消失
                    sendEmptyMessageDelayed(TYPE_CONTINUE, getToastDuration(text));
                }else {
                    isShow = false;
                }
                break;
            case TYPE_CONTINUE:
                // 移除并返问队列头部的元素，如果队列为空，则返回null
                mQueue.poll();
                if (!mQueue.isEmpty()) {
                    sendEmptyMessage(TYPE_SHOW);
                }else {
                    isShow = false;
                    currentToastString = null;
                }
                break;
            case TYPE_CANCEL:
                isShow = false;
                mQueue.clear();
                mToast.cancel();
                break;
            default:
                break;
        }
    }

    /**
     * 根据文本来获取吐司的显示时间
     */
    private int getToastDuration (CharSequence text) {
        return text.length() > 40 ? CONTINUE_LONG_DURATION_TIMEOUT : CONTINUE_SHORT_DURATION_TIMEOUT;
    }
}