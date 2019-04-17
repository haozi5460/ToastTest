package com.android.toastlib.toast;

import android.view.View;

public interface IToast{
    void setMessageText(CharSequence s);

    void showToast();

    void cancel();

    View getView();
}
