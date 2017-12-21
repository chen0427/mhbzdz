package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.adapter.SearchRecordListAdapter;
import com.chenzhipeng.mhbzdz.utils.ConfigUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;

import java.util.List;


public class AppSearchView extends FrameLayout implements View.OnClickListener, TextView.OnEditorActionListener, BaseQuickAdapter.OnItemChildClickListener, TextWatcher {
    private Toolbar toolbar;
    private AppCompatImageView clearImageView;
    private AppCompatEditText editText;
    private RecyclerView recyclerView;
    private Listener listener;
    private SearchRecordListAdapter adapter;

    public AppSearchView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.includeview_search, this);
        init();
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.includeview_search, this);
        init();
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.includeview_search, this);
        init();
    }

    public void setToolbar(AppCompatActivity activity) {
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            ActionBar supportActionBar = activity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void init() {
        AppBarLayout appBarLayout = findViewById(R.id.AppBarLayout);
        toolbar = findViewById(R.id.Toolbar);
        clearImageView = findViewById(R.id.AppCompatImageView);
        editText = findViewById(R.id.AppCompatEditText);
        recyclerView = findViewById(R.id.RecyclerView);
        editText.setSingleLine();
        editText.setOnEditorActionListener(this);
        editText.addTextChangedListener(this);
        clearImageView.setOnClickListener(this);
        appBarLayout.setBackgroundResource(ConfigUtils.getChoiceToAppColor());
    }

    /**
     * 设置历史记录数据
     */
    public void setRecordData(List<String> strings) {
        if (!EmptyUtils.isListsEmpty(strings)) {
            if (adapter == null) {
                adapter = new SearchRecordListAdapter(R.layout.itemview_search_record, strings);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                adapter.setOnItemChildClickListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setNewData(strings);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AppCompatImageView:
                //clear
                editText.setText("");
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (i) {
            case EditorInfo.IME_ACTION_SEARCH:
                if (listener != null) {
                    listener.doSearch(editText.getText().toString());
                }
                break;
        }
        return true;
    }

    @Override
    public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
        String str = (String) adapter.getData().get(position);
        if (listener != null) {
            if (view.getId() == R.id.ll_searchRecordItem) {
                listener.onClickItem(str);
            } else {
                listener.onDeleteItem(str);
                adapter.remove(position);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editText.getText().toString();
        clearImageView.setVisibility(TextUtils.isEmpty(s) ? INVISIBLE : VISIBLE);
    }

    public void setListener(Listener Listener) {
        this.listener = Listener;
    }


    public interface Listener {
        void doSearch(String str);

        void onClickItem(String str);

        void onDeleteItem(String str);
    }
}

