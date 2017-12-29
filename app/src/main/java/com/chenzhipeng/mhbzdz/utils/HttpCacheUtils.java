package com.chenzhipeng.mhbzdz.utils;


import android.text.TextUtils;
import android.util.Base64;

import com.chenzhipeng.mhbzdz.sqlite.AppDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class HttpCacheUtils {
    private final static String TIME_FLAG = "time_flag";

    public static Object getHttpCache(String url) {
        Object o = null;
       /* String time = (String) SPUtils.get(url + TIME_FLAG);
        if (!TextUtils.isEmpty(time)) {
            long l = Long.parseLong(time);
            long millis = System.currentTimeMillis();
            if (millis - l <= ConfigUtils.getHttpCacheTime() * 60 * 1000) {
                return SPUtils.get(url);
            }
        }
        return null;*/
        if (!TextUtils.isEmpty(url)) {
            String httpCache = AppDatabase.getInstance().getHttpCache(url);
            o = getObject(httpCache);
        }
        return o;
    }

    public static void addHttpCache(final String url, final Object o) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                if (!TextUtils.isEmpty(url) && o != null) {
                    String s = formatObject(o);
                    AppDatabase.getInstance().addHttpCache(url, s);


                    // SPUtils.put(url, o);
                    // SPUtils.put(url + TIME_FLAG, String.valueOf(System.currentTimeMillis()));
                }
            }
        }).subscribeOn(Schedulers.newThread()).subscribe();
    }

    private static String formatObject(Object o) {
        String objectString = null;
        if (o != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(o);
                objectString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                byteArrayOutputStream.close();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objectString;
    }

    private static Object getObject(String objectString) {
        Object object = null;
        if (!TextUtils.isEmpty(objectString)) {
            try {
                byte[] bytes = Base64.decode(objectString, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                object = objectInputStream.readObject();
                byteArrayInputStream.close();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

}
