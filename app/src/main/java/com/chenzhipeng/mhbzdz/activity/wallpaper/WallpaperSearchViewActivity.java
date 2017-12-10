package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.utils.SPUtils;
import com.chenzhipeng.mhbzdz.widget.AppSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperSearchViewActivity extends BaseActivity implements AppSearchView.Listener {
    @BindView(R.id.csv_wallpaperSearchView)
    AppSearchView searchView;
    private static final String WALLPAPER_SEARCH_RECORD = "wallpaper_search_record";


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WallpaperSearchViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TM);
        setContentView(R.layout.activity_wallpaper_search_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, ConfigUtils.getChoiceToAppColor()));
        }
        ButterKnife.bind(this);
        searchView.setToolbar(this);
        searchView.setListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSearchRecord();
    }

    @Override
    public void doSearch(String str) {
        WallpaperSearchDataActivity.startActivity(this, str);
        saveSearchRecord(str);
    }

    @Override
    public void onClickItem(String str) {
        WallpaperSearchDataActivity.startActivity(this, str);
    }

    @Override
    public void onDeleteItem(String str) {
        deleteSearchRecord(str);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    public void getSearchRecord() {
        List<String> list = (List<String>) SPUtils.get(WALLPAPER_SEARCH_RECORD);
        searchView.setRecordData(list);
    }

    /**
     * 保存搜索记录
     *
     * @param s
     */
    public void saveSearchRecord(String s) {
        if (!TextUtils.isEmpty(s)) {
            List<String> list = (List<String>) SPUtils.get(WALLPAPER_SEARCH_RECORD);
            if (EmptyUtils.isListsEmpty(list)) {
                List<String> strings = new ArrayList<>();
                strings.add(s);
                SPUtils.put(WALLPAPER_SEARCH_RECORD, strings);
            } else {
                if (!list.contains(s)) {
                    list.add(s);
                    SPUtils.put(WALLPAPER_SEARCH_RECORD, list);
                }
            }
        }
    }

    /**
     * 删除搜索记录
     *
     * @param s
     */
    public void deleteSearchRecord(String s) {
        if (!TextUtils.isEmpty(s)) {
            List<String> list = (List<String>) SPUtils.get(WALLPAPER_SEARCH_RECORD);
            if (!EmptyUtils.isListsEmpty(list)) {
                list.remove(s);
                SPUtils.put(WALLPAPER_SEARCH_RECORD, list);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
