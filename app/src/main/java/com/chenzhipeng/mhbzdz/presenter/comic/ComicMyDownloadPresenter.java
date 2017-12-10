package com.chenzhipeng.mhbzdz.presenter.comic;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicMyBookActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.download.ComicDownloaderManager;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicMyDownloadFragment;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.document.ComicDocumentHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicMyDownloadView;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/12.
 */

public class ComicMyDownloadPresenter {
    private IComicMyDownloadView iComicMyDownloadView;
    private ComicBookListAdapter adapter;
    private List<ComicItemBean> comicItemBeanList;
    private ComicMyDownloadFragment fragment;

    public ComicMyDownloadPresenter(ComicMyDownloadFragment fragment) {
        this.iComicMyDownloadView = fragment;
        this.fragment = fragment;
    }


    public void initData() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                List<ComicItemBean> comicItemBeanList = ComicDatabase.getInstance().getDownloadBook();
                e.onNext(comicItemBeanList);
            }
        }).compose(fragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        comicItemBeanList = (List<ComicItemBean>) o;
                        if (adapter == null) {
                            adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, comicItemBeanList);
                            iComicMyDownloadView.onAdapter(adapter);
                        } else {
                            adapter.setNewData(comicItemBeanList);
                        }
                    }
                });


    }

    public void setAllChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(comicItemBeanList)) {
            for (ComicItemBean c : comicItemBeanList) {
                c.setChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void setShowChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(comicItemBeanList)) {
            for (ComicItemBean c : comicItemBeanList) {
                c.setShowChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isShowChecked() {
        if (!EmptyUtils.isListsEmpty(comicItemBeanList)) {
            for (ComicItemBean c : comicItemBeanList) {
                if (!c.isShowChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isAllChecked() {
        if (!EmptyUtils.isListsEmpty(comicItemBeanList)) {
            for (ComicItemBean c : comicItemBeanList) {
                if (!c.isChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    private boolean isCanDelete() {
        if (!EmptyUtils.isListsEmpty(comicItemBeanList)) {
            for (ComicItemBean c : comicItemBeanList) {
                if (c.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void showDeleteDialog() {
        if (isCanDelete()) {
            iComicMyDownloadView.showDeleteDialog();
        }
    }

    public void delete() {
        if (!EmptyUtils.isListsEmpty(comicItemBeanList) && adapter != null) {
            Iterator<ComicItemBean> iterator = comicItemBeanList.iterator();
            while (iterator.hasNext()) {
                ComicItemBean next = iterator.next();
                if (next.isChecked()) {
                    if (ComicDatabase.getInstance().deleteDownloadBook(next.getComicId())) {
                        ComicDownloaderManager.getInstance().remove(next.getComicId());
                        ComicDocumentHelper.getInstance().deleteBook(next.getComicId(), next.getComicName());
                        iterator.remove();
                    }
                }
            }
            adapter.notifyDataSetChanged();
            closeMenu();
        }
    }

    public boolean closeMenu() {
        if (isShowChecked()) {
            setShowChecked(false);
            invalidateOptionsMenu();
            return true;
        }
        return false;
    }

    private void invalidateOptionsMenu() {
        ComicMyBookActivity activity = (ComicMyBookActivity) fragment.getActivity();
        if (activity != null) {
            activity.invalidateOptionsMenu();
        }
    }

    public void edit() {
        setShowChecked(!isShowChecked());
    }

    public void allChecked() {
        setAllChecked(!isAllChecked());
    }
}
