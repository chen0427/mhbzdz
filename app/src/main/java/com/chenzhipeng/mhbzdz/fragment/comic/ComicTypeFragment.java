package com.chenzhipeng.mhbzdz.fragment.comic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicTypeDataPresenter;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicTypeDataView;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class ComicTypeFragment extends BaseFragment implements IComicTypeDataView,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {
    private View fragmentView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;
    private ComicBookListAdapter adapter;
    private ComicTypeDataPresenter presenter;
    private int page = 1;
    private boolean isActivityCreated = false;
    private boolean isVisibleToUser = false;

    public ComicTypeDataPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicTypeDataPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_type, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
        refreshLayout.setOnRefreshListener(this);
        if (isVisibleToUser) {
            getPresenter().initData(page = 1, false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (isActivityCreated) {
                //判断数据是否为空
                if (adapter == null || EmptyUtils.isListsEmpty(adapter.getData())) {
                    getPresenter().initData(page = 1, false);
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }

    @Override
    public <T> void onAdapter(T data) {
        if (data != null) {
            adapter = (ComicBookListAdapter) data;
            adapter.setOnLoadMoreListener(this, recyclerView);
            adapter.setOnItemChildClickListener(this);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
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
    public void setProgress(boolean b) {
        refreshLayout.setRefreshing(b);
    }

    @Override
    public void onLoadMoreRequested() {
        //加载更多
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                page++;
                getPresenter().initData(page, true);
            }
        });
    }

    @Override
    public void onRefresh() {
        getPresenter().initData(page = 1, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ComicItemBean bean = (ComicItemBean) adapter.getData().get(position);
        ComicDetailsActivity.startActivity(getActivity(), bean);
    }
}
