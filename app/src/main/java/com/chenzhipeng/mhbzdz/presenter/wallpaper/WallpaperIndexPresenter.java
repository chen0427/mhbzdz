package com.chenzhipeng.mhbzdz.presenter.wallpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperViewPagerAdapter;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperIndexFragment;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperTypeFragment;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperIndexView;

import java.util.ArrayList;
import java.util.List;


public class WallpaperIndexPresenter {
    private Fragment fragment;
    private IWallpaperIndexView wallpaperView;


    public WallpaperIndexPresenter(Fragment fragment) {
        this.fragment = fragment;
        this.wallpaperView = (IWallpaperIndexView) fragment;
    }

    public void initAdapter() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titleStrings = new ArrayList<>();
        titleStrings.add(fragment.getString(R.string.news));
        titleStrings.add(fragment.getString(R.string.hot));
        titleStrings.add(fragment.getString(R.string.classification));

        WallpaperTypeFragment newFragment = new WallpaperTypeFragment();
        newFragment.setArguments(getBundle(WallpaperIndexFragment.TYPE_NEW));
        WallpaperTypeFragment hotFragment = new WallpaperTypeFragment();
        hotFragment.setArguments(getBundle(WallpaperIndexFragment.TYPE_HOT));
        WallpaperTypeFragment categoryFragment = new WallpaperTypeFragment();
        categoryFragment.setArguments(getBundle(WallpaperIndexFragment.TYPE_CLASSIFY));

        fragments.add(newFragment);
        fragments.add(hotFragment);
        fragments.add(categoryFragment);
        WallpaperViewPagerAdapter adapter = new WallpaperViewPagerAdapter(fragment.getChildFragmentManager(), fragments, titleStrings);
        wallpaperView.onAdapter(adapter);
    }

    private Bundle getBundle(int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(WallpaperIndexFragment.KEY_BUNDLE, value);
        return bundle;
    }
}
