package com.chenzhipeng.mhbzdz.presenter.joke;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.joke.JokeViewPagerAdapter;
import com.chenzhipeng.mhbzdz.fragment.joke.JokeIndexFragment;
import com.chenzhipeng.mhbzdz.fragment.joke.JokeTypeFragment;
import com.chenzhipeng.mhbzdz.utils.JokeApiUtils;
import com.chenzhipeng.mhbzdz.view.joke.IJokeIndexView;

import java.util.ArrayList;
import java.util.List;


public class JokeIndexPresenter {
    private Fragment fragment;
    private IJokeIndexView iJokeIndexView;

    public JokeIndexPresenter(Fragment fragment) {
        this.fragment = fragment;
        this.iJokeIndexView = (IJokeIndexView) fragment;
    }

    public void initAdapter() {
        List<JokeTypeFragment> fragments = new ArrayList<>();
        List<String> titleStrings = new ArrayList<>();
        JokeTypeFragment textNeiHanTypeFragment = new JokeTypeFragment();
        textNeiHanTypeFragment.setArguments(getBundle(JokeApiUtils.TEXT));
        JokeTypeFragment pictureNeiHanTypeFragment = new JokeTypeFragment();
        pictureNeiHanTypeFragment.setArguments(getBundle(JokeApiUtils.PICTURE));
        JokeTypeFragment videoNeiHanTypeFragment = new JokeTypeFragment();
        videoNeiHanTypeFragment.setArguments(getBundle(JokeApiUtils.VIDEO));
        JokeTypeFragment recommendNeiHanTypeFragment = new JokeTypeFragment();
        recommendNeiHanTypeFragment.setArguments(getBundle(JokeApiUtils.RECOMMEND));
        fragments.add(recommendNeiHanTypeFragment);
        fragments.add(pictureNeiHanTypeFragment);
        fragments.add(videoNeiHanTypeFragment);
        fragments.add(textNeiHanTypeFragment);
        titleStrings.add(fragment.getActivity().getString(R.string.recommend));
        titleStrings.add(fragment.getActivity().getString(R.string.picture));
        titleStrings.add(fragment.getActivity().getString(R.string.video));
        titleStrings.add(fragment.getActivity().getString(R.string.joke));
        JokeViewPagerAdapter adapter = new JokeViewPagerAdapter(fragment.getChildFragmentManager(), fragments, titleStrings);
        iJokeIndexView.onAdapter(adapter);
    }

    private Bundle getBundle(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(JokeIndexFragment.BUNDLE_KEY, type);
        return bundle;
    }
}
