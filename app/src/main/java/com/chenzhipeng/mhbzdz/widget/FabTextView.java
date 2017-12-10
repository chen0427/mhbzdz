package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.chenzhipeng.mhbzdz.utils.DisplayUtils;

/**
 * Created by Administrator on 2017/10/18.
 */

public class FabTextView extends FloatingActionButton {
    private Paint paint;
    private String text;
    private float textSize;
    private int textColor;


    public FabTextView(Context context) {
        super(context);
        init();
    }

    public FabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textSize = DisplayUtils.spToPx(11);
        textColor = Color.WHITE;
        text = "";
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float textWidth = paint.measureText(text);
        float width = getHeight() / 2 - textWidth / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText(text, width, height, paint);
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(200);
        startAnimation(animationSet);
    }

    public void hide() {
        setVisibility(GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(200);
        startAnimation(animationSet);
    }
}
