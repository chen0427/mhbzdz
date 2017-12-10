package com.chenzhipeng.mhbzdz.presenter.joke;

import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.adapter.NeiHanAdapter;
import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;
import com.chenzhipeng.mhbzdz.retrofit.joke.JokeBeanService;
import com.chenzhipeng.mhbzdz.fragment.joke.JokeTypeFragment;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.JokeApiUtils;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.view.joke.IJokeTypeView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/8.
 */

public class JokeTypePresenter {
    private IJokeTypeView iJokeTypeView;
    private Fragment fragment;
    private NeiHanAdapter neiHanAdapter;
    private int refreshFlag = 0;
    private int refreshFlagPosition = 0;

    public JokeTypePresenter(Fragment fragment) {
        this.fragment = fragment;
        this.iJokeTypeView = (IJokeTypeView) fragment;
    }

    /**
     * 获取数据
     *
     * @param type
     */
    public void initData(final int type) {
        RetrofitHelper.getInstance()
                .create(JokeBeanService.class)
                .get(JokeApiUtils.getApi(type))
                .compose(((JokeTypeFragment) fragment).<JokeBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JokeBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        iJokeTypeView.setProgress(true);
                    }

                    @Override
                    public void onNext(@NonNull final JokeBean jokeBean) {
                        if (jokeBean != null) {
                            removeAd(jokeBean);
                        } else {
                            iJokeTypeView.onEmptyData();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iJokeTypeView.onFail(e);
                        iJokeTypeView.setProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        iJokeTypeView.setProgress(false);
                    }
                });
    }

    /**
     * 移除广告
     *
     * @param jokeBean
     */
    private void removeAd(final JokeBean jokeBean) {
        if (jokeBean == null) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<JokeBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<JokeBean> e) throws Exception {
                List<JokeBean.Data.Dates> newDates = new ArrayList<>();
                JokeBean.Data data = jokeBean.getData();
                if (data == null) {
                    return;
                }
                List<JokeBean.Data.Dates> dates = data.getDates();
                if (!EmptyUtils.isListsEmpty(dates)) {
                    for (int i = 0; i < dates.size(); i++) {
                        JokeBean.Data.Dates jokeData = dates.get(i);
                        if (jokeData != null) {
                            String type = jokeData.getType();
                            if (Integer.parseInt(type) == 1) {
                                //不是广告
                                newDates.add(jokeData);
                            }
                        }
                    }
                    data.setDates(newDates);
                }
                e.onNext(jokeBean);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JokeBean>() {
                    @Override
                    public void accept(JokeBean bean) throws Exception {
                        setData(bean);
                    }
                });
    }

    private void setData(JokeBean bean) {
        if (bean != null) {
            JokeBean.Data data = bean.getData();
            if (data != null) {
                List<JokeBean.Data.Dates> dates = data.getDates();
                if (!EmptyUtils.isListsEmpty(dates)) {
                    if (neiHanAdapter == null) {
                        neiHanAdapter = new NeiHanAdapter(fragment.getActivity(), dates);
                        iJokeTypeView.onAdapter(neiHanAdapter);
                    } else {
                        refreshFlag++;
                        //移除上一次数据 "上次看到这里 点击刷新"
                        if (refreshFlag > 1) {
                            neiHanAdapter.remove(refreshFlagPosition);
                            refreshFlag--;
                        }
                        //在新增加的数据后面  添加 "上次看到这里 点击刷新"
                        JokeBean.Data.Dates showRefresh = new JokeBean.Data.Dates();
                        showRefresh.setShowRefresh(true);
                        dates.add(showRefresh);
                        refreshFlagPosition = dates.size() - 1;
                        neiHanAdapter.addData(0, dates);
                        iJokeTypeView.setRecyclerViewTop();
                    }
                }
            }
        }
    }
}
