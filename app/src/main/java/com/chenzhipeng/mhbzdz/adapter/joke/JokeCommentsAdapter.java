package com.chenzhipeng.mhbzdz.adapter.joke;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.joke.NeiHanCommentsItemBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class JokeCommentsAdapter extends BaseMultiItemQuickAdapter<NeiHanCommentsItemBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public JokeCommentsAdapter(List<NeiHanCommentsItemBean> data) {
        super(data);
        addItemType(NeiHanCommentsItemBean.TYPE_HOT, R.layout.itemview_comment_flag);
        addItemType(NeiHanCommentsItemBean.TYPE_NEW, R.layout.itemview_comment_flag);
        addItemType(NeiHanCommentsItemBean.TYPE_NORMAL, R.layout.itemview_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, NeiHanCommentsItemBean item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case NeiHanCommentsItemBean.TYPE_NORMAL:
                helper.setText(R.id.tv_authorNameJoke, item.getUserName());
                helper.setText(R.id.tv_comment_content, item.getText());
                CircleImageView imageView = helper.getView(R.id.civ_authorHeadJoke);
                ImageHelper.setImage(item.getAvatarUrl(), imageView, -1);
                break;
            case NeiHanCommentsItemBean.TYPE_HOT:
                helper.setText(R.id.tv, R.string.hot_comment);
                break;
            case NeiHanCommentsItemBean.TYPE_NEW:
                helper.setText(R.id.tv, R.string.news_comment);
                break;
        }

    }
}
