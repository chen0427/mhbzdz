package com.chenzhipeng.mhbzdz.presenter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.MainActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicIndexFragment;
import com.chenzhipeng.mhbzdz.fragment.joke.JokeIndexFragment;
import com.chenzhipeng.mhbzdz.fragment.wallpaper.WallpaperIndexFragment;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.view.IMainView;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter {
    private IMainView iMainView;
    private MainActivity activity;
    private ComicIndexFragment comicIndexFragment;
    private JokeIndexFragment jokeIndexFragment;
    private Disposable subscribe;
    private WallpaperIndexFragment wallpaperIndexFragment;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        this.iMainView = activity;
    }


    private void addFragment(Fragment fragment) {
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().add(R.id.fl_container_fragment, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    private void showFragment(Fragment fragment) {
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().show(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().hide(fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }


    public void addComic() {
        hideFragment(wallpaperIndexFragment);
        hideFragment(jokeIndexFragment);
        if (comicIndexFragment != null) {
            showFragment(comicIndexFragment);
        } else {
            comicIndexFragment = new ComicIndexFragment();
            addFragment(comicIndexFragment);
        }
    }

    public void addJoke() {
        hideFragment(comicIndexFragment);
        hideFragment(wallpaperIndexFragment);
        if (jokeIndexFragment != null) {
            showFragment(jokeIndexFragment);
        } else {
            jokeIndexFragment = new JokeIndexFragment();
            addFragment(jokeIndexFragment);
        }
    }

    public void addWallpaper() {
        hideFragment(jokeIndexFragment);
        hideFragment(comicIndexFragment);
        if (wallpaperIndexFragment != null) {
            showFragment(wallpaperIndexFragment);
        } else {
            wallpaperIndexFragment = new WallpaperIndexFragment();
            addFragment(wallpaperIndexFragment);
        }
    }

    public void addFragment() {
        Intent intent = activity.getIntent();
        if (intent != null) {
            int intExtra = intent.getIntExtra(SettingPresenter.KEY_THEM, -1);
            if (intExtra != -1) {
                add(intExtra);
                return;
            }
        }
        add(ConfigUtils.getChoiceToIndex());
    }

    private void add(int i) {
        if (i == 0) {
            MainActivity.index = MainActivity.INDEX_COMIC;
            addComic();
            iMainView.leftItemSelected(i);
        } else if (i == 1) {
            MainActivity.index = MainActivity.INDEX_WALLPAPER;
            addWallpaper();
            iMainView.leftItemSelected(i);
        } else if (i == 2) {
            MainActivity.index = MainActivity.INDEX_JOKE;
            addJoke();
            iMainView.leftItemSelected(i);
        }
    }

    /**
     * 一分钟转一次图片
     *
     * @param view
     */
    public void setLeftImageChange(final View view) {
        if (view != null) {
            subscribe = Observable.interval(0, 60, TimeUnit.SECONDS)
                    .take(100000000)
                    .compose(activity.<Long>bindToLifecycle())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            setLeftImage(view);
                        }
                    });
        }
    }


    private void setLeftImage(View view) {
        if (view != null) {
            AppCompatImageView imageView = view.findViewById(R.id.AppCompatImageView);
            File wallpaperFile = getWallpaper();
            if (wallpaperFile != null && wallpaperFile.exists()) {
                view.setTag(wallpaperFile.getAbsolutePath());
                ImageHelper.setImage(wallpaperFile, imageView, R.color.white);
            } else {
                imageView.setBackgroundResource(R.mipmap.moren);
            }
        }
    }


    private File getWallpaper() {
        File file = new File(BaseApplication.WALLPAPER_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                return files[new Random().nextInt(files.length)];
            }
        }
        return null;
    }

    public void imageChangeFinish() {
        if (subscribe != null && subscribe.isDisposed()) {
            subscribe.dispose();
        }
    }
}
