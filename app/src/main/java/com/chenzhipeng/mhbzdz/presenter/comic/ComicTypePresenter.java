package com.chenzhipeng.mhbzdz.presenter.comic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicTypeActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicTypeViewPagerAdapter;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicTypeFragment;
import com.chenzhipeng.mhbzdz.view.comic.IComicTypeView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class ComicTypePresenter {
    private RxAppCompatActivity activity;
    private IComicTypeView typeView;
    public static final String KEY_BUNDLE_1 = "key_bundle_1";
    public static final String KEY_BUNDLE_2 = "key_bundle_2";
    /**
     * 人气
     */
    private static final String TAG_CLICK = "click";
    /**
     * 评分
     */
    private static final String TAG_SCORE = "score";
    /**
     * 更新
     */
    private static final String TAG_DATE = "date";

    public ComicTypePresenter(RxAppCompatActivity activity) {
        this.activity = activity;
        this.typeView = (IComicTypeView) activity;
    }

    public void init() {
        Intent intent = activity.getIntent();
        if (intent == null) {
            return;
        }
        String str;
        if (ComicTypeActivity.isSearch) {
            str = intent.getStringExtra(ComicTypeActivity.KEY_INTENT_1);
            typeView.onTitle(str);
        } else {
            typeView.onTitle(intent.getStringExtra(ComicTypeActivity.KEY_INTENT_1));
            str = intent.getStringExtra(ComicTypeActivity.KEY_INTENT_2);
        }
        List<Fragment> fragments = new ArrayList<>();
        List<String> titleStrings = new ArrayList<>();
        titleStrings.add(activity.getString(R.string.popularity));
        titleStrings.add(activity.getString(R.string.score));
        titleStrings.add(activity.getString(R.string.update));
        ComicTypeFragment clickFragment = new ComicTypeFragment();
        clickFragment.setArguments(getBundle(str, TAG_CLICK));
        ComicTypeFragment scoreFragment = new ComicTypeFragment();
        scoreFragment.setArguments(getBundle(str, TAG_SCORE));
        ComicTypeFragment dateFragment = new ComicTypeFragment();
        dateFragment.setArguments(getBundle(str, TAG_DATE));
        fragments.add(clickFragment);
        fragments.add(scoreFragment);
        fragments.add(dateFragment);
        ComicTypeViewPagerAdapter adapter = new ComicTypeViewPagerAdapter(activity.getSupportFragmentManager(), titleStrings, fragments);
        typeView.onAdapter(adapter);
    }

    private Bundle getBundle(String tag, String orderby) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_1, tag);
        bundle.putString(KEY_BUNDLE_2, orderby);
        return bundle;
    }

}
