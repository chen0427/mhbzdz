package com.chenzhipeng.mhbzdz.adapter.joke;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.widget.JokePictureView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/2.
 */

public class JokePictureListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public JokePictureListAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        JokePictureView jokePictureView = helper.getView(R.id.jpv_jokePicture);
        jokePictureView.setImage(item, R.color.black);
    }
}
