package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.view.Menu;
import android.view.View;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperDownloadActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperDownloadView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class WallpaperDownloadPresenter {
    private IWallpaperDownloadView downloadView;
    private WallpaperDownloadActivity activity;
    private WallpaperListAdapter adapter;
    private List<WallpaperItemBean> list;

    public WallpaperDownloadPresenter(WallpaperDownloadActivity activity) {
        this.downloadView = activity;
        this.activity = activity;
    }

    public void updateMenu(Menu menu, BottomCheckedView bottomCheckedView) {
        if (menu != null && bottomCheckedView != null) {
            boolean showChecked = isShowChecked();
            boolean allChecked = isAllChecked();
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setTitle(showChecked ? "完成" : "整理");
            bottomCheckedView.setVisibility(showChecked ? View.VISIBLE : View.GONE);
            bottomCheckedView.setChecked(allChecked);
        }
    }

    public void initData() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                List<WallpaperItemBean> list = getWallpaperItemBeanList();
                if (!EmptyUtils.isListsEmpty(list)) {
                    e.onNext(list);
                }
            }
        }).compose(activity.bindToLifecycle()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        list = (List<WallpaperItemBean>) o;
                        if (adapter == null) {
                            adapter = new WallpaperListAdapter(R.layout.itemview_wallpaper_item, list);
                            downloadView.onAdapter(adapter);
                        } else {
                            adapter.setNewData(list);
                        }
                    }
                });
    }

    private List<WallpaperItemBean> getWallpaperItemBeanList() {
        List<WallpaperItemBean> beanList = new ArrayList<>();
        String wallpaperPath = BaseApplication.WALLPAPER_PATH;
        File file = new File(wallpaperPath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    String path = f.getAbsolutePath();
                    WallpaperItemBean bean = new WallpaperItemBean(path, false, false);
                    beanList.add(bean);
                }
            }
        }
        return beanList;
    }

    public void edit() {
        setShowChecked(!isShowChecked());
        activity.invalidateOptionsMenu();
    }

    public void allChecked() {
        setAllChecked(!isAllChecked());
        activity.invalidateOptionsMenu();
    }


    public void closeMenu() {
        setShowChecked(false);
        activity.invalidateOptionsMenu();
    }

    public void showDeleteDialog() {
        if (isCanDelete()) {
            downloadView.showDeleteDialog();
        }
    }

    private boolean isCanDelete() {
        if (!EmptyUtils.isListsEmpty(list)) {
            for (WallpaperItemBean bean : list) {
                if (bean.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否全选
     *
     * @return
     */
    private boolean isAllChecked() {
        if (!EmptyUtils.isListsEmpty(list)) {
            for (WallpaperItemBean bean : list) {
                if (!bean.isChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 全选 取消全选
     *
     * @param b
     */
    private void setAllChecked(boolean b) {
        if (!EmptyUtils.isListsEmpty(list) && adapter != null) {
            for (WallpaperItemBean bean : list) {
                bean.setChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置显示checked
     *
     * @param b
     */
    private void setShowChecked(boolean b) {
        if (!EmptyUtils.isListsEmpty(list) && adapter != null) {
            for (WallpaperItemBean bean : list) {
                bean.setShowChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否显示checked
     *
     * @return
     */
    public boolean isShowChecked() {
        if (!EmptyUtils.isListsEmpty(list) && adapter != null) {
            for (WallpaperItemBean bean : list) {
                if (bean.isShowChecked()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteChecked() {
        if (!EmptyUtils.isListsEmpty(list) && adapter != null) {
            Iterator<WallpaperItemBean> iterator = list.iterator();
            while (iterator.hasNext()) {
                WallpaperItemBean next = iterator.next();
                if (next.isChecked()) {
                    String localPath = next.getLocalPath();
                    File file = new File(localPath);
                    if (file.exists()) {
                        boolean delete = file.delete();
                        if (delete) {
                            iterator.remove();
                        }
                    }
                }
            }
            adapter.notifyDataSetChanged();
            activity.invalidateOptionsMenu();
            closeMenu();
        }
    }
}
