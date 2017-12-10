package com.chenzhipeng.mhbzdz.view.wallpaper;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface IWallpaperClassifyView {
    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);

}
