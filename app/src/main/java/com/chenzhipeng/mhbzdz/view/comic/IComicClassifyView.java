package com.chenzhipeng.mhbzdz.view.comic;


public interface IComicClassifyView {
    void onFail(Throwable e);

    <T> void onLeftAdapter(T data);

    <T> void onRightAdapter(T data);

    void setProgress(boolean b);

}
