package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/30.
 */

public interface IComicHistoryView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void showDeleteDialog();
}
