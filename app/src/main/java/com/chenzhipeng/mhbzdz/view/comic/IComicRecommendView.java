package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicRecommendView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void setProgress(boolean b);

    void onFail(Throwable e);

}
