package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface IComicRecommendView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void setProgress(boolean b);

    void onFail(Throwable e);

}
