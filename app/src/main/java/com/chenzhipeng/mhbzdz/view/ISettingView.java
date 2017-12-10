package com.chenzhipeng.mhbzdz.view;

/**
 * Created by Administrator on 2017/11/15.
 */

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
