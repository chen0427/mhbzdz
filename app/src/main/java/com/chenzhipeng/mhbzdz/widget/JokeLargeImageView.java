package com.chenzhipeng.mhbzdz.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.joke.JokePictureActivity;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Administrator on 2017/10/16.
 */

public class JokeLargeImageView extends FrameLayout implements View.OnClickListener {
    private AppCompatTextView seeTextView;
    private Context context;
    private String url;
    private SubsamplingScaleImageView scaleImageView;

    public JokeLargeImageView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_large_image, this);
        init();
    }

    public JokeLargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_large_image, this);
        init();
    }

    public JokeLargeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_large_image, this);
        init();
    }

    private void init() {
        scaleImageView = findViewById(R.id.siv_customLargeImage);
        seeTextView = findViewById(R.id.tv_customSee);
        findViewById(R.id.v_customClick).setOnClickListener(this);
        scaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
    }

    public void setImage(String largeImageUrl) {
        this.url = largeImageUrl;
        ImageHelper.setLargeImageToList(url, scaleImageView, seeTextView);
    }

    @Override
    public void onClick(View view) {
        JokePictureActivity.startActivity((Activity) context, url, 0);
    }


}
