package com.chenzhipeng.mhbzdz.fragment.comic;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.activity.comic.ComicDownloadDataActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicMyDownloadPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicMyDownloadView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicMyDownloadFragment extends BaseFragment implements IComicMyDownloadView, BaseQuickAdapter.OnItemChildClickListener, BottomCheckedView.Listener {
    private View fragmentView;
    @BindView(R.id.rv_myDownload)
    RecyclerView recyclerView;
    private ComicMyDownloadPresenter presenter;
    @BindView(R.id.BottomCheckedView)
    BottomCheckedView bottomCheckedView;


    private ComicMyDownloadPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicMyDownloadPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_download, container, false);
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
        getPresenter().initData();
    }

    @Override
    public <T> void onAdapter(T data) {
        ComicBookListAdapter adapter = (ComicBookListAdapter) data;
        if (adapter != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.setHasFixedSize(true);
            adapter.setOnItemChildClickListener(this);
            recyclerView.setAdapter(adapter);
        }
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
        ComicItemBean comicItemBean = (ComicItemBean) adapter.getData().get(position);
        if (comicItemBean.isShowChecked()) {
            AppCompatCheckBox checkBox = view.findViewById(R.id.cb_itemComicBook);
            boolean checked = comicItemBean.isChecked();
            checkBox.setChecked(!checked);
            comicItemBean.setChecked(!checked);
            getActivity().invalidateOptionsMenu();
        } else {
            ComicDownloadDataActivity.startActivity(getActivity(), comicItemBean.getComicId());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFragmentView(fragmentView);
    }


    public boolean isShowChecked() {
        return getPresenter().isShowChecked();
    }

    public boolean isAllChecked() {
        return getPresenter().isAllChecked();
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

    public void edit() {
        getPresenter().edit();
    }
}
