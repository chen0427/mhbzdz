package com.chenzhipeng.mhbzdz.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.chenzhipeng.mhbzdz.base.BaseApplication;

@SuppressLint("StaticFieldLeak")
public class DisplayUtils {
    private static Activity activity;

    public static void init(Activity activity) {
        DisplayUtils.activity = activity;
    }


    /**
     * dp转px
     */
    public static int dpToPx(int dip) {
        return (int) (dip * BaseApplication.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        int height = 0;
        if (activity != null && !activity.isFinishing()) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            height = metric.heightPixels;
        }
        return height;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        int width = 0;
        if (activity != null && !activity.isFinishing()) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            width = metric.widthPixels;
        }
        return width;
    }

    /**
     * 获取StatusBar高度
     */
    public static int getStatusBarHeight() {
        Context context = BaseApplication.getContext();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     */
    public static int getDaoHangHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * sp转换成px
     */
    public static int spToPx(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                BaseApplication.getContext().getResources().getDisplayMetrics());

    }

}
