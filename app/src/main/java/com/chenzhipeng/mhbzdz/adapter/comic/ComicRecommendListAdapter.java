package com.chenzhipeng.mhbzdz.adapter.comic;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicRecommendTagActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicRecommendTypeBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.ComicApiUtils;

import java.util.List;


@SuppressWarnings("unchecked")
public class ComicRecommendListAdapter extends BaseQuickAdapter<ComicRecommendTypeBean, BaseViewHolder> implements View.OnClickListener {
    private int[] containerId = {R.id.fl_comic_1, R.id.fl_comic_2, R.id.fl_comic_3, R.id.fl_comic_4, R.id.fl_comic_5, R.id.fl_comic_6};
    private Context context;

    public ComicRecommendListAdapter(Context context,
                                     @LayoutRes int layoutResId,
                                     @Nullable List<ComicRecommendTypeBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicRecommendTypeBean item) {
        String tabTitle = item.getTabTitle();
        helper.setText(R.id.tv_manhua_type, tabTitle);
        initItem(helper, item);
        View view = helper.getView(R.id.ll_tabTitle);
        view.setTag(new Object[]{item, helper.getAdapterPosition()});
        view.setOnClickListener(this);
    }

    private void initItem(BaseViewHolder helper, ComicRecommendTypeBean item) {
        helper.setText(R.id.tv_manhua_type, item.getTabTitle());
        List<ComicItemBean> itemBeanList = item.getItemBeanList();
        for (int i = 0; i < containerId.length; i++) {
            ComicItemBean bean = itemBeanList.get(i);
            View view = helper.getView(containerId[i]);
            TextView nameTextView = view.findViewById(R.id.tv_manhua_name);
            TextView flagTextView = view.findViewById(R.id.tv_manhua_flag);
            AppCompatImageView imageView = view.findViewById(R.id.iv_manhua_img);
            ImageHelper.setImage(ComicApiUtils.getComicImg(bean.getComicId()), imageView, R.color.white);
            flagTextView.setText(bean.getLastChapterName());
            nameTextView.setText(bean.getComicName());
            addListeners(view, bean, i);
        }
    }


    private void addListeners(View view, ComicItemBean item, int i) {
        Object[] objects = new Object[]{item, i};
        view.setTag(objects);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_tabTitle) {
            Object[] objects = (Object[]) v.getTag();
            ComicRecommendTypeBean bean = (ComicRecommendTypeBean) objects[0];
            int position = (int) objects[1];
            ComicRecommendTagActivity.startActivity(context, bean, position - 1);
        } else {
            Object[] objects = (Object[]) v.getTag();
            ComicItemBean bean = (ComicItemBean) objects[0];
            ComicDetailsActivity.startActivity(context, bean);
        }
    }

}
