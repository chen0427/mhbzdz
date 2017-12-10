package com.chenzhipeng.mhbzdz.download;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicDownloadService;
import com.chenzhipeng.mhbzdz.document.ComicDocumentHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 漫画单个章节下载
 */
public class ComicChapterDownloader {
    private String comicId;
    private String comicName;
    private String chapterName;
    private List<String> urls = new ArrayList<>();
    private Listener listener;
    private boolean isPause = false;
    private boolean isDelete = false;


    public ComicChapterDownloader(String comicId, String comicName, String chapterName) {
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterName = chapterName;
    }

    public ComicChapterDownloader setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    public ComicChapterDownloader setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public boolean isPause() {
        return isPause;
    }

    public void start(int position) {
        isPause = false;
        setStart();
        download(position);
    }

    public void pause() {
        isPause = true;
        setPause();
    }

    public void delete() {
        isDelete = true;
        setDelete();
    }

    private void download(final int position) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPause) {
                    if (!EmptyUtils.isListsEmpty(urls)) {
                        if (position < urls.size()) {
                            String url = urls.get(position);
                            String path = ComicDocumentHelper.getInstance().getStoragePath(url, comicId, comicName, chapterName);
                            if (!TextUtils.isEmpty(path)) {
                                if (new File(path).exists()) {
                                    //图片存在直接跳过
                                    setCompleteToItem(position);
                                    download(position + 1);
                                } else {
                                    //图片不存在直接下载
                                    RetrofitHelper.getInstance()
                                            .create(ComicDownloadService.class)
                                            .get(url)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DownloadObserver(url, position));
                                }
                            }
                        } else if (position == urls.size()) {
                            setCompleteToAll();
                        }
                    }
                }
            }
        }, 30);
    }

    private void setCompleteToAll() {
        if (listener != null) {
            listener.onCompleteToAll();
        }
    }

    private void setPause() {
        if (listener != null) {
            listener.onPause();
        }
    }

    private void setCompleteToItem(int position) {
        if (listener != null) {
            listener.onCompleteToItem(position);
        }
    }

    private void setError(Throwable e) {
        if (listener != null) {
            listener.onError(e);
        }
    }

    private void setStart() {
        if (listener != null) {
            listener.onStart();
        }
    }


    private class DownloadObserver implements Observer<ResponseBody> {
        private String url;
        private int position;

        DownloadObserver(String url, int position) {
            this.url = url;
            this.position = position;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            if (!isDelete) {
                if (!isPause) {
                    String path = ComicDocumentHelper.getInstance().getStoragePath(url, comicId, comicName, chapterName);
                    if (!TextUtils.isEmpty(path)) {
                        FileUtils.copy(responseBody.byteStream(), new File(path));
                    }
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            setError(e);
        }

        @Override
        public void onComplete() {
            if (!isDelete) {
                if (!isPause) {
                    setCompleteToItem(position);
                    download(position + 1);
                }
            }
        }
    }


    private void setDelete() {
        if (listener != null) {
            listener.onDelete();
        }
    }


    public interface Listener {
        void onStart();

        void onCompleteToItem(int position);

        void onPause();

        void onCompleteToAll();

        void onError(Throwable e);

        void onDelete();
    }
}
