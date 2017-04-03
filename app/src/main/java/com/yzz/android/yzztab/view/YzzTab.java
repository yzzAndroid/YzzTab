package com.yzz.android.yzztab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @name YzzTab
 * @class name：com.yzz.android.yzztab.view
 * @anthor yzz
 * @Email:yzzandroid@163.com
 * @time 2017/4/one 0001 下午 7:44
 */
public class YzzTab extends ViewGroup{
    public static final int MODEL_DEFAULT = 0;
    public static final int MODEL_CENTER = 1;
    public static final int IS_SCROLL = 0;
    public static final int UN_SCROLL = 1;
    private int mModel = MODEL_CENTER;
    private int mIsScroll = UN_SCROLL;
    private int margin = 0;
    ;
    private int widthMeasureSpec;
    private int heightMeasureSpec;
    private int maxScroll = 0;
    public static final int MINE_MARGIN = 20;
    protected boolean isNeedScroll = false;
    private int currentPosition = 0;
    private Paint linePaint;
    private float fx;
    private float sx;


    public YzzTab(Context context) {
        this(context,null);
    }

    public YzzTab(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public YzzTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wModel = MeasureSpec.getMode(widthMeasureSpec);
        int hModel = MeasureSpec.getMode(heightMeasureSpec);
        int count = getChildCount();
        Log.e("====width======", "=========" + width);
        switch (mModel) {
            case MODEL_CENTER:
                measureCenter(count, wModel, hModel, width, height);
                break;
            case MODEL_DEFAULT:
                measureDefault(count, wModel, hModel, width, height);
                break;
        }
    }


    private void measureCenter(int count, int wModel, int hModel, int width, int height) {
        int w = 0;
        int h = 0;
        if (wModel == MeasureSpec.EXACTLY) {
            w = width;
            //计算margin
            getMargin(count, width);
        } else {
            w = getCenterW(count, width);
        }
        if (hModel == MeasureSpec.EXACTLY) {
            h = height;
        } else {
            h = getH(count);
        }

        setMeasuredDimension(w, h);
    }

    private int getCenterW(int count, int pW) {
        int w = 0;
        int childW = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            childW += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        }
//        if (w + getPaddingLeft() + getPaddingRight() < pW) {
//            getMargin(count, pW);
//            return pW;
//        }
        w = childW + getPaddingRight() + getPaddingLeft() + (count - 1) * margin;
        if (w > pW) getMargin(count, pW);
        return w > pW ? pW : w;

    }


    private void getMargin(int count, int w) {
        int childW = 0;
        int num = 0;
        for (int i = 0; i < count; i++) {
            num++;
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            childW += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            if (num > 1) {
                margin = (w - (childW + getPaddingLeft() + getPaddingRight())) / (num - 1);
                if (margin < MINE_MARGIN) {
                    childW -= child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    num--;
                    margin = (w - (childW + getPaddingLeft() + getPaddingRight())) / (num - 1);
                    break;
                }
            }


//            if (childW > w - getPaddingLeft() - getPaddingRight()) {
//                childW -= child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//                num--;
//                break;
//            }
        }
        //if (num<=one)return;
        //margin = (int) ((w - (childW + getPaddingLeft() + getPaddingRight())) / (num - one));
    }

    private void measureDefault(int count, int wModel, int hModel, int width, int height) {
        int w = 0;
        int h = 0;
        if (wModel == MeasureSpec.EXACTLY) {
            w = width;
        } else {
            w = getW(count, width);
        }
        if (hModel == MeasureSpec.EXACTLY) {
            h = height;
        } else {
            h = getH(count);
        }
        setMeasuredDimension(w, h);
    }

    /**
     * 获取高度
     */
    private int getH(int count) {
        int h = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            h = Math.max(h, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        }
        return h + getPaddingBottom() + getPaddingTop();
    }

    /**
     * 获取宽度
     */
    private int getW(int count, int pW) {
        int w = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            w += lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
//            if (w + getPaddingLeft() + getPaddingRight() < pW) {
//                return pW;
//            }
        }
        return (w + getPaddingLeft() + getPaddingRight()) > pW ? pW : (w + getPaddingLeft() + getPaddingRight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        switch (mModel) {
            case MODEL_DEFAULT:
                layoutDefault(count);
                break;
            case MODEL_CENTER:
                layoutCenter(count);
                break;
        }
        Log.e("maxScroll", "============" + maxScroll + "=margin=" + margin);
    }

    private void layoutDefault(int count) {
        int w = getPaddingLeft();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            int l = w + lp.leftMargin;
            int centerH = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - child.getMeasuredHeight()) / 2;
            int t = centerH + lp.topMargin;
            int r = l + child.getMeasuredWidth();
            int b = t + child.getMeasuredHeight();
            if (r > getMeasuredWidth()) {
                isNeedScroll = true;
            } else {
                isNeedScroll = false;
            }
            child.layout(l, t, r, b);
            w = r + lp.rightMargin;
            if (i == count - 1) {
                maxScroll = child.getRight() - getMeasuredWidth();
            }
        }
    }

    private void layoutCenter(int count) {
        int w = getPaddingLeft();
        int h = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp;
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                lp = (MarginLayoutParams) child.getLayoutParams();
            } else {
                lp = new MarginLayoutParams(child.getLayoutParams());
            }
            //lp = (LayoutParams) child.getLayoutParams();

            int l = w + lp.leftMargin;
            if (i == count - 1 && getMeasuredWidth() - w > 0) {
                l = getMeasuredWidth() - child.getMeasuredWidth() - lp.leftMargin;
            }
            int centerH = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - child.getMeasuredHeight()) / 2;
            int t = lp.topMargin + centerH;
            int r = l + child.getMeasuredWidth();
            int b = t + child.getMeasuredHeight();
            if (r > getMeasuredWidth()) {
                isNeedScroll = true;
            } else {
                isNeedScroll = false;
            }
            child.layout(l, t, r, b);
            w = r + lp.rightMargin + margin;
            if (i == count - 1) {
                maxScroll = child.getRight() - getMeasuredWidth();
            }
        }

    }

    public int getMaxScroll() {
        return maxScroll;
    }

    protected void setCurrentPosition(int position, Paint paint) {
        currentPosition = position;
        linePaint = paint;
        invalidate();
    }

    private void drawLine(Canvas canvas) {
        if (linePaint == null) return;
        View tab = getChildAt(currentPosition);
        fx = tab.getLeft();
        sx = fx+tab.getMeasuredWidth();
        int fy = getMeasuredHeight();
        int sy = fy;
        canvas.drawLine(fx, fy, sx, sy, linePaint);
    }

}
