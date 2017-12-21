package com.chenzhipeng.mhbzdz.view.wallpaper;


public interface IWallpaperDownloadView {
    <T> void onAdapter(T data);

    void showDeleteDialog();
}
