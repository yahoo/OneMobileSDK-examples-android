package com.aol.mobile.sdk.testapp.aol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.aol.mobile.sdk.testapp.R;


public final class AspectFrameLayout extends FrameLayout {
    private static final int DEFAULT_XRATIO = 1;

    private static final int DEFAULT_YRATIO = 1;

    private int xRatio = DEFAULT_XRATIO;

    private int yRatio = DEFAULT_YRATIO;

    public int getXRatio() {
        return xRatio;
    }

    public void setXRatio(int xRatio) {
        this.xRatio = xRatio;
    }

    public int getYRatio() {
        return yRatio;
    }

    public void setYRatio(int yRatio) {
        this.yRatio = yRatio;
    }

    public AspectFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AspectFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectFrameLayout, 0, 0);
        try {
            xRatio = a.getInt(R.styleable.AspectFrameLayout_xRatio, DEFAULT_XRATIO);
            yRatio = a.getInt(R.styleable.AspectFrameLayout_yRatio, DEFAULT_YRATIO);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = (int) ((double) measuredWidth / xRatio * yRatio);

        measureChildren(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
