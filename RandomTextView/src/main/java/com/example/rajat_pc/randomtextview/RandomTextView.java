package com.example.rajat_pc.randomtextview;


import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.rajat_pc.rippleoutview.RippleView;


public class RandomTextView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String tag = RandomTextView.class.getSimpleName();
    private static final int MAX = 5;
    private static final int IDX_X = 0;
    private static final int IDX_Y = 1;
    private static final int IDX_TXT_LENGTH = 2;
    private static final int IDX_DIS_Y = 3;
    private static final int TEXT_SIZE = 12;

    private Random random;
    private Vector<String> vecKeywords;
    private int width;
    private int height;
    private int mode = RippleView.MODE_OUT;
    private int fontColor = 0xff0000ff;
    private int shadowColor = 0xdd696969;

    public interface OnRippleViewClickListener {
        void onRippleViewClicked(View view);
    }

    private OnRippleViewClickListener onRippleOutViewClickListener;

    public RandomTextView(Context context) {
        super(context);
        init(null, context);
    }

    public RandomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @TargetApi(21)
    public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr,
                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setOnRippleViewClickListener(OnRippleViewClickListener listener) {
        onRippleOutViewClickListener = listener;
    }

    /**
     * @param keyword
     */
    public void addKeyWord(String keyword) {
        if (vecKeywords.size() < MAX) {
            if (!vecKeywords.contains(keyword))
                vecKeywords.add(keyword);
        }
    }

    public Vector<String> getKeyWords() {
        return vecKeywords;
    }

    public void removeKeyWord(String keyword) {
        if (vecKeywords.contains(keyword)) {
            vecKeywords.remove(keyword);
        }
    }

    private void init(AttributeSet attrs, Context context) {
        random = new Random();
        vecKeywords = new Vector<String>(MAX);
        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @Override
    public void onGlobalLayout() {
        int tmpW = getWidth();
        int tmpH = getHeight();
        if (width != tmpW || height != tmpH) {
            width = tmpW;
            height = tmpH;
            Log.d(tag, "RandomTextView width = " + width + "; height = " + height);
        }
    }

    public void show() {
        this.removeAllViews();

        if (width > 0 && height > 0 && vecKeywords != null && vecKeywords.size() > 0) {
            //Find the center point
            int xCenter = width >> 1;
            int yCenter = height >> 1;
            //The number of keywords。
            int size = vecKeywords.size();
            int xItem = width / (size + 1);
            int yItem = height / (size + 1);
            LinkedList<Integer> listX = new LinkedList<>();
            LinkedList<Integer> listY = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                listX.add(i * xItem);
                listY.add(i * yItem + (yItem >> 2));
            }
            LinkedList<RippleView> listTxtTop = new LinkedList<>();
            LinkedList<RippleView> listTxtBottom = new LinkedList<>();

            for (int i = 0; i < size; i++) {
                String keyword = vecKeywords.get(i);
                // random color
                int ranColor = fontColor;
                // Random position, roughness
                int xy[] = randomXY(random, listX, listY, xItem);

                int txtSize = TEXT_SIZE;
                // Instantiate RippleOutView
                final RippleView txt = new RippleView(getContext());
                if (mode == RippleView.MODE_IN) {
                    txt.setMode(RippleView.MODE_IN);
                } else {
                    txt.setMode(RippleView.MODE_OUT);
                }

                txt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onRippleOutViewClickListener != null)
                            onRippleOutViewClickListener.onRippleViewClicked(view);
                    }
                });
                txt.setText(keyword);
                txt.setTextColor(ranColor);
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
                txt.setShadowLayer(1, 1, 1, shadowColor);
                txt.setGravity(Gravity.CENTER);
                txt.startRippleAnimation();

                // Get the text length
                //Paint paint = txt.getPaint();
                int strWidth = /* (int) Math.ceil(paint.measureText(keyword)) */txt
                        .getMeasuredWidth();
                xy[IDX_TXT_LENGTH] = strWidth;
                // first: fix x coordinate
                if (xy[IDX_X] + strWidth > width - (xItem/* >> 1 */)) {
                    int baseX = width - strWidth;
                    // Reduce the probability of the right edge of the text
                    xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);
                } else if (xy[IDX_X] == 0) {
                    // Reduce the probability of the left edge of the text
                    xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
                }
                xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);
                txt.setTag(xy);
                if (xy[IDX_Y] > yCenter) {
                    listTxtBottom.add(txt);
                } else {
                    listTxtTop.add(txt);
                }
            }

            attach2Screen(listTxtTop, xCenter, yCenter, yItem);
            attach2Screen(listTxtBottom, xCenter, yCenter, yItem);
        }
    }

    /**
     * Fix the Y coordinate of the RippleOutView to add it to the container。
     */
    private void attach2Screen(LinkedList<RippleView> listTxt, int xCenter, int yCenter,
                               int yItem) {
        int size = listTxt.size();
        sortXYList(listTxt, size);
        for (int i = 0; i < size; i++) {
            RippleView txt = listTxt.get(i);
            int[] iXY = (int[]) txt.getTag();
            // The second correction: correct y coordinates
            int yDistance = iXY[IDX_Y] - yCenter;
            int yMove = Math.abs(yDistance);
            inner:
            for (int k = i - 1; k >= 0; k--) {
                int[] kXY = (int[]) listTxt.get(k).getTag();
                int startX = kXY[IDX_X];
                int endX = startX + kXY[IDX_TXT_LENGTH];
                if (yDistance * (kXY[IDX_Y] - yCenter) > 0) {
                    if (isXMixed(startX, endX, iXY[IDX_X], iXY[IDX_X]
                            + iXY[IDX_TXT_LENGTH])) {
                        int tmpMove = Math.abs(iXY[IDX_Y] - kXY[IDX_Y]);
                        if (tmpMove > yItem) {
                            yMove = tmpMove;
                        } else if (yMove > 0) {
                            yMove = 0;
                        }
                        break inner;
                    }
                }
            }

            if (yMove > yItem) {
                int maxMove = yMove - yItem;
                int randomMove = random.nextInt(maxMove);
                int realMove = Math.max(randomMove, maxMove >> 1) * yDistance
                        / Math.abs(yDistance);
                iXY[IDX_Y] = iXY[IDX_Y] - realMove;
                iXY[IDX_DIS_Y] = Math.abs(iXY[IDX_Y] - yCenter);
                sortXYList(listTxt, i + 1);
            }
            FrameLayout.LayoutParams layParams = new FrameLayout.LayoutParams(
            /* FrameLayout.LayoutParams.WRAP_CONTENT */200,
            /* FrameLayout.LayoutParams.WRAP_CONTENT */200);
            layParams.gravity = Gravity.START | Gravity.TOP;
            layParams.leftMargin = iXY[IDX_X];
            layParams.topMargin = iXY[IDX_Y];
            addView(txt, layParams);
        }
    }

    private int[] randomXY(Random ran, LinkedList<Integer> listX,
                           LinkedList<Integer> listY, int xItem) {
        int[] arr = new int[4];
        arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
        arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
        return arr;
    }

    /**
     * Whether there is an intersection on the X-axis mapping of the line represented by line A and line B。
     */
    private boolean isXMixed(int startA, int endA, int startB, int endB) {
        boolean result = false;
        if (startB >= startA && startB <= endA) {
            result = true;
        } else if (endB >= startA && endB <= endA) {
            result = true;
        } else if (startA >= startB && startA <= endB) {
            result = true;
        } else if (endA >= startB && endA <= endB) {
            result = true;
        }
        return result;
    }

    /**
     * According to the distance from the center of the bubble from near to far sort。
     *
     * @param endIdx
     * @param listTxt
     */
    private void sortXYList(LinkedList<RippleView> listTxt, int endIdx) {
        for (int i = 0; i < endIdx; i++) {
            for (int k = i + 1; k < endIdx; k++) {
                if (((int[]) listTxt.get(k).getTag())[IDX_DIS_Y] < ((int[]) listTxt
                        .get(i).getTag())[IDX_DIS_Y]) {
                    RippleView iTmp = listTxt.get(i);
                    RippleView kTmp = listTxt.get(k);
                    listTxt.set(i, kTmp);
                    listTxt.set(k, iTmp);
                }
            }
        }
    }
}
