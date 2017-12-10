package com.chenzhipeng.mhbzdz.view.wallpaper;

/**
 * Created by Administrator on 2017/10/4.
 */

public interface IWallpaperDownloadView {
    <T> void onAdapter(T data);

    void showDeleteDialog();
}
