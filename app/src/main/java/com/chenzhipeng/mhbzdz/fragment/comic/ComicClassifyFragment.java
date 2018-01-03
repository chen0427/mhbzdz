package com.chenzhipeng.mhbzdz.fragment.comic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicTypeActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicClassifyLeftListAdapter;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicClassifyRightListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicClassifyBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicClassifyPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicClassifyView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 漫画分类
 */
@SuppressWarnings("unchecked")
public class ComicClassifyFragment extends BaseFragment implements IComicClassifyView, BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View fragmentView;
    @BindView(R.id.rv_comic_left)
    RecyclerView leftRecyclerView;
    @BindView(R.id.rv_comic_right)
    RecyclerView rightRecyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ComicClassifyPresenter presenter;
    private int selectPosition = 0;
    public static final int TAG_LEFT = 1;
    public static final int TAG_RIGHT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_classify, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    public ComicClassifyPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicClassifyPresenter(this);
        }
        return presenter;
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
    public void onFail(Throwable e) {
        showToast(getString(R.string.networFail));
    }

    @Override
    public <T> void onLeftAdapter(T data) {
        ComicClassifyLeftListAdapter adapter = (ComicClassifyLeftListAdapter) data;
        if (adapter != null) {
            leftRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            leftRecyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            leftRecyclerView.setAdapter(adapter);
            if (selectPosition == 0) {
                List<ComicClassifyBean> list = adapter.getData();
                getPresenter().updateRightList(list, selectPosition);
            }
        }

    }

    @Override
    public <T> void onRightAdapter(T data) {
        if (data != null) {
            ComicClassifyRightListAdapter adapter = (ComicClassifyRightListAdapter) data;
            rightRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rightRecyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            rightRecyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void setProgress(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        int tag = (int) view.getTag();
        if (tag == TAG_LEFT) {
            final List<ComicClassifyBean> data = adapter.getData();
            if (selectPosition != position) {
                data.get(selectPosition).setSelect(false);
                data.get(position).setSelect(true);
                selectPosition = position;
                adapter.notifyDataSetChanged();
                getPresenter().updateRightList(data, position);
            }
        } else {
            ComicClassifyBean.ClassifyType type = (ComicClassifyBean.ClassifyType) adapter.getData().get(position);
            ComicTypeActivity.startActivity(getActivity(), type.getTitle(), type.getTag());
        }
    }

    @Override
    public void onRefresh() {
        selectPosition = 0;
        getPresenter().initData();
    }
}
