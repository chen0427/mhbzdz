package com.chenzhipeng.mhbzdz.fragment.comic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicTypeActivity;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicIntroducePresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicIntroduceView;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 动漫详情
 */
public class ComicIntroduceFragment extends BaseFragment
        implements IComicIntroduceView, ExpandableTextView.OnExpandStateChangeListener, View.OnClickListener {
    private View fragmentView;
    @BindView(R.id.TextView1)
    TextView authorTextView;
    @BindView(R.id.TextView2)
    TextView lastChapterTextView;
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
    public void onBaseIntroduce(String authorName, String lastChapterName, String desc) {
        authorTextView.setText(authorName);
        lastChapterTextView.setText(lastChapterName);
        expandableTextView.setText(desc);
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
        }
    }
}
