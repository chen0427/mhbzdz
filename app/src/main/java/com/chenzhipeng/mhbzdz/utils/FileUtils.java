package com.chenzhipeng.mhbzdz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/23.
 */

public class FileUtils {

    public static void copy(final File oldFile, final File newFile) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                if (oldFile != null && oldFile.exists() && !oldFile.isDirectory() && newFile != null) {
                    if (!newFile.exists()) {
                        File parentFile = newFile.getParentFile();
                        if (parentFile != null) {
                            boolean mkdir = true;
                            if (!parentFile.exists()) {
                                mkdir = parentFile.mkdir();
                            }
                            if (mkdir) {
                                FileInputStream inputStream = new FileInputStream(oldFile);
                                FileOutputStream outputStream = new FileOutputStream(newFile);
                                byte[] bytes = new byte[1024];
                                int tmp;
                                while ((tmp = inputStream.read(bytes)) != -1) {
                                    outputStream.write(bytes, 0, tmp);
                                }
                                inputStream.close();
                                outputStream.close();
                            }
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public static void copy(final InputStream inputStream, final File newFile) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                if (inputStream != null && newFile != null) {
                    if (!newFile.exists()) {
                        File parentFile = newFile.getParentFile();
                        if (parentFile != null) {
                            boolean mkdir = true;
                            if (!parentFile.exists()) {
                                mkdir = parentFile.mkdirs();
                            }
                            if (mkdir) {
                                OutputStream outputStream = new FileOutputStream(newFile);
                                byte[] bytes = new byte[1024];
                                int temp;
                                while ((temp = inputStream.read(bytes)) != -1) {
                                    outputStream.write(bytes, 0, temp);
                                }
                                inputStream.close();
                                outputStream.close();
                            }
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public static boolean deleteDir(File file) {
        if (file != null && file.exists()) {
            String[] children = file.list();
            if (children != null && children.length > 0) {
                for (String aChildren : children) {
                    boolean success = deleteDir(new File(file, aChildren));
                    if (!success) {
                        return false;
                    }
                }
            }
            return file.delete();
        }
        return false;
    }
}
