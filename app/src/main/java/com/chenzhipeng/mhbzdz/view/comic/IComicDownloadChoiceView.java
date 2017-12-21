package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicDownloadChoiceView {

    <T> void onAdapter(T data);

    void setProgress(boolean b);

    void onEmptyData();
}
