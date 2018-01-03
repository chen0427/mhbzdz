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
import com.chenzhipeng.mhbzdz.activity.comic.ComicDetailsActivity;
import com.chenzhipeng.mhbzdz.adapter.comic.ComicBookListAdapter;
import com.chenzhipeng.mhbzdz.base.BaseFragment;
import com.chenzhipeng.mhbzdz.bean.comic.ComicItemBean;
import com.chenzhipeng.mhbzdz.presenter.comic.ComicHistoryPresenter;
import com.chenzhipeng.mhbzdz.view.comic.IComicHistoryView;
import com.chenzhipeng.mhbzdz.widget.BottomCheckedView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 漫画历史
 */
public class ComicHistoryFragment extends BaseFragment implements IComicHistoryView, BaseQuickAdapter.OnItemChildClickListener, BottomCheckedView.Listener {
    private View fragmentView;
    private ComicHistoryPresenter presenter;
    @BindView(R.id.rv_comicHistory)
    RecyclerView recyclerView;
    @BindView(R.id.BottomCheckedView)
    BottomCheckedView bottomCheckedView;

    private ComicHistoryPresenter getPresenter() {
        if (presenter == null) {
            presenter = new ComicHistoryPresenter(this);
        }
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_comic_history, container, false);
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
            adapter.setOnItemChildClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onEmptyData() {
        //没有历史记录
    }

    @Override
    public void showDeleteDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.sure_delete)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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
            boolean checked = checkBox.isChecked();
            checkBox.setChecked(!checked);
            bean.setChecked(!checked);
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

    public boolean closeMenu() {
        return getPresenter().closeMenu();
    }

    public void delete() {
        getPresenter().delete();
    }

    public void setBottomCheckedViewVisibility(boolean b) {
        if (bottomCheckedView != null) {
            bottomCheckedView.setVisibility(b ? View.VISIBLE : View.GONE);
        }
    }

    public void allChecked() {
        getPresenter().allChecked();
    }

    public void setBottomCheckedViewChecked(boolean b) {
        if (bottomCheckedView != null) {
            bottomCheckedView.setChecked(b);
        }
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
