package com.chenzhipeng.mhbzdz.presenter.comic;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDownloadDataActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicDownloadDataListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.download.ComicBookDownloader;
import com.chenzhipeng.mhbzdz.download.ComicDownloaderManager;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.document.ComicDocumentHelper;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicDownloadDataView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

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


@SuppressWarnings("unchecked")
public class ComicDownloadDataPresenter {
    private ComicDownloadDataActivity activity;
    private IComicDownloadDataView downloadDataView;
    private ComicDownloadDataListAdapter adapter;
    private ComicBookDownloader comicBookDownloader;
    private String comicId;
    private String comicName;

    private String getStringId(int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }

    public ComicDownloadDataPresenter(ComicDownloadDataActivity activity) {
        this.activity = activity;
        this.downloadDataView = activity;
    }

    public void updateMenu(Menu menu, BottomCheckedView bottomCheckedView) {
        if (menu != null && bottomCheckedView != null) {
            boolean showChecked = isShowChecked();
            bottomCheckedView.setChecked(isAllSelect());
            bottomCheckedView.setVisibility(showChecked ? View.VISIBLE : View.GONE);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setTitle(showChecked ? activity.getString(R.string.complete) : activity.getString(R.string.edit));
            if (comicBookDownloader != null) {
                boolean pause = comicBookDownloader.isPause();
                menu.getItem(0).setIcon(pause ? R.drawable.ic_download_start : R.drawable.ic_download_pause);
                menu.getItem(0).setTitle(pause ? activity.getString(R.string.all_start) : activity.getString(R.string.all_pause));
            }
        }
    }

    public void delete() {
        downloadDataView.showRunningDeleteDialog();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                boolean success = false;
                if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
                    Iterator<ComicDownloadBean> iterator = adapter.getData().iterator();
                    while (iterator.hasNext()) {
                        ComicDownloadBean next = iterator.next();
                        if (next.isChecked()) {
                            iterator.remove();
                            next.delete();
                            ComicDatabase.getInstance().deleteDownloadData(next.getComicId(), next.getChapterName());
                            ComicDownloaderManager.getInstance().remove(next);
                            ComicDocumentHelper.getInstance().deleteDownloadData(next);
                            success = true;
                        }
                    }
                }
                e.onNext(success);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        downloadDataView.dismissRunningDeleteDialog();
                        if ((boolean) o) {
                            setAllChecked(false);
                            setShowChecked(false);
                            adapter.notifyDataSetChanged();
                            activity.invalidateOptionsMenu();
                            if (adapter.getData().size() == 0) {
                                ComicDatabase.getInstance().deleteDownloadBook(comicId);
                                ComicDocumentHelper.getInstance().deleteBook(comicId, comicName);
                            }
                        }
                    }
                });

    }


    private void setAllChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                c.setChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isAllSelect() {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                if (!c.isChecked()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void setShowChecked(boolean b) {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                c.setShowChecked(b);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isCanStart() {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                if (c.getState().equals(getStringId(R.string.pause_download))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCanPause() {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                if (c.getState().equals(getStringId(R.string.wait_download)) || c.getState().equals(getStringId(R.string.running_download))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCanDelete() {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                if (c.isChecked()) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isShowChecked() {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            for (ComicDownloadBean c : adapter.getData()) {
                if (!c.isShowChecked()) {
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
            activity.invalidateOptionsMenu();
            return true;
        }
        return false;
    }

    public void edit() {
        setShowChecked(!isShowChecked());
        activity.invalidateOptionsMenu();
    }

    public void allChecked() {
        setAllChecked(!isAllSelect());
        activity.invalidateOptionsMenu();
    }

    public void showDeleteDialog() {
        if (isCanDelete()) {
            downloadDataView.showDeleteDialog();
        }
    }

    public void play() {
        if (comicBookDownloader != null) {
            boolean pause = comicBookDownloader.isPause();
            if (pause) {
                if (isCanStart()) {
                    downloadDataView.showAllStartDialog();
                }
            } else {
                if (isCanPause()) {
                    downloadDataView.showAllPauseDialog();
                }
            }
        }
    }

    public void allStart() {
        if (comicBookDownloader != null) {
            comicBookDownloader.setAllStart();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            activity.invalidateOptionsMenu();
        }
    }

    public void allPause() {
        if (comicBookDownloader != null) {
            comicBookDownloader.setAllPause();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            activity.invalidateOptionsMenu();
        }
    }


    private void insertDownloadData(List<ComicDownloadBean> comicDownloadBeanList) {
        if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
            for (ComicDownloadBean c : comicDownloadBeanList) {
                ComicDatabase.getInstance().insertDownloadData(c);
            }
        }
    }

    private List<ComicDownloadBean> getComicDownloadBeanList(List<ComicChapterItemBean> comicChapterItemBeanList) {
        List<ComicDownloadBean> comicDownloadBeanList = new ArrayList<>();
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
            for (ComicChapterItemBean c : comicChapterItemBeanList) {
                comicDownloadBeanList.add(getComicDownloadBean(c));
            }
        }
        return comicDownloadBeanList;
    }

    private ComicDownloadBean getComicDownloadBean(ComicChapterItemBean comicChapterItemBean) {
        if (comicChapterItemBean != null) {
            String chapterName = comicChapterItemBean.getChapterName();
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < comicChapterItemBean.getComicItemPictureList().size(); i++) {
                urls.add(comicChapterItemBean.getComicItemPictureList().get(i).getUrl());
            }
            int size = urls.size();
            String pagerStr = 0 + "/" + size;
            return new ComicDownloadBean(comicChapterItemBean.getComicId(), comicChapterItemBean.getComicName(), chapterName, BaseApplication.getContext().getResources().getString(R.string.wait_download), pagerStr, 0, size, false, false, false, urls);
        }
        return null;
    }

    /**
     * 处理正在下载的任务
     */
    private void setDownloadDataToRunning(List<ComicDownloadBean> beforeComicDownloadBeanList, List<ComicDownloadBean> afterComicDownloadBeanList) {
        if (!EmptyUtils.isListsEmpty(beforeComicDownloadBeanList)) {
            for (ComicDownloadBean c : beforeComicDownloadBeanList) {
                //获取正在下载任务
                ComicDownloadBean runningComicDownloadBean = ComicDownloaderManager.getInstance().get(c.getComicId(), c.getChapterName());
                if (runningComicDownloadBean != null) {
                    //有正在下载任务 直接添加到集合中
                    afterComicDownloadBeanList.add(runningComicDownloadBean);
                } else {
                    //没有正在下载任务 根据本地文件配置任务进度
                    ComicDownloadBean dataToStorage = ComicDocumentHelper.getInstance().getDataToStorage(c);
                    afterComicDownloadBeanList.add(dataToStorage);
                    ComicDownloaderManager.getInstance().put(dataToStorage);
                }
            }
        }
    }


    public void init() {
        final Intent intent = activity.getIntent();
        if (intent != null) {
            downloadDataView.setProgress(true);
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    List<ComicChapterItemBean> comicChapterItemBeanList = (List<ComicChapterItemBean>) intent.getSerializableExtra(ComicDownloadDataActivity.KEY_INTENT_1);
                    comicId = intent.getStringExtra(ComicDownloadDataActivity.KEY_INTENT_2);
                    List<ComicDownloadBean> comicDownloadBeanList = new ArrayList<>();
                    List<ComicDownloadBean> dbList = ComicDatabase.getInstance().getDownloadData(comicId);
                    if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
                        comicName = comicChapterItemBeanList.get(0).getComicName();
                        ComicDatabase.getInstance().insertDownloadBook(comicId, comicName);
                        //有新添加的任务
                        List<ComicDownloadBean> addList = getComicDownloadBeanList(comicChapterItemBeanList);
                        insertDownloadData(addList);
                        dbList.addAll(addList);
                        setDownloadDataToRunning(dbList, comicDownloadBeanList);
                    } else {
                        //没有新添加的任务 直接从数据库中取
                        comicName = dbList.get(0).getComicName();
                        setDownloadDataToRunning(dbList, comicDownloadBeanList);
                    }
                    e.onNext(comicDownloadBeanList);
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            downloadDataView.setProgress(false);
                            List<ComicDownloadBean> comicDownloadBeanList = (List<ComicDownloadBean>) o;
                            if (!EmptyUtils.isListsEmpty(comicDownloadBeanList)) {
                                if (adapter == null) {
                                    adapter = new ComicDownloadDataListAdapter(R.layout.itemview_comic_download_details, comicDownloadBeanList);
                                    checkedHistoryRecord(false);
                                    downloadDataView.onAdapter(adapter);
                                } else {
                                    checkedHistoryRecord(false);
                                    adapter.setNewData(comicDownloadBeanList);
                                }
                            }

                            comicBookDownloader = new ComicBookDownloader();
                            comicBookDownloader.setData(comicDownloadBeanList).setListener(new BookDownloadListener()).start();
                            //更新播放按钮
                            activity.invalidateOptionsMenu();
                        }
                    });
        }
    }

    /**
     * 检查有没有阅读 记录
     *
     * @param isUpdateAdapter 是否更新adapter
     */
    public void checkedHistoryRecord(boolean isUpdateAdapter) {
        if (adapter != null && !EmptyUtils.isListsEmpty(adapter.getData())) {
            String chapterName = ComicDatabase.getInstance().getHistoryChapterName(comicId);
            if (!TextUtils.isEmpty(chapterName)) {
                for (ComicDownloadBean c : adapter.getData()) {
                    if (c.getChapterName().equals(chapterName)) {
                        c.setRead(true);
                    } else {
                        c.setRead(false);
                    }
                }
                if (isUpdateAdapter) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void startActivity() {
        ComicDetailsActivity.startActivity(activity, new ComicItemBean(comicId, comicName, false, false));
        closeMenu();
        activity.finish();
    }

    public boolean isExistDownload() {
        return comicBookDownloader != null && comicBookDownloader.isExistDownload();
    }


    private class BookDownloadListener implements ComicBookDownloader.Listener {

        @Override
        public void updateState(String stateStr, int group) {
            downloadDataView.onAdapterState(stateStr, group);
        }

        @Override
        public void updateProgress(int progress, int group) {
            downloadDataView.onAdapterProgress(progress, group);
        }

        @Override
        public void updatePage(String pageStr, int group) {
            downloadDataView.onAdapterPage(pageStr, group);
        }

        @Override
        public void onCompleteItem() {
            activity.invalidateOptionsMenu();
        }

        @Override
        public void onCompleteAllGroup() {

        }
    }
}
