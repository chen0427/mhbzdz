package com.chenzhipeng.mhbzdz.presenter.comic;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicRecommendListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendSlideBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendTypeBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicRecommendFragment;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicRecommendService;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicRecommendView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
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

/**
 * Created by Administrator on 2017/8/29.
 */
@SuppressWarnings("unchecked")
public class ComicRecommendPresenter implements OnBannerListener {
    private IComicRecommendView recommendView;
    private ComicRecommendFragment fragment;
    private ComicRecommendListAdapter adapter;
    /**
     * 是否打乱内容顺序
     */
    private boolean isShuffle = true;

    private List<String> comicIdList;

    public ComicRecommendPresenter(ComicRecommendFragment fragment) {
        this.recommendView = fragment;
        this.fragment = fragment;
    }


    public void initData() {
        retrofit();
    }


    private void retrofit() {
        RetrofitHelper.getInstance()
                .create(ComicRecommendService.class)
                .get(ComicApiUtils.getRecommend())
                .compose(fragment.<ResponseBody>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull ResponseBody responseBody) throws Exception {
                        ComicRecommendBean bean = ComicRecommendBean.getInstance(responseBody.string(), isShuffle);
                        return Observable.just(bean);
                    }
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                recommendView.setProgress(true);
            }

            @Override
            public void onNext(@NonNull Object o) {
                ComicRecommendBean bean = (ComicRecommendBean) o;
                if (bean != null) {
                    List<ComicRecommendTypeBean> typeBeanList = bean.getTypeBeanList();
                    if (!EmptyUtils.isListsEmpty(typeBeanList)) {
                        if (adapter == null) {
                            setAdapter(typeBeanList);
                        } else {
                            adapter.setNewData(typeBeanList);
                        }
                    } else {
                        recommendView.onEmptyData();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                recommendView.setProgress(false);
                recommendView.onFail(e);
            }

            @Override
            public void onComplete() {
                recommendView.setProgress(false);
            }
        });
    }


    public void setAdapter(final List<ComicRecommendTypeBean> typeBeanList) {
        if (!EmptyUtils.isListsEmpty(typeBeanList)) {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    Object[] data = getHeadViewData(typeBeanList);
                    if (data != null) {
                        e.onNext(data);
                    }
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Object[] data = (Object[]) o;
                            View headView = getHeadView(data);
                            if (headView != null) {
                                adapter = new ComicRecommendListAdapter(fragment.getActivity(),
                                        R.layout.itemview_comic_recommend, typeBeanList);
                                adapter.addHeaderView(headView);
                                recommendView.onAdapter(adapter);
                            }
                        }
                    });


        }
    }

    private Object[] getHeadViewData(List<ComicRecommendTypeBean> typeBeanList) {
        if (!EmptyUtils.isListsEmpty(typeBeanList)) {
            List<String> imgList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            comicIdList = new ArrayList<>();
            for (ComicRecommendTypeBean c : typeBeanList) {
                List<ComicRecommendSlideBean> slideBeanList = c.getSlideBeanList();
                for (ComicRecommendSlideBean s : slideBeanList) {
                    imgList.add(s.getImgUrl());
                    titleList.add(s.getSlideDesc());
                    comicIdList.add(s.getComicId());
                }
            }
            return new Object[]{imgList, titleList};
        }
        return null;
    }

    private View getHeadView(Object[] objects) {
        if (objects != null && objects.length > 0) {
            FragmentActivity activity = fragment.getActivity();
            View view = LayoutInflater.from(activity).inflate(R.layout.itemview_banner, new FrameLayout(activity), false);
            List<String> imgList = (List<String>) objects[0];
            List<String> titleList = (List<String>) objects[1];
            Banner banner = view.findViewById(R.id.bnr_comic_item);
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(imgList);
            banner.setBannerTitles(titleList);
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
            banner.isAutoPlay(false);
            banner.setOnBannerListener(this);
            banner.start();
            return view;
        }
        return null;
    }

    @Override
    public void OnBannerClick(int position) {
        if (!EmptyUtils.isListsEmpty(comicIdList)) {
            String comicId = comicIdList.get(position);
            ComicItemBean bean = new ComicItemBean(comicId, null, null);
            ComicDetailsActivity.startActivity(fragment.getActivity(), bean);
        }
    }

    private class GlideImageLoader extends com.youth.banner.loader.ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageHelper.setImage(String.valueOf(path), imageView, R.color.white);
        }
    }
}
