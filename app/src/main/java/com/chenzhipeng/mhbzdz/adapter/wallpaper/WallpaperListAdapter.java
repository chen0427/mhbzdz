package com.chenzhipeng.mhbzdz.adapter.wallpaper;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class WallpaperListAdapter extends BaseQuickAdapter<WallpaperItemBean, BaseViewHolder> {

    public WallpaperListAdapter(@LayoutRes int layoutResId, @Nullable List<WallpaperItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WallpaperItemBean item) {
        helper.addOnClickListener(R.id.cv_itemWallpaperPicture);
        helper.addOnClickListener(R.id.cb_itemWallpaperPicture);
        AppCompatImageView imageView = helper.getView(R.id.iv_itemWallpaperPicture);
        AppCompatCheckBox checkBox = helper.getView(R.id.cb_itemWallpaperPicture);
        String thumb = item.getThumb();
        if (!TextUtils.isEmpty(thumb)) {
            ImageHelper.setImage(thumb, imageView, R.color.white);
        } else {
            String localPath = item.getLocalPath();
            ImageHelper.setImage(new File(localPath), imageView, R.color.white);
        }
        helper.getView(R.id.fl_itemWallpaperPicture).setVisibility(item.isShowChecked() ? View.VISIBLE : View.GONE);
        checkBox.setChecked(item.isChecked());
    }
}
