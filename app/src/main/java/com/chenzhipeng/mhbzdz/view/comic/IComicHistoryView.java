package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicHistoryView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void showDeleteDialog();
}
