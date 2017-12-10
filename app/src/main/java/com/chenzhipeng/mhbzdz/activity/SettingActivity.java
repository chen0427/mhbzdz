package com.chenzhipeng.mhbzdz.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.presenter.SettingPresenter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.image.ImageCacheHelper;
import com.chenzhipeng.mhbzdz.view.ISettingView;
import com.chenzhipeng.mhbzdz.widget.ColorChoiceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends BaseActivity implements View.OnClickListener, ISettingView, ColorChoiceView.Listener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.LinearLayout1)
    LinearLayout indexLinearLayout;
    @BindView(R.id.AppCompatTextView1)
    AppCompatTextView indexTextView;

    @BindView(R.id.LinearLayout2)
    LinearLayout appColorLinearLayout;
    @BindView(R.id.AppCompatTextView2)
    AppCompatTextView chapterSortTextView;


    @BindView(R.id.LinearLayout3)
    LinearLayout chapterSortLinearLayout;
    @BindView(R.id.CircleImageView)
    CircleImageView circleImageView;

    @BindView(R.id.LinearLayout4)
    LinearLayout canNetLinearLayout;
    @BindView(R.id.SwitchCompat)
    SwitchCompat switchCompat;

    @BindView(R.id.LinearLayout5)
    LinearLayout clearImgCacheLayout;
    @BindView(R.id.AppCompatTextView3)
    AppCompatTextView clearImgCacheTextView;


    private AlertDialog appColorAlertDialog;
    private SettingPresenter presenter;


    private SettingPresenter getPresenter() {
        if (presenter == null) {
            presenter = new SettingPresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setToolbar(toolbar, getString(R.string.setting), true);
        initListeners();
        getPresenter().initData();
    }

    private void initListeners() {
        indexLinearLayout.setOnClickListener(this);
        appColorLinearLayout.setOnClickListener(this);
        chapterSortLinearLayout.setOnClickListener(this);
        canNetLinearLayout.setOnClickListener(this);
        switchCompat.setOnCheckedChangeListener(this);
        clearImgCacheLayout.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LinearLayout1:
                getPresenter().showIndexChoiceDialog();
                break;
            case R.id.LinearLayout2:
                getPresenter().showAppColorChoiceDialog();
                break;
            case R.id.LinearLayout3:
                getPresenter().ComicChapterSort();
                break;
            case R.id.LinearLayout4:
                switchCompat.setChecked(!switchCompat.isChecked());
                break;
            case R.id.LinearLayout5:
                String emptyImgData = "0.00KB";
                ImageCacheHelper.clearImageAllCache();
                clearImgCacheTextView.setText(emptyImgData);
                break;
        }
    }

    @Override
    public void showIndexChoiceDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.comic), getString(R.string.wallpaper), getString(R.string.joke)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConfigUtils.setChoiceToIndex(which);
                                BaseApplication.choiceToIndex = which;
                                indexTextView.setText(which == 0 ? getString(R.string.comic)
                                        : which == 1 ? getString(R.string.wallpaper) : getString(R.string.joke));
                            }
                        }).show();
    }

    @Override
    public void showAppColorChoiceDialog() {
        ColorChoiceView colorChoiceView = new ColorChoiceView(this);
        colorChoiceView.setListener(this);
        appColorAlertDialog = new AlertDialog.Builder(this).create();
        appColorAlertDialog.setView(colorChoiceView);
        appColorAlertDialog.show();
    }

    @Override
    public void showComicChapterSort() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.positive_sequence), getString(R.string.reverse)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConfigUtils.setComicChapterSort(which);
                                BaseApplication.comicChapterSort = which;
                                chapterSortTextView.setText(which == 0 ? getString(R.string.positive_sequence) : getString(R.string.reverse));
                            }
                        }).show();
    }

    @Override
    public void setComicChapterSort(int i) {
        chapterSortTextView.setText(i == 0 ? getString(R.string.positive_sequence) : getString(R.string.reverse));
    }

    @Override
    public void setIndexChoice(String s) {
        indexTextView.setText(s);
    }

    @Override
    public void setAppColorChoice(int colorId) {
        circleImageView.setImageResource(colorId);
    }

    @Override
    public void setImgCache(String s) {
        clearImgCacheTextView.setText(s);
    }

    @Override
    public void setVolumePage(boolean b) {
        switchCompat.setChecked(b);
    }


    @Override
    public void onChoiceColorClick(int colorId, int position) {
        circleImageView.setImageResource(colorId);
        ConfigUtils.setChoiceToAppColor(position);
        BaseApplication.choiceToAppColor = colorId;
        appColorAlertDialog.dismiss();
        if (appColorAlertDialog != null) {
            appColorAlertDialog.dismiss();
        }
        getPresenter().initThem();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ConfigUtils.setVolumePage(isChecked);
        BaseApplication.volumePage = isChecked;
    }
}
