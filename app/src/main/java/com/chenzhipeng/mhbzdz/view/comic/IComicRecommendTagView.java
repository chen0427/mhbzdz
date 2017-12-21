package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicRecommendTagView {
    <T> void onAdapter(T data);

    void onTitle(String title);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
