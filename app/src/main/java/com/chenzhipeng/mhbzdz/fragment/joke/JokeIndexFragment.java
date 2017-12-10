package com.chenzhipeng.mhbzdz.fragment.joke;


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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.joke.JokeViewPagerAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.presenter.joke.JokeIndexPresenter;
import com.chenzhipeng.mhbzdz.view.joke.IJokeIndexView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeIndexFragment extends BaseFragment implements IJokeIndexView {
    @BindView(R.id.tl)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public static final String BUNDLE_KEY = "type";
    private JokeIndexPresenter presenter;
    private View fragmentView;

    public JokeIndexPresenter getPresenter() {
        if (presenter == null) {
            presenter = new JokeIndexPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_joke_index, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setToolbar(toolbar, getString(R.string.joke), true);
        showNavigationViewIconMenu();
        getPresenter().initAdapter();
    }

    @Override
    public <T> void onAdapter(T data) {
        JokeViewPagerAdapter adapter = (JokeViewPagerAdapter) data;
        if (adapter != null) {
            viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
            viewPager.setAdapter(adapter);
            tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.no_select), Color.WHITE);
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                clickDrawerLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
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
}
