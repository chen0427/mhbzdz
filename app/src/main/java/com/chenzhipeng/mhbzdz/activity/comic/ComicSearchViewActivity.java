package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.widget.AppSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicSearchViewActivity extends BaseActivity implements AppSearchView.Listener {
    @BindView(R.id.csv_comicSearchView)
    AppSearchView searchView;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ComicSearchViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TM);
        setContentView(R.layout.activity_comic_search);
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
        List<String> list = ComicDatabase.getInstance().getSearch();
        searchView.setRecordData(list);
    }

    @Override
    public void doSearch(String str) {
        ComicTypeActivity.startSearch(this, str);
        ComicDatabase.getInstance().insertSearch(str);
    }

    @Override
    public void onClickItem(String str) {
        ComicTypeActivity.startSearch(this, str);
    }

    @Override
    public void onDeleteItem(String str) {
        ComicDatabase.getInstance().deleteSearch(str);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
