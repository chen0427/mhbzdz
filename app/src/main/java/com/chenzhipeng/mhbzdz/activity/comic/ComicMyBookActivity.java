package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicMyBookViewPagerAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicMyBookPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicMyBookView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicMyBookActivity extends BaseActivity implements IComicMyBookView, ViewPager.OnPageChangeListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tl_myBook)
    TabLayout tabLayout;
    @BindView(R.id.vp_myBook)
    ViewPager viewPager;
    private ComicMyBookPresenter presenter;


    public static void startActivity(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ComicMyBookActivity.class));
        }
    }

    private ComicMyBookPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicMyBookPresenter(this);
        }
        return presenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_my_book);
        ButterKnife.bind(this);
        setToolbar(toolbar, getString(R.string.bookshelf), true);
        getPresenter().initAdapter();
    }

    @Override
    public <T> void onAdapter(T data) {
        ComicMyBookViewPagerAdapter adapter = (ComicMyBookViewPagerAdapter) data;
        if (adapter != null) {
            viewPager.setOffscreenPageLimit(2);
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.no_select), Color.WHITE);
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getPresenter().setFragmentPosition(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_editComic:
                getPresenter().edit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comic_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getPresenter().updateMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void finish() {
        if (getPresenter().clseMenu()) {
            return;
        }
        super.finish();
    }
}
