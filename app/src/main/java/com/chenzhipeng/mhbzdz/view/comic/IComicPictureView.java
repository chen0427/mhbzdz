package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicPictureView {
    <T> void onAdapter(T data, int readPosition);

    void updateBottomBar(String comicId, String comicName, String chapterName, String currentNumber, String endNumber, String pictureUrl);

    void showWorkDialog();

    void dismissWorkDialog();

}
