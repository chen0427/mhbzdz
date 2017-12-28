package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.os.Bundle;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperClassifyActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperClassifyFragment;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.wallpaper.WallpaperBeanService;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.HttpCacheUtils;
import com.chenzhipeng.mhbzdz.utils.WallpaperApiUtils;
import com.chenzhipeng.mhbzdz.utils.WallpaperChangeUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperClassifyView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("unchecked")
public class WallpaperClassifyPresenter {
    private IWallpaperClassifyView classifyView;
    private WallpaperClassifyFragment fragment;
    private WallpaperListAdapter adapter;

    public WallpaperClassifyPresenter(WallpaperClassifyFragment fragment) {
        this.classifyView = fragment;
        this.fragment = fragment;
    }


    public void initData(int skip, boolean isLoadMore) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            return;
        }
        String id = arguments.getString(WallpaperClassifyActivity.KEY_BUNDLE_1);
        int type = arguments.getInt(WallpaperClassifyActivity.KEY_BUNDLE_2);
        if (TextUtils.isEmpty(id) || type == 0) {
            return;
        }
        if (type == WallpaperClassifyActivity.TYPE_NEW) {
            //最新
            String url = WallpaperApiUtils.getClassifyNewContent(id, skip);
            retrofit(url, isLoadMore);
        } else {
            //热门
            String url = WallpaperApiUtils.getClassifyHotContent(id, skip);
            retrofit(url, isLoadMore);
        }
    }

    private void retrofit(final String url, final boolean isLoadMore) {
        if (!TextUtils.isEmpty(url)) {
            //缓存
            Object httpCache = HttpCacheUtils.getHttpCache(url);
            if (httpCache != null) {
                classifyView.setProgress(false);
                if (adapter == null) {
                    adapter = new WallpaperListAdapter(R.layout.itemview_wallpaper_item, (List<WallpaperItemBean>) httpCache);
                    classifyView.onAdapter(adapter);
                } else {
                    adapter.setNewData((List<WallpaperItemBean>) httpCache);
                }
                return;
            }
            //----------------------
            RetrofitHelper.getInstance()
                    .create(WallpaperBeanService.class)
                    .get(url)
                    .compose(fragment.<WallpaperBean>bindToLifecycle())
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
                        classifyView.setProgress(true);
                    }
                }

                @Override
                public void onNext(@NonNull Object o) {
                    List<WallpaperItemBean> beanList = (List<WallpaperItemBean>) o;
                    if (isLoadMore) {
                        setLoadMore(beanList);
                    } else {
                        if (!EmptyUtils.isListsEmpty(beanList)) {
                            //缓存
                            HttpCacheUtils.addHttpCache(url, beanList);
                            if (adapter == null) {
                                adapter = new WallpaperListAdapter(R.layout.itemview_wallpaper_item, beanList);
                                classifyView.onAdapter(adapter);
                            } else {
                                adapter.setNewData(beanList);
                            }
                        } else {
                            classifyView.onEmptyData();
                        }
                    }

                }

                @Override
                public void onError(@NonNull Throwable e) {
                    classifyView.setProgress(false);
                    if (isLoadMore && adapter != null) {
                        adapter.loadMoreFail();
                    } else {
                        classifyView.onFail(e);
                    }
                }

                @Override
                public void onComplete() {
                    classifyView.setProgress(false);
                }
            });
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

}
