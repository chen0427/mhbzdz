package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicTypeDataView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void onFail(Throwable e);


    void setProgress(boolean b);
}
