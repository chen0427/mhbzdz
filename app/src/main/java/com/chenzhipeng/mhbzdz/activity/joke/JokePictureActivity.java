package com.chenzhipeng.mhbzdz.activity.joke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.joke.JokePictureListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.presenter.joke.JokePicturePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokePictureView;
import com.chenzhipeng.mhbzdz.widget.PictureBottomView;
import com.chenzhipeng.mhbzdz.widget.rvp.RecyclerViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class JokePictureActivity extends BaseActivity implements IJokePictureView, PictureBottomView.Listener {
    @BindView(R.id.pbv_jokePicture)
    PictureBottomView pictureBottomView;
    @BindView(R.id.RecyclerViewPager)
    RecyclerViewPager recyclerViewPager;
    private JokePicturePresenter presenter;
    private AlertDialog alertDialog;

    private int readPosition = 0;


    public static <T> void startActivity(Activity activity, T data, int currentPosition) {
        if (activity != null && data != null) {
            Intent intent = new Intent(activity, JokePictureActivity.class);
            if (data instanceof ArrayList) {
                SuperIntent.getInstance().put(SuperIntent.S16, data);
            } else if (data instanceof String) {
                ArrayList<String> strings = new ArrayList<>();
                strings.add(String.valueOf(data));
                SuperIntent.getInstance().put(SuperIntent.S16, strings);
            }
            SuperIntent.getInstance().put(SuperIntent.S15, currentPosition);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    @Override
    public void finish() {
        SuperIntent.getInstance().remove(SuperIntent.S15, SuperIntent.S16);
        super.finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public JokePicturePresenter getPresenter() {
        if (presenter == null) {
            presenter = new JokePicturePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_joke_picture);
        ButterKnife.bind(this);
        pictureBottomView.setHideCenter(true);
        pictureBottomView.setListener(this);
        getPresenter().initData();
    }


    @Override
    public <T> void onAdapter(T data, int position) {
        JokePictureListAdapter adapter = (JokePictureListAdapter) data;
        if (adapter != null) {
            recyclerViewPager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewPager.setHasFixedSize(true);
            recyclerViewPager.addOnScrollListener(new RecyclerViewScroll());
            recyclerViewPager.setScrollSpeed(BaseApplication.SCROLL_SPEED);
            recyclerViewPager.setAdapter(adapter);
            recyclerViewPager.scrollToPosition(position);
            this.readPosition = position;
        }
    }

    @Override
    public void onUpdateBottomBar(String s) {
        if (!TextUtils.isEmpty(s)) {
            pictureBottomView.setPageSize(s);
        }
    }

    @Override
    public void start() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(this).create();
        }
        alertDialog.setMessage(getString(R.string.picture_downloading));
        alertDialog.show();
    }

    @Override
    public void complete() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), R.string.album_download, Snackbar.LENGTH_SHORT).show();
            }
        }, 300);

    }

    @Override
    public void error(Throwable e) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), R.string.picture_download_fail, Snackbar.LENGTH_SHORT).show();
            }
        }, 300);
    }

    @Override
    public void centerClick() {

    }

    @Override
    public void downloadClick() {
        getPresenter().download();
    }


    /**
     * 监听滑动到第几页
     */
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
