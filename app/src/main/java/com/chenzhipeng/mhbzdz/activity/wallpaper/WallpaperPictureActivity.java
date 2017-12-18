package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperPictureListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperPicturePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperPictureView;
import com.chenzhipeng.mhbzdz.widget.PictureBottomView;
import com.chenzhipeng.mhbzdz.widget.rvp.RecyclerViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class WallpaperPictureActivity extends BaseActivity implements
        IWallpaperPictureView, PictureBottomView.Listener {
    @BindView(R.id.RecyclerViewPager)
    RecyclerViewPager recyclerViewPager;
    @BindView(R.id.pbv_wallpaperPicture)
    PictureBottomView pictureBottomView;
    private WallpaperPicturePresenter presenter;
    private AlertDialog alertDialog;

    private int readPosition = 0;


    public static void startActivity(Context context, List<WallpaperItemBean> beanList, int position) {
        if (context == null || beanList == null || beanList.size() == 0) {
            return;
        }
        Intent intent = new Intent(context, WallpaperPictureActivity.class);
        SuperIntent.getInstance().put(SuperIntent.S12, beanList);
        SuperIntent.getInstance().put(SuperIntent.S13, position);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void finish() {
        SuperIntent.getInstance().remove(SuperIntent.S12, SuperIntent.S13);
        super.finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public WallpaperPicturePresenter getPresenter() {
        if (presenter == null) {
            presenter = new WallpaperPicturePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_wallpaper_picture);
        ButterKnife.bind(this);
        pictureBottomView.setHideCenter(false);
        pictureBottomView.setListener(this);
        getPresenter().initData();
    }

    @Override
    public <T> void onAdapter(T data, int position) {
        WallpaperPictureListAdapter adapter = (WallpaperPictureListAdapter) data;
        if (adapter != null) {
            recyclerViewPager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewPager.setScrollSpeed(BaseApplication.SCROLL_SPEED);
            recyclerViewPager.addOnScrollListener(new RecyclerViewScroll());
            recyclerViewPager.setAdapter(adapter);
            recyclerViewPager.scrollToPosition(position);
            this.readPosition = position;
        }
    }

    @Override
    public void onBottomBar(String s) {
        pictureBottomView.setPageSize(s);
    }

    @Override
    public void onDownloadState(boolean b) {
        pictureBottomView.setDownload(b);
    }

    @Override
    public void onStartDownload() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        alertDialog.setMessage(getString(R.string.picture_downloading));
        alertDialog.show();
    }

    @Override
    public void onComplete() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.complete_download), Snackbar.LENGTH_SHORT)
                        .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                pictureBottomView.setDownload(true);
                            }
                        }).show();
            }
        }, 300);
    }

    @Override
    public void onError(Throwable e) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(android.R.id.content), R.string.picture_download_fail, Snackbar.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }, 300);

    }

    @Override
    public void centerClick() {
        getPresenter().setWallpaper();
    }

    @Override
    public void downloadClick() {
        getPresenter().startDownload();
    }

    private class RecyclerViewScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    readPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    getPresenter().updateBottomBar(readPosition);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConfigUtils.getVolumePage()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (readPosition != 0) {
                        int a = readPosition - 1;
                        if (a >= 0) {
                            readPosition = a;
                            recyclerViewPager.smoothScrollToPosition(readPosition);
                        }
                    }
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    int itemCount = recyclerViewPager.getAdapter().getItemCount();
                    if (readPosition != itemCount - 1) {
                        int b = readPosition + 1;
                        if (b <= itemCount - 1) {
                            readPosition = b;
                            recyclerViewPager.smoothScrollToPosition(readPosition);
                        }
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
