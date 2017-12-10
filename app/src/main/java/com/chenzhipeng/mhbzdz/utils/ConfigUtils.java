package com.chenzhipeng.mhbzdz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ConfigUtils {
    private static final String CONFIG_NAME = "config";
    private static final String KEY_INDEX = "index";
    private static final String KEY_COLOR = "color";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_COMIC_CHAPTER_SORT = "comic_chapter_sort";

    public static int getChoiceToIndex() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_INDEX, 0);
    }


    public static void setChoiceToIndex(int i) {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_INDEX, i);
        edit.apply();
    }

    public static void setComicChapterSort(int i) {
        //i=0 是正序 i=1是倒序
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(KEY_COMIC_CHAPTER_SORT, i).apply();
    }


    public static void setChoiceToAppColor(int colorId) {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(KEY_COLOR, colorId);
        edit.apply();
    }

    public static void setVolumePage(boolean b) {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(KEY_VOLUME, b).apply();
    }

    public static boolean getVolumePage() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_VOLUME, false);
    }

    public static int getComicChapterSort() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_COMIC_CHAPTER_SORT, 0);
    }

    public static int getChoiceToAppColorThem() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return getTheme(sharedPreferences.getInt(KEY_COLOR, 0));
    }

    public static int getChoiceToAppColor() {
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return getColor(sharedPreferences.getInt(KEY_COLOR, 0));
    }


    private static int getTheme(int i) {
        switch (i) {
            case 0:
                return R.style.AppNoActionBar_1;
            case 1:
                return R.style.AppNoActionBar_2;
            case 2:
                return R.style.AppNoActionBar_3;
            case 3:
                return R.style.AppNoActionBar_4;
            case 4:
                return R.style.AppNoActionBar_5;
            case 5:
                return R.style.AppNoActionBar_6;
            case 6:
                return R.style.AppNoActionBar_7;
            case 7:
                return R.style.AppNoActionBar_8;
            case 8:
                return R.style.AppNoActionBar_9;
            case 9:
                return R.style.AppNoActionBar_10;
            default:
                return R.style.AppNoActionBar_1;
        }
    }

    private static int getColor(int i) {
        switch (i) {
            case 0:
                return R.color.colorPrimary_1;
            case 1:
                return R.color.colorPrimary_2;
            case 2:
                return R.color.colorPrimary_3;
            case 3:
                return R.color.colorPrimary_4;
            case 4:
                return R.color.colorPrimary_5;
            case 5:
                return R.color.colorPrimary_6;
            case 6:
                return R.color.colorPrimary_7;
            case 7:
                return R.color.colorPrimary_8;
            case 8:
                return R.color.colorPrimary_9;
            case 9:
                return R.color.colorPrimary_10;
            default:
                return R.color.colorPrimary_1;
        }
    }


}
