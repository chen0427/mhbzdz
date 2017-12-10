package com.chenzhipeng.mhbzdz.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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


  /*  protected void showToastToNetworkError() {
        Toast.makeText(BaseApplication.getContext(), R.string.networFail, Toast.LENGTH_SHORT).show();
    }

    protected void showToastToEmptyData() {
        Toast.makeText(BaseApplication.getContext(), R.string.emptyData, Toast.LENGTH_SHORT).show();
    }*/

   /* protected void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }*/

    protected void showSnackbar(String s) {
        if (!TextUtils.isEmpty(s)) {
            View rootView = getActivity().findViewById(android.R.id.content);
            if (rootView != null) {
                Snackbar.make(rootView, s, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


}
