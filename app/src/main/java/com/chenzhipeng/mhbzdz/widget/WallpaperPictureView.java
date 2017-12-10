package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

/**
 * Created by Administrator on 2017/11/20.
 */

public class WallpaperPictureView extends FrameLayout {
    private PhotoView photoView;
    private ProgressBar progressBar;

    public WallpaperPictureView(Context context) {
        super(context);
        init(context);
    }

    public WallpaperPictureView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    public WallpaperPictureView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_wallpaper_picture, this);
        photoView = findViewById(R.id.pv_wallpaperPicture);
        progressBar = findViewById(R.id.pb_wallpaperPicture);
    }


    public void setImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            ImageHelper.setWallpaper(url, photoView, progressBar);
        }
    }

    public void setImage(File file) {
        if (file != null && file.exists()) {
            ImageHelper.setWallpaper(file, photoView, progressBar);
        }
    }
}
