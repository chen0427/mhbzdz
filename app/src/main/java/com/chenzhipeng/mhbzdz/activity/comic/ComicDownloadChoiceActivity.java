package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicDownloadListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterTypeBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicDownloadChoicePresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicDownloadChoiceView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownloadChoiceActivity extends BaseActivity implements IComicDownloadChoiceView, BaseQuickAdapter.OnItemChildClickListener {
    public static final String KEY_INTENT = "ComicChapterTypeBean";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_comicDownloadList)
    RecyclerView recyclerView;
    @BindView(R.id.pb_comicDownloadList)
    ProgressBar progressBar;
    private ComicDownloadChoicePresenter presenter;

    private ComicDownloadChoicePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicDownloadChoicePresenter(this);
        }
        return presenter;
    }

    public static void startActivity(Context context, ComicChapterTypeBean typeBean) {
        if (context != null && typeBean != null) {
            Intent intent = new Intent(context, ComicDownloadChoiceActivity.class);
            intent.putExtra(KEY_INTENT, typeBean);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_download_choice);
        ButterKnife.bind(this);
        setToolbar(toolbar, getString(R.string.download), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().init();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getPresenter().updateMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comic_download_choice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_comicDownloadListAllSelect:
                getPresenter().clickMenuAllSelect();
                break;
            case R.id.item_comicDownloadListSort:
                getPresenter().clickMenuSort();
                break;
            case R.id.item_comicDownloadListDownload:
                getPresenter().startComicDownloadChoiceActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public <T> void onAdapter(T data) {
        ComicDownloadListAdapter adapter = (ComicDownloadListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void setProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEmptyData() {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ComicChapterItemBean chapterItemBean = (ComicChapterItemBean) adapter.getData().get(position);
        AppCompatCheckBox checkBox = view.findViewById(R.id.cb_itemComicDownloadChoice);
        boolean checked = checkBox.isChecked();
        checkBox.setChecked(!checked);
        chapterItemBean.setChecked(!checked);
        getPresenter().checked(chapterItemBean, checkBox.isChecked());
        invalidateOptionsMenu();
    }

}
