package com.chenzhipeng.mhbzdz.presenter.comic;

import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicReadPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicPictureListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.intent.SuperIntent;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicPictureView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("unchecked")
public class ComicReadPicturePresenter {
    private ComicReadPictureActivity activity;
    private IComicPictureView pictureView;
    private String comicId;
    private String comicName;
    private List<ComicItemPicture> pictures;
    private String chapterName;
    private int readPosition;

    public ComicReadPicturePresenter(ComicReadPictureActivity activity) {
        this.activity = activity;
        this.pictureView = activity;
    }

    private void checkReadHistory(List<ComicItemPicture> pictures) {
        if (!EmptyUtils.isListsEmpty(pictures)) {
            String pictureUrl = ComicDatabase.getInstance().getHistoryPictureUrl(comicId, chapterName);
            if (!TextUtils.isEmpty(pictureUrl)) {
                int count = 0;
                for (ComicItemPicture p : pictures) {
                    if (p.getUrl().equals(pictureUrl)) {
                        readPosition = count;
                        break;
                    }
                    count++;
                }
            }
        }
    }


    public void updateHorizontalList() {
        pictureView.showWorkDialog();
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                List<ComicItemPicture> pictureList = getData();
                checkReadHistory(pictureList);
                e.onNext(pictureList);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        pictureView.dismissWorkDialog();
                        pictures = (List<ComicItemPicture>) o;
                        if (!EmptyUtils.isListsEmpty(pictures)) {
                            ComicPictureListAdapter adapter =
                                    new ComicPictureListAdapter(R.layout.itemview_comic_picture, pictures, comicId, comicName, chapterName);
                            pictureView.onAdapter(adapter, readPosition);
                            updateBottomBar(readPosition);
                        }
                    }
                });
    }


    /**
     * 更新底部栏 (章节提示)
     */
    public void updateBottomBar(int position) {
        if (!EmptyUtils.isListsEmpty(pictures) && 0 <= position && position < pictures.size()) {
            ComicItemPicture comicItemPicture = pictures.get(position);
            if (comicItemPicture != null) {
                String chapterName = comicItemPicture.getChapterName();
                String currentNumber = comicItemPicture.getCurrentNumber();
                String endNumber = comicItemPicture.getEndNumber();
                String pictureUrl = comicItemPicture.getUrl();
                pictureView.updateBottomBar(comicId, comicName, chapterName, currentNumber, endNumber, pictureUrl);
            }
        }
    }

    private List<ComicItemPicture> getChapterPictures() {
        List<ComicItemPicture> comicItemPictureList = new ArrayList<>();
        int position = (int) SuperIntent.getInstance().get(SuperIntent.S8);
        boolean isReverse = (boolean) SuperIntent.getInstance().get(SuperIntent.S7);
        List<ComicChapterItemBean> comicChapterItemBeanList = new ArrayList<>();
        List<ComicChapterItemBean> list = (List<ComicChapterItemBean>) SuperIntent.getInstance().get(SuperIntent.S17);
        comicChapterItemBeanList.addAll(list);
        if (!EmptyUtils.isListsEmpty(comicChapterItemBeanList)) {
            ComicChapterItemBean chapterItemBean = comicChapterItemBeanList.get(position);
            comicId = chapterItemBean.getComicId();
            comicName = chapterItemBean.getComicName();
            chapterName = chapterItemBean.getChapterName();
            //获取你选择章节第一张图的url 后面用来计算整个图片集合中你张图的position
            String url = chapterItemBean.getComicItemPictureList().get(0).getUrl();
            if (isReverse) {
                //左滑动图片是一直升序
                Collections.reverse(comicChapterItemBeanList);
            }
            int count = 0;
            for (ComicChapterItemBean c : comicChapterItemBeanList) {
                for (ComicItemPicture p : c.getComicItemPictureList()) {
                    comicItemPictureList.add(p);
                    if (p.getUrl().equals(url)) {
                        readPosition = count;
                    }
                    count++;
                }
            }
        }
        return comicItemPictureList;
    }

    private List<ComicItemPicture> getDownloadPictures() {
        List<ComicItemPicture> comicItemPictureList = (List<ComicItemPicture>) SuperIntent.getInstance().get(SuperIntent.S4);
        String[] strings = (String[]) SuperIntent.getInstance().get(SuperIntent.S5);
        if (!EmptyUtils.isListsEmpty(comicItemPictureList) && strings != null && strings.length == 3) {
            comicId = strings[0];
            comicName = strings[1];
            chapterName = strings[2];
        }
        return comicItemPictureList;
    }


    private List<ComicItemPicture> getData() {
        int type = (int) SuperIntent.getInstance().get(SuperIntent.S6);
        if (type == ComicReadPictureActivity.TYPE_CHAPTER) {
            return getChapterPictures();
        } else if (type == ComicReadPictureActivity.TYPE_DOWNLOAD) {
            return getDownloadPictures();
        }
        return new ArrayList<>();
    }
}
