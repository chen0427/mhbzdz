package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/10/21.
 */

public interface IComicDownloadDataView {
    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onAdapterProgress(int progress, int group);

    void onAdapterState(String stateStr, int group);

    void onAdapterPage(String pageStr, int group);

    void showAllStartDialog();

    void showAllPauseDialog();

    void showDeleteDialog();

    void showRunningDeleteDialog();

    void dismissRunningDeleteDialog();
}
