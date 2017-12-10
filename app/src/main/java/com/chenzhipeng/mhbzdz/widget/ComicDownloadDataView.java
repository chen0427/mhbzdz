package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.chenzhipeng.mhbzdz.R;
import com.chenzhipeng.mhbzdz.base.BaseApplication;

/**
 * Created by Administrator on 2017/10/21.
 */

public class ComicDownloadDataView extends FrameLayout {
    private AppCompatTextView nameTextView;
    private AppCompatTextView stateTextView;
    private AppCompatTextView pageTextView;
    private ProgressBar progressBar;
    private AppCompatCheckBox checkBox;

    public ComicDownloadDataView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ComicDownloadDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ComicDownloadDataView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_download_details, this);
        nameTextView = findViewById(R.id.tv_customDownloadDetailsName);
        stateTextView = findViewById(R.id.tv_customDownloadDetailsState);
        pageTextView = findViewById(R.id.tv_customDownloadDetailsPage);
        progressBar = findViewById(R.id.pb_customDownloadDetails);
        checkBox = findViewById(R.id.cb_customDownloadDetails);
    }

    public int getOnClickListenerId() {
        return R.id.ll_customDownloadDetails;
    }

    public void setCheckBox(boolean b) {
        checkBox.setChecked(b);
    }

    public void setShowChecked(boolean b) {
        checkBox.setVisibility(b ? VISIBLE : GONE);
    }

    public void setNameText(String s) {
        nameTextView.setText(s);
    }

    public void setNameTextColor(int id) {
        nameTextView.setTextColor(ContextCompat.getColor(BaseApplication.getContext(), id));
    }

    public void setPageText(String s) {
        pageTextView.setText(s);
    }

    public void setStateText(String s) {
        stateTextView.setText(s);
    }

    public void setProgress(int i) {
        progressBar.setProgress(i);
    }

    public void setMaxProgress(int i) {
        progressBar.setMax(i);
    }


}
