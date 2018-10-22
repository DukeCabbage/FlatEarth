package com.cabbage.flatearth.ui.hud;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.cabbage.flatearth.R;

import timber.log.Timber;

public final class HUDView extends View {

    int mWidth, mHeight;
    Paint mGreenPaint, mShadowPaint;
    RectF mOvalBounds = new RectF();

    long mAnimStartTime;

    Handler mHandler = new Handler();
    Runnable mTick = new Runnable() {
        public void run() {
            long currentTime = SystemClock.uptimeMillis();
            if ((currentTime - mAnimStartTime) > 50000L) {
                stopAnimation();
                return;
            }

            invalidate();
            mHandler.postDelayed(this, 20); // 20ms == 60fps
        }
    };

    public void startAnimation() {
        mAnimStartTime = SystemClock.uptimeMillis();
        mHandler.removeCallbacks(mTick);
        mHandler.post(mTick);
    }

    public void stopAnimation() {
        mHandler.removeCallbacks(mTick);
    }

    public HUDView(Context context) {
        super(context);
        init();
    }

    public HUDView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mGreenPaint = new Paint();
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           color = getResources().getColor(R.color.colorGreenA400, getContext().getTheme());
        } else {
            //noinspection deprecation
            color = getResources().getColor(R.color.colorGreenA400);
        }
        mGreenPaint.setColor(color);
        mGreenPaint.setStyle(Paint.Style.STROKE);
        mGreenPaint.setStrokeWidth(8.0f);
        mShadowPaint = new Paint(mGreenPaint);

//        EmbossMaskFilter filter = new EmbossMaskFilter()
        mGreenPaint.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL));

        mShadowPaint.setStrokeWidth(16.0f);
        mShadowPaint.setAlpha(230);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Timber.d("onMeasure");
        int a = getPaddingLeft();
        int b = getPaddingRight();
        int c = getMeasuredWidth();
        int d = getSuggestedMinimumWidth();
        // Try for a width based on our minimum
        int minw = a + b + d;
        int width = resolveSizeAndState(minw, widthMeasureSpec, 1);

        int e = getPaddingTop();
        int f = getPaddingBottom();
        int g = getMeasuredHeight();
        int h = getSuggestedMinimumHeight();
        // Try for a width based on our minimum
        int minh = e + f + h;
        int height = resolveSizeAndState(minh, heightMeasureSpec, 1);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Timber.d("onSizeChanged");
        Timber.v("old width: %d height: %d, new width: %d height: %d", oldw, oldh, w, h);

        mWidth = getWidth();
        mHeight = getHeight();
        mOvalBounds.set(0.2f*mWidth, -0.05f*mHeight, 0.8f*mWidth, 0.8f*mHeight);

        int xpad = getPaddingLeft() + getPaddingRight();
        int ypad = getPaddingTop() + getPaddingBottom();
        Timber.v("xpad: %d, ypad: %d", xpad, ypad);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Timber.d("onLayout");
        Timber.v("left: %d, right: %d, top: %d, bottom: %d, changed:, %b", left, right, top, bottom, changed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long diff = SystemClock.uptimeMillis() - mAnimStartTime;
        float angle = diff / 20000f * 360f;
        Timber.d("onDraw %d", diff);

        canvas.drawLine(0, mHeight/2, mWidth, mHeight/2, mGreenPaint);
        canvas.rotate(angle, mWidth/2, mHeight/2);
        canvas.drawOval(mOvalBounds, mGreenPaint);
        canvas.drawOval(mOvalBounds, mShadowPaint);
    }
}
