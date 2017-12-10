package com.chenzhipeng.mhbzdz.presenter.comic;

import android.content.Intent;
import android.text.TextUtils;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicReadPictureActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicPictureListAdapter;
import com.chenzhipeng.mhbzdz.bean.comic.ComicChapterItemBean;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemPicture;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicChapterFragment;
import com.chenzhipeng.mhbzdz.sqlite.ComicDatabase;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.chenzhipeng.mhbzdz.view.comic.IComicPictureView;

import java.util.ArrayList;
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
        Intent intent = activity.getIntent();
        if (intent != null) {
            int position = intent.getIntExtra(ComicReadPictureActivity.KEY_INTENT_2, 0);
            if (!EmptyUtils.isListsEmpty(ComicChapterFragment.comicChapterItemBeanList)) {
                ComicChapterItemBean chapterItemBean = ComicChapterFragment.comicChapterItemBeanList.get(position);
                comicId = chapterItemBean.getComicId();
                comicName = chapterItemBean.getComicName();
                chapterName = chapterItemBean.getChapterName();
                ComicItemPicture picture = chapterItemBean.getComicItemPictureList().get(0);
                int count = 0;
                for (ComicChapterItemBean c : ComicChapterFragment.comicChapterItemBeanList) {
                    for (ComicItemPicture p : c.getComicItemPictureList()) {
                        comicItemPictureList.add(p);
                        if (p.getUrl().equals(picture.getUrl())) {
                            readPosition = count;
                        }
                        count++;
                    }
                }
            }
        }
        return comicItemPictureList;
    }

    private List<ComicItemPicture> getDownloadPictures() {
        List<ComicItemPicture> comicItemPictureList = new ArrayList<>();
        Intent intent = activity.getIntent();
        if (intent != null) {
            comicItemPictureList = (List<ComicItemPicture>) intent.getSerializableExtra(ComicReadPictureActivity.KEY_INTENT_1);
            String[] strings = intent.getStringArrayExtra(ComicReadPictureActivity.KEY_INTENT_2);
            comicId = strings[0];
            comicName = strings[1];
            chapterName = strings[2];
        }
        return comicItemPictureList;
    }


    private List<ComicItemPicture> getData() {
        Intent intent = activity.getIntent();
        if (intent != null) {
            int type = intent.getIntExtra(ComicReadPictureActivity.KEY_INTENT_3, ComicReadPictureActivity.TYPE_CHAPTER);
            if (type == ComicReadPictureActivity.TYPE_CHAPTER) {
                return getChapterPictures();
            } else if (type == ComicReadPictureActivity.TYPE_DOWNLOAD) {
                return getDownloadPictures();
            }
        }
        return new ArrayList<>();
    }
}
