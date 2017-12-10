package com.chenzhipeng.mhbzdz.download;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.wallpaper.WallpaperPictureService;
import com.chenzhipeng.mhbzdz.utils.FileUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 图片下载
 */
public class PictureDownloader {
    private File file;
    private RxAppCompatActivity activity;
    private Listener listener;
    private String url;


    public PictureDownloader(RxAppCompatActivity activity) {
        this.activity = activity;
    }

    public PictureDownloader setFile(File file) {
        this.file = file;
        return this;
    }


    public PictureDownloader setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public PictureDownloader setUrl(String url) {
        this.url = url;
        return this;
    }

    public void start() {
        if (!TextUtils.isEmpty(url) && file != null) {
            RetrofitHelper.getInstance()
                    .create(WallpaperPictureService.class)
                    .get(url)
                    .compose(activity.<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new WallpaperObserver(file));
        }
    }

    private class WallpaperObserver implements Observer<ResponseBody> {
        private File file;

        WallpaperObserver(File file) {
            this.file = file;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            if (listener != null) {
                listener.onStart();
            }
        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            if (responseBody != null) {
                FileUtils.copy(responseBody.byteStream(), file);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            if (listener != null) {
                listener.onError(e);
            }
        }

        @Override
        public void onComplete() {
            if (listener != null) {
                listener.onComplete();
            }
        }
    }

    public interface Listener {
        void onStart();

        void onComplete();

        void onError(Throwable e);
    }


}
