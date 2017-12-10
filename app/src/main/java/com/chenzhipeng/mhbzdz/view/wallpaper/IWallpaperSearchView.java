package com.chenzhipeng.mhbzdz.view.wallpaper;

/**
 * Created by Administrator on 2017/8/18.
 */

public interface IWallpaperSearchView {
    void onTitle(String s);

    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
