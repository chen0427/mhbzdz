package com.chenzhipeng.mhbzdz.adapter.wallpaper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class WallpaperViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titleStrings;

    public WallpaperViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleStrings) {
        super(fm);
        this.fragments = fragments;
        this.titleStrings = titleStrings;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleStrings.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}