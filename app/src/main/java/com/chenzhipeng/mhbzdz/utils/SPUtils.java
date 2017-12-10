package com.chenzhipeng.mhbzdz.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.chenzhipeng.mhbzdz.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 保存对象
 */
@SuppressWarnings("unchecked")
public class SPUtils {
    private static final String SP_NAME = "SaveObject";

    public static void put(String key, Object o) {
        if (!TextUtils.isEmpty(key) && o != null) {
            BaseApplication
                    .getContext()
                    .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                    .edit().putString(key, formatObject(o))
                    .apply();

        }
    }

    public static Object get(String key) {
        Object object = null;
        if (!TextUtils.isEmpty(key)) {
            object = getObject(BaseApplication
                    .getContext()
                    .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                    .getString(key, null));
        }
        return object;
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
