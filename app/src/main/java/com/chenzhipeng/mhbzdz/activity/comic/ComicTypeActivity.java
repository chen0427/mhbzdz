package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicTypePresenter;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicTypeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicTypeActivity extends BaseActivity implements IComicTypeView {
    public static final String KEY_INTENT_1 = "key_intent_1";
    public static final String KEY_INTENT_2 = "key_intent_2";
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.TabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ViewPager)
    ViewPager viewPager;
    private ComicTypePresenter presenter;
    public static boolean isSearch = false;


    public static void startActivity(Context context, String title, String tag) {
        if (context != null && !EmptyUtils.isStringsEmpty(title, tag)) {
            Intent intent = new Intent(context, ComicTypeActivity.class);
            intent.putExtra(KEY_INTENT_1, title);
            intent.putExtra(KEY_INTENT_2, tag);
            context.startActivity(intent);
            isSearch = false;
        }
    }

    public static void startSearch(Context context, String searchKey) {
        if (context != null && !TextUtils.isEmpty(searchKey)) {
            Intent intent = new Intent(context, ComicTypeActivity.class);
            intent.putExtra(KEY_INTENT_1, searchKey);
            context.startActivity(intent);
            isSearch = true;
        }
    }

    private ComicTypePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicTypePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_type);
        ButterKnife.bind(this);
        getPresenter().init();
    }

    @Override
    public <T> void onAdapter(T data) {
        if (data != null) {
            viewPager.setAdapter((PagerAdapter) data);
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.gray_cc), Color.WHITE);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    public void onTitle(String name) {
        if (TextUtils.isEmpty(name)) {
            setToolbar(toolbar, " ", true);
        } else {
            setToolbar(toolbar, name, true);
        }
    }
}
