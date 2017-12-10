package com.chenzhipeng.mhbzdz.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.joke.JokePictureActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class JokeGifTagImageView extends FrameLayout implements View.OnClickListener {
    private AppCompatImageView imageView;
    private AppCompatTextView tagTextView;
    private Context context;
    private String url;
    private boolean isSetSize = true;
    private List<JokeBean.Data.Dates.Group.LargeImageList> urlList;
    private int position = 0;
    private String gifUrl;

    public JokeGifTagImageView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.customview_joke_tag_image, this);
        init(context);
    }

    public JokeGifTagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.customview_joke_tag_image, this);
        init(context);
    }

    public JokeGifTagImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.customview_joke_tag_image, this);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        imageView = findViewById(R.id.iv_customImageView);
        tagTextView = findViewById(R.id.tv_customTag);
        imageView.setOnClickListener(this);
    }

    public void setSetSize(boolean setSize) {
        isSetSize = setSize;
    }

    public void setUrlList(List<JokeBean.Data.Dates.Group.LargeImageList> urlList, int position) {
        this.urlList = urlList;
        this.position = position;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }


    private Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = null;
        if (width > 0 && height > 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            bitmap.eraseColor(ContextCompat.getColor(BaseApplication.getContext(), R.color.placeholder));
        }
        return bitmap;
    }

    public void setImage(String url, final int width, final int height) {
        this.url = url;
        ImageHelper.setGIFToList(url, imageView, tagTextView, isSetSize, getBitmap(width, height));
    }

    @Override
    public void onClick(View view) {
        if (!EmptyUtils.isListsEmpty(urlList)) {
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < urlList.size(); i++) {
                String s = urlList.get(i).getUrlLists().get(0).getUrl();
                strings.add(s);
            }
            JokePictureActivity.startActivity((Activity) context, strings, position);
        } else if (!TextUtils.isEmpty(gifUrl)) {
            JokePictureActivity.startActivity((Activity) context, gifUrl, 0);
        } else {
            JokePictureActivity.startActivity((Activity) context, url, 0);
        }
    }


}
