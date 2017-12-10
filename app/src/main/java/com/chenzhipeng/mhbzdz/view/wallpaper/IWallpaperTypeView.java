package com.chenzhipeng.mhbzdz.view.wallpaper;

/**
 * Created by Administrator on 2017/8/14.
 */

public interface IWallpaperTypeView {
    <T> void onListAdapter(T data);

    <T> void onClassifyAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
