package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chenzhipeng.mhbzdz.R;


public class BottomCheckedView extends FrameLayout implements View.OnClickListener {
    private AppCompatCheckBox checkBox;
    private Listener listener;

    public BottomCheckedView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BottomCheckedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomCheckedView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_bottom_checked, this);
        AppCompatTextView textView = findViewById(R.id.AppCompatTextView1);
        textView.setTextColor(Color.RED);
        checkBox = findViewById(R.id.AppCompatCheckBox);
        checkBox.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setChecked(boolean b) {
        checkBox.setChecked(b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AppCompatCheckBox:
                if (listener != null) {
                    listener.clickChecked();
                }
                break;
            case R.id.AppCompatTextView1:
                if (listener != null) {
                    listener.clickDelete();
                }
                break;
        }
    }

    public interface Listener {
        void clickDelete();

        void clickChecked();
    }
}
