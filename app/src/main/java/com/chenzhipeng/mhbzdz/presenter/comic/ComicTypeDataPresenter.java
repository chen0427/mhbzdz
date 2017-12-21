package com.chenzhipeng.mhbzdz.presenter.comic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicTypeActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicTypeFragment;
import com.chenzhipeng.mhbzdz.retrofit.RetrofitHelper;
import com.chenzhipeng.mhbzdz.retrofit.comic.ComicTypeService;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicTypeDataView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


@SuppressWarnings("unchecked")
public class ComicTypeDataPresenter {
    private Fragment fragment;
    private IComicTypeDataView dataView;
    private ComicBookListAdapter adapter;

    public ComicTypeDataPresenter(Fragment fragment) {
        this.fragment = fragment;
        this.dataView = (IComicTypeDataView) fragment;
    }

    //-------------------------------------------------

    public void initData(int page, boolean isLoadMore) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) {
            return;
        }
        String tag = arguments.getString(ComicTypePresenter.KEY_BUNDLE_1);
        String orderby = arguments.getString(ComicTypePresenter.KEY_BUNDLE_2);
        retrofit(tag, orderby, page, isLoadMore);
    }


    private void retrofit(String tag, String orderby, int page, final boolean isLoadMore) {
        if (!EmptyUtils.isStringsEmpty(tag, orderby)) {
            String url;
            if (ComicTypeActivity.isSearch) {
                url = ComicApiUtils.getSearch(tag, orderby, page);
            } else {
                url = ComicApiUtils.getType(tag, orderby, page);
            }
            RetrofitHelper.getInstance()
                    .create(ComicTypeService.class)
                    .get(url)
                    .compose(((ComicTypeFragment) fragment).<ResponseBody>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull ResponseBody responseBody) throws Exception {
                            return Observable.just(getComicItemBeen(responseBody.string()));
                        }
                    }).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    if (!isLoadMore) {
                        dataView.setProgress(true);
                    }
                }

                @Override
                public void onNext(@NonNull Object o) {
                    List<ComicItemBean> list = (List<ComicItemBean>) o;
                    if (isLoadMore) {
                        if (adapter != null) {
                            if (!EmptyUtils.isListsEmpty(list)) {
                                adapter.addData(list);
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }
                    } else {
                        if (!EmptyUtils.isListsEmpty(list)) {
                            if (adapter == null) {
                                adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, list);
                                dataView.onAdapter(adapter);
                            } else {
                                adapter.setNewData(list);
                            }
                        } else {
                            dataView.onEmptyData();
                        }
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    if (isLoadMore) {
                        adapter.loadMoreFail();
                    } else {
                        dataView.onFail(e);
                        dataView.setProgress(false);
                    }
                }

                @Override
                public void onComplete() {
                    if (!isLoadMore) {
                        dataView.setProgress(false);
                    }
                }
            });
        }
    }


    private List<ComicItemBean> getComicItemBeen(String str) {
        List<ComicItemBean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String comicId = object.getString("comic_id");
                        String comicName = object.getString("comic_name");
                        String lastChapterName = object.getString("last_chapter_name");
                        ComicItemBean bean = new ComicItemBean(comicId, comicName, lastChapterName);
                        list.add(bean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
