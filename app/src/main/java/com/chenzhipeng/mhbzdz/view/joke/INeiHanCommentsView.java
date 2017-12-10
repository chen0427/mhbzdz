package com.chenzhipeng.mhbzdz.view.joke;

import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsItemBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/12.
 */

public interface INeiHanCommentsView {
    void onUpdate(List<NeiHanCommentsItemBean> itemBeen);

    void onError();
}
