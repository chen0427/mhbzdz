package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendTypeBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicRecommendTagPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicRecommendTagView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicRecommendTagActivity extends BaseActivity implements IComicRecommendTagView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {
    private ComicRecommendTagPresenter presenter;
    @BindView(R.id.rv_recommendTagList)
    RecyclerView recyclerView;
    @BindView(R.id.srl_recommendTagListRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ComicBookListAdapter adapter;

    public static ComicRecommendTypeBean data;

    public static int recommendPosition;


    public static void startActivity(Context context, ComicRecommendTypeBean typeBean, int recommendPosition) {
        if (context != null && typeBean != null) {
            Intent intent = new Intent(context, ComicRecommendTagActivity.class);
            data = typeBean;
            ComicRecommendTagActivity.recommendPosition = recommendPosition;
            context.startActivity(intent);
        }
    }

    public ComicRecommendTagPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicRecommendTagPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_recommend_tag);
        ButterKnife.bind(this);
        refreshLayout.setOnRefreshListener(this);
        getPresenter().initAdapter();
    }

    @Override
    public <T> void onAdapter(T data) {
        adapter = (ComicBookListAdapter) data;
        if (adapter != null) {
            adapter.setOnItemChildClickListener(this);
            adapter.setOnLoadMoreListener(this, recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onTitle(String title) {
        setToolbar(toolbar, title, true);
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
    public void onRefresh() {
        getPresenter().updateList();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (adapter != null) {
                    adapter.loadMoreEnd();
                }
            }
        });

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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ComicDetailsActivity.startActivity(this, (ComicItemBean) adapter.getData().get(position));
    }

    @Override
    public void finish() {
        data = null;
        recommendPosition = 0;
        super.finish();
    }
}
