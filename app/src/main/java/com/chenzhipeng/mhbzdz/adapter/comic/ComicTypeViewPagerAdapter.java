package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class ComicTypeViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titleStrings;

    public ComicTypeViewPagerAdapter(FragmentManager fm, List<String> titleStrings, List<Fragment> fragments) {
        super(fm);
        this.titleStrings = titleStrings;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleStrings.get(position);
    }
}
