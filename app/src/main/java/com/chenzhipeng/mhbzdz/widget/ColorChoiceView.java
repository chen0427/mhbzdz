package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenzhipeng.mhbzdz.R;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ColorChoiceView extends FrameLayout implements BaseQuickAdapter.OnItemChildClickListener {
    private Integer[] colorIds = {R.color.colorPrimary_1, R.color.colorPrimary_2,
            R.color.colorPrimary_3, R.color.colorPrimary_4, R.color.colorPrimary_5,
            R.color.colorPrimary_6, R.color.colorPrimary_7, R.color.colorPrimary_8,
            R.color.colorPrimary_9, R.color.colorPrimary_10};

    private Listener listener;

    public ColorChoiceView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ColorChoiceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorChoiceView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.customview_color_choice, this);
        RecyclerView recyclerView = findViewById(R.id.rv_colorChoice);
        ColorChoiceAdapter adapter = new ColorChoiceAdapter(Arrays.asList(colorIds));
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        recyclerView.setHasFixedSize(true);
        adapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private class ColorChoiceAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
        ColorChoiceAdapter(@Nullable List<Integer> data) {
            super(R.layout.itemview_color_choice, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            CircleImageView circleImageView = helper.getView(R.id.civ_colorChoice);
            helper.addOnClickListener(R.id.civ_colorChoice);
            circleImageView.setImageResource(item);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (listener != null) {
            int colorId = (int) adapter.getData().get(position);
            listener.onChoiceColorClick(colorId, position);
        }
    }


    public interface Listener {
        void onChoiceColorClick(int colorId, int position);
    }
}
