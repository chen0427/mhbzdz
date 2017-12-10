package com.chenzhipeng.mhbzdz.fragment.wallpaper;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperDownloadActivity;
import com.chenzhipeng.mhbzdz.activity.wallpaper.WallpaperSearchViewActivity;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperViewPagerAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperIndexPresenter;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperIndexView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperIndexFragment extends BaseFragment implements IWallpaperIndexView {
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.tl)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;
    public static final int TYPE_NEW = 1;
    public static final int TYPE_HOT = 2;
    public static final int TYPE_CLASSIFY = 3;
    public static final String KEY_BUNDLE = "type";

    private WallpaperIndexPresenter presenter;

    private WallpaperIndexPresenter getPresenter() {
        if (presenter == null) {
            presenter = new WallpaperIndexPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallpaper_index, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbar(toolbar, getString(R.string.wallpaper), true);
        showNavigationViewIconMenu();
        getPresenter().initAdapter();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wallpaper_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = getDrawerLayout();
                if (drawerLayout != null) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    } else {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                break;
            case R.id.item_wallpaperSearch:
                WallpaperSearchViewActivity.startActivity(getActivity());
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.item_wallpaperMyDownload:
                WallpaperDownloadActivity.startActivity(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public <T> void onAdapter(T data) {
        WallpaperViewPagerAdapter adapter = (WallpaperViewPagerAdapter) data;
        if (adapter != null) {
            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.no_select), Color.WHITE);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }
}
