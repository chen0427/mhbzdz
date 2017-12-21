package com.chenzhipeng.mhbzdz.view.wallpaper;


public interface IWallpaperClassifyView {
    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);

}
