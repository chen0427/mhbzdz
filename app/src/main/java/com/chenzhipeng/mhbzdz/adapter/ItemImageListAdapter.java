package com.chenzhipeng.mhbzdz.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;
import com.chenzhipeng.mhbzdz.widget.JokeGifTagImageView;
import com.chenzhipeng.mhbzdz.widget.JokeNormalImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

class ItemImageListAdapter extends BaseQuickAdapter<JokeBean.Data.Dates.Group.ThumbImageList, BaseViewHolder> {
    private List<JokeBean.Data.Dates.Group.LargeImageList> largeImageList;

    ItemImageListAdapter(@LayoutRes int layoutResId, List<JokeBean.Data.Dates.Group.ThumbImageList> thumbImageList,
                         @Nullable List<JokeBean.Data.Dates.Group.LargeImageList> largeImageList) {
        super(layoutResId, thumbImageList);
        this.largeImageList = largeImageList;
    }

    @Override
    protected void convert(BaseViewHolder helper, JokeBean.Data.Dates.Group.ThumbImageList item) {
        final int position = helper.getAdapterPosition();
        String url = item.getUrlLists().get(0).getUrl();
        String largeImageUrl = largeImageList.get(position).getUrlLists().get(0).getUrl();
        JokeGifTagImageView jokeGifTagImageView = helper.getView(R.id.iv_jokeGifTagImage);
        JokeNormalImageView jokeNormalImageView = helper.getView(R.id.iv_jokeNormalImage);
        if (largeImageUrl.contains("webp")) {
            //不是gif图
            jokeGifTagImageView.setVisibility(View.GONE);
            jokeNormalImageView.setVisibility(View.VISIBLE);
            jokeNormalImageView.setUrlList(largeImageList, helper.getAdapterPosition());
            jokeNormalImageView.setSetSize(false);
            jokeNormalImageView.setImage(url, 0, 0);
        } else {
            //gif图
            jokeNormalImageView.setVisibility(View.GONE);
            jokeGifTagImageView.setVisibility(View.VISIBLE);
            jokeGifTagImageView.setUrlList(largeImageList, helper.getAdapterPosition());
            jokeGifTagImageView.setSetSize(false);
            jokeGifTagImageView.setImage(url, 0, 0);
        }
    }

}
