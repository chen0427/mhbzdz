package com.chenzhipeng.mhbzdz.adapter.wallpaper;

import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyItemBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.lid.lib.LabelImageView;

import java.util.List;

public class WallpaperClassifyListAdapter extends
        BaseQuickAdapter<WallpaperClassifyItemBean, BaseViewHolder> {


    public WallpaperClassifyListAdapter(@LayoutRes int layoutResId,
                                        @Nullable List<WallpaperClassifyItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WallpaperClassifyItemBean item) {
        LabelImageView view = helper.getView(R.id.liv_itemWallpaperClassifyItem);
        view.setLabelBackgroundColor(ContextCompat.getColor(mContext, ConfigUtils.getChoiceToAppColor()));
        view.setLabelText(item.getName());
        view.setLabelHeight(35);
        view.setLabelTextColor(Color.WHITE);
        helper.addOnClickListener(R.id.cv_itemWallpaperClassifyItem);
        ImageHelper.setImage(item.getCover(), view, R.color.white);
    }
}
