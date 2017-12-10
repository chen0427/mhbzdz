package com.chenzhipeng.mhbzdz.adapter.comic;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/9.
 */

public class ComicChapterListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public ComicChapterListAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        ComicChapterItemBean bean = (ComicChapterItemBean) item;
        String chapterName = bean.getChapterName();
        AppCompatTextView textView = helper.getView(R.id.AppCompatTextView1);
        textView.setTextColor(bean.isRead() ? getColor() : Color.BLACK);
        helper.addOnClickListener(R.id.LinearLayout);
        helper.setText(R.id.AppCompatTextView1, chapterName);
    }

    private int getColor() {
        return ContextCompat.getColor(BaseApplication.getContext(), ConfigUtils.getChoiceToAppColor());
    }
}
