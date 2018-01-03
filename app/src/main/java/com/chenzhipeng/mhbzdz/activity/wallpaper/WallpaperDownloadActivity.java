
package com.chenzhipeng.mhbzdz.activity.wallpaper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.wallpaper.WallpaperListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.presenter.wallpaper.WallpaperDownloadPresenter;
import com.chenzhipeng.mhbzdz.view.wallpaper.IWallpaperDownloadView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class WallpaperDownloadActivity extends BaseActivity implements IWallpaperDownloadView, BaseQuickAdapter.OnItemChildClickListener, BottomCheckedView.Listener {
    @BindView(R.id.rv_wallpaperDownload)
    RecyclerView recyclerView;
    private WallpaperDownloadPresenter presenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.BottomCheckedView)
    BottomCheckedView bottomCheckedView;
    @BindView(R.id.AppBarLayout)
    AppBarLayout appBarLayout;


    private WallpaperDownloadPresenter getPresenter() {
        if (presenter == null) {
            presenter = new WallpaperDownloadPresenter(this);
        }
        return presenter;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WallpaperDownloadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_download);
        ButterKnife.bind(this);
        setToolbar(toolbar, getString(R.string.download), true);
        bottomCheckedView.setListener(this);
        getPresenter().initData();
    }

    @Override
    public <T> void onAdapter(T data) {
        WallpaperListAdapter adapter = (WallpaperListAdapter) data;
        if (adapter != null) {
            adapter.setOnItemChildClickListener(this);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showDeleteDialog() {
        new AlertDialog.Builder(this).setMessage(getString(R.string.sure_delete))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().deleteChecked();
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void finish() {
        if (getPresenter().isShowChecked()) {
            getPresenter().closeMenu();
            return;
        }
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallpaper_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getPresenter().updateMenu(menu, bottomCheckedView);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_editWallpaper:
                getPresenter().edit();
                break;
            case R.id.item_delete:
                getPresenter().deleteChecked();
                break;
            case R.id.item_select:
                getPresenter().allChecked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WallpaperItemBean wallpaperItemBean = (WallpaperItemBean) adapter.getData().get(position);
        if (wallpaperItemBean.isShowChecked()) {
            AppCompatCheckBox checkBox = view.findViewById(R.id.cb_itemWallpaperPicture);
            boolean checked = wallpaperItemBean.isChecked();
            checkBox.setChecked(!checked);
            wallpaperItemBean.setChecked(!checked);
            invalidateOptionsMenu();
        } else {
            WallpaperPictureActivity.startActivity(this, adapter.getData(), position);
        }
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
