package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/30.
 */

public interface IComicRecommendTagView {
    <T> void onAdapter(T data);

    void onTitle(String title);

    void setProgress(boolean b);

    void onEmptyData();

    void onFail(Throwable e);
}
