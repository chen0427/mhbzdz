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
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperClassifyActivity;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperClassifyListAdapter;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyItemBean;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperTypePresenter;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperTypeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class WallpaperTypeFragment extends BaseFragment implements IWallpaperTypeView
        , BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_wallpaperType)
    RecyclerView recyclerView;
    private WallpaperTypePresenter presenter;
    private View fragmentVew;
    private boolean isCreateActivity = false;
    private boolean isVisible = false;
    private int skip = 0;
    @BindView(R.id.srl)
    SwipeRefreshLayout refreshLayout;


    public WallpaperTypePresenter getPresenter() {
        if (presenter == null) {
            presenter = new WallpaperTypePresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentVew == null) {
            fragmentVew = inflater.inflate(R.layout.fragment_wallpaper_type, container, false);
            ButterKnife.bind(this, fragmentVew);
        }
        return fragmentVew;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreateActivity = true;
        refreshLayout.setOnRefreshListener(this);
        if (isVisible) {
            getPresenter().initData(skip, false);
        }
    }

    @Override
    public <T> void onListAdapter(T data) {
        WallpaperListAdapter listAdapter = (WallpaperListAdapter) data;
        if (listAdapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setHasFixedSize(true);
            listAdapter.setOnItemChildClickListener(this);
            listAdapter.setOnLoadMoreListener(this, recyclerView);
            recyclerView.setAdapter(listAdapter);
        }
    }

    @Override
    public <T> void onClassifyAdapter(T data) {
        WallpaperClassifyListAdapter categoryListAdapter = (WallpaperClassifyListAdapter) data;
        if (categoryListAdapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(categoryListAdapter);
            categoryListAdapter.setOnItemChildClickListener(this);
        }
    }


    @Override
    public void onFail(Throwable e) {
        showSnackbar(getString(R.string.networFail));
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            //可见
            if (isCreateActivity) {
                BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
                if (adapter == null || EmptyUtils.isListsEmpty(adapter.getData())) {
                    //没有数据
                    skip = 0;
                    getPresenter().initData(skip, false);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentVew);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (getPresenter().getType() == WallpaperIndexFragment.TYPE_CLASSIFY) {
            WallpaperClassifyItemBean bean = (WallpaperClassifyItemBean) adapter.getData().get(position);
            WallpaperClassifyActivity.startActivity(getActivity(), bean);
        } else {
            List data = adapter.getData();
            WallpaperPictureActivity.startActivity(getActivity(), data, position);
        }
    }

    @Override
    public void onRefresh() {
        skip = 0;
        getPresenter().initData(skip, false);
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                skip = skip + 30;
                getPresenter().initData(skip, true);
            }
        }, BaseApplication.LOAD_MORE_DELAY);
    }
}
