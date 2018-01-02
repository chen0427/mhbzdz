package com.chenzhipeng.mhbzdz.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.chenzhipeng.mhbzdz.utils.ConfigUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("StaticFieldLeak")
public class BaseApplication extends Application {
    private static Context context;

    /**
     * 壁纸下载路径  //com.chenzhipeng.mhbzdz/wallpaper
     */
    public static final String WALLPAPER_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "com.chenzhipeng.mhbzdz" + File.separator + "wallpaper";

    /**
     * 漫画下载路径 //com.chenzhipeng.mhbzdz/comic
     */
    public static final String COMIC_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "com.chenzhipeng.mhbzdz" + File.separator + "comic";

    /**
     * 段子图片下载路径 //com.chenzhipeng.mhbzdz/joke
     */
    public static final String JOKE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "com.chenzhipeng.mhbzdz" + File.separator + "joke";


    public static int choiceToIndex;
    public static int choiceToAppColor;
    public static int comicChapterSort;
    public static boolean volumePage;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initConfig();
    }

    private void initConfig() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                comicChapterSort = ConfigUtils.getComicChapterSort();
                choiceToIndex = ConfigUtils.getChoiceToIndex();
                choiceToAppColor = ConfigUtils.getChoiceToAppColor();
                volumePage = ConfigUtils.getVolumePage();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public static Context getContext() {
        return context;
    }
}
