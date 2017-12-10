
package com.chenzhipeng.mhbzdz.presenter.comic;

import android.support.v4.app.Fragment;
import android.view.Menu;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicMyBookActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicMyBookViewPagerAdapter;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicCollectionFragment;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicHistoryFragment;
import com.chenzhipeng.mhbzdz.fragment.comic.ComicMyDownloadFragment;
import com.chenzhipeng.mhbzdz.view.comic.IComicMyBookView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class ComicMyBookPresenter {
    private IComicMyBookView bookView;
    private ComicMyBookActivity activity;
    private ComicCollectionFragment fragment_1;
    private ComicHistoryFragment fragment_2;
    private ComicMyDownloadFragment fragment_3;
    private int position = 0;


    public ComicMyBookPresenter(ComicMyBookActivity activity) {
        this.bookView = activity;
        this.activity = activity;
    }

    public void setFragmentPosition(int position) {
        this.position = position;
        activity.invalidateOptionsMenu();
    }


    public void initAdapter() {
        fragment_1 = new ComicCollectionFragment();
        fragment_2 = new ComicHistoryFragment();
        fragment_3 = new ComicMyDownloadFragment();
        List<String> titleStrings = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        titleStrings.add(activity.getString(R.string.collection));
        titleStrings.add(activity.getString(R.string.history));
        titleStrings.add(activity.getString(R.string.download));
        fragments.add(fragment_1);
        fragments.add(fragment_2);
        fragments.add(fragment_3);
        ComicMyBookViewPagerAdapter adapter = new ComicMyBookViewPagerAdapter(activity.getSupportFragmentManager(), fragments, titleStrings);
        bookView.onAdapter(adapter);
    }

    /**
     * 整理or完成
     */
    public void edit() {
        switch (position) {
            case 0:
                if (fragment_1 != null) {
                    fragment_1.edit();
                }
                break;
            case 1:
                if (fragment_2 != null) {
                    fragment_2.edit();
                }
                break;
            case 2:
                if (fragment_3 != null) {
                    fragment_3.edit();
                }
                break;
        }
        activity.invalidateOptionsMenu();
    }


    public void updateMenu(Menu menu) {
        if (menu != null) {
            switch (position) {
                case 0:
                    if (fragment_1 != null) {
                        boolean showChecked_1 = fragment_1.isShowChecked();
                        boolean allChecked_1 = fragment_1.isAllChecked();
                        menu.getItem(0).setVisible(false);
                        menu.getItem(1).setVisible(false);
                        menu.getItem(2).setTitle(showChecked_1 ? activity.getString(R.string.complete) : activity.getString(R.string.edit));
                        fragment_1.setBottomCheckedViewVisibility(showChecked_1);
                        fragment_1.setBottomCheckedViewChecked(allChecked_1);
                    }
                    break;
                case 1:
                    if (fragment_2 != null) {
                        boolean showChecked_2 = fragment_2.isShowChecked();
                        boolean allChecked_2 = fragment_2.isAllChecked();
                        menu.getItem(0).setVisible(false);
                        menu.getItem(1).setVisible(false);
                        menu.getItem(2).setTitle(showChecked_2 ? activity.getString(R.string.complete) : activity.getString(R.string.edit));
                        fragment_2.setBottomCheckedViewVisibility(showChecked_2);
                        fragment_2.setBottomCheckedViewChecked(allChecked_2);
                    }
                    break;
                case 2:
                    if (fragment_3 != null) {
                        boolean showChecked_3 = fragment_3.isShowChecked();
                        boolean allChecked_3 = fragment_3.isAllChecked();
                        menu.getItem(0).setVisible(false);
                        menu.getItem(1).setVisible(false);
                        menu.getItem(2).setTitle(showChecked_3 ? activity.getString(R.string.complete) : activity.getString(R.string.edit));
                        fragment_3.setBottomCheckedViewVisibility(showChecked_3);
                        fragment_3.setBottomCheckedViewChecked(allChecked_3);
                    }
                    break;
            }
        }
    }

    public boolean clseMenu() {
        switch (position) {
            case 0:
                if (fragment_1 != null) {
                    return fragment_1.closeMenu();
                }
                break;
            case 1:
                if (fragment_2 != null) {
                    return fragment_2.closeMenu();
                }
                break;
            case 2:
                if (fragment_3 != null) {
                    return fragment_3.closeMenu();
                }
                break;

        }
        return false;
    }
}