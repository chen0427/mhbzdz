package com.chenzhipeng.mhbzdz.intent;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * intent 不能传大数据 用这个传
 */
public class SuperIntent {
    private static volatile SuperIntent instance;
    public static final String S1 = "跳到下载列表界面";
    public static final String S2 = "跳到推荐标签界面";
    public static final String S3 = "跳到推荐标签界面参数position";
    public static final String S4 = "漫画下载界面跳到漫画阅读界面对象参数";
    public static final String S5 = "漫画下载界面跳到漫画阅读界面id-name参数";
    public static final String S6 = "漫画下载界面跳到漫画阅读界面类型参数";
    public static final String S7 = "漫画下载界面跳到漫画阅读界面是否倒序参数";
    public static final String S8 = "漫画下载界面跳到漫画阅读界面position参数";
    public static final String S9 = "跳到壁纸分类界面对象参数";
    public static final String S10 = "跳到漫画下载详情列表对象参数";
    public static final String S11 = "跳到漫画下载详情列表comicId参数";
    public static final String S12 = "跳到浏览壁纸界面对象参数";
    public static final String S13 = "跳到浏览壁纸界面position参数";
    public static final String S14 = "跳到漫画详情界面对象参数";
    public static final String S15 = "跳到段子图片浏览界面position参数";
    public static final String S16 = "跳到段子图片浏览界面对象参数";
    public static final String S17 = "跳到漫画阅读界面对象参数";

    private SuperIntent() {
    }

    public static SuperIntent getInstance() {
        if (instance == null) {
            synchronized (SuperIntent.class) {
                if (instance == null) {
                    instance = new SuperIntent();
                }
            }
        }
        return instance;
    }

    private Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            map.put(key, value);
        }
    }

    public Object get(String key) {
        if (!TextUtils.isEmpty(key)) {
            return map.get(key);
        }
        return null;
    }

    public void remove(final String... keys) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                if (keys != null && keys.length > 0) {
                    for (String key : keys) {
                        map.remove(key);
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }
}
