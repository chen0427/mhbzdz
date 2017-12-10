package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */

public class ComicBookListAdapter extends BaseQuickAdapter<ComicItemBean, BaseViewHolder> {

    public ComicBookListAdapter(@LayoutRes int layoutResId, @Nullable List<ComicItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicItemBean item) {
        helper.addOnClickListener(R.id.cv_comicBook);
        AppCompatImageView imageView = helper.getView(R.id.iv_comicBookImg);
        AppCompatCheckBox checkBox = helper.getView(R.id.cb_itemComicBook);
        LinearLayout layout = helper.getView(R.id.ll_itemComicBook);
        ImageHelper.setImage(ComicApiUtils.getComicImg(item.getComicId()), imageView, R.color.white);
        String lastChapterName = item.getLastChapterName();
        helper.setVisible(R.id.tv_comicBookFlag, !TextUtils.isEmpty(lastChapterName));
        helper.setText(R.id.tv_comicBookFlag, lastChapterName);
        helper.setText(R.id.tv_comicBookName, item.getComicName());
        // checkBox.setVisibility(item.isShowChecked() ? View.VISIBLE : View.GONE);
        layout.setVisibility(item.isShowChecked() ? View.VISIBLE : View.GONE);
        checkBox.setChecked(item.isChecked());
    }
}
