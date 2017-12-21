package com.chenzhipeng.mhbzdz.adapter.comic;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicClassifyBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicClassifyFragment;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;

import java.util.List;


public class ComicClassifyLeftListAdapter extends BaseQuickAdapter<ComicClassifyBean, BaseViewHolder> {
    public ComicClassifyLeftListAdapter(@LayoutRes int layoutResId, @Nullable List<ComicClassifyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicClassifyBean item) {
        helper.getView(R.id.bt_left_item).setTag(ComicClassifyFragment.TAG_LEFT);
        helper.addOnClickListener(R.id.bt_left_item);
        ((Button) helper.getView(R.id.bt_left_item)).setText(item.getClassifyTitle());
        if (item.isSelect()) {
            ((Button) helper.getView(R.id.bt_left_item)).setTextColor(ContextCompat.getColor(BaseApplication.getContext(), ConfigUtils.getChoiceToAppColor()));
            helper.setBackgroundRes(R.id.root, R.color.screenDefault);
        } else {
            ((Button) helper.getView(R.id.bt_left_item)).setTextColor(Color.BLACK);
            helper.setBackgroundRes(R.id.root, R.color.comicLeft);
        }
    }
}
