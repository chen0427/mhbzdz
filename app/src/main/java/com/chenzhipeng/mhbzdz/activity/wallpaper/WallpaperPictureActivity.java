package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.widget.Toast;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperPicturePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperPictureView;
import com.chenzhipeng.mhbzdz.widget.WallpaperViewPaper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class WallpaperPictureActivity extends BaseActivity implements
        IWallpaperPictureView, WallpaperViewPaper.Listener {
    private WallpaperPicturePresenter presenter;
    private AlertDialog alertDialog;
    @BindView(R.id.WallpaperViewPaper)
    WallpaperViewPaper viewPaper;
    public static List<WallpaperItemBean> dataList;
    public static int readPosition = 0;


    public static void startActivity(Context context, List<WallpaperItemBean> beanList, int position) {
        if (context == null || beanList == null || beanList.size() == 0) {
            return;
        }
        dataList = beanList;
        readPosition = position;
        Intent intent = new Intent(context, WallpaperPictureActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public void finish() {
        dataList = null;
        readPosition = 0;
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
        getPresenter().initData();
    }


    @Override
    public <T> void onData(T data, int position) {
        viewPaper.setData((List<WallpaperItemBean>) data, position);
        viewPaper.setListener(this);
        viewPaper.setActivity(this);
    }

    @Override
    public void start() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        alertDialog.setMessage(getString(R.string.picture_downloading));
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void complete() {
        alertDialog.dismiss();
        Toast.makeText(BaseApplication.getContext(), getString(R.string.complete_download), Toast.LENGTH_SHORT).show();
        viewPaper.updateDownload();
    }

    @Override
    public void error() {
        Toast.makeText(BaseApplication.getContext(), R.string.picture_download_fail, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConfigUtils.getVolumePage()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    viewPaper.volumeDown();
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    viewPaper.volumeUp();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
