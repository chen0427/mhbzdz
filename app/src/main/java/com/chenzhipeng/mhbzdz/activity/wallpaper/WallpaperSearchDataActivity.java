package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperSearchPresenter;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class WallpaperSearchDataActivity extends BaseActivity implements IWallpaperSearchView, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_wallpaperSearch)
    RecyclerView recyclerView;
    @BindView(R.id.srl_wallpaperSearch)
    SwipeRefreshLayout refreshLayout;
    private int skip = 0;
    private WallpaperSearchPresenter presenter;
    public static String KEY_INTENT = "wallpaper_searchKey";


    public static void startActivity(Context context, String searchKey) {
        if (context != null && !TextUtils.isEmpty(searchKey)) {
            Intent intent = new Intent(context, WallpaperSearchDataActivity.class);
            intent.putExtra(KEY_INTENT, searchKey);
            context.startActivity(intent);
        }
    }

    public WallpaperSearchPresenter getPresenter() {
        if (presenter == null) {
            presenter = new WallpaperSearchPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_search);
        ButterKnife.bind(this);
        refreshLayout.setOnRefreshListener(this);
        getPresenter().initData(skip = 0, false);
    }


    @Override
    public void onTitle(String s) {
        if (!TextUtils.isEmpty(s)) {
            setToolbar(toolbar, getString(R.string.search_result)  + s, true);
        }
    }

    @Override
    public <T> void onAdapter(T data) {
        WallpaperListAdapter adapter = (WallpaperListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            adapter.setOnLoadMoreListener(this, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void setProgress(boolean b) {
        refreshLayout.setRefreshing(b);
    }

    @Override
    public void onEmptyData() {
        showSnackbar(getString(R.string.emptyData));
    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(getString(R.string.networFail));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WallpaperPictureActivity.startActivity(this, adapter.getData(), position);
    }

    @Override
    public void onRefresh() {
        getPresenter().initData(skip = 0, false);
    }

    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skip = skip + 30;
                getPresenter().initData(skip, true);
            }
        }, BaseApplication.LOAD_MORE_DELAY);
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
}
