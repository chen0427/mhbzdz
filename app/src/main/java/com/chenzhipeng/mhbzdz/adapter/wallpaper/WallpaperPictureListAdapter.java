package com.chenzhipeng.mhbzdz.adapter.wallpaper;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.widget.WallpaperPictureView;

import java.io.File;
import java.util.List;


public class WallpaperPictureListAdapter extends BaseQuickAdapter<WallpaperItemBean, BaseViewHolder> {

    public WallpaperPictureListAdapter(@LayoutRes int layoutResId, @Nullable List<WallpaperItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WallpaperItemBean item) {
        WallpaperPictureView pictureView = helper.getView(R.id.wpv_itemWallpaperPicture);
        if (TextUtils.isEmpty(item.getImg())) {
            pictureView.setImage(new File(item.getLocalPath()));
        } else {
            pictureView.setImage(item.getImg());
        }
    }
}
