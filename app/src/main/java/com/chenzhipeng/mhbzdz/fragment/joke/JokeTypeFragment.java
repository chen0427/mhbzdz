package com.chenzhipeng.mhbzdz.fragment.joke;


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
import com.chenzhipeng.mhbzdz.adapter.NeiHanAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;
import com.chenzhipeng.mhbzdz.presenter.joke.JokeTypePresenter;
import com.chenzhipeng.mhbzdz.comment.CommentsDialogHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.JokeApiUtils;
import com.chenzhipeng.mhbzdz.utils.VideoUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokeTypeView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;

public class JokeTypeFragment extends BaseFragment implements
        IJokeTypeView,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener, RecyclerView.OnChildAttachStateChangeListener {
    @BindView(R.id.rv_fragment_text)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;
    private JokeTypePresenter jokeTypePresenter;
    private View fragmentView;

    private boolean isCreateActivity = false;
    private boolean isVisible = false;

    public JokeTypePresenter getJokeTypePresenter() {
        if (jokeTypePresenter == null) {
            jokeTypePresenter = new JokeTypePresenter(this);
        }
        return jokeTypePresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_joke_type, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreateActivity = true;
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnChildAttachStateChangeListener(this);
        if (isVisible) {
            initData();
        }
    }

    private void initData() {
        int type = getArguments().getInt(JokeIndexFragment.BUNDLE_KEY);
        getJokeTypePresenter().initData(type == 0 ? JokeApiUtils.TEXT : type);
    }

    /**
     * 让fragment可见时才去网络获取数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isCreateActivity) {
            BaseQuickAdapter adapter = (BaseQuickAdapter) recyclerView.getAdapter();
            if (adapter == null || EmptyUtils.isListsEmpty(adapter.getData())) {
                initData();
            }
        } else {
            VideoUtils.pause();
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }


    @Override
    public <T> void onAdapter(T data) {
        NeiHanAdapter adapter = (NeiHanAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemChildClickListener(this);
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
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void setRecyclerViewTop() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<JokeBean.Data.Dates> dates = adapter.getData();
        if (dates.size() == 0) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_refresh:
                initData();
                break;
            case R.id.cv_itemJoke:
                //list item
                break;
            case R.id.ll_commentJoke:
                String groupId1 = dates.get(position).getGroup().getGroupId();
                CommentsDialogHelper.getCommentsDialogHelper().show((RxAppCompatActivity) getActivity(), groupId1);
                break;
            case R.id.ll_hotcommentJoke:
                String groupId2 = dates.get(position).getGroup().getGroupId();
                CommentsDialogHelper.getCommentsDialogHelper().show((RxAppCompatActivity) getActivity(), groupId2);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnChildAttachStateChangeListener(this);
        removeFragmentView(fragmentView);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {

    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        //判断销毁的view是否在播放视频
        JCVideoPlayer videoPlayer = JCVideoPlayerManager.getCurrentJcvd();
        if (videoPlayer != null) {
            int currentState = videoPlayer.currentState;
            JCVideoPlayer viewById = view.findViewById(R.id.jc_videoJoke);
            if (viewById != null) {
                if (viewById.equals(videoPlayer) && currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                    JCVideoPlayer.releaseAllVideos();
                }
            }
        }
    }
}
