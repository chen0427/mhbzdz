package com.chenzhipeng.mhbzdz.presenter.comic;

import android.view.Menu;
import android.view.MenuItem;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDownloadChoiceActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDownloadDataActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicDownloadListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterTypeBean;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.sqlite.AppDatabase;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicDownloadChoiceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ComicDownloadChoicePresenter {
    private String comicId;
    private IComicDownloadChoiceView downloadChoiceView;
    private ComicDownloadChoiceActivity activity;
    private ComicDownloadListAdapter adapter;
    private List<ComicChapterItemBean> comicChapterItemBeanList;
    private List<ComicChapterItemBean> checkedComicChapterItemBeanList;

    public ComicDownloadChoicePresenter(ComicDownloadChoiceActivity activity) {
        this.downloadChoiceView = activity;
        this.activity = activity;
        this.comicChapterItemBeanList = new ArrayList<>();
        this.checkedComicChapterItemBeanList = new ArrayList<>();
    }


    private void setChecked(List<ComicChapterItemBean> comicChapterItemBeanList) {
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
            for (ComicChapterItemBean c : comicChapterItemBeanList) {
                String chapterName = c.getChapterName();
                String comicId = c.getComicId();
                if (AppDatabase.getInstance().isExistDownloadData(comicId, chapterName)) {
                    c.setChecked(true);
                    c.setEnabled(false);
                } else {
                    c.setChecked(false);
                    c.setEnabled(true);
                }
            }
        }
    }


    public void init() {
        downloadChoiceView.setProgress(true);
        checkedComicChapterItemBeanList.clear();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                ComicChapterTypeBean typeBean = (ComicChapterTypeBean) SuperIntent.getInstance().get(SuperIntent.S1);
                if (typeBean != null) {
                    comicId = typeBean.getComicId();
                    comicChapterItemBeanList = typeBean.getChapterItemBeanList();
                    setChecked(comicChapterItemBeanList);
                }
                e.onNext(comicChapterItemBeanList);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        downloadChoiceView.setProgress(false);
                        comicChapterItemBeanList = (List<ComicChapterItemBean>) o;
                        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
                            if (adapter == null) {
                                adapter = new ComicDownloadListAdapter(R.layout.itemview_comic_download_list, comicChapterItemBeanList);
                                downloadChoiceView.onAdapter(adapter);
                            } else {
                                adapter.setNewData(comicChapterItemBeanList);
                            }
                        } else {
                            downloadChoiceView.onEmptyData();
                        }
                    }
                });
    }


    public void updateMenu(Menu menu) {
        if (menu != null && menu.size() == 3) {
            MenuItem item = menu.getItem(2);
            item.setTitle(isAllSelect() ? activity.getString(R.string.select_cancel) : activity.getString(R.string.select));
        }
    }

    public void clickMenuAllSelect() {
        if (adapter != null) {
            setAllSelect(!isAllSelect());
            adapter.notifyDataSetChanged();
            activity.invalidateOptionsMenu();
        }
    }

    public void clickMenuSort() {
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList) && adapter != null) {
            Collections.reverse(comicChapterItemBeanList);
            if (!EmptyUtils.isListsEmpty(checkedComicChapterItemBeanList)) {
                Collections.reverse(checkedComicChapterItemBeanList);
            }
            adapter.notifyDataSetChanged();
        }
    }


    private boolean isAllSelect() {
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
            for (ComicChapterItemBean bean : comicChapterItemBeanList) {
                if (!bean.isChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void setAllSelect(boolean b) {
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
            for (ComicChapterItemBean bean : comicChapterItemBeanList) {
                if (bean.isEnabled()) {
                    bean.setChecked(b);
                    checked(bean, b);
                }
            }
        }
    }

    public void checked(ComicChapterItemBean bean, boolean isChecked) {
        if (bean != null) {
            if (isChecked) {
                checkedComicChapterItemBeanList.add(bean);
            } else {
                checkedComicChapterItemBeanList.remove(bean);
            }
        }
    }


    public void startComicDownloadChoiceActivity() {
        if (!EmptyUtils.isListsEmpty(checkedComicChapterItemBeanList)) {
            ComicDownloadDataActivity.startActivity(activity, comicId, checkedComicChapterItemBeanList);
        } else if (AppDatabase.getInstance().isExistDownloadData(comicId)) {
            ComicDownloadDataActivity.startActivity(activity, comicId);
        }
    }
}
