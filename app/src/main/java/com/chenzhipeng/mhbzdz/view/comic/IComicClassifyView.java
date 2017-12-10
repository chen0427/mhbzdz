package com.chenzhipeng.mhbzdz.view.comic;

/**
 * Created by Administrator on 2017/8/31.
 */

public interface IComicClassifyView {
    void onFail(Throwable e);

    <T> void onLeftAdapter(T data);

    <T> void onRightAdapter(T data);

    void setProgress(boolean b);

}
