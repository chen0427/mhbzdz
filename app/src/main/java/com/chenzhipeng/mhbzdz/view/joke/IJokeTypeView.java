package com.chenzhipeng.mhbzdz.view.joke;


public interface IJokeTypeView {
    <T> void onAdapter(T data);

    void onEmptyData();

    void onFail(Throwable e);

    void setProgress(boolean b);

    void setRecyclerViewTop();
}
