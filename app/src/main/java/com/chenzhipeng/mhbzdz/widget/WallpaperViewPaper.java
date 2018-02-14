package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
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
import com.chenzhipeng.mhbzdz.bean.wallpaper.WallpaperItemBean;
import com.chenzhipeng.mhbzdz.download.PictureDownloader;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.wallpaper.SetWallpaperHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WallpaperViewPaper extends FrameLayout implements PictureBottomView.Listener {
    private List<WallpaperItemBean> list = new ArrayList<>();
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


    public WallpaperViewPaper(@NonNull Context context) {
        super(context);
        init();
    }

    public WallpaperViewPaper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WallpaperViewPaper(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.customview_wallpaper_view_paper, this);
        viewPager = findViewById(R.id.SuperViewPager);
        pictureBottomView = findViewById(R.id.PictureBottomView);
        pictureBottomView.setHideCenter(false);
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

    public void setData(List<WallpaperItemBean> list, int position) {
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
        setWallpaper();
    }

    @Override
    public void downloadClick() {
        startDownload();
    }

    private class Adapter extends PagerAdapter {
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
            WallpaperPictureView pictureView;
            if (viewList.size() > 0) {
                pictureView = (WallpaperPictureView) viewList.get(0);
                viewList.remove(0);
            } else {
                pictureView = new WallpaperPictureView(getContext());
            }
            if (!TextUtils.isEmpty(list.get(position).getImg())) {
                pictureView.setImage(list.get(position).getImg());
            } else {
                pictureView.setImage(new File(list.get(position).getLocalPath()));
            }
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
        if (!TextUtils.isEmpty(s) && !EmptyUtils.isListsEmpty(list)) {
            pictureBottomView.setPageSize(s);
            WallpaperItemBean bean = list.get(readPosition);
            if (!TextUtils.isEmpty(bean.getImg())) {
                pictureBottomView.setDownload(checkHaveFile(bean.getImg()));
            } else if (!TextUtils.isEmpty(bean.getLocalPath())) {
                pictureBottomView.setDownload(true);
            }
        }
    }

    public void updateDownload() {
        if (!EmptyUtils.isListsEmpty(list)) {
            WallpaperItemBean bean = list.get(readPosition);
            if (!TextUtils.isEmpty(bean.getImg())) {
                pictureBottomView.setDownload(checkHaveFile(bean.getImg()));
            } else if (!TextUtils.isEmpty(bean.getLocalPath())) {
                pictureBottomView.setDownload(true);
            }
        }
    }

    private boolean checkHaveFile(String url) {
        if (!TextUtils.isEmpty(url)) {
            String pictureName = getPictureName(url);
            if (!TextUtils.isEmpty(pictureName)) {
                return getPath(pictureName).exists();
            }
        }
        return false;
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

    private void setWallpaper() {
        if (!EmptyUtils.isListsEmpty(list) && activity != null && !activity.isFinishing()) {
            String img = list.get(readPosition).getImg();
            if (!TextUtils.isEmpty(img)) {
                SetWallpaperHelper.getInstance().setLockScreen(activity, img);
            } else {
                String path = list.get(readPosition).getLocalPath();
                if (!TextUtils.isEmpty(path)) {
                    SetWallpaperHelper.getInstance().setLockScreen(activity, path);
                }
            }
        }
    }

    private void startDownload() {
        if (!EmptyUtils.isListsEmpty(list)) {
            String img = list.get(readPosition).getImg();
            String pictureName = getPictureName(img);
            if (!TextUtils.isEmpty(pictureName)) {
                getDownloader().setUrl(img)
                        .setFile(getPath(pictureName))
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
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (listener != null) {
                                    listener.error();
                                }
                            }
                        }).start();
            }
        }
    }

    private File getPath(String pictureName) {
        return new File(BaseApplication.WALLPAPER_PATH, pictureName + ".jpg");
    }

    private String getPictureName(String url) {
        String pictureName = null;
        if (!TextUtils.isEmpty(url)) {
            String[] split = url.split(File.separator);
            if (split.length >= 4) {
                pictureName = split[3];
            }
        }
        return pictureName;
    }

    public interface Listener {
        void start();

        void complete();

        void error();
    }
}
