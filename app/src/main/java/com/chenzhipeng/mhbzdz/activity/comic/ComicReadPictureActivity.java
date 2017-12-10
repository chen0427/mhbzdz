package com.chenzhipeng.mhbzdz.activity.comic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicPictureListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicReadPicturePresenter;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicPictureView;
import com.chenzhipeng.mhbzdz.widget.rvp.RecyclerViewPager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("unchecked")
public class ComicReadPictureActivity extends BaseActivity
        implements IComicPictureView {
    public static final String KEY_INTENT_1 = "key_intent_1";
    public static final String KEY_INTENT_2 = "key_intent_2";
    public static final String KEY_INTENT_3 = "type";
    public static final int TYPE_CHAPTER = 0;
    public static final int TYPE_DOWNLOAD = 1;
    @BindView(R.id.AppCompatTextView_1)
    AppCompatTextView chapterNameTextView;
    @BindView(R.id.AppCompatTextView_2)
    AppCompatTextView comicNameTextView;
    @BindView(R.id.RecyclerViewPager)
    RecyclerViewPager recyclerViewPager;
    @BindView(R.id.root)
    RelativeLayout relativeLayout;
    private ComicReadPicturePresenter presenter;

    private int readPosition = 0;


    public static void startActivity(Context context, int selectChapter) {
        if (context != null) {
            Intent intent = new Intent(context, ComicReadPictureActivity.class);
            intent.putExtra(KEY_INTENT_2, selectChapter);
            intent.putExtra(KEY_INTENT_3, TYPE_CHAPTER);
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, List<ComicItemPicture> pictures, String[] comicIdAndComicName) {
        if (context != null && !EmptyUtils.isListsEmpty(pictures) && comicIdAndComicName != null && comicIdAndComicName.length == 3) {
            Intent intent = new Intent(context, ComicReadPictureActivity.class);
            intent.putExtra(KEY_INTENT_1, (Serializable) pictures);
            intent.putExtra(KEY_INTENT_2, comicIdAndComicName);
            intent.putExtra(KEY_INTENT_3, TYPE_DOWNLOAD);
            context.startActivity(intent);
        }
    }

    private ComicReadPicturePresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicReadPicturePresenter(this);
        }
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_picture);
        setStatusBarColor(Color.BLACK);
        ButterKnife.bind(this);
        getPresenter().updateHorizontalList();
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }


    @Override
    public <T> void onAdapter(T data, int readPosition) {
        if (data != null) {
            ComicPictureListAdapter adapter = (ComicPictureListAdapter) data;
            recyclerViewPager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerViewPager.setScrollSpeed(BaseApplication.SCROLL_SPEED);
            recyclerViewPager.setHasFixedSize(true);
            recyclerViewPager.addOnScrollListener(new RecyclerViewScroll());
            recyclerViewPager.setAdapter(adapter);
            recyclerViewPager.scrollToPosition(readPosition);
            this.readPosition = readPosition;
        }
    }

    @Override
    public void updateBottomBar(String comicId, String comicName, String chapterName, String currentNumber, String endNumber, String pictureUrl) {
        chapterNameTextView.setText(chapterName + "   " + currentNumber + " / " + endNumber);
        comicNameTextView.setText(comicName);
        ComicDatabase.getInstance().changeHistory(comicId, comicName, chapterName, pictureUrl);
    }

    @Override
    public void showWorkDialog() {
        chapterNameTextView.setText(R.string.handle);
    }

    @Override
    public void dismissWorkDialog() {
        chapterNameTextView.setText("");
    }


    /**
     * 监听滑动到第几页
     */
    private class RecyclerViewScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    readPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    getPresenter().updateBottomBar(readPosition);
                }
            }
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (ConfigUtils.getVolumePage()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (readPosition != 0) {
                        int a = readPosition - 1;
                        if (a >= 0) {
                            readPosition = a;
                            recyclerViewPager.smoothScrollToPosition(readPosition);
                        }
                    }
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    int itemCount = recyclerViewPager.getAdapter().getItemCount();
                    if (readPosition != itemCount - 1) {
                        int b = readPosition + 1;
                        if (b <= itemCount - 1) {
                            readPosition = b;
                            recyclerViewPager.smoothScrollToPosition(readPosition);
                        }
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
