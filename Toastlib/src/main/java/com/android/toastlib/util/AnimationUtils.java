package com.android.toastlib.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 *  This class is used for storing some types of Animation.
 *
 *  @author Chen Yu
 *  @version 1.0
 *  @date 2016-08-19
 */
public class AnimationUtils {
    public static final int ANIMATION_PULL = 0X003;
    public static final int ANIMATION_HIDE = 0X004;
    public static final String TRANSLATIONY = "translationY";

    public static AnimatorSet getShowAnimation(View view, int animationType, int height){
        switch (animationType){
            case ANIMATION_PULL:
                AnimatorSet pullSet = new AnimatorSet();
                pullSet.playTogether(
                        ObjectAnimator.ofFloat(view, TRANSLATIONY,
                                -height-BaseCustomToastHandler.OTHER_HEIGHT, 0,-BaseCustomToastHandler.OTHER_HEIGHT)
                );
                pullSet.setDuration(600);
                return pullSet;

            case ANIMATION_HIDE:
                AnimatorSet hideSet = new AnimatorSet();
                hideSet.playTogether(
                        ObjectAnimator.ofFloat(view, TRANSLATIONY, -BaseCustomToastHandler.OTHER_HEIGHT,-height)
                );
                hideSet.setDuration(300);
                return hideSet;

            default:
                return new AnimatorSet();
        }
    }
}
