package com.chenzhipeng.mhbzdz.fragment.comic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicTypeActivity;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicIntroducePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicIntroduceView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 动漫详情
 */
public class ComicIntroduceFragment extends BaseFragment
        implements IComicIntroduceView, ExpandableTextView.OnExpandStateChangeListener, View.OnClickListener {
    private View fragmentView;
    @BindView(R.id.AppCompatTextView1)
    AppCompatTextView authorTextView;
    @BindView(R.id.AppCompatTextView2)
    AppCompatTextView lastChapterTextView;
    @BindView(R.id.AppCompatTextView3)
    AppCompatTextView updateTimeTextView;
    @BindView(R.id.LinearLayout1)
    LinearLayout tagLinearLayout;
    @BindView(R.id.ExpandableTextView)
    ExpandableTextView expandableTextView;
    private ComicIntroducePresenter presenter;
    private LayoutInflater inflater;

    private ComicIntroducePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicIntroducePresenter(this);
        }
        return presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_introduce, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        expandableTextView.setOnExpandStateChangeListener(this);
        inflater = LayoutInflater.from(getActivity());
        getPresenter().initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }

    @Override
    public void onTag(String tagStr, String tagIdStr) {
        if (!EmptyUtils.isStringsEmpty(tagStr, tagIdStr) && inflater != null) {
            View view = inflater.inflate(R.layout.itemview_comic_details_tag, new FrameLayout(getActivity()), false);
            AppCompatTextView textView = view.findViewById(R.id.tv_comicTag);
            textView.setText("#" + tagStr + "#");
            textView.setTextColor(ContextCompat.getColor(getActivity(), ConfigUtils.getChoiceToAppColor()));
            textView.setTag(new Object[]{tagStr, tagIdStr});
            textView.setOnClickListener(this);
            tagLinearLayout.addView(view);
        }
    }

    @Override
    public void onBaseIntroduce(String authorName, String lastChapterName, String desc, String updateTime) {
        if (!TextUtils.isEmpty(authorName)) {
            authorTextView.setText(authorName);
            authorTextView.setTag(authorName);
            authorTextView.setOnClickListener(this);
            authorTextView.setVisibility(View.VISIBLE);
        } else {
            authorTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(lastChapterName)) {
            lastChapterTextView.setText(lastChapterName);
            lastChapterTextView.setOnClickListener(this);
            lastChapterTextView.setVisibility(View.VISIBLE);
        } else {
            lastChapterTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(desc)) {
            expandableTextView.setText(desc);
            expandableTextView.setVisibility(View.VISIBLE);
        } else {
            expandableTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(updateTime)) {
            String time = getString(R.string.update_str) + dateToStrLong(new Date(Long.parseLong(updateTime) * 1000));
            updateTimeTextView.setText(time);
            updateTimeTextView.setVisibility(View.VISIBLE);
        } else {
            updateTimeTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExpandStateChanged(TextView textView, boolean b) {
        if (!b) {
            textView.setMaxLines(3);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_comicTag:
                Object[] tag = (Object[]) view.getTag();
                String title = (String) tag[0];
                String tagId = (String) tag[1];
                ComicTypeActivity.startActivity(getActivity(), title, tagId);
                break;
            case R.id.AppCompatTextView1:
                String s = String.valueOf(view.getTag());
                ComicTypeActivity.startSearch(getActivity(), s);
                break;
            case R.id.AppCompatTextView2:
                ComicDetailsActivity activity = (ComicDetailsActivity) getActivity();
                if (activity != null) {
                    activity.startLast();
                }
                break;
        }
    }

    public String dateToStrLong(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        return formatter.format(date);
    }
}
