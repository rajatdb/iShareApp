package com.example.rajat_pc.rippleoutlayout;


import java.util.ArrayList;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;


/* This is a ripple effect layout similar to that
   paid by the pay bucket.
   The default view of the invisible circle is
   added to the layout. When the animation is
   started, the zoom, Animation makes the ripple effect.
   These animations are infinite loop, and each View
   animation between the time interval, these time intervals will
   lead to a large and small view,
   to produce corrugated Effect. */


public class RippleOutLayout extends RelativeLayout
{

    private static final int DEFAULT_RIPPLE_COUNT = 6;
    private static final int DEFAULT_DURATION_TIME = 4 * 1000;
    private static final float DEFAULT_SCALE = 5.0f;
    private static final int DEFAULT_RIPPLE_COLOR = Color.rgb(0x33, 0x99, 0xcc);
    private static final int DEFAULT_RADIUS = 100;
    private static final int DEFAULT_STROKE_WIDTH = 0;

    private int mRippleColor = DEFAULT_RIPPLE_COLOR;
    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;
    private float mRippleRadius = DEFAULT_RADIUS;
    private int mAnimDuration = DEFAULT_DURATION_TIME;
    private int mRippleViewNums = DEFAULT_RIPPLE_COUNT;
    private float mRippleScale = DEFAULT_SCALE;

    private boolean animationRunning = false;
    private int mAnimDelay;

    private Paint mPaint;

    private AnimatorSet mAnimatorSet = new AnimatorSet();

    private ArrayList<Animator> mAnimatorList = new ArrayList<Animator>();

    private LayoutParams mRippleViewParams;

    public RippleOutLayout(Context context)
    {
        super(context);
        init(context, null);
    }

    public RippleOutLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleOutLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (isInEditMode())
            return;

        if (attrs != null)
        {
            initTypedArray(context, attrs);
        }
        initPaint();
        initRippleViewLayoutParams();
        generateRippleViews();
    }

    private void initTypedArray(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
            R.styleable.RippleOutLayout);

        mRippleColor = typedArray.getColor(R.styleable.RippleOutLayout_rippleout_color,
            DEFAULT_RIPPLE_COLOR);
        mStrokeWidth = typedArray.getDimension(
            R.styleable.RippleOutLayout_rippleout_stroke_width, DEFAULT_STROKE_WIDTH);
        mRippleRadius = typedArray.getDimension(
            R.styleable.RippleOutLayout_rippleout_radius, DEFAULT_RADIUS);
        mAnimDuration = typedArray.getInt(R.styleable.RippleOutLayout_rippleout_duration,
            DEFAULT_DURATION_TIME);
        mRippleViewNums = typedArray.getInt(
            R.styleable.RippleOutLayout_rippleout_rippleNums, DEFAULT_RIPPLE_COUNT);
        mRippleScale = typedArray.getFloat(R.styleable.RippleOutLayout_rippleout_scale,
            DEFAULT_SCALE);

        typedArray.recycle();
    }

    private void initPaint()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mRippleColor);
    }

    private void initRippleViewLayoutParams()
    {
        int rippleSide = (int) (2 * (mRippleRadius + mStrokeWidth));
        mRippleViewParams = new LayoutParams(rippleSide, rippleSide);
        mRippleViewParams.addRule(CENTER_IN_PARENT, TRUE);  //居中显示
    }


    private void generateRippleViews()
    {
        calculateAnimDelay();
        initAnimSet();
        for (int i = 0; i < mRippleViewNums; i++)
        {
            RippleView rippleView = new RippleView(getContext());
            addView(rippleView, mRippleViewParams);
            addAnimToRippleView(rippleView, i);
        }

        mAnimatorSet.playTogether(mAnimatorList);
    }

    /**
     * @param rippleView
     * @param i
     */
    private void addAnimToRippleView(RippleView rippleView, int i)
    {
        // The x-axis zoom animation
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "scaleX",
            1.0f, mRippleScale);
        scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
        scaleXAnimator.setStartDelay(i * mAnimDelay);
        scaleXAnimator.setDuration(mAnimDuration);
        mAnimatorList.add(scaleXAnimator);

        // Y-axis zoom animation
        final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView,
            "scaleY", 1.0f, mRippleScale);
        scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
        scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnimator.setStartDelay(i * mAnimDelay);
        scaleYAnimator.setDuration(mAnimDuration);
        mAnimatorList.add(scaleYAnimator);

        // Color alpha gradient animation
        final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleView, "alpha",
            1.0f, 0f);
        alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
        alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        alphaAnimator.setDuration(mAnimDuration);
        alphaAnimator.setStartDelay(i * mAnimDelay);
        mAnimatorList.add(alphaAnimator);
    }

    private void initAnimSet()
    {
        mAnimatorSet.setDuration(mAnimDuration);
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void calculateAnimDelay()
    {
        mAnimDelay = mAnimDuration / mRippleViewNums;
    }

    public void startRippleAnimation()
    {
        if (!isRippleAnimationRunning())
        {
            animationRunning = true;
            mAnimatorSet.start();
            makeRippleViewsVisible();
        }
    }

    public void stopRippleAnimation()
    {
        if (isRippleAnimationRunning())
        {
            mAnimatorSet.end();
            animationRunning = false;
        }
    }

    private void makeRippleViewsVisible()
    {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View childView = this.getChildAt(i);
            if (childView instanceof RippleView)
            {
                childView.setVisibility(VISIBLE);
            }
        }
    }

    public boolean isRippleAnimationRunning()
    {
        return animationRunning;
    }

    private class RippleView extends View
    {

        public RippleView(Context context)
        {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            int radius = (Math.min(getWidth(), getHeight())) / 2;
            canvas.drawCircle(radius, radius, radius - mStrokeWidth, mPaint);
        }
    }

}
