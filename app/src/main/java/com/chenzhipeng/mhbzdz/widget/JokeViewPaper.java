package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.download.PictureDownloader;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class JokeViewPaper extends FrameLayout implements PictureBottomView.Listener {
    private List<String> list = new ArrayList<>();
    private PictureBottomView pictureBottomView;
    private PictureDownloader downloader;

    private List<View> viewList = new ArrayList<>();
    private SuperViewPager viewPager;
    private Listener listener;
    private int readPosition;
    private RxAppCompatActivity activity;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public JokeViewPaper(@NonNull Context context) {
        super(context);
        init();
    }

    public JokeViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JokeViewPaper(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.customview_joke_view_paper, this);
        viewPager = findViewById(R.id.SuperViewPager);
        pictureBottomView = findViewById(R.id.PictureBottomView);
        pictureBottomView.setHideCenter(true);
        pictureBottomView.setListener(this);
    }

    public PictureDownloader getDownloader() {
        if (downloader == null) {
            downloader = new PictureDownloader(activity);
        }
        return downloader;
    }

    public void setActivity(RxAppCompatActivity activity) {
        this.activity = activity;
    }

    public void setDownload(boolean b) {
        pictureBottomView.setDownload(b);
    }

    public void setData(List<String> list, int position) {
        if (!EmptyUtils.isListsEmpty(list)) {
            this.list = list;
            this.readPosition = position;
            viewPager.setAdapter(new Adapter());
            viewPager.setCurrentItem(readPosition);
            viewPager.addOnPageChangeListener(new OnPageChangeListener());
            updateBottom(readPosition + 1 + "/" + list.size());
        }
    }


    @Override
    public void centerClick() {

    }

    @Override
    public void downloadClick() {
        download();
    }

    private class Adapter extends PagerAdapter implements JokePictureView.Listener {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            JokePictureView pictureView;
            if (viewList.size() > 0) {
                pictureView = (JokePictureView) viewList.get(0);
                viewList.remove(0);
            } else {
                pictureView = new JokePictureView(getContext());
            }
            pictureView.setListener(this);
            pictureView.setImage(list.get(position));
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
        public void click() {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }


    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            readPosition = position;
            String s = position + 1 + "/" + list.size();
            updateBottom(s);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void updateBottom(String s) {
        if (!TextUtils.isEmpty(s)) {
            pictureBottomView.setPageSize(s);
        }
    }

    public void volumeUp() {
        if (!EmptyUtils.isListsEmpty(list)) {
            int a = readPosition - 1;
            if (a >= 0) {
                readPosition = a;
                viewPager.setCurrentItem(readPosition);
            }
        }
    }


    public void volumeDown() {
        if (!EmptyUtils.isListsEmpty(list)) {
            int currentItem = viewPager.getCurrentItem();
            int b = currentItem + 1;
            if (b <= list.size() - 1) {
                readPosition = b;
                viewPager.setCurrentItem(readPosition);
            }
        }
    }

    public void download() {
        if (!EmptyUtils.isListsEmpty(list) && activity != null && !activity.isFinishing()) {
            String url = list.get(readPosition);
            final File file = getFileName(url);
            if (file != null) {
                if (!file.exists()) {
                    getDownloader().setUrl(url)
                            .setFile(file)
                            .setListener(new PictureDownloader.Listener() {
                                @Override
                                public void onStart() {
                                    if (listener != null) {
                                        listener.start();
                                    }
                                }

                                @Override
                                public void onComplete() {
                                    if (listener != null) {
                                        listener.complete();
                                    }
                                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (listener != null) {
                                        listener.error();
                                    }
                                }
                            }).start();
                } else {
                    if (listener != null) {
                        listener.start();
                        listener.complete();
                    }
                }
            }
        }
    }


    private File getFileName(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("webp")) {
                String name = url.replace("http://", "").replace(File.separator, "") + ".jpg";
                return new File(BaseApplication.JOKE_PATH, name);
            } else {
                String name = url.replace("http://", "").replace(File.separator, "") + ".gif";
                return new File(BaseApplication.JOKE_PATH, name);
            }
        }
        return null;
    }

    public interface Listener {
        void start();

        void complete();

        void error();
    }
}
