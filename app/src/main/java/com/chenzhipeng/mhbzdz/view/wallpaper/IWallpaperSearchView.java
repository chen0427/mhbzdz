package com.chenzhipeng.mhbzdz.view.wallpaper;


public interface IWallpaperSearchView {
    void onTitle(String s);

    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
