package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
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

import com.chenzhipeng.mhbzdz.R;


public class PictureBottomView extends FrameLayout implements View.OnClickListener {
    private AppCompatTextView pictureSizeTextView;
    private AppCompatImageView pictureDownloadImageView;
    private AppCompatTextView pictureFlagDownloadTextView;
    private AppCompatTextView pictureCenterTextView;
    private Listener listener;
    private boolean isHideCenter = true;

    public PictureBottomView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PictureBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PictureBottomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_picture_bottom, this);
        pictureSizeTextView = findViewById(R.id.tv_pictureBottomSize);
        pictureDownloadImageView = findViewById(R.id.iv_pictureBottomDownload);
        pictureFlagDownloadTextView = findViewById(R.id.tv_pictureBottomDownload);
        pictureCenterTextView = findViewById(R.id.tv_pictureBottomCenter);
        pictureCenterTextView.setVisibility(isHideCenter ? INVISIBLE : VISIBLE);
        pictureCenterTextView.setOnClickListener(this);
        pictureDownloadImageView.setOnClickListener(this);
    }

    public void setHideCenter(boolean hideCenter) {
        isHideCenter = hideCenter;
        pictureCenterTextView.setVisibility(isHideCenter ? INVISIBLE : VISIBLE);
        invalidate();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public void setPageSize(String str) {
        if (!TextUtils.isEmpty(str)) {
            pictureSizeTextView.setText(str);
        }
    }

    public void setDownload(boolean download) {
        pictureDownloadImageView.setVisibility(download ? GONE : VISIBLE);
        pictureFlagDownloadTextView.setVisibility(download ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pictureBottomCenter:
                if (listener != null) {
                    listener.centerClick();
                }
                break;
            case R.id.iv_pictureBottomDownload:
                if (listener != null) {
                    listener.downloadClick();
                }
                break;
        }
    }



    public interface Listener {
        void centerClick();

        void downloadClick();
    }
}
