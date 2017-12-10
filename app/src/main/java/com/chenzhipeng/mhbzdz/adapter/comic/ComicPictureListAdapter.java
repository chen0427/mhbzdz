package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.widget.ComicReadPictureView;

import java.util.List;

/**
 * 漫画 页面
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class ComicPictureListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private String comicId;
    private String comicName;
    private String chapterName;

    public ComicPictureListAdapter(@LayoutRes int layoutResId, @Nullable List<T> data, String comicId, String comicName, String chapterName) {
        super(layoutResId, data);
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterName = chapterName;
    }


    @Override
    protected void convert(BaseViewHolder helper, T item) {
        ComicItemPicture picture = (ComicItemPicture) item;
        ComicReadPictureView comicReadPictureView = helper.getView(R.id.crpv_itemComicPicture);
        comicReadPictureView.setImage(picture.getUrl(), comicId, comicName, chapterName);
        helper.addOnClickListener(R.id.crpv_itemComicPicture);
    }

}
