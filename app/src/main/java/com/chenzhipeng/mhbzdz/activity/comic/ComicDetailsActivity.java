package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicDetailsPresenter;
import com.chenzhipeng.mhbzdz.utils.DisplayUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicDetailsView;
import com.chenzhipeng.mhbzdz.widget.FabTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDetailsActivity extends BaseActivity implements IComicDetailsView, ViewPager.OnPageChangeListener, View.OnClickListener {
    public static final String KEY_INTENT = "intent_key";
    public static final String KEY_BUNDLE = "key_bundle";
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.TabLayout)
    TabLayout tabLayout;
    @BindView(R.id.ViewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_itemComicPicture)
    ImageView imageView;
    @BindView(R.id.ProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.fb_comicDetailsXk)
    FabTextView fabTextView;
    @BindView(R.id.AppCompatButton)
    AppCompatButton xkButton;
    private ComicDetailsPresenter presenter;
    private boolean isCollection;


    public static void startActivity(Context context, ComicItemBean data) {
        Intent intent = new Intent(context, ComicDetailsActivity.class);
        intent.putExtra(KEY_INTENT, data);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatus();
        setContentView(R.layout.activity_comic_details);
        ButterKnife.bind(this);
        fabTextView.setOnClickListener(this);
        xkButton.setOnClickListener(this);
        getPresenter().updateViewPager();
    }

    /**
     * 获取详情数据
     */
    private ComicDetailsPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicDetailsPresenter(this);
        }
        return presenter;
    }

    @Override
    public void setFabVisibility(boolean b) {
        //暂时不需要fab按钮 不显示出来
        fabTextView.hide();
    }

    @Override
    public void setCollectionStatus(boolean b) {
        isCollection = b;
        invalidateOptionsMenu();
    }

    @Override
    public void setShowProgress(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setToolbar() {
        setToolbar(toolbar, null, true);
        setToolbarHeight();
    }

    @Override
    public void onTitleName(String name) {
        if (!TextUtils.isEmpty(name) && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }
    }


    @Override
    public void onTopImgUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            ImageHelper.setBlurry(url, imageView);
        }
    }

    @Override
    public <T> void onAdapter(T data) {
        if (data != null) {
            viewPager.setAdapter((PagerAdapter) data);
            viewPager.addOnPageChangeListener(this);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }


    @Override
    public void onEmptyData() {
        showSnackbar(getString(R.string.emptyData));
    }

    @Override
    public void onFail(Throwable e) {
        showSnackbar(getString(R.string.networFail));
    }

    @Override
    public void onCollectionToast(String s) {
        showSnackbar(s);
    }

    private void setToolbarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CollapsingToolbarLayout.LayoutParams layoutParams
                    = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            layoutParams.topMargin = DisplayUtils.getStatusBarHeight();
            toolbar.setLayoutParams(layoutParams);
        }
    }


    public void setContinueToSee(String s) {
        if (!TextUtils.isEmpty(s)) {
            xkButton.setText(s);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            MenuItem item = menu.getItem(0);
            if (item != null) {
                item.setIcon(isCollection ? R.drawable.ic_star_white : R.drawable.ic_no_star_white);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_star:
                getPresenter().onClickCollection(isCollection);
                break;
            case R.id.item_download:
                getPresenter().startComicDownloadListActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comic_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getPresenter().setPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AppCompatButton:
                getPresenter().goXk();
                break;
            case R.id.fb_comicDetailsXk:
                getPresenter().goXk();
                break;
        }

    }


    public FabTextView getFabTextView() {
        return fabTextView;
    }

    public void startLast() {
       getPresenter().startLast();
    }
}
