package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


public class JokePictureView extends FrameLayout {
    private SubsamplingScaleImageView scaleImageView;
    private AppCompatImageView imageView;
    private ProgressBar progressBar;


    public JokePictureView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public JokePictureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JokePictureView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_joke_picture, this);
        scaleImageView = findViewById(R.id.siv_JokePicture);
        imageView = findViewById(R.id.iv_JokePicture);
        progressBar = findViewById(R.id.pb_JokePicture);
    }


    public void setImage(String url, int placeholderId) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("webp")) {
                ImageHelper.setLargeImage(url, scaleImageView, progressBar, placeholderId);
            } else {
                ImageHelper.setGIF(url, imageView, progressBar, placeholderId);
            }
        }
    }
}
