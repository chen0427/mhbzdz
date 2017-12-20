package com.chenzhipeng.mhbzdz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.chenzhipeng.mhbzdz.document.ComicDocumentHelper;
import com.chenzhipeng.mhbzdz.image.ImageHelper;
import com.chenzhipeng.mhbzdz.utils.DisplayUtils;
import com.chenzhipeng.mhbzdz.utils.EmptyUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;


public class ComicReadPictureView extends PhotoView {
    private Paint paint;
    private float textSize = DisplayUtils.dpToPx(14);
    private int textColor;
    private String text;
    private boolean isTextVisibility = true;


    public ComicReadPictureView(Context context) {
        super(context);
        init();
    }

    public ComicReadPictureView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public ComicReadPictureView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLACK);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textColor = Color.WHITE;
        text = "";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isTextVisibility) {
            initText(canvas);
        }
    }

    private void initText(Canvas canvas) {
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);
        float width = getWidth() / 2 - textWidth / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = getHeight() / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText(text, width, height, paint);
    }


    public void setImage(String url, String comicId, String comicName, String chapterName) {
        if (!TextUtils.isEmpty(url) && !EmptyUtils.isStringsEmpty(comicId, comicName, chapterName)) {
            String path = ComicDocumentHelper.getInstance().getStoragePath(url, comicId, comicName, chapterName);
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    ImageHelper.setComicPicture(file, this);
                } else {
                    ImageHelper.setComicPicture(url, this);
                }
            }
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setText(String text) {
        isTextVisibility = true;
        this.text = text;
        invalidate();
    }

    public void setTextVisibility(boolean textVisibility) {
        isTextVisibility = textVisibility;
        invalidate();
    }
}
