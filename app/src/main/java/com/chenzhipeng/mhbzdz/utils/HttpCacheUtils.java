package com.chenzhipeng.mhbzdz.utils;


import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class HttpCacheUtils {
    private final static String TIME_FLAG = "time_flag";

    public static Object getHttpCache(String url) {
        String time = (String) SPUtils.get(url + TIME_FLAG);
        if (!TextUtils.isEmpty(time)) {
            long l = Long.parseLong(time);
            long millis = System.currentTimeMillis();
            if (millis - l <= ConfigUtils.getHttpCacheTime() * 60 * 1000) {
                return SPUtils.get(url);
            }
        }
        return null;
    }

    public static void addHttpCache(final String url, final Object o) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                if (!TextUtils.isEmpty(url) && o != null) {
                    SPUtils.put(url, o);
                    SPUtils.put(url + TIME_FLAG, String.valueOf(System.currentTimeMillis()));
                }
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

}
