package com.chenzhipeng.mhbzdz.presenter;

import android.content.Intent;

import com.chenzhipeng.mhbzdz.activity.MainActivity;
import com.chenzhipeng.mhbzdz.activity.SettingActivity;
import com.chenzhipeng.mhbzdz.base.BaseApplication;
import com.chenzhipeng.mhbzdz.image.ImageCacheHelper;
import com.chenzhipeng.mhbzdz.view.ISettingView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/15.
 */

public class SettingPresenter {
    private SettingActivity activity;
    private ISettingView iSettingView;
    static final String KEY_THEM = "them";
    private String cacheSize;

    public SettingPresenter(SettingActivity activity) {
        this.activity = activity;
        this.iSettingView = activity;
    }

    public void initData() {
        iSettingView.setIndexChoice(BaseApplication.choiceToIndex == 0 ? "漫画" : BaseApplication.choiceToIndex == 1 ? "壁纸" : "段子");
        iSettingView.setAppColorChoice(BaseApplication.choiceToAppColor);
        iSettingView.setComicChapterSort(BaseApplication.comicChapterSort);
        iSettingView.setVolumePage(BaseApplication.volumePage);
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                cacheSize = ImageCacheHelper.getCacheSize();
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).compose(activity.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        iSettingView.setImgCache(cacheSize);
                    }
                });
    }

    public void showIndexChoiceDialog() {
        iSettingView.showIndexChoiceDialog();
    }

    public void showAppColorChoiceDialog() {
        iSettingView.showAppColorChoiceDialog();
    }

    public void ComicChapterSort() {
        iSettingView.showComicChapterSort();
    }

    public void initThem() {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KEY_THEM, MainActivity.index);
        activity.startActivity(intent);
    }


}
