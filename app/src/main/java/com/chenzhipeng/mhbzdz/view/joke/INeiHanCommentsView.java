package com.chenzhipeng.mhbzdz.view.joke;

import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsItemBean;

import java.util.List;


public interface INeiHanCommentsView {
    void onUpdate(List<NeiHanCommentsItemBean> itemBeen);

    void onError();
}
