package com.chenzhipeng.mhbzdz.presenter.comic;

import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicClassifyLeftListAdapter;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicClassifyRightListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicClassifyBean;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicClassifyService;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.HttpCacheUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicClassifyView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


@SuppressWarnings("unchecked")
public class ComicClassifyPresenter {
    private IComicClassifyView iComicClassifyView;
    private Fragment fragment;
    private ComicClassifyRightListAdapter rightAdapter;
    private List<ComicClassifyBean.ClassifyType> classifyTypes;
    private ComicClassifyLeftListAdapter leftAdapter;

    public ComicClassifyPresenter(Fragment fragment) {
        this.iComicClassifyView = (IComicClassifyView) fragment;
        this.fragment = fragment;
    }

    public void initData() {
        retrofit();
    }

    private void retrofit() {
        //缓存
        Object httpCache = HttpCacheUtils.getHttpCache(ComicApiUtils.getClassify());
        if (httpCache != null) {
            iComicClassifyView.setProgress(false);
            if (leftAdapter == null) {
                leftAdapter = new ComicClassifyLeftListAdapter(R.layout.itemview_classify_left, (List<ComicClassifyBean>) httpCache);
                iComicClassifyView.onLeftAdapter(leftAdapter);
            } else {
                leftAdapter.setNewData((List<ComicClassifyBean>) httpCache);
            }
            return;
        }
        //----------------------
        RetrofitHelper.getInstance()
                .create(ComicClassifyService.class)
                .get(ComicApiUtils.getClassify())
                .compose(((RxFragment) fragment).<ResponseBody>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull ResponseBody responseBody) throws Exception {
                        List<ComicClassifyBean> beanList = ComicClassifyBean.getBeanList(responseBody.string());
                        return Observable.just(beanList);
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        iComicClassifyView.setProgress(true);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        List<ComicClassifyBean> beanList = (List<ComicClassifyBean>) o;
                        if (!EmptyUtils.isListsEmpty(beanList)) {
                            //缓存
                            HttpCacheUtils.addHttpCache(ComicApiUtils.getClassify(), beanList);
                            if (leftAdapter == null) {
                                leftAdapter = new ComicClassifyLeftListAdapter(R.layout.itemview_classify_left, beanList);
                                iComicClassifyView.onLeftAdapter(leftAdapter);
                            } else {
                                leftAdapter.setNewData(beanList);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iComicClassifyView.onFail(e);
                        iComicClassifyView.setProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        iComicClassifyView.setProgress(false);
                    }
                });
    }


    public void updateRightList(final List<ComicClassifyBean> beanList, final int position) {
        if (!EmptyUtils.isListsEmpty(beanList)) {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    classifyTypes = beanList.get(position).getClassifyTypes();
                    e.onNext(true);
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if (rightAdapter == null) {
                                rightAdapter = new ComicClassifyRightListAdapter(R.layout.itemview_classfy_right, classifyTypes);
                                iComicClassifyView.onRightAdapter(rightAdapter);
                            } else {
                                rightAdapter.setNewData(classifyTypes);
                            }
                        }
                    });
        }
    }

}
