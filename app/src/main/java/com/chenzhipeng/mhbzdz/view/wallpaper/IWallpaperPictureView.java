package com.chenzhipeng.mhbzdz.view.wallpaper;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface IWallpaperPictureView {

    <T> void onAdapter(T data, int position);

    void onBottomBar(String s);

    void onDownloadState(boolean b);

    void onStartDownload();

    void onComplete();

    void onError(Throwable e);

}
