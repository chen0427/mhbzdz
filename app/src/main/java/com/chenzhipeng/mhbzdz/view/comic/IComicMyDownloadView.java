package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/11/12.
 */

public interface IComicMyDownloadView {
    <T> void onAdapter(T data);

    void showDeleteDialog();
}
