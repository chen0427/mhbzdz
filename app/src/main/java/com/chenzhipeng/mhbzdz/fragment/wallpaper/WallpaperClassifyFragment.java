package com.chenzhipeng.mhbzdz.fragment.wallpaper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperClassifyPresenter;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperClassifyView;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressWarnings("unchecked")
public class WallpaperClassifyFragment extends BaseFragment implements IWallpaperClassifyView,
        BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    private View fragmentView;
    @BindView(R.id.rv_wallpaperType)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;
    private WallpaperClassifyPresenter presenter;
    private boolean isVisible = false;
    private boolean isCreateActivity = false;
    private int skip = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_wallpaper_classify, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreateActivity = true;
        swipeRefreshLayout.setOnRefreshListener(this);
        presenter = new WallpaperClassifyPresenter(this);
        if (isVisible) {
            initData();
        }
    }

    private void initData() {
        presenter.initData(skip = 0, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }


    @Override
    public <T> void onAdapter(T data) {
        WallpaperListAdapter adapter = (WallpaperListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            adapter.setOnLoadMoreListener(this, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void setProgress(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onEmptyData() {
        //没有数据
        showToast(getString(R.string.emptyData));
    }

    @Override
    public void onFail(Throwable e) {
        showToast(getString(R.string.networFail));
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        if (isVisible) {
            if (isCreateActivity) {
                BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
                if (adapter == null || EmptyUtils.isListsEmpty(adapter.getData())) {
                    initData();
                }
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WallpaperPictureActivity.startActivity(getActivity(), adapter.getData(), position);
    }

    @Override
    public void onRefresh() {
        presenter.initData(skip = 0, false);
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                skip = skip + 30;
                presenter.initData(skip, true);
            }
        });
    }
}
