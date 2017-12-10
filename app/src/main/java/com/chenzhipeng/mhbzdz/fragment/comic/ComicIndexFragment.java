package com.chenzhipeng.mhbzdz.fragment.comic;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicMyBookActivity;
import com.chenzhipeng.mhbzdz.activity.comic.ComicSearchViewActivity;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicIndexPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicIndexView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicIndexFragment extends BaseFragment implements IComicIndexView {
    private View fragmentView;
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.tl_comic_index)
    TabLayout tabLayout;
    @BindView(R.id.vp_comic_index)
    ViewPager viewPager;
    private ComicIndexPresenter presenter;


    private ComicIndexPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicIndexPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_index, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbar(toolbar, getString(R.string.comic), true);
        showNavigationViewIconMenu();
        getPresenter().initData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.comic_index, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                clickDrawerLayout();
                break;
            case R.id.item_comicBook:
                ComicMyBookActivity.startActivity(getActivity());
                break;
            case R.id.item_comicSearch:
                ComicSearchViewActivity.startActivity(getActivity());
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public <T> void onAdapter(T data) {
        viewPager.setAdapter((PagerAdapter) data);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.gray_cc), Color.WHITE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void clickDrawerLayout() {
        DrawerLayout drawerLayout = getDrawerLayout();
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }
}
