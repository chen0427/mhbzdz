package com.chenzhipeng.mhbzdz.presenter.comic;

import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicIndexViewPagerAdapter;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicClassifyFragment;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicRecommendFragment;
import com.chenzhipeng.mhbzdz.view.comic.IComicIndexView;

import java.util.ArrayList;
import java.util.List;

public class ComicIndexPresenter {
    private Fragment fragment;
    private IComicIndexView indexView;

    public ComicIndexPresenter(Fragment fragment) {
        this.indexView = (IComicIndexView) fragment;
        this.fragment = fragment;
    }


    public void initData() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        ComicRecommendFragment recommendFragment = new ComicRecommendFragment();
        ComicClassifyFragment classifyFragment = new ComicClassifyFragment();
        fragmentList.add(recommendFragment);
        fragmentList.add(classifyFragment);
        titleList.add(fragment.getActivity().getString(R.string.recommend));
        titleList.add(fragment.getActivity().getString(R.string.classification));
        indexView.onAdapter(new ComicIndexViewPagerAdapter(fragment.getChildFragmentManager(), fragmentList, titleList));
    }
}
