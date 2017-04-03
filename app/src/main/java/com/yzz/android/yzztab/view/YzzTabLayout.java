package com.yzz.android.yzztab.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @name YzzTabLayout
 * @class name：com.yzz.android.yzztab.view
 * @anthor yzz
 * @Email:yzzandroid@163.com
 * @time 2017/4/one 0001 下午 7:40
 */
public class YzzTabLayout extends ViewGroup implements ViewPager.OnPageChangeListener {
    public static final int INDICATOR_COLOR = 0xff00ff00;
    public static final int INDICATOR_WIDTH = 10;
    private static Context mContext;
    private static List<Tab> tabList;
    private static List<View> tabViews;

    static {
        tabList = new ArrayList<>();
        tabViews = new ArrayList<>();
    }

    private int mineScrollDistance = 0;
    private float firstTouch = 0;
    private float touchFirst = 0;
    private float maxScroll = 0;
    private boolean isClickEvent = false;
    private YzzTab yzzTab;
    private static int currentChosePosition = 0;
    private static int lastChosePosition = 0;
    private YzzTabSelectListener yzzTabSelectListener;
    private ViewPager viewPager;
    private Paint paint;
    private int indicatorColor = INDICATOR_COLOR;
    private int indicatorWidth = INDICATOR_WIDTH;
    private boolean isNeedIndicator = true;


    public YzzTabLayout(Context context) {
        this(context, null);
    }

    public YzzTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public YzzTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = this.getContext();
        mineScrollDistance = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(indicatorColor);
        paint.setStrokeWidth(indicatorWidth);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 1) throw new RuntimeException("only can have one son");
        if (count == 1) {
            if (!(getChildAt(0) instanceof YzzTab)) {
                throw new RuntimeException("the son must be com.yzz.android.yzztab.view");
            } else {
                yzzTab = (YzzTab) getChildAt(0);
                int size = yzzTab.getChildCount();
                for (int i = 0; i < size; i++) {
                    tabViews.add(yzzTab.getChildAt(i));
                }
            }
        }
        if (count == 0) {
            yzzTab = new YzzTab(getContext());
            yzzTab.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(yzzTab);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) return;
        yzzTab = (YzzTab) getChildAt(0);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wModel = MeasureSpec.getMode(widthMeasureSpec);
        int hModel = MeasureSpec.getMode(heightMeasureSpec);
        int w;
        int h;
        if (wModel == MeasureSpec.EXACTLY) {
            w = width;
        } else {
            w = yzzTab.getMeasuredWidth();
        }

        if (hModel == MeasureSpec.EXACTLY) {
            h = height;
        } else {
            h = yzzTab.getMeasuredHeight();
        }
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) return;
        MarginLayoutParams lp;
        if (yzzTab.getLayoutParams() instanceof MarginLayoutParams) {
            lp = (MarginLayoutParams) yzzTab.getLayoutParams();
        } else {
            lp = new MarginLayoutParams(yzzTab.getLayoutParams());
        }
        int ll = getPaddingLeft() + lp.leftMargin;
        int centerH = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - yzzTab.getMeasuredHeight()) / 2;
        int tt = centerH + lp.topMargin;
        int rr = ll + yzzTab.getMeasuredWidth();
        int bb = tt + yzzTab.getMeasuredHeight();
        yzzTab.layout(ll, tt, rr, bb);
        maxScroll = yzzTab.getMaxScroll();
    }

    public YzzTabLayout setTab(Tab tab) {
        return this;
    }

    public YzzTabLayout setTabList(List<String> tabList) {
        if (tabList == null) return this;
        int size = tabList.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.isEmpty(tabList.get(i))) return this;
            Tab.newTab().setText(tabList.get(i));
        }
        return this;
    }

    public void changeTabView(int textSize, int textColor, Typeface typeface) {
        if (tabList == null) return;
        int size = tabList.size();
        for (int i = 0; i < size; i++) {
            tabList.get(i).changeFace(textSize, textColor, typeface);
        }
    }

    public void commit() {
        yzzTab.removeAllViews();
        int count = tabViews.size();
        for (int i = 0; i < count; i++) {
            yzzTab.addView(tabViews.get(i));
            if (i == 0) changeTabTextColor(true);
        }
//        if (count > 0)
//            yzzTab.setCurrentPosition(currentChosePosition, paint);
    }

    private void changeTabTextColor(boolean isFirst) {
        if (viewPager != null && viewPager.getAdapter() != null) {
            if (viewPager.getAdapter().getCount() != tabViews.size()) {
                throw new RuntimeException("the tab's num must equal the viewpager's child num");
            }
            viewPager.setCurrentItem(currentChosePosition);
        }
        if (isFirst && yzzTabSelectListener != null) {
            yzzTabSelectListener.onSelect(tabViews.get(currentChosePosition), currentChosePosition);
        }
        if (!isFirst && yzzTabSelectListener != null) {
            if (currentChosePosition == lastChosePosition) {
                yzzTabSelectListener.onReSelect(tabViews.get(currentChosePosition), currentChosePosition);
            } else {
                yzzTabSelectListener.onSelect(tabViews.get(currentChosePosition), currentChosePosition);
                yzzTabSelectListener.onUnSelect(tabViews.get(lastChosePosition), lastChosePosition);
            }
        }
        DrawHelper.changeTextColor(yzzTab, isNeedIndicator, paint);
    }

    private void changeTabTextColorByViewPager() {
        if (yzzTabSelectListener != null) {
            if (currentChosePosition == lastChosePosition) {
                yzzTabSelectListener.onReSelect(tabViews.get(currentChosePosition), currentChosePosition);
            } else {
                yzzTabSelectListener.onSelect(tabViews.get(currentChosePosition), currentChosePosition);
                yzzTabSelectListener.onUnSelect(tabViews.get(lastChosePosition), lastChosePosition);
            }
        }
        DrawHelper.changeTextColor(yzzTab, isNeedIndicator, paint);
    }

    private void changeTabTextColor() {
        changeTabTextColor(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstTouch = event.getRawX();
                touchFirst = event.getRawX();
                isClickEvent = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getRawX() - touchFirst) < mineScrollDistance) {
                    isClickEvent = true;
                } else if (yzzTab.isNeedScroll) {
                    isClickEvent = false;
                    if (getScrollX() < 0) scrollTo(0, 0);
                    if (getScrollX() > maxScroll) scrollTo((int) maxScroll, 0);
                    isClickEvent = false;
                    float d = firstTouch - event.getRawX();
                    if (d < 0 && yzzTab.getScrollX() == 0) {
                        break;
                    }
                    if (yzzTab.getScrollX() < 0) {
                        yzzTab.scrollTo(0, 0);
                        break;
                    }
                    if (yzzTab.getScrollX() == maxScroll && d > 0) {
                        break;
                    }

                    if (yzzTab.getScrollX() > maxScroll) {
                        yzzTab.scrollTo((int) maxScroll, 0);
                        return false;
                    }
                    yzzTab.scrollTo((int) (d + yzzTab.getScrollX()), 0);
                    firstTouch = event.getRawX();
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("=====ddd=======","========"+isClickEvent);
                if (isClickEvent) {
                    clickEvent(event);
                }
                break;
        }
        return true;
    }

    /**
     * 处理点击事件
     *
     * @param event
     */
    private void clickEvent(MotionEvent event) {
        Log.e("=====ddd=======","========"+currentChosePosition);
        int size = tabViews.size();
        for (int i = 0; i < size; i++) {
            View tab = tabViews.get(i);
            float x = event.getRawX() + yzzTab.getScrollX();
            if (x >= tab.getLeft() && x <= tab.getRight()) {
                //该tab点击事件发生
                //记录选中的位置记录
                lastChosePosition = currentChosePosition;
                currentChosePosition = i;
                changeTabTextColor();
                Log.e("=====ddd=======","========"+currentChosePosition);
                //当超过父布局的宽度后，我们移动tab个宽度
                if (tab.getLeft() - yzzTab.getScrollX() < 0) {
                    int scroll = tab.getLeft();
                    yzzTab.scrollTo(scroll, 0);

                }
                int ll = tab.getRight() - yzzTab.getMeasuredWidth() - yzzTab.getScrollX();
                if (ll > 0 && ll < tab.getMeasuredWidth()) {
                    int scroll = tab.getRight() - yzzTab.getMeasuredWidth();
                    yzzTab.scrollTo(scroll, 0);
                }
                break;
            }
        }
    }

    public YzzTabLayout setUpWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        return this;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (currentChosePosition != position) {
            lastChosePosition = currentChosePosition;
            currentChosePosition = position;
            changeTabTextColorByViewPager();
            //当超过父布局的宽度后，我们移动tab个宽度
            View tab = tabViews.get(position);
            if (tab.getLeft() > yzzTab.getMeasuredWidth()) {
                int scroll = tab.getRight() - yzzTab.getMeasuredWidth();
                yzzTab.scrollTo(scroll, 0);
            }
            if (tab.getLeft() <= yzzTab.getMeasuredWidth() && yzzTab.getScrollX() != 0) {
                yzzTab.scrollTo(0, 0);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public static class Tab {
        private TextView textView;
        private View customView;
        private LayoutParams layoutParams;
        public static final int TEXT_SIZE = 15;
        public static final int TEXT_COLOR = 0xff333333;
        public static final int TEXT_SELECTOR_COLOR = 0xff00ff00;
        private static int selectColor = TEXT_SELECTOR_COLOR;
        private static int nomalColor = TEXT_COLOR;

        private Tab(Context context) {
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textView = new TextView(context);
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(TEXT_SIZE);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setTextColor(TEXT_COLOR);
            tabList.add(this);
            tabViews.add(textView);
        }

        public static Tab newTab() {
            return new Tab(mContext);
        }

        public Tab setText(String textContent) {
            if (TextUtils.isEmpty(textContent)) return this;
            textView.setText(textContent);
            return this;
        }

        public Tab setTextSize(int size) {
            textView.setTextSize(size);
            return this;
        }

        public Tab setTexrColor(int color) {
            nomalColor = color;
            textView.setTextColor(color);
            return this;
        }

        public Tab setTextStyle(Typeface typeface) {
            textView.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
            return this;
        }

        public Tab setSelectColor(int color) {
            selectColor = color;
            return this;
        }

        public void setCustomView(View customView) {
            if (customView == null) return;
            this.customView = customView;
            tabViews.remove(textView);
            tabViews.add(customView);
        }

        public void changeFace(int textSize, int textColor, Typeface typeface) {
            nomalColor = textColor;
            setTextSize(textSize).setTexrColor(textColor).setTextStyle(typeface == null ? Typeface.DEFAULT : typeface);
        }
    }

    private static class DrawHelper {

        private static void changeTextColor(YzzTab yzzTab, boolean isNeedIndicator, Paint paint) {
            View last = tabViews.get(lastChosePosition);
            View now = tabViews.get(currentChosePosition);
            if (isNeedIndicator) {
                yzzTab.setCurrentPosition(currentChosePosition, paint);
            }
            if (last instanceof TextView) {
                ((TextView) last).setTextColor(Tab.nomalColor);
            }
            if (now instanceof TextView) {
                ((TextView) now).setTextColor(Tab.selectColor);
            }

        }

    }

    public YzzTabLayout setYzzTabSelectListener(YzzTabSelectListener yzzTabSelectListener) {
        this.yzzTabSelectListener = yzzTabSelectListener;
        return this;
    }

    public interface YzzTabSelectListener {
        void onSelect(View view, int position);

        void onReSelect(View view, int position);

        void onUnSelect(View view, int position);
    }
}
