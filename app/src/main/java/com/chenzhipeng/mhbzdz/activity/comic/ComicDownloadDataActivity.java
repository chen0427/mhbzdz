package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicDownloadDataListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicDownloadBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicDownloadDataPresenter;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicDownloadDataView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownloadDataActivity extends BaseActivity implements IComicDownloadDataView, BaseQuickAdapter.OnItemChildClickListener, BottomCheckedView.Listener {
    public static final String KEY_INTENT_1 = "download_data";
    public static final String KEY_INTENT_2 = "download_comicId";
    @BindView(R.id.rv_comicDownloadDetails)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_comicDownloadDetails)
    ProgressBar progressBar;
    private ComicDownloadDataPresenter presenter;
    private AlertDialog dialog;
    private boolean haveCheckedHistoryRecord = false;
    @BindView(R.id.BottomCheckedView)
    BottomCheckedView bottomCheckedView;


    public static void startActivity(Context context, String comicId, List<ComicChapterItemBean> list) {
        if (context != null && !TextUtils.isEmpty(comicId) && !EmptyUtils.isListsEmpty(list)) {
            Intent intent = new Intent(context, ComicDownloadDataActivity.class);
            intent.putExtra(KEY_INTENT_1, (Serializable) list);
            intent.putExtra(KEY_INTENT_2, comicId);
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, String comicId) {
        if (context != null && !TextUtils.isEmpty(comicId)) {
            Intent intent = new Intent(context, ComicDownloadDataActivity.class);
            intent.putExtra(KEY_INTENT_2, comicId);
            context.startActivity(intent);
        }
    }


    private ComicDownloadDataPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicDownloadDataPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_download_task);
        ButterKnife.bind(this);
        setToolbar(toolbar, getString(R.string.task_list), true);
        bottomCheckedView.setListener(this);
        getPresenter().init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveCheckedHistoryRecord) {
            getPresenter().checkedHistoryRecord(true);
            haveCheckedHistoryRecord = false;
        }
    }

    @Override
    public <T> void onAdapter(T data) {
        ComicDownloadDataListAdapter adapter = (ComicDownloadDataListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.getItemAnimator().setChangeDuration(0);
            adapter.setOnItemChildClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void setProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAdapterProgress(int progress, int group) {
        ComicDownloadDataListAdapter adapter = (ComicDownloadDataListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            List<View> viewList = adapter.getViewList();
            if (!EmptyUtils.isListsEmpty(viewList)) {
                for (View view : viewList) {
                    int position = (int) view.getTag();
                    if (position == group) {
                        ProgressBar progressBar = view.findViewById(R.id.pb_customDownloadDetails);
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            progressBar.setProgress(progress);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onAdapterState(String stateStr, int group) {
        ComicDownloadDataListAdapter adapter = (ComicDownloadDataListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            List<View> viewList = adapter.getViewList();
            if (!EmptyUtils.isListsEmpty(viewList)) {
                for (View view : viewList) {
                    int position = (int) view.getTag();
                    if (position == group) {
                        AppCompatTextView stateTextView = view.findViewById(R.id.tv_customDownloadDetailsState);
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            stateTextView.setText(stateStr);
                        }
                    }
                }
            }
        }


    }

    @Override
    public void onAdapterPage(String pageStr, int group) {
        ComicDownloadDataListAdapter adapter = (ComicDownloadDataListAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            List<View> viewList = adapter.getViewList();
            if (!EmptyUtils.isListsEmpty(viewList)) {
                for (View view : viewList) {
                    int position = (int) view.getTag();
                    if (position == group) {
                        AppCompatTextView textView = view.findViewById(R.id.tv_customDownloadDetailsPage);
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            textView.setText(pageStr);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void showAllStartDialog() {
        new AlertDialog.Builder(this).setMessage(R.string.is_all_start).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().allStart();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void showAllPauseDialog() {
        new AlertDialog.Builder(this).setMessage(R.string.is_all_pause).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().allPause();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void showDeleteDialog() {
        new AlertDialog.Builder(this).setMessage(R.string.is_delete_download).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                getPresenter().delete();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    public void showRunningDeleteDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(ComicDownloadDataActivity.this).create();
        }
        dialog.setMessage(getString(R.string.loading_delete));
        dialog.show();
    }

    @Override
    public void dismissRunningDeleteDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getPresenter().updateMenu(menu, bottomCheckedView);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comic_download_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_comicDownloadDetailsPlay:
                getPresenter().play();
                break;
            case R.id.item_comicDownloadDetailsEdit:
                getPresenter().edit();
                break;

            case R.id.item_1:
                getPresenter().startActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ComicDownloadBean comicDownloadBean = (ComicDownloadBean) adapter.getData().get(position);
        if (comicDownloadBean.isShowChecked()) {
            AppCompatCheckBox checkBox = view.findViewById(R.id.cb_customDownloadDetails);
            boolean checked = checkBox.isChecked();
            checkBox.setChecked(!checked);
            comicDownloadBean.setChecked(!checked);
            invalidateOptionsMenu();
        } else if (comicDownloadBean.getState().equals(getStringId(R.string.complete_download))) {
            List<String> urls = comicDownloadBean.getUrls();
            int size = urls.size();
            List<ComicItemPicture> pictures = new ArrayList<>();
            String[] strings = {comicDownloadBean.getComicId(), comicDownloadBean.getComicName(), comicDownloadBean.getChapterName()};
            for (int i = 0; i < size; i++) {
                String url = urls.get(i);
                ComicItemPicture picture = new ComicItemPicture(url, comicDownloadBean.getChapterName(), String.valueOf(i + 1), String.valueOf(size));
                pictures.add(picture);
            }
            haveCheckedHistoryRecord = true;
            ComicReadPictureActivity.startActivity(this, pictures, strings,false);
        } else if (comicDownloadBean.getState().equals(getStringId(R.string.wait_download))) {
            comicDownloadBean.pause();
            invalidateOptionsMenu();
        } else if (comicDownloadBean.getState().equals(getStringId(R.string.running_download))) {
            comicDownloadBean.pause();
            invalidateOptionsMenu();
        } else if (comicDownloadBean.getState().equals(getStringId(R.string.pause_download))) {
            if (getPresenter().isExistDownload()) {
                comicDownloadBean.setState(getStringId(R.string.wait_download));
                adapter.notifyItemChanged(position);
            } else {
                comicDownloadBean.start();
            }
            invalidateOptionsMenu();
        } else if (comicDownloadBean.getState().equals(getStringId(R.string.fail_download))) {
            if (getPresenter().isExistDownload()) {
                comicDownloadBean.setState(getStringId(R.string.wait_download));
                adapter.notifyItemChanged(position);
            } else {
                comicDownloadBean.start();
            }
            invalidateOptionsMenu();
        }
    }

    private String getStringId(int id) {
        return getResources().getString(id);
    }

    @Override
    public void finish() {
        if (getPresenter().closeMenu()) {
            return;
        }
        super.finish();
    }

    @Override
    public void clickDelete() {
        getPresenter().showDeleteDialog();
    }

    @Override
    public void clickChecked() {
        getPresenter().allChecked();
    }
}
