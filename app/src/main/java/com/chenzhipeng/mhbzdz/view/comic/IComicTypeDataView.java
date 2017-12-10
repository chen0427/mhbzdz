package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/9/27.
 */

public interface IComicTypeDataView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void onFail(Throwable e);


    void setProgress(boolean b);
}
