package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperPictureListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.download.PictureDownloader;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.lockScreen.SetWallpaperHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperPictureView;

import java.io.File;
import java.util.List;

@SuppressWarnings("unchecked")
public class WallpaperPicturePresenter {
    private IWallpaperPictureView pictureView;
    private WallpaperPictureActivity activity;
    private List<WallpaperItemBean> beanList;
    private int position = 0;
    private PictureDownloader downloader;


    public WallpaperPicturePresenter(WallpaperPictureActivity activity) {
        this.pictureView = this.activity = activity;
        this.downloader = new PictureDownloader(activity);
    }

    public void updateBottomBar(int position) {
        this.position = position;
        if (!EmptyUtils.isListsEmpty(beanList)) {
            String s = position + 1 + "/" + beanList.size();
            pictureView.onBottomBar(s);
            WallpaperItemBean bean = beanList.get(position);
            if (!TextUtils.isEmpty(bean.getImg())) {
                pictureView.onDownloadState(checkHaveFile(bean.getImg()));
            } else if (!TextUtils.isEmpty(bean.getLocalPath())) {
                pictureView.onDownloadState(true);
            }
        }
    }


    private boolean checkHaveFile(String url) {
        if (!TextUtils.isEmpty(url)) {
            String pictureName = getPictureName(url);
            if (!TextUtils.isEmpty(pictureName)) {
                return getPath(pictureName).exists();
            }
        }
        return false;
    }


    public void initData() {
        position = (int) SuperIntent.getInstance().get(SuperIntent.S13);
        beanList = (List<WallpaperItemBean>) SuperIntent.getInstance().get(SuperIntent.S12);
        if (!EmptyUtils.isListsEmpty(beanList)) {
            WallpaperPictureListAdapter adapter = new WallpaperPictureListAdapter(R.layout.itemview_wallpaper_picture, beanList);
            pictureView.onAdapter(adapter, position);
            updateBottomBar(position);
        }
    }

    private String getPictureName(String url) {
        String pictureName = null;
        if (!TextUtils.isEmpty(url)) {
            String[] split = url.split(File.separator);
            if (split.length >= 4) {
                pictureName = split[3];
            }
        }
        return pictureName;
    }

    private File getPath(String pictureName) {
        return new File(BaseApplication.WALLPAPER_PATH, pictureName + ".jpg");
    }

    public void startDownload() {
        if (!EmptyUtils.isListsEmpty(beanList)) {
            String img = beanList.get(position).getImg();
            String pictureName = getPictureName(img);
            if (!TextUtils.isEmpty(pictureName)) {
                downloader.setUrl(img)
                        .setFile(getPath(pictureName))
                        .setListener(new PictureDownloader.Listener() {
                            @Override
                            public void onStart() {
                                pictureView.onStartDownload();
                            }

                            @Override
                            public void onComplete() {
                                pictureView.onComplete();
                                //用于更新是否下载图标状态
                                //  pictureView.onDownloadState(true);
                            }

                            @Override
                            public void onError(Throwable e) {
                                pictureView.onError(e);
                            }
                        }).start();
            }
        }
    }

    public void setWallpaper() {
        if (!EmptyUtils.isListsEmpty(beanList)) {
            String img = beanList.get(position).getImg();
            if (!TextUtils.isEmpty(img)) {
                SetWallpaperHelper.getInstance().setLockScreen(activity, img);
            } else {
                String path = beanList.get(position).getLocalPath();
                if (!TextUtils.isEmpty(path)) {
                    SetWallpaperHelper.getInstance().setLockScreen(activity, path);
                }
            }
        }
    }
}
