package com.chenzhipeng.mhbzdz.presenter.comic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicRecommendTagActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendSlideBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendTypeBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicRecommendService;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.HttpCacheUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicRecommendTagView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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


public class ComicRecommendTagPresenter implements OnBannerListener {
    private ComicRecommendTagActivity activity;
    private IComicRecommendTagView recommendTagView;
    private int recommendPosition = 0;
    private List<ComicRecommendSlideBean> slideBeanList;
    private ComicBookListAdapter adapter;
    private boolean isShuffle = false;

    public ComicRecommendTagPresenter(ComicRecommendTagActivity activity) {
        this.activity = activity;
        recommendTagView = activity;
    }

    public void initAdapter() {
        ComicRecommendTypeBean typeBean = (ComicRecommendTypeBean) SuperIntent.getInstance().get(SuperIntent.S2);
        recommendPosition = (int) SuperIntent.getInstance().get(SuperIntent.S3);
        if (typeBean != null) {
            recommendTagView.onTitle(typeBean.getTabTitle());
            List<ComicItemBean> itemBeanList = typeBean.getItemBeanList();
            if (!EmptyUtils.isListsEmpty(itemBeanList)) {
                if (adapter == null) {
                    adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, itemBeanList);
                    adapter.addHeaderView(getHeadView(typeBean));
                    recommendTagView.onAdapter(adapter);
                } else {
                    adapter.setNewData(itemBeanList);
                }
            } else {
                recommendTagView.onEmptyData();
            }
        }
    }

    public void updateList() {
        retrofit();
    }

    private void retrofit() {
        // 缓存
        Object httpCache = HttpCacheUtils.getHttpCache(ComicApiUtils.getRecommend());
        if (httpCache != null) {
            List<ComicRecommendTypeBean> typeBeanList = (List<ComicRecommendTypeBean>) httpCache;
            ComicRecommendTypeBean comicRecommendTypeBean = typeBeanList.get(recommendPosition);
            recommendTagView.setProgress(false);
            if (adapter == null) {
                adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, comicRecommendTypeBean.getItemBeanList());
                adapter.addHeaderView(getHeadView(comicRecommendTypeBean));
                recommendTagView.onAdapter(adapter);
            } else {
                adapter.setNewData(comicRecommendTypeBean.getItemBeanList());
            }
            return;
        }
        //--------------------------------------------------------
        RetrofitHelper.getInstance().create(ComicRecommendService.class)
                .get(ComicApiUtils.getRecommend())
                .compose((activity).<ResponseBody>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull ResponseBody responseBody) throws Exception {
                        ComicRecommendBean instance = ComicRecommendBean.getInstance(responseBody.string(), isShuffle);
                        return Observable.just(instance);
                    }
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                recommendTagView.setProgress(true);
            }

            @Override
            public void onNext(@NonNull Object o) {
                ComicRecommendBean bean = (ComicRecommendBean) o;
                if (bean != null) {
                    List<ComicRecommendTypeBean> typeBeanList = bean.getTypeBeanList();
                    if (!EmptyUtils.isListsEmpty(typeBeanList)) {
                        //缓存
                        HttpCacheUtils.addHttpCache(ComicApiUtils.getRecommend(), typeBeanList);
                        ComicRecommendTypeBean typeBean = typeBeanList.get(recommendPosition);
                        if (typeBeanList.size() >= recommendPosition + 1) {
                            if (typeBean != null) {
                                List<ComicItemBean> itemBeanList = typeBean.getItemBeanList();
                                if (!EmptyUtils.isListsEmpty(itemBeanList)) {
                                    if (adapter == null) {
                                        adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, itemBeanList);
                                        adapter.addHeaderView(getHeadView(typeBean));
                                        recommendTagView.onAdapter(adapter);
                                    } else {
                                        adapter.setNewData(itemBeanList);
                                    }
                                } else {
                                    recommendTagView.onEmptyData();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                recommendTagView.setProgress(false);
                recommendTagView.onFail(e);
            }

            @Override
            public void onComplete() {
                recommendTagView.setProgress(false);
            }
        });
    }


    private View getHeadView(ComicRecommendTypeBean typeBean) {
        if (typeBean != null) {
            List<ComicRecommendSlideBean> slideBeanList = typeBean.getSlideBeanList();
            if (!EmptyUtils.isListsEmpty(slideBeanList)) {
                this.slideBeanList = slideBeanList;
                List<String> imgUrlList = new ArrayList<>();
                List<String> descList = new ArrayList<>();
                for (ComicRecommendSlideBean slide : slideBeanList) {
                    String imgUrl = slide.getImgUrl();
                    String slideDesc = slide.getSlideDesc();
                    imgUrlList.add(imgUrl);
                    descList.add(slideDesc);
                }
                View view = LayoutInflater.from(activity).inflate(R.layout.itemview_banner, new FrameLayout(activity), false);
                view.setPadding(0, 0, 0, 30);
                Banner banner = view.findViewById(R.id.bnr_comic_item);
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(imgUrlList);
                banner.setBannerTitles(descList);
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                banner.isAutoPlay(false);
                banner.setOnBannerListener(this);
                banner.start();
                return view;
            }
        }
        return null;
    }

    @Override
    public void OnBannerClick(int position) {
        if (!EmptyUtils.isListsEmpty(slideBeanList)) {
            ComicRecommendSlideBean slideBean = slideBeanList.get(position);
            ComicItemBean bean = new ComicItemBean(slideBean.getComicId(), null, null);
            ComicDetailsActivity.startActivity(activity, bean);
        }
    }

    private class GlideImageLoader extends com.youth.banner.loader.ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageHelper.setImage(String.valueOf(path), imageView, R.color.white);
        }
    }
}
