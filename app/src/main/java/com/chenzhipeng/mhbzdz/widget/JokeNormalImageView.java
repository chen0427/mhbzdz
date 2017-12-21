package com.chenzhipeng.mhbzdz.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
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


public class JokeNormalImageView extends FrameLayout implements View.OnClickListener {
    private AppCompatImageView imageView;
    private Context context;
    private String url;
    private boolean isSetSize = true;
    private List<JokeBean.Data.Dates.Group.LargeImageList> urlList;
    private int position = 0;

    public JokeNormalImageView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_normal_image, this);
        init();
    }

    public JokeNormalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_normal_image, this);
        init();
    }

    public JokeNormalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.customview_joke_normal_image, this);
        init();
    }

    private void init() {
        imageView = findViewById(R.id.iv_customImageView);
        imageView.setOnClickListener(this);
    }


    public void setSetSize(boolean setSize) {
        isSetSize = setSize;
    }

    public void setUrlList(List<JokeBean.Data.Dates.Group.LargeImageList> urlList, int position) {
        this.urlList = urlList;
        this.position = position;
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
        ImageHelper.setImageToList(url, imageView, isSetSize, getBitmap(width, height));
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
        } else {
            JokePictureActivity.startActivity((Activity) context, url, 0);
        }
    }
}
