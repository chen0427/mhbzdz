package com.chenzhipeng.mhbzdz.adapter.comic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class ComicMyBookViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titleStrings;

    public ComicMyBookViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titleStrings) {
        super(fm);
        this.fragments = fragments;
        this.titleStrings = titleStrings;
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
