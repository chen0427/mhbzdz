package com.chenzhipeng.mhbzdz.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.joke.JokeBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.DisplayUtils;
import com.chenzhipeng.mhbzdz.widget.JokeGifTagImageView;
import com.chenzhipeng.mhbzdz.widget.JokeLargeImageView;
import com.chenzhipeng.mhbzdz.widget.JokeNormalImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017/7/17.
 */

public class NeiHanAdapter extends BaseMultiItemQuickAdapter<JokeBean.Data.Dates, BaseViewHolder> {
    private Context context;

    public NeiHanAdapter(Context context, @Nullable List<JokeBean.Data.Dates> dates) {
        super(dates);
        this.context = context;
        addItemType(JokeBean.TYPE_TEXT, R.layout.itemview_joke_text);
        addItemType(JokeBean.TYPE_VIDEO, R.layout.itemview_joke_video);
        addItemType(JokeBean.TYPE_GIF_PICTURE, R.layout.itemview_joke_gif_image);
        addItemType(JokeBean.TYPE_LARGE_PICTURE, R.layout.itemview_joke_large_image);
        addItemType(JokeBean.TYPE_LIST_PICTURE, R.layout.itemview_joke_list_image);
        addItemType(JokeBean.TYPE_NORMAL_PICTURE, R.layout.itemview_joke_image);
        addItemType(JokeBean.TYPE_REFRESH, R.layout.itemview_joke_refresh_text);
    }

    private void setBaseType(BaseViewHolder helper, JokeBean.Data.Dates item) {
        //cardView item
        helper.addOnClickListener(R.id.cv_itemJoke);
        // comment
        helper.addOnClickListener(R.id.ll_commentJoke);
        //hotcomment
        helper.addOnClickListener(R.id.ll_hotcommentJoke);
        //author head
        CircleImageView view = helper.getView(R.id.civ_authorHeadJoke);
        String url = item.getGroup().getUser().getAvatarUrl();
        ImageHelper.setImage(url, view, -1);

        helper.setText(R.id.tv_authorNameJoke, item.getGroup().getUser().getName());
        helper.setVisible(R.id.tv_textContentJoke, !TextUtils.isEmpty(item.getGroup().getText()));
        helper.setText(R.id.tv_textContentJoke, item.getGroup().getText());
        helper.setText(R.id.tv_commentJokeSize, item.getGroup().getCommentCount());

        List<JokeBean.Data.Dates.Comment> comments = item.getComments();
        if (comments == null || comments.size() == 0) {
            helper.setVisible(R.id.ll_hotcommentJoke, false);
        } else {
            if (comments.size() >= 1) {
                JokeBean.Data.Dates.Comment comment = comments.get(0);
                helper.setVisible(R.id.ll_hotcommentJoke, true);
                helper.setVisible(R.id.ll_hotcommentJoke_1, true);
                helper.setVisible(R.id.ll_hotcommentJoke_2, false);

                CircleImageView imageView1 = helper.getView(R.id.civ_headJoke_1);
                ImageHelper.setImage(comment.getAvatarUrl(), imageView1, -1);
                helper.setText(R.id.tv_nameJoke_1, comment.getUserName());
                helper.setText(R.id.tv_hotcommentJoke_1, comment.getText());
            }
            if (comments.size() >= 2) {
                helper.setVisible(R.id.ll_hotcommentJoke_2, true);
                JokeBean.Data.Dates.Comment comment = comments.get(1);
                CircleImageView imageView2 = helper.getView(R.id.civ_headJoke_2);
                ImageHelper.setImage(comment.getAvatarUrl(), imageView2, -1);
                helper.setText(R.id.tv_nameJoke_2, comment.getUserName());
                helper.setText(R.id.tv_hotcommentJoke_2, comment.getText());
            }
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, JokeBean.Data.Dates item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case JokeBean.TYPE_TEXT:
                setBaseType(helper, item);
                break;
            case JokeBean.TYPE_VIDEO:
                setBaseType(helper, item);
                loadVideo(helper, item);
                break;
            case JokeBean.TYPE_GIF_PICTURE:
                setBaseType(helper, item);
                loadGifPreview(helper, item);
                break;
            case JokeBean.TYPE_LARGE_PICTURE:
                setBaseType(helper, item);
                LoadLargeImage(helper, item);
                break;
            case JokeBean.TYPE_LIST_PICTURE:
                setBaseType(helper, item);
                loadImageList(helper, item);
                break;
            case JokeBean.TYPE_NORMAL_PICTURE:
                setBaseType(helper, item);
                loadImage(helper, item);
                break;
            case JokeBean.TYPE_REFRESH:
                helper.addOnClickListener(R.id.ll_refresh);
                break;
        }
    }

    private void loadImageList(BaseViewHolder helper, JokeBean.Data.Dates item) {
        List<JokeBean.Data.Dates.Group.LargeImageList> largeImageLists = item.getGroup().getLargeImageLists();
        List<JokeBean.Data.Dates.Group.ThumbImageList> thumbImageLists = item.getGroup().getThumbImageLists();
        //先算出recycleView的大小,防止item自动变化
        int height;
        if (thumbImageLists.size() <= 3) {
            //120dp
            height = DisplayUtils.dpToPx(120 + 6);
        } else if (thumbImageLists.size() > 3 && thumbImageLists.size() <= 6) {
            //240dp
            height = DisplayUtils.dpToPx(120 * 2 + 12);
        } else {
            //360dp
            height = DisplayUtils.dpToPx(120 * 3 + 18);
        }
        RecyclerView recyclerView = helper.getView(R.id.rv_listJoke);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = height;
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setHasFixedSize(true);
        ItemImageListAdapter itemImageListAdapter = new ItemImageListAdapter(R.layout.itemview_joke_item_image, thumbImageLists, largeImageLists);
        recyclerView.setAdapter(itemImageListAdapter);
    }

    private void loadImage(BaseViewHolder helper, JokeBean.Data.Dates item) {
        int largeWidth = Integer.parseInt(item.getGroup().getLargeImage().getWidth());
        int largeHeight = Integer.parseInt(item.getGroup().getLargeImage().getHeight());
        String largeImageUrl = item.getGroup().getLargeImage().getUrlLists().get(0).getUrl();
        JokeNormalImageView jokeNormalImageView = helper.getView(R.id.iv_jokeNormalImage);
        jokeNormalImageView.setImage(largeImageUrl, largeWidth, largeHeight);
    }

    private void LoadLargeImage(BaseViewHolder helper, JokeBean.Data.Dates item) {
        JokeLargeImageView jokeLargeImageView = helper.getView(R.id.jiv_jokeLargeImage);
        String largeImageUrl = item.getGroup().getLargeImage().getUrlLists().get(0).getUrl();
        jokeLargeImageView.setImage(largeImageUrl);
    }

    private void loadVideo(BaseViewHolder helper, JokeBean.Data.Dates item) {
        JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.jc_videoJoke);
        ImageView thumbImageView = jcVideoPlayerStandard.thumbImageView;
        String previewUrl = item.getGroup().getLargeCover().getUrlLists().get(0).getUrl();
        String videoUrl = item.getGroup().getVideo().getUrlLists().get(0).getUrl();
        ImageHelper.setImage(previewUrl, thumbImageView, R.color.black);
        jcVideoPlayerStandard.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
    }

    private void loadGifPreview(BaseViewHolder helper, JokeBean.Data.Dates item) {
        int middleWidth = Integer.parseInt(item.getGroup().getMiddleImage().getWidth());
        int middleHeight = Integer.parseInt(item.getGroup().getMiddleImage().getHeight());
        String middleImageUrl = item.getGroup().getMiddleImage().getUrlLists().get(0).getUrl();
        JokeGifTagImageView jokeGifTagImageView = helper.getView(R.id.iv_jokeTagImage);
        String largeImageUrl = item.getGroup().getLargeImage().getUrlLists().get(0).getUrl();
        jokeGifTagImageView.setGifUrl(largeImageUrl);
        jokeGifTagImageView.setImage(middleImageUrl, middleWidth, middleHeight);
    }

}
