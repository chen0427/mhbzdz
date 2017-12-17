package com.chenzhipeng.mhbzdz.presenter.comic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicDetailsViewPagerAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDetailsBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicChapterFragment;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicIntroduceFragment;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicDetailsService;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicDetailsView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class ComicDetailsPresenter {
    private IComicDetailsView detailsView;
    private ComicDetailsActivity activity;
    private ComicChapterFragment chapterFragment;
    private ComicIntroduceFragment introduceFragment;
    private String comicId;
    private String comicName;

    public ComicDetailsPresenter(ComicDetailsActivity activity) {
        this.detailsView = activity;
        this.activity = activity;
        init();
    }

    /**
     * 初始化toolbar fab 显示or隐藏 颜色
     */
    private void init() {
        detailsView.setToolbar();
        //主题颜色设置
        setPageSelected(0);
    }

    public void updateViewPager() {
        ComicItemBean comicItemBean = (ComicItemBean) SuperIntent.getInstance().get(SuperIntent.S14);
        if (comicItemBean != null) {
            comicId = comicItemBean.getComicId();
            detailsView.onTopImgUrl(ComicApiUtils.getComicImg(comicId));
            //此漫画有收藏or没有收藏
            detailsView.setCollectionStatus(ComicDatabase.getInstance().isCollection(comicId));
            retrofit(comicId);
        }
    }

    private void retrofit(final String comicId) {
        if (!TextUtils.isEmpty(comicId)) {
            RetrofitHelper.getInstance().create(ComicDetailsService.class)
                    .get(ComicApiUtils.getDetails(comicId))
                    .compose((activity).<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull ResponseBody responseBody) throws Exception {
                            return Observable.just(ComicDetailsBean.getInstance(responseBody.string(), comicId));
                        }
                    })
                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            detailsView.setShowProgress(true);
                        }

                        @Override
                        public void onNext(@NonNull Object o) {
                            ComicDetailsBean detailsBean = (ComicDetailsBean) o;
                            if (detailsBean != null) {
                                comicName = detailsBean.getComicName();
                                detailsView.onTitleName(comicName);
                                List<String> titleList = new ArrayList<>();
                                List<Fragment> fragmentList = new ArrayList<>();
                                titleList.add(activity.getString(R.string.introduce));
                                titleList.add(activity.getString(R.string.chapter));

                                introduceFragment = new ComicIntroduceFragment();
                                introduceFragment.setArguments(getBundle(detailsBean));
                                chapterFragment = new ComicChapterFragment();
                                chapterFragment.setArguments(getBundle(detailsBean));
                                fragmentList.add(introduceFragment);
                                fragmentList.add(chapterFragment);
                                FragmentManager manager = ((RxAppCompatActivity) activity).getSupportFragmentManager();
                                detailsView.onAdapter(new ComicDetailsViewPagerAdapter(manager, titleList, fragmentList));
                            } else {
                                detailsView.onEmptyData();
                            }
                        }

                        @Override
                        public void onError(@NonNull final Throwable e) {
                            detailsView.setShowProgress(false);
                            detailsView.onFail(e);
                        }

                        @Override
                        public void onComplete() {
                            detailsView.setShowProgress(false);
                        }
                    });
        }

    }

    private <T extends Serializable> Bundle getBundle(T data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ComicDetailsActivity.KEY_BUNDLE, data);
        return bundle;
    }

    /**
     * 收藏漫画
     */
    private void saveCollection() {
        if (ComicDatabase.getInstance().insertCollection(comicId, comicName)) {
            detailsView.setCollectionStatus(true);
            detailsView.onCollectionToast("收藏成功");
        }
    }

    /**
     * 取消收藏
     */
    private void cancelCollection() {
        if (ComicDatabase.getInstance().deleteCollection(comicId)) {
            detailsView.setCollectionStatus(false);
            detailsView.onCollectionToast("取消收藏");
        }
    }


    public void onClickCollection(boolean b) {
        if (b) {
            cancelCollection();
        } else {
            saveCollection();
        }
    }


    /**
     * 续看按钮隐藏or显示
     */
    public void setPageSelected(int position) {
        detailsView.setFabVisibility(position == 1);
    }


    /**
     * 跳到续看页
     */
    public void goXk() {
        if (chapterFragment != null) {
            chapterFragment.startActivityToHistory();
        }
    }

    /**
     * 跳到下载界面
     */
    public void startComicDownloadListActivity() {
        if (chapterFragment != null) {
            chapterFragment.startDownloadListActivity();
        }
    }

    public void startLast() {
        if (chapterFragment != null) {
            chapterFragment.startLast();
        }
    }

    public String getUpdateTime() {
        if (introduceFragment != null) {
            return introduceFragment.getUpdateTime();
        }
        return null;
    }
}