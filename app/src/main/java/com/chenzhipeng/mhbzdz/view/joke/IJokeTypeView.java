package com.chenzhipeng.mhbzdz.view.joke;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface IJokeTypeView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void onFail(Throwable e);

    void setProgress(boolean b);

    void setRecyclerViewTop();
}
