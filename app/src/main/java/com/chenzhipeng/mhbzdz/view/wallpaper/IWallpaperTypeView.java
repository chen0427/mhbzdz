package com.chenzhipeng.mhbzdz.view.wallpaper;


public interface IWallpaperTypeView {
    <T> void onListAdapter(T data);

    <T> void onClassifyAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
