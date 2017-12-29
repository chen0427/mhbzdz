package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.os.Bundle;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperClassifyListAdapter;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyItemBean;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperIndexFragment;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperTypeFragment;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.wallpaper.WallpaperBeanService;
import com.chenzhipeng.mhbzdz.retrofit.wallpaper.WallpaperClassifyBeanService;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.HttpCacheUtils;
import com.chenzhipeng.mhbzdz.utils.WallpaperApiUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperTypeView;

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
public class WallpaperTypePresenter {
    private IWallpaperTypeView view;
    private WallpaperTypeFragment fragment;
    private WallpaperListAdapter listAdapter;
    private WallpaperClassifyListAdapter classifyListAdapter;

    public WallpaperTypePresenter(WallpaperTypeFragment fragment) {
        this.view = fragment;
        this.fragment = fragment;
    }


    public void initData(int skip, boolean isLoadMore) {
        int type = getType();
        if (type == WallpaperIndexFragment.TYPE_CLASSIFY) {
            getClassify(WallpaperApiUtils.getBigClassify());
        } else if (type == WallpaperIndexFragment.TYPE_HOT) {
            getHot(skip, isLoadMore);
        } else if (type == WallpaperIndexFragment.TYPE_NEW) {
            getNew(skip, isLoadMore);
        }
    }

    private void getHot(int skip, boolean isLoadMore) {
        retrofit(WallpaperApiUtils.getHot(skip), isLoadMore);
    }

    private void getNew(int skip, boolean isLoadMore) {
        retrofit(WallpaperApiUtils.getNew(skip), isLoadMore);
    }

    private void retrofit(final String url, final boolean isLoadMore) {
        if (!TextUtils.isEmpty(url)) {
            //缓存
            Object httpCache = HttpCacheUtils.getHttpCache(url);
            if (httpCache != null) {
                view.setProgress(false);
                setData((List<WallpaperItemBean>) httpCache);
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
                            return Observable.just(WallpaperItemBean.getWallpaperItemList(bean));
                        }
                    }).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    if (!isLoadMore) {
                        view.setProgress(true);
                    }
                }

                @Override
                public void onNext(@NonNull Object o) {
                    List<WallpaperItemBean> beanList = (List<WallpaperItemBean>) o;
                    if (isLoadMore) {
                        setLoadMore(beanList);
                    } else {
                        //缓存
                        HttpCacheUtils.addHttpCache(url, beanList);
                        setData(beanList);
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    view.setProgress(false);
                    if (isLoadMore && listAdapter != null) {
                        listAdapter.loadMoreFail();
                    } else {
                        view.onFail(e);
                    }
                }

                @Override
                public void onComplete() {
                    view.setProgress(false);
                }
            });
        }
    }

    private void setLoadMore(List<WallpaperItemBean> beanList) {
        if (listAdapter != null) {
            if (!EmptyUtils.isListsEmpty(beanList)) {
                listAdapter.addData(beanList);
                listAdapter.loadMoreComplete();
            } else {
                listAdapter.loadMoreEnd();
            }
        }
    }

    private void setData(List<WallpaperItemBean> beanList) {
        if (!EmptyUtils.isListsEmpty(beanList)) {
            if (listAdapter == null) {
                listAdapter = new WallpaperListAdapter(R.layout.itemview_wallpaper_item, beanList);
                view.onListAdapter(listAdapter);
            } else {
                listAdapter.setNewData(beanList);
            }
        } else {
            view.onEmptyData();
        }
    }

    private void getClassify(final String url) {
        if (!TextUtils.isEmpty(url)) {
            //缓存
            Object httpCache = HttpCacheUtils.getHttpCache(url);
            if (httpCache != null) {
                view.setProgress(false);
                if (classifyListAdapter == null) {
                    classifyListAdapter = new WallpaperClassifyListAdapter(R.layout.itemview_wallpaper_classify, (List<WallpaperClassifyItemBean>) httpCache);
                    view.onClassifyAdapter(classifyListAdapter);
                } else {
                    classifyListAdapter.setNewData((List<WallpaperClassifyItemBean>) httpCache);
                }
                return;
            }
            //---------------------------
            RetrofitHelper.getInstance()
                    .create(WallpaperClassifyBeanService.class)
                    .get(url)
                    .compose(fragment.<WallpaperClassifyBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Function<WallpaperClassifyBean, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull WallpaperClassifyBean bean) throws Exception {
                            return Observable.just(WallpaperItemBean.getWallpaperClassifyItemList(bean));
                        }
                    }).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    view.setProgress(true);
                }

                @Override
                public void onNext(@NonNull Object o) {
                    List<WallpaperClassifyItemBean> beanList = (List<WallpaperClassifyItemBean>) o;
                    if (!EmptyUtils.isListsEmpty(beanList)) {
                        HttpCacheUtils.addHttpCache(url, beanList);
                        if (classifyListAdapter == null) {
                            classifyListAdapter = new WallpaperClassifyListAdapter(R.layout.itemview_wallpaper_classify, beanList);
                            view.onClassifyAdapter(classifyListAdapter);
                        } else {
                            classifyListAdapter.setNewData(beanList);
                        }
                    } else {
                        view.onEmptyData();
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    view.onFail(e);
                    view.setProgress(false);
                }

                @Override
                public void onComplete() {
                    view.setProgress(false);
                }
            });
        }
    }

    public int getType() {
        int type = 1;
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            type = arguments.getInt(WallpaperIndexFragment.KEY_BUNDLE);
        }
        return type;
    }
}
