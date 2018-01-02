package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperPictureActivity;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperPictureView;

@SuppressWarnings("unchecked")
public class WallpaperPicturePresenter {
    private IWallpaperPictureView pictureView;


    public WallpaperPicturePresenter(WallpaperPictureActivity activity) {
        this.pictureView = activity;
    }


    public void initData() {
        if (!EmptyUtils.isListsEmpty(WallpaperPictureActivity.dataList)) {
            pictureView.onData(WallpaperPictureActivity.dataList, WallpaperPictureActivity.readPosition);
        }
    }
}
