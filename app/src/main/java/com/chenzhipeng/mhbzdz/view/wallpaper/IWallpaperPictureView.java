package com.chenzhipeng.mhbzdz.view.wallpaper;


public interface IWallpaperPictureView {

    <T> void onAdapter(T data, int position);

    void onBottomBar(String s);

    void onDownloadState(boolean b);

    void onStartDownload();

    void onComplete();

    void onError(Throwable e);

}
