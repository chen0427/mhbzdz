package com.chenzhipeng.mhbzdz.adapter.joke;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chenzhipeng.mhbzdz.fragment.joke.JokeTypeFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/10/2.
 */

public class JokeViewPagerAdapter extends FragmentPagerAdapter {
    private List<JokeTypeFragment> fragments;
    private List<String> titleStrings;

    public JokeViewPagerAdapter(FragmentManager fm, List<JokeTypeFragment> fragments, List<String> titleStrings) {
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