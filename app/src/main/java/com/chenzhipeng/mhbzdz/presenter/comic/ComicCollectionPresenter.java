package com.chenzhipeng.mhbzdz.presenter.comic;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicMyBookActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicCollectionFragment;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicCollectionView;

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
 * Created by Administrator on 2017/9/30.
 */

public class ComicCollectionPresenter {
    private IComicCollectionView collectionView;
    private ComicBookListAdapter adapter;
    private List<ComicItemBean> list;
    private ComicCollectionFragment fragment;

    public ComicCollectionPresenter(ComicCollectionFragment fragment) {
        this.collectionView = fragment;
        this.fragment = fragment;
    }


    public void initData() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                List<ComicItemBean> comicItemBeanList = ComicDatabase.getInstance().getCollection();
                e.onNext(comicItemBeanList);
            }
        }).compose(fragment.bindToLifecycle()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        list = (List<ComicItemBean>) o;
                        if (adapter == null) {
                            adapter = new ComicBookListAdapter(R.layout.itemview_comic_book, list);
                            collectionView.onAdapter(adapter);
                        } else {
                            adapter.setNewData(list);
                        }
                    }
                });
    }


    private void setAllChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(list)) {
            for (ComicItemBean c : list) {
                c.setChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void setShowChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(list)) {
            for (ComicItemBean c : list) {
                c.setShowChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isShowChecked() {
        if (!EmptyUtils.isListsEmpty(list)) {
            for (ComicItemBean c : list) {
                if (!c.isShowChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isAllChecked() {
        if (!EmptyUtils.isListsEmpty(list)) {
            for (ComicItemBean c : list) {
                if (!c.isChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
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

    public void showDeleteDialog() {
        if (isCanDelete()) {
            collectionView.showDeleteDialog();
        }
    }

    private boolean isCanDelete() {
        if (!EmptyUtils.isListsEmpty(list)) {
            for (ComicItemBean c : list) {
                if (c.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void delete() {
        if (!EmptyUtils.isListsEmpty(list) && adapter != null) {
            Iterator<ComicItemBean> iterator = list.iterator();
            while (iterator.hasNext()) {
                ComicItemBean next = iterator.next();
                if (next.isChecked()) {
                    if (ComicDatabase.getInstance().deleteCollection(next.getComicId())) {
                        iterator.remove();
                    }
                }
            }
            adapter.notifyDataSetChanged();
            closeMenu();
        }
    }

    public void allChecked() {
        setAllChecked(!isAllChecked());
    }

    public void edit() {
        setShowChecked(!isShowChecked());
    }
}
