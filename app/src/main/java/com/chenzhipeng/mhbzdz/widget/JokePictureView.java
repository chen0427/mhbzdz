package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;


public class JokePictureView extends FrameLayout implements View.OnClickListener {
    private SubsamplingScaleImageView scaleImageView;
    private AppCompatImageView imageView;
    private ProgressBar progressBar;
    private AppCompatTextView textView;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

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
        textView = findViewById(R.id.AppCompatTextView);
        textView.setTextColor(Color.WHITE);
        scaleImageView.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    public void setTextVisibility(boolean b) {
        textView.setVisibility(b ? VISIBLE : GONE);
    }

    public void setText(String s) {
        if (!TextUtils.isEmpty(s)) {
            textView.setText(s);
        }
    }

    public void setProgressBarVisibility(boolean b) {
        progressBar.setVisibility(b ? VISIBLE : GONE);
    }

    public SubsamplingScaleImageView getScaleImageView() {
        return this.scaleImageView;
    }

    public AppCompatImageView getImageView() {
        return this.imageView;
    }


    public void setImage(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("webp")) {
                imageView.setVisibility(GONE);
                scaleImageView.setVisibility(VISIBLE);
                ImageHelper.setJokeToLargeImage(url, this);
            } else {
                imageView.setVisibility(VISIBLE);
                scaleImageView.setVisibility(GONE);
                ImageHelper.setJokeToGIF(url, this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.click();
        }
    }

    public interface Listener {
        void click();
    }
}
