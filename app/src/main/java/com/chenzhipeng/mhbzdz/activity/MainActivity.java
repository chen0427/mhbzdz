package com.chenzhipeng.mhbzdz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperPictureActivity;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.presenter.MainPresenter;
import com.chenzhipeng.mhbzdz.utils.DisplayUtils;
import com.chenzhipeng.mhbzdz.utils.VideoUtils;
import com.chenzhipeng.mhbzdz.view.IMainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

public class MainActivity extends BaseActivity implements IMainView, NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener, View.OnClickListener {
    @BindView(R.id.nv_main)
    NavigationView navigationView;
    @BindView(R.id.root)
    DrawerLayout drawerLayout;
    private long secondTime = 0;
    private MainPresenter presenter;
    public static int index = 0;
    public static final int INDEX_COMIC = 0;
    public static final int INDEX_WALLPAPER = 1;
    public static final int INDEX_JOKE = 2;


    private MainPresenter getPresenter() {
        if (presenter == null) {
            presenter = new MainPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        DisplayUtils.init(this);
        View leftHeaderView = navigationView.getHeaderView(0);
        drawerLayout.addDrawerListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        leftHeaderView.setOnClickListener(this);
        getPresenter().addFragment();
        getPresenter().setLeftImageChange(leftHeaderView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer jcVideoPlayer = JCVideoPlayerManager.getCurrentJcvd();
        if (jcVideoPlayer != null && jcVideoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
            jcVideoPlayer.startButton.performClick();
        }
    }

    private boolean closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (closeDrawer()) {
            return;
        }
        VideoUtils.backPress();
        super.onBackPressed();
    }


    @Override
    public void finish() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - secondTime > 2000) {
            secondTime = currentTime;
            showSnackbar(getString(R.string.try_out));
        } else {
            VideoUtils.destroy();
            drawerLayout.removeDrawerListener(this);
            getPresenter().imageChangeFinish();
            super.finish();
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_comic:
                index = INDEX_COMIC;
                VideoUtils.destroy();
                item.setChecked(true);
                closeDrawer();
                break;
            case R.id.item_wallpaper:
                index = INDEX_WALLPAPER;
                VideoUtils.destroy();
                item.setChecked(true);
                closeDrawer();
                break;
            case R.id.item_joke:
                index = INDEX_JOKE;
                item.setChecked(true);
                closeDrawer();
                break;
            case R.id.item_share:
                item.setChecked(true);
                break;
            case R.id.item_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        switch (index) {
            case INDEX_COMIC:
                getPresenter().addComic();
                break;
            case INDEX_WALLPAPER:
                getPresenter().addWallpaper();
                break;
            case INDEX_JOKE:
                getPresenter().addJoke();
                break;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void leftItemSelected(int i) {
        switch (index) {
            case INDEX_COMIC:
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case INDEX_WALLPAPER:
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            case INDEX_JOKE:
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //left
        String path = String.valueOf(v.getTag());
        if (!TextUtils.isEmpty(path)) {
            WallpaperItemBean wallpaperItemBean = new WallpaperItemBean(path, false, false);
            List<WallpaperItemBean> wallpaperItemBeanList = new ArrayList<>();
            wallpaperItemBeanList.add(wallpaperItemBean);
            WallpaperPictureActivity.startActivity(this, wallpaperItemBeanList, 0);
        }
    }
}
