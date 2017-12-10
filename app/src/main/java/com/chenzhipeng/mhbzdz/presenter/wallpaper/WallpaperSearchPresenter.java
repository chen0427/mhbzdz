package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperSearchDataActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.retrofit.wallpaper.WallpaperBeanService;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.utils.WallpaperApiUtils;
import com.chenzhipeng.mhbzdz.utils.WallpaperChangeUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperSearchView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/18.
 */

@SuppressWarnings("unchecked")
public class WallpaperSearchPresenter {
    private IWallpaperSearchView iWallpaperSearchView;
    private WallpaperSearchDataActivity activity;
    private WallpaperListAdapter adapter;

    public WallpaperSearchPresenter(Context context) {
        iWallpaperSearchView = (IWallpaperSearchView) context;
        activity = (WallpaperSearchDataActivity) context;
    }

    public void initData(int skip, boolean isLoadMore) {
        Intent intent = activity.getIntent();
        if (intent != null) {
            String s = intent.getStringExtra(WallpaperSearchDataActivity.KEY_INTENT);
            if (!TextUtils.isEmpty(s)) {
                iWallpaperSearchView.onTitle(s);
                retrofit(WallpaperApiUtils.getSearch(s, skip), isLoadMore);
            }
        }
    }

    private void setLoadMore(List<WallpaperItemBean> beanList) {
        if (adapter != null) {
            if (!EmptyUtils.isListsEmpty(beanList)) {
                adapter.addData(beanList);
                adapter.loadMoreComplete();
            } else {
                adapter.loadMoreEnd();
            }
        }
    }

    private void setData(List<WallpaperItemBean> beanList) {
        if (!EmptyUtils.isListsEmpty(beanList)) {
            if (adapter == null) {
                adapter = new WallpaperListAdapter(R.layout.itemview_wallpaper_item, beanList);
                iWallpaperSearchView.onAdapter(adapter);
            } else {
                adapter.setNewData(beanList);
            }
        } else {
            iWallpaperSearchView.onEmptyData();
        }
    }


    private void retrofit(String url, final boolean isLoadMore) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        RetrofitHelper.getInstance()
                .create(WallpaperBeanService.class)
                .get(url)
                .compose(activity.<WallpaperBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<WallpaperBean, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull WallpaperBean bean) throws Exception {
                        return Observable.just(WallpaperChangeUtils.getWallpaperItemBeen(bean));
                    }
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (!isLoadMore) {
                    iWallpaperSearchView.setProgress(true);
                }
            }

            @Override
            public void onNext(@NonNull Object o) {
                List<WallpaperItemBean> beanList = (List<WallpaperItemBean>) o;
                if (isLoadMore) {
                    setLoadMore(beanList);
                } else {
                    setData(beanList);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                iWallpaperSearchView.setProgress(false);
                iWallpaperSearchView.onFail(e);
                if (isLoadMore && adapter != null) {
                    adapter.loadMoreFail();
                }
            }

            @Override
            public void onComplete() {
                iWallpaperSearchView.setProgress(false);
            }
        });
    }
}
