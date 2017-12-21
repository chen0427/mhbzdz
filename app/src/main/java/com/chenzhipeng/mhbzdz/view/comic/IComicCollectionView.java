package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicCollectionView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void showDeleteDialog();
}
