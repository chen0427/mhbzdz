package com.chenzhipeng.mhbzdz.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.MainActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

public class BaseFragment extends RxFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    protected DrawerLayout getDrawerLayout() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getDrawerLayout();
    }

    protected void setToolbar(Toolbar toolbar, @Nullable String title, boolean isShowLeftTopIcon) {
        if (toolbar == null) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }
        RxAppCompatActivity activity = (RxAppCompatActivity) getActivity();
        if (activity == null) {
            return;
        }
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isShowLeftTopIcon);
        }
    }

    protected void showNavigationViewIconMenu() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        DrawerLayout drawerLayout = activity.getDrawerLayout();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    protected void removeFragmentView(View childView) {
        if (childView != null) {
            ViewParent parent = childView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(childView);
            }
        }
    }


    protected void showToast(String s) {
        if (!TextUtils.isEmpty(s)) {
            Toast.makeText(BaseApplication.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }


}
