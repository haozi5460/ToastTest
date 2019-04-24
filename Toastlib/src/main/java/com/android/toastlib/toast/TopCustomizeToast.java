package com.android.toastlib.toast;

import android.app.Application;
import android.view.View;

import com.android.toastlib.util.TopCustomizeToastHandler;

public class TopCustomizeToast implements IToast{
    private TopCustomizeToastHandler topToastHandler;
    public TopCustomizeToast(Application application){
        topToastHandler = new TopCustomizeToastHandler(application);
    }

    @Override
    public void showToast(CharSequence s) {
        topToastHandler.setMessage(s);
        if(topToastHandler.isToastShowing()){
            topToastHandler.sendHideToastMessage(s);
        }else {
            topToastHandler.sendShowToastMessage(s);
        }
    }

    @Override
    public void cancel() {
        topToastHandler.removeView();
    }

    @Override
    public View getView() {
        return topToastHandler.getView();
    }
}
