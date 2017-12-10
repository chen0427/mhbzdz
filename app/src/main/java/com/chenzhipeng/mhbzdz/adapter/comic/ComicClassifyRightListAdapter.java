package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicClassifyBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicClassifyFragment;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ComicClassifyRightListAdapter extends BaseQuickAdapter<ComicClassifyBean.ClassifyType, BaseViewHolder> {
    public ComicClassifyRightListAdapter(@LayoutRes int layoutResId, @Nullable List<ComicClassifyBean.ClassifyType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicClassifyBean.ClassifyType item) {
        helper.getView(R.id.CardView).setTag(ComicClassifyFragment.TAG_RIGHT);
        helper.addOnClickListener(R.id.CardView);
        helper.setText(R.id.tv_classify_name, item.getTitle());
        AppCompatImageView imageView = helper.getView(R.id.iv_classify_img);
        ImageHelper.setImage(ComicApiUtils.getClassifyImg(item.getTag()), imageView, R.color.white);
    }
}
