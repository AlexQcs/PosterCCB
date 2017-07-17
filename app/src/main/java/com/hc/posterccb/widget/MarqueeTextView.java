package com.hc.posterccb.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;

import com.hc.posterccb.util.LogUtils;

/**
 * Created by alex on 2017/7/17.
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    private float textLength = 0f;// 文本长度
    private float viewWidth = 0f;
    private float step = 0f;// 文字的横坐标
    private float y = 0f;// 文字的纵坐标
    private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
    private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
    public boolean isStarting = false;// 是否开始滚动
    private Paint paint = null;// 绘图样式
    private String text = "";// 文本内容

    private static int SCREEN_WIDTH;
    private static int SCREEN_HEIGHT;

    public MarqueeTextView(Context context) {
        super(context);
        initView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public void init(WindowManager windowManager) {
        paint = getPaint();
        // 邹奇   2016/11/30  这里可以自己设置文字显示的颜色，这里我设置为了蓝色，下载我的apk自己体验
        // 默认为黑色
        if(color != 0){
            paint.setColor(color);
        }
        text = getText().toString();
        textLength = paint.measureText(text);
        viewWidth = getWidth();
        if (viewWidth == 0) {
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                SCREEN_WIDTH= size.x;
                SCREEN_HEIGHT= size.y;
                viewWidth = SCREEN_WIDTH;
            }
        }
        step = textLength;
        temp_view_plus_text_length = viewWidth + textLength;
        temp_view_plus_two_text_length = viewWidth + textLength * 2;
        y = getTextSize() + getPaddingTop();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.step = step;
        ss.isStarting = isStarting;

        return ss;

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        step = ss.step;
        isStarting = ss.isStarting;

    }

    public static class SavedState extends BaseSavedState {
        public boolean isStarting = false;
        public float step = 0.0f;

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBooleanArray(new boolean[] { isStarting });
            out.writeFloat(step);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
        };

        private SavedState(Parcel in) {
            super(in);
            boolean[] b = null;
            in.readBooleanArray(b);
            if (b != null && b.length > 0)
                isStarting = b[0];
            step = in.readFloat();
        }
    }

    public void startScroll() {
        isStarting = true;
        invalidate();
    }

    public void stopScroll() {
        isStarting = false;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(text, temp_view_plus_text_length - step, y, paint);
        if (!isStarting) {
            return;
        }
        if(speed != 0){
            step += speed;// speed为用户自己设定的文字滚动速度
        }else {
            step += 0.5;// 用户没有设置速度，则默认0.5为文字滚动速度。
        }
        if (step > temp_view_plus_two_text_length)
            step = textLength;
        invalidate();

    }

    private double speed = 0;// 邹奇  2016/11/30  声明变量表示文字滚动的速度
    /**
     * 邹奇   2016/11/30  用户自己设定文字的滚动速度
     * @param speed 速度（一般设置值为2.0即可，快慢自己可以设置新值调节）
     */
    public void setSpeed(double speed){
        this.speed = speed;
    }

    private int color = 0;// 邹奇 2016/11/30  声明变量表示文字显示的颜色
    /**
     * 邹奇   2016/11/30  用户自己设定文字显示的颜色
     * @param color 颜色
     */
    public void setColors(int color){
        this.color = color;
    }


    public void restartScroll() {
        super.computeScroll();
        startScroll();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        startScroll();
    }

    /**
     * 判断TextView的内容宽度是否超出其可用宽度
     * @return
     */
    public boolean isOverFlowed() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        Paint textViewPaint = getPaint();
        float textWidth = textViewPaint.measureText(getText().toString());
        if (textWidth > availableWidth) {
            LogUtils.e("isOverFlowed","滑动");
            return true;
        } else {
            LogUtils.e("isOverFlowed","不滑动");
            return false;
        }
    }


}
