package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicMyDownloadView {
    <T> void onAdapter(T data);

    void showDeleteDialog();
}
