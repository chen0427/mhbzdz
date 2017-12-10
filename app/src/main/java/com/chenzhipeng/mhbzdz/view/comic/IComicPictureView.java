package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/21.
 */

public interface IComicPictureView {
    <T> void onAdapter(T data, int readPosition);

    void updateBottomBar(String comicId, String comicName, String chapterName, String currentNumber, String endNumber, String pictureUrl);

    void showWorkDialog();

    void dismissWorkDialog();

}
