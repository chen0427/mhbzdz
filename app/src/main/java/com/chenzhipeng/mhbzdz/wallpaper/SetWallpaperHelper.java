package com.chenzhipeng.mhbzdz.wallpaper;


import android.app.WallpaperManager;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 设置锁屏
 */
public class SetWallpaperHelper {
    private File file;

    private AlertDialog alertDialog;
    private static volatile SetWallpaperHelper instance;

    private SetWallpaperHelper() {
    }

    public static SetWallpaperHelper getInstance() {
        if (instance == null) {
            synchronized (SetWallpaperHelper.class) {
                if (instance == null) {
                    instance = new SetWallpaperHelper();
                }
            }
        }
        return instance;
    }

    public void setLockScreen(final RxAppCompatActivity activity, final String url) {
        if (activity != null && !TextUtils.isEmpty(url)) {
            showDialog(activity);
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    file = ImageHelper.getFile(url);
                    e.onNext(true);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (file != null && file.exists()) {
                                WallpaperManager.getInstance(activity).setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                                Toast.makeText(BaseApplication.getContext(), R.string.setting_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BaseApplication.getContext(), R.string.setting_fail, Toast.LENGTH_SHORT).show();
                            }
                            dismissDialog();
                        }
                    });
        }
    }

    private void showDialog(RxAppCompatActivity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        alertDialog = builder.create();
        alertDialog.setMessage(activity.getString(R.string.setting_loading));
        alertDialog.show();
    }

    private void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
