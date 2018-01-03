package com.chenzhipeng.mhbzdz.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;

import java.util.List;


public class SearchRecordListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchRecordListAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.ll_searchRecordItem);
        helper.addOnClickListener(R.id.iv_itemSearchDelete);
        helper.setText(R.id.tv_itemSearchRecord, item);
    }
}
