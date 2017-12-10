package com.chenzhipeng.mhbzdz.fragment.comic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicRecommendListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicRecommendPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicRecommendView;
import com.chenzhipeng.mhbzdz.widget.VpSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicRecommendFragment extends BaseFragment
        implements IComicRecommendView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private View fragmentView;
    @BindView(R.id.vsrl_manhua)
    VpSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_manhua)
    RecyclerView recyclerView;
    private ComicRecommendPresenter presenter;
    private ComicRecommendListAdapter adapter;


    private ComicRecommendPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicRecommendPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_recommend, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        getPresenter().initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }

    @Override
    public void onEmptyData() {
        showSnackbar(getString(R.string.emptyData));
    }

    @Override
    public void setProgress(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(getString(R.string.networFail));
    }

    @Override
    public void onRefresh() {
        getPresenter().initData();
    }

    @Override
    public <T> void onAdapter(T data) {
        adapter = (ComicRecommendListAdapter) data;
        if (adapter != null) {
            adapter.setOnLoadMoreListener(this, recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onLoadMoreRequested() {
        if (adapter != null) {
            adapter.loadMoreEnd();
        }
    }
}
