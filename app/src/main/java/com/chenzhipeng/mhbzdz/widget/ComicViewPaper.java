package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.sqlite.AppDatabase;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;


public class ComicViewPaper extends FrameLayout {
    public static final int MOVE_LEFT = 0;
    public static final int MOVE_RIGHT = 1;
    private List<View> viewList = new ArrayList<>();
    private List<ComicItemPicture> pictures = new ArrayList<>();
    private SuperViewPager viewPager;
    private String comicId;
    private String comicName;
    private Listener listener;
    private Adapter adapter;
    private boolean isDragPage;
    private AppCompatTextView textView3;
    private AppCompatTextView textView1;
    private AppCompatTextView textView2;
    private RelativeLayout layout;

    private int readPosition = 0;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ComicViewPaper(@NonNull Context context) {
        super(context);
        init();
    }

    public ComicViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ComicViewPaper(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.customview_comic_view_paper, this);
        viewPager = findViewById(R.id.SuperViewPager);
        textView1 = findViewById(R.id.AppCompatTextView1);
        textView2 = findViewById(R.id.AppCompatTextView2);
        textView3 = findViewById(R.id.AppCompatTextView3);
        layout = findViewById(R.id.RelativeLayout);
        textView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    Object tag = textView3.getTag();
                    if (tag != null) {
                        listener.move((int) tag);
                    }

                }
            }
        });
    }

    public void updateData(List<ComicItemPicture> pictures) {
        if (adapter != null && !EmptyUtils.isListsEmpty(pictures)) {
            this.pictures = pictures;
            adapter.notifyDataSetChanged();
            this.readPosition = 0;
            viewPager.setCurrentItem(readPosition, false);
            setBottom(pictures.get(readPosition));
        }
    }

    public void setData(String comicId, String comicName, final List<ComicItemPicture> pictures) {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName) && !EmptyUtils.isListsEmpty(pictures)) {
            this.pictures = pictures;
            this.comicId = comicId;
            this.comicName = comicName;
            checkReadHistory();
            viewPager.setAdapter(adapter = new Adapter());
            setBottom(pictures.get(readPosition));
            viewPager.setCurrentItem(readPosition, false);
            viewPager.addOnPageChangeListener(new OnPageChangeListener());
        }
    }

    private void checkReadHistory() {
        if (!EmptyUtils.isStringsEmpty(comicId, comicName) && !EmptyUtils.isListsEmpty(pictures)) {
            String pictureUrl = AppDatabase.getInstance().getHistoryPictureUrl(comicId, pictures.get(0).getChapterName());
            if (!TextUtils.isEmpty(pictureUrl)) {
                for (int i = 0; i < pictures.size(); i++) {
                    if (pictureUrl.equals(pictures.get(i).getUrl())) {
                        this.readPosition = i;
                        break;
                    }
                }
            }
        }
    }

    private class Adapter extends PagerAdapter implements OnClickListener {
        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ComicReadPictureView pictureView;
            if (viewList.size() > 0) {
                pictureView = (ComicReadPictureView) viewList.get(0);
                viewList.remove(0);
            } else {
                pictureView = new ComicReadPictureView(getContext());
            }
            pictureView.setOnClickListener(this);
            ComicItemPicture picture = pictures.get(position);
            pictureView.setImage(picture.getUrl(), comicId, comicName, picture.getChapterName());
            container.addView(pictureView);
            return pictureView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 0) {
                container.removeView((View) object);
            } else {
                container.removeView((View) object);
                if (object != null) {
                    viewList.add((View) object);
                }
            }

        }

        @Override
        public int getItemPosition(Object object) {
            // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
            return POSITION_NONE;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick();
            }
        }
    }


    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (isDragPage && position == 0 && positionOffsetPixels == 0) {
                //左滑到边缘---上一章
                if (listener != null) {
                    listener.onMove(MOVE_LEFT);
                }
            } else if (isDragPage && position == pictures.size() - 1 && positionOffsetPixels == 0) {
                //右滑到边缘---下一章
                if (listener != null) {
                    listener.onMove(MOVE_RIGHT);
                }
            } else {
                if (layout.getVisibility() == VISIBLE) {
                    textHide();
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (!EmptyUtils.isListsEmpty(pictures)) {
                ComicItemPicture picture = pictures.get(position);
                setBottom(picture);
                if (listener != null) {
                    listener.onPageSelected(position);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            isDragPage = state == 1;
        }
    }

    public void left() {
        textView3.setTag(MOVE_LEFT);
        if (layout.getVisibility() == GONE) {
            textView3.setText(R.string.click_up);
            textShow();
        }
    }

    public void right() {
        textView3.setTag(MOVE_RIGHT);
        if (layout.getVisibility() == GONE) {
            textView3.setText(R.string.click_down);
            textShow();
        }
    }

    private void setBottom(ComicItemPicture picture) {
        if (picture != null) {
            textView1.setText(picture.getChapterName() + "   " + picture.getCurrentNumber() + " / " + picture.getEndNumber());
            textView2.setText(comicName);
            AppDatabase.getInstance().changeHistory(comicId, comicName, picture.getChapterName(), picture.getUrl());
        }
    }


    public void volumeUp() {
        if (!EmptyUtils.isListsEmpty(pictures)) {
            int a = readPosition - 1;
            if (a >= 0) {
                readPosition = a;
                viewPager.setCurrentItem(readPosition);
            } else if (a < 0) {
                textView3.setTag(MOVE_LEFT);
                textView3.setText(R.string.click_up);
                textShow();
            }
        }
    }


    public void volumeDown() {
        if (!EmptyUtils.isListsEmpty(pictures)) {
            int currentItem = viewPager.getCurrentItem();
            int b = currentItem + 1;
            if (b <= pictures.size() - 1) {
                readPosition = b;
                viewPager.setCurrentItem(readPosition);
            } else if (b == pictures.size()) {
                textView3.setTag(MOVE_RIGHT);
                textView3.setText(R.string.click_down);
                textShow();
            }
        }
    }

    public void textShow() {
        if (layout.getVisibility() == GONE) {
            layout.setVisibility(VISIBLE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(200);
            layout.startAnimation(animationSet);
        }
    }

    public void textHide() {
        if (layout.getVisibility() == VISIBLE) {
            layout.setVisibility(GONE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(200);
            layout.startAnimation(animationSet);
        }
    }

    public void showNoChapter() {
        if (layout.getVisibility() == GONE) {
            layout.setVisibility(VISIBLE);
            textView3.setText(getContext().getString(R.string.no_chapter));
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(200);
            layout.startAnimation(animationSet);
        }
    }

    public interface Listener {
        void move(int i);

        void onMove(int i);

        void onClick();

        void onPageSelected(int position);
    }
}
