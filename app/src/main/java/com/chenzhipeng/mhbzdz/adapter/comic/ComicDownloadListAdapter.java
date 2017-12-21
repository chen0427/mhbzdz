package com.chenzhipeng.mhbzdz.adapter.comic;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;

import java.util.List;


public class ComicDownloadListAdapter extends BaseQuickAdapter<ComicChapterItemBean, BaseViewHolder> {

    public ComicDownloadListAdapter(@LayoutRes int layoutResId, @Nullable List<ComicChapterItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicChapterItemBean item) {
        helper.addOnClickListener(R.id.ll_itemComicDownloadList);
        LinearLayout layout = helper.getView(R.id.ll_itemComicDownloadList);
        AppCompatTextView textView = helper.getView(R.id.tv_itemComicDownloadList);
        AppCompatCheckBox checkBox = helper.getView(R.id.cb_itemComicDownloadChoice);

        layout.setEnabled(item.isEnabled());
        checkBox.setEnabled(item.isEnabled());
        textView.setEnabled(item.isEnabled());
        textView.setTextColor(item.isEnabled() ? Color.BLACK : Color.GRAY);

        textView.setText(item.getChapterName());
        checkBox.setChecked(item.isChecked());
    }

}
