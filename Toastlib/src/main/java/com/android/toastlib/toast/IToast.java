package com.android.toastlib.toast;

import android.view.View;

public interface IToast{

    void showToast(CharSequence s);

    void cancel();

    View getView();
}
