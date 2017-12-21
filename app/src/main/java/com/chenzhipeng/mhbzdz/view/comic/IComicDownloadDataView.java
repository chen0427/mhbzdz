package com.chenzhipeng.mhbzdz.view.comic;


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
