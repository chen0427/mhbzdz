package com.chenzhipeng.mhbzdz.fragment.comic;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicCollectionPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicCollectionView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 漫画收藏
 */
public class ComicCollectionFragment extends BaseFragment implements IComicCollectionView, BaseQuickAdapter.OnItemChildClickListener, BottomCheckedView.Listener {
    private View fragmentView;
    @BindView(R.id.rv_comicCollection)
    RecyclerView recyclerView;
    @BindView(R.id.BottomCheckedView)
    BottomCheckedView bottomCheckedView;
    private ComicCollectionPresenter presenter;

    private ComicCollectionPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicCollectionPresenter(this);
        }
        return presenter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_collection, container, false);
            ButterKnife.bind(this, fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bottomCheckedView.setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getPresenter().isShowChecked()) {
            getPresenter().initData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }

    @Override
    public <T> void onAdapter(T data) {
        ComicBookListAdapter adapter = (ComicBookListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            adapter.setOnItemChildClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onEmptyData() {
        //没有收藏
    }

    @Override
    public void showDeleteDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.sure_delete))
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().delete();
            }
        }).show();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (getPresenter().isShowChecked()) {
            ComicItemBean bean = (ComicItemBean) adapter.getData().get(position);
            AppCompatCheckBox checkBox = view.findViewById(R.id.cb_itemComicBook);
            boolean checked = bean.isChecked();
            bean.setChecked(!checked);
            checkBox.setChecked(!checked);
            getActivity().invalidateOptionsMenu();
        } else {
            ComicItemBean bean = (ComicItemBean) adapter.getData().get(position);
            ComicDetailsActivity.startActivity(getActivity(), bean);
        }
    }

    public boolean isShowChecked() {
        return getPresenter().isShowChecked();
    }

    public boolean isAllChecked() {
        return getPresenter().isAllChecked();
    }

    public void edit() {
        getPresenter().edit();
    }

    public boolean closeMenu() {
        return getPresenter().closeMenu();
    }

    public void setBottomCheckedViewVisibility(boolean b) {
        bottomCheckedView.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public void setBottomCheckedViewChecked(boolean b) {
        bottomCheckedView.setChecked(b);
    }

    @Override
    public void clickDelete() {
        getPresenter().showDeleteDialog();
    }

    @Override
    public void clickChecked() {
        getPresenter().allChecked();
    }

}
