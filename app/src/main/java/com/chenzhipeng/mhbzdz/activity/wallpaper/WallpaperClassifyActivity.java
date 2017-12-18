package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperClassifyItemBean;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperClassifyFragment;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperClassifyActivity extends BaseActivity {
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.tl)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager viewPager;
    public static final String KEY_BUNDLE_1 = "key_bundle_1";
    public static final String KEY_BUNDLE_2 = "key_bundle_2";
    public static final int TYPE_NEW = 1;
    public static final int TYPE_HOT = 2;
    private List<WallpaperClassifyFragment> fragments;
    private List<String> titleStrings;


    public static void startActivity(Activity activity, WallpaperClassifyItemBean bean) {
        Intent intent = new Intent(activity, WallpaperClassifyActivity.class);
        SuperIntent.getInstance().put(SuperIntent.S9, bean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_classify);
        ButterKnife.bind(this);
        WallpaperClassifyItemBean itemBean = (WallpaperClassifyItemBean) SuperIntent.getInstance().get(SuperIntent.S9);
        if (itemBean == null) {
            return;
        }
        toolbar.setTitle(itemBean.getName());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WallpaperClassifyFragment newFragment = new WallpaperClassifyFragment();
        newFragment.setArguments(getBundle(itemBean.getId(), TYPE_NEW));

        WallpaperClassifyFragment hotFragment = new WallpaperClassifyFragment();
        hotFragment.setArguments(getBundle(itemBean.getId(), TYPE_HOT));

        fragments = new ArrayList<>();
        fragments.add(newFragment);
        fragments.add(hotFragment);
        titleStrings = new ArrayList<>();
        titleStrings.add(getString(R.string.news));
        titleStrings.add(getString(R.string.hot));

        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.no_select), Color.WHITE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    private class Adapter extends FragmentPagerAdapter {

        Adapter(FragmentManager fm) {
            super(fm);
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

    private Bundle getBundle(String value, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_1, value);
        bundle.putInt(KEY_BUNDLE_2, type);
        return bundle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        SuperIntent.getInstance().remove(SuperIntent.S9);
        super.finish();
    }
}
