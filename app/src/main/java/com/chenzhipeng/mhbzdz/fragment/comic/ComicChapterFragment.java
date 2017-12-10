package com.chenzhipeng.mhbzdz.fragment.comic;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicReadPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicChapterListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicChapterPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicChapterView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class ComicChapterFragment extends BaseFragment
        implements View.OnClickListener, IComicChapterView, BaseQuickAdapter.OnItemChildClickListener {
    private View fragmentView;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.LinearLayout2)
    LinearLayout typeChapterLinearLayout;
    @BindView(R.id.TextView1)
    TextView positiveSequenceTextView;
    @BindView(R.id.TextView2)
    TextView reverseTextView;
    @BindView(R.id.ImageButton)
    ImageButton orderImageButton;
    private ComicChapterPresenter presenter;
    private boolean haveCheckedHistoryRecord = false;
    public static List<ComicChapterItemBean> comicChapterItemBeanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_chapter, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        positiveSequenceTextView.setOnClickListener(this);
        reverseTextView.setOnClickListener(this);
        orderImageButton.setOnClickListener(this);
        getPresenter().initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (haveCheckedHistoryRecord) {
            getPresenter().checkedHistoryRecord(true);
            haveCheckedHistoryRecord = false;
        }

    }

    private ComicChapterPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicChapterPresenter(this);
        }
        return presenter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AppCompatTextView1:
                //连载 番外 等
                getPresenter().clickChapterType(view);
                break;
            case R.id.TextView1:
                //正序
                getPresenter().positiveSequence();
                break;
            case R.id.TextView2:
                //倒序
                getPresenter().reverser();
                break;
            case R.id.ImageButton:
                getPresenter().order();
                break;
        }
    }


    @Override
    public void addChapterType(View view, AppCompatTextView textView) {
        if (view != null && textView != null) {
            textView.setOnClickListener(this);
            typeChapterLinearLayout.addView(view);
        }
    }

    @Override
    public <T> void onAdapter(T data) {
        if (data != null) {
            ComicChapterListAdapter adapter = (ComicChapterListAdapter) data;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemChildClickListener(this);
            comicChapterItemBeanList = adapter.getData();

        }
    }

    /**
     * 更改章节类型按钮状态
     *
     * @param position
     */
    @Override
    public void updateBarForLeft(int position) {
        int childCount = typeChapterLinearLayout.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View childAt = typeChapterLinearLayout.getChildAt(i);
                if (childAt != null) {
                    LinearLayout layout = childAt.findViewById(R.id.LinearLayout);
                    if (layout != null) {
                        AppCompatTextView textView = layout.findViewById(R.id.AppCompatTextView1);
                        if (textView != null) {
                            textView.setTextColor(i == position ? getPresenter().getColor() : Color.BLACK);
                        }
                        View view = layout.findViewById(R.id.View);
                        if (view != null) {
                            view.setVisibility(i == position ? View.VISIBLE : View.GONE);
                        }
                    }
                }
            }
        }
    }

    /**
     * 更改顺序按钮状态
     *
     * @param isReverse
     */
    @Override
    public void updateBarForRight(boolean isReverse) {
        if (isReverse) {
            reverseTextView.setTextColor(Color.RED);
            positiveSequenceTextView.setTextColor(Color.BLACK);
        } else {
            reverseTextView.setTextColor(Color.BLACK);
            positiveSequenceTextView.setTextColor(Color.RED);
        }
    }


    @Override
    public void setFabText(String s) {
        ComicDetailsActivity activity = (ComicDetailsActivity) getActivity();
        if (activity != null && !TextUtils.isEmpty(s)) {
            activity.getFabTextView().setText(s);
        }
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        haveCheckedHistoryRecord = true;
        ComicReadPictureActivity.startActivity(getActivity(), position);
    }

    /**
     * 跳到历史记录
     */
    public void startActivityToHistory() {
        haveCheckedHistoryRecord = true;
        getPresenter().startActivityToHistory();
    }

    /**
     * 跳到下载界面
     */
    public void startDownloadListActivity() {
        haveCheckedHistoryRecord = true;
        getPresenter().startDownloadListActivity();
    }

}
