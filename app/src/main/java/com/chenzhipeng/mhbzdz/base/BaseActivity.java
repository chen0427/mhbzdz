package com.chenzhipeng.mhbzdz.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ConfigUtils.getChoiceToAppColorThem());
    }


    protected void showSnackbar(String s) {
        if (!TextUtils.isEmpty(s)) {
            Toast.makeText(BaseApplication.getContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    protected void setToolbar(Toolbar toolbar, @Nullable String title, boolean isShowLeftTopIcon) {
        if (toolbar != null) {
            if (!TextUtils.isEmpty(title)) {
                toolbar.setTitle(title);
            } else {
                toolbar.setTitle("");
            }
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(isShowLeftTopIcon);
            }
        }
    }


    protected void transparentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 隐藏状态栏
     */
    protected void hideStatusBar() {
        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }

}
