package com.chenzhipeng.mhbzdz.view;


public interface ISettingView {
    void showIndexChoiceDialog();

    void showAppColorChoiceDialog();

    void showComicChapterSort();

    void setComicChapterSort(int i);

    void setIndexChoice(String s);

    void setAppColorChoice(int colorId);

    void setImgCache(String s);

    void setVolumePage(boolean b);
}
