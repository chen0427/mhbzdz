package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicReadPicturePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.LogUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicPictureView;
import com.chenzhipeng.mhbzdz.widget.ComicViewPaper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class ComicReadPictureActivity extends BaseActivity
        implements IComicPictureView, ComicViewPaper.Listener {
    public static final int TYPE_CHAPTER = 0;
    public static final int TYPE_DOWNLOAD = 1;
    @BindView(R.id.ComicViewPaper)
    ComicViewPaper paper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.FrameLayout)
    FrameLayout frameLayout;
    private ComicReadPicturePresenter presenter;
    public static List<ComicChapterItemBean> dataList;
    public static int position;
    public static boolean isReverse;
    public static int type;
    public static List<ComicItemPicture> pictures;
    public static String[] comicIdAndComicName;


    public static void startActivity(Context context,
                                     List<ComicItemPicture> pictures,
                                     String[] comicIdAndComicName, boolean isReverse) {
        if (context != null && !EmptyUtils.isListsEmpty(pictures) && comicIdAndComicName != null && comicIdAndComicName.length == 3) {
            Intent intent = new Intent(context, ComicReadPictureActivity.class);
            ComicReadPictureActivity.comicIdAndComicName = comicIdAndComicName;
            ComicReadPictureActivity.pictures = pictures;
            ComicReadPictureActivity.isReverse = isReverse;
            ComicReadPictureActivity.type = TYPE_DOWNLOAD;
            context.startActivity(intent);
        }
    }


    public static void startActivity(Context context, List<ComicChapterItemBean> beanList, int position, boolean isReverse) {
        if (context != null && !EmptyUtils.isListsEmpty(beanList)) {
            ComicReadPictureActivity.dataList = beanList;
            ComicReadPictureActivity.position = position;
            ComicReadPictureActivity.isReverse = isReverse;
            context.startActivity(new Intent(context, ComicReadPictureActivity.class));
        }
    }

    private ComicReadPicturePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicReadPicturePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_picture);
        setStatusBarColor(Color.BLACK);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getPresenter().initData();
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    private void showToolbar() {
        if (frameLayout.getVisibility() == View.GONE) {
            frameLayout.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(170);
            frameLayout.startAnimation(animationSet);
        }
    }

    private void hideToolbar() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(170);
            frameLayout.startAnimation(animationSet);
        }
    }

    @Override
    public <T> void onData(T data) {
        ComicChapterItemBean bean = (ComicChapterItemBean) data;
        if (bean != null) {
            paper.setListener(this);
            paper.setData(bean.getComicId(), bean.getComicName(), bean.getComicItemPictureList());
        }
    }

    @Override
    public void onDataToDownload(String comicId, String comicName, String chapterName, List<ComicItemPicture> pictures) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName, chapterName) && !EmptyUtils.isListsEmpty(pictures)) {
            position = 0;
            paper.setListener(this);
            paper.setData(comicId, comicName, pictures);
            LogUtils.d(pictures.get(0).getUrl());
        }
    }

    @Override
    public void onTitle(String s) {
        if (!TextUtils.isEmpty(s)) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(s);
            }
        }
    }

    @Override
    public void move(int i) {
        if (i == ComicViewPaper.MOVE_LEFT) {
            if (!EmptyUtils.isListsEmpty(dataList)) {
                int a;
                if (isReverse) {
                    a = position + 1;
                } else {
                    a = position - 1;
                }
                if (a >= 0 && a < dataList.size()) {
                    position = a;
                    paper.updateData(dataList.get(position).getComicItemPictureList());
                }
            }
        } else if (i == ComicViewPaper.MOVE_RIGHT) {
            if (!EmptyUtils.isListsEmpty(dataList)) {
                int a;
                if (isReverse) {
                    a = position - 1;
                } else {
                    a = position + 1;
                }
                if (a >= 0 && a < dataList.size()) {
                    position = a;
                    paper.updateData(dataList.get(position).getComicItemPictureList());
                }
            }
        }
    }

    @Override
    public void onMove(int i) {
        if (i == ComicViewPaper.MOVE_LEFT) {
            if (!EmptyUtils.isListsEmpty(dataList)) {
                int a;
                if (isReverse) {
                    a = position + 1;
                } else {
                    a = position - 1;
                }
                if (a >= 0 && a < dataList.size()) {
                    paper.left();
                } else {
                    //没有
                    paper.showNoChapter();
                }
            }
        } else if (i == ComicViewPaper.MOVE_RIGHT) {
            if (!EmptyUtils.isListsEmpty(dataList)) {
                int a;
                if (isReverse) {
                    a = position - 1;
                } else {
                    a = position + 1;
                }
                if (a >= 0 && a < dataList.size()) {
                    paper.right();
                } else {
                    //没有
                    paper.showNoChapter();
                }
            }
        }
    }

    @Override
    public void onClick() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            hideToolbar();
        } else {
            showToolbar();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            hideToolbar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConfigUtils.getVolumePage()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    paper.volumeDown();
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    paper.volumeUp();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        dataList = null;
        position = 0;
        isReverse = false;
        type = TYPE_CHAPTER;
        pictures = null;
        comicIdAndComicName = null;
        super.finish();
    }
}
