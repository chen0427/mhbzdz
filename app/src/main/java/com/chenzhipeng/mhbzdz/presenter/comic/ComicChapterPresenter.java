package com.chenzhipeng.mhbzdz.presenter.comic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDownloadChoiceActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicReadPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicChapterListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterTypeBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDetailsBean;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicChapterFragment;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicChapterView;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unchecked")
public class ComicChapterPresenter {
    private ComicChapterFragment fragment;
    private IComicChapterView chapterView;
    private ComicChapterTypeBean currentTypeBean;
    private ComicChapterListAdapter adapter;
    private String comicId;
    private int historyPosition = 0;

    public ComicChapterPresenter(ComicChapterFragment fragment) {
        this.chapterView = fragment;
        this.fragment = fragment;
    }

    public void initData() {
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            ComicDetailsBean bean = (ComicDetailsBean) arguments.getSerializable(ComicDetailsActivity.KEY_BUNDLE);
            if (bean != null) {
                comicId = bean.getComicId();
                List<ComicChapterTypeBean> comicChapterTypeBeanList = bean.getChapterTypeBeanList();
                if (!EmptyUtils.isListsEmpty(comicChapterTypeBeanList)) {
                    addChapterType(comicChapterTypeBeanList);
                    updateList(comicChapterTypeBeanList.get(0));
                }
            }
        }
    }

    /**
     * 倒序
     */
    public void reverser() {
        if (currentTypeBean != null) {
            if (!currentTypeBean.isReverse()) {
                currentTypeBean.setReverse(true);
                ComicChapterFragment.comicChapterItemBeanList = currentTypeBean.getChapterItemBeanList();
                Collections.reverse(ComicChapterFragment.comicChapterItemBeanList);
                updateList(currentTypeBean);
            }
        }
    }

    /**
     * 正序
     */
    public void positiveSequence() {
        if (currentTypeBean != null) {
            if (currentTypeBean.isReverse()) {
                currentTypeBean.setReverse(false);
                ComicChapterFragment.comicChapterItemBeanList = currentTypeBean.getChapterItemBeanList();
                Collections.reverse(ComicChapterFragment.comicChapterItemBeanList);
                updateList(currentTypeBean);
            }
        }
    }

    /**
     * 改变顺序
     */
    public void order() {
        if (currentTypeBean != null) {
            currentTypeBean.setReverse(!currentTypeBean.isReverse());
            ComicChapterFragment.comicChapterItemBeanList = currentTypeBean.getChapterItemBeanList();
            Collections.reverse(ComicChapterFragment.comicChapterItemBeanList);
            updateList(currentTypeBean);
        }
    }


    /**
     * 更新列表
     *
     * @param typeBean
     */
    private void updateList(ComicChapterTypeBean typeBean) {
        if (typeBean != null) {
            currentTypeBean = typeBean;
            ComicChapterFragment.comicChapterItemBeanList = typeBean.getChapterItemBeanList();
            if (!EmptyUtils.isListsEmpty(ComicChapterFragment.comicChapterItemBeanList)) {
                if (adapter == null) {
                    adapter = new ComicChapterListAdapter(R.layout.itemview_comic_chapter, ComicChapterFragment.comicChapterItemBeanList);
                    chapterView.updateBarForRight(typeBean.isReverse());
                    checkedHistoryRecord(false);
                    chapterView.onAdapter(adapter);
                } else {
                    chapterView.updateBarForRight(typeBean.isReverse());
                    checkedHistoryRecord(false);
                    adapter.setNewData(ComicChapterFragment.comicChapterItemBeanList);
                }
            }
        }
    }

    /**
     * 检查是否右历史记录
     */
    public void checkedHistoryRecord(boolean isUpdateAdapter) {
        if (currentTypeBean != null && adapter != null) {
            historyPosition = 0;
            boolean isHaveRead = false;
            ComicChapterFragment.comicChapterItemBeanList = currentTypeBean.getChapterItemBeanList();
            if (!EmptyUtils.isListsEmpty(ComicChapterFragment.comicChapterItemBeanList)) {
                String chapterName = ComicDatabase.getInstance().getHistoryChapterName(comicId);
                if (!TextUtils.isEmpty(chapterName)) {
                    int count = 0;
                    for (ComicChapterItemBean c : ComicChapterFragment.comicChapterItemBeanList) {
                        if (c.getChapterName().equals(chapterName)) {
                            c.setRead(true);
                            isHaveRead = true;
                            historyPosition = count;
                        } else {
                            c.setRead(false);
                        }
                        count++;
                    }
                    if (isUpdateAdapter) {
                        adapter.notifyDataSetChanged();
                    }
                }
                chapterView.setFabText(isHaveRead ? fragment.getString(R.string.continue_read) : fragment.getString(R.string.start_read));
                setContinueToSee(isHaveRead);
            }
        }
    }

    private void setContinueToSee(boolean b) {
        if (b) {
            if (!EmptyUtils.isListsEmpty(ComicChapterFragment.comicChapterItemBeanList)) {
                String chapterName = ComicChapterFragment.comicChapterItemBeanList.get(historyPosition).getChapterName();
                ComicDetailsActivity activity = (ComicDetailsActivity) fragment.getActivity();
                if (activity != null) {
                    activity.setContinueToSee(fragment.getString(R.string.continue_see) + " " + chapterName);
                }
            }
        } else {
            ComicDetailsActivity activity = (ComicDetailsActivity) fragment.getActivity();
            if (activity != null) {
                activity.setContinueToSee(fragment.getString(R.string.start_read));
            }
        }
    }


    /**
     * 添加 连载 番外等类型
     *
     * @param list
     */
    private void addChapterType(List<ComicChapterTypeBean> list) {
        if (!EmptyUtils.isListsEmpty(list)) {
            ComicDetailsActivity activity = (ComicDetailsActivity) fragment.getActivity();
            if (activity != null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                for (int i = 0; i < list.size(); i++) {
                    ComicChapterTypeBean typeBean = list.get(i);
                    View view = inflater.inflate(R.layout.itemview_chapter_type, new FrameLayout(activity), false);
                    AppCompatTextView textView = view.findViewById(R.id.AppCompatTextView1);
                    AppCompatTextView updateTextView = view.findViewById(R.id.AppCompatTextView2);
                    View lineView = view.findViewById(R.id.View);
                    lineView.setVisibility(View.GONE);
                    lineView.setBackgroundColor(Color.BLACK);
                    textView.setText(typeBean.getChapterType());
                    if (!TextUtils.isEmpty(activity.getUpdateTime())) {
                        updateTextView.setText(activity.getUpdateTime());
                    }
                    textView.setTextColor(Color.BLACK);
                    Object[] objects = new Object[]{typeBean, i};
                    textView.setTag(objects);
                    chapterView.addChapterType(view, textView);
                }
            }
        }
    }

    public int getColor() {
        return ContextCompat.getColor(fragment.getActivity(), ConfigUtils.getChoiceToAppColor());
    }


    public void startDownloadListActivity() {
        if (currentTypeBean != null && fragment != null) {
            ComicDetailsActivity activity = (ComicDetailsActivity) fragment.getActivity();
            if (activity != null) {
                ComicDownloadChoiceActivity.startActivity(activity, currentTypeBean);
            }
        }
    }

    public void startActivityToHistory() {
        if (currentTypeBean != null) {
            ComicChapterFragment.comicChapterItemBeanList = currentTypeBean.getChapterItemBeanList();
            if (!EmptyUtils.isListsEmpty(ComicChapterFragment.comicChapterItemBeanList)) {
                if (fragment.getActivity() != null) {
                    ComicReadPictureActivity.startActivity(fragment.getActivity(), historyPosition, currentTypeBean.isReverse());
                }
            }
        }
    }


    public void startReadActivity(int position) {
        if (currentTypeBean != null) {
            ComicReadPictureActivity.startActivity(fragment.getActivity(), position, currentTypeBean.isReverse());
        }
    }

    public void startLast() {
        if (currentTypeBean != null) {
            int size = currentTypeBean.getChapterItemBeanList().size();
            if (currentTypeBean.isReverse()) {
                ComicReadPictureActivity.startActivity(fragment.getActivity(), 0, currentTypeBean.isReverse());
            } else {
                ComicReadPictureActivity.startActivity(fragment.getActivity(), size - 1, currentTypeBean.isReverse());
            }
        }
    }
}
