package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.widget.ComicDownloadDataView;

import java.util.ArrayList;
import java.util.List;


public class ComicDownloadDataListAdapter extends BaseQuickAdapter<ComicDownloadBean, BaseViewHolder> {
    private List<View> viewList = new ArrayList<>();

    public ComicDownloadDataListAdapter(@LayoutRes int layoutResId, @Nullable List<ComicDownloadBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicDownloadBean item) {
        ComicDownloadDataView comicDownloadDataView = helper.getView(R.id.cddv_itemComicDownloadDetails);
        helper.addOnClickListener(comicDownloadDataView.getOnClickListenerId());
        comicDownloadDataView.setShowChecked(item.isShowChecked());
        comicDownloadDataView.setCheckBox(item.isChecked());
        comicDownloadDataView.setNameText(item.getChapterName());
        comicDownloadDataView.setPageText(item.getPage());
        comicDownloadDataView.setMaxProgress(item.getMaxProgress());
        comicDownloadDataView.setStateText(item.getState());
        comicDownloadDataView.setProgress(item.getProgress());
        comicDownloadDataView.setNameTextColor(item.isRead() ? ConfigUtils.getChoiceToAppColor() : R.color.black);

        comicDownloadDataView.setTag(helper.getAdapterPosition());
        viewList.add(comicDownloadDataView);
    }

    public List<View> getViewList() {
        return viewList;
    }

}
