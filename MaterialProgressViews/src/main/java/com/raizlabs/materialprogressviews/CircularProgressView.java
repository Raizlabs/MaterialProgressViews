package com.raizlabs.materialprogressviews;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircularProgressView extends View {

    private RectF circleRect;

    private Paint radiusPaint;
    private Paint endcapPaint;
    private int lineThickness;

    private int sweepAngle1, sweepAngle2;

    private float fastSpeed, slowSpeed;
    private float swapSweepAngle;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        readArray(arr);
        arr.recycle();
    }

    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        readArray(arr);
        arr.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressView);
        readArray(arr);
        arr.recycle();
    }

    public void init() {
        circleRect = new RectF();
        radiusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        radiusPaint.setStyle(Paint.Style.STROKE);

        endcapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        if (!isInEditMode()) {
            setSweepAngle1(0);
            setSweepAngle2(0);

            setLineColorResource(R.color.CircularProgressView_Defaults_LineColor);
            setLineThickness(getResources().getDimensionPixelSize(R.dimen.CircularProgressView_Defaults_LineThickness));

            setFastSpeed(getResources().getInteger(R.integer.CircularProgressView_Defaults_FastSpeed));
            setSlowSpeed(getResources().getInteger(R.integer.CircularProgressView_Defaults_SlowSpeed));

            setSwapAngle(getResources().getInteger(R.integer.CircularProgressView_Defaults_SwapAngle));
        } else {

            setSweepAngle1(0);
            setSweepAngle2(270);

            setLineColor(Color.BLUE);
            setLineThickness(20);

            setFastSpeed(500);
            setSlowSpeed(200);

            setSwapAngle(270);
        }

        updateCircleRect();
    }

    private void readArray(TypedArray arr) {
        setLineColor(arr.getColor(R.styleable.CircularProgressView_circularProgressViewLineColor, radiusPaint.getColor()));
        setLineThickness(arr.getDimensionPixelSize(R.styleable.CircularProgressView_circularProgressViewLineThickness, lineThickness));

        setFastSpeed(arr.getFloat(R.styleable.CircularProgressView_circularProgressViewFastSpeed, fastSpeed));
        setSlowSpeed(arr.getFloat(R.styleable.CircularProgressView_circularProgressViewSlowSpeed, slowSpeed));

        setSwapAngle(arr.getFloat(R.styleable.CircularProgressView_circularProgressViewSwapAngle, swapSweepAngle));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateCircleRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float circleCenterX = circleRect.centerX();
        final float circleCenterY = circleRect.centerY();
        final float circleRadius = circleRect.width() / 2;

        final float sweep1X = ((float) Math.cos(Math.toRadians(sweepAngle1)) * circleRadius) + circleCenterX;
        final float sweep1Y = ((float) Math.sin(Math.toRadians(sweepAngle1)) * circleRadius) + circleCenterY;

        final float sweep2X = ((float) Math.cos(Math.toRadians(sweepAngle2)) * circleRadius) + circleCenterX;
        final float sweep2Y = ((float) Math.sin(Math.toRadians(sweepAngle2)) * circleRadius) + circleCenterY;

        canvas.drawArc(circleRect, sweepAngle1, sweepAngle2 - sweepAngle1, false, radiusPaint);
        canvas.drawCircle(sweep1X, sweep1Y, lineThickness / 2, endcapPaint);
        canvas.drawCircle(sweep2X, sweep2Y, lineThickness / 2, endcapPaint);
    }

    /**
     * Sets the speed of the faster head.
     *
     * @param speed The speed in degrees per second.
     */
    public void setFastSpeed(float speed) {
        this.fastSpeed = speed;
    }

    /**
     * Sets the speed of the slower head.
     *
     * @param speed The speed in degrees per second.
     */
    public void setSlowSpeed(float speed) {
        this.slowSpeed = speed;
    }

    /**
     * Sets the max angle between the two heads before a swap happens.
     *
     * @param swapAngle The angle between the two heads before a swap happens. In degrees.
     */
    public void setSwapAngle(float swapAngle) {
        this.swapSweepAngle = swapAngle;
    }

    /**
     * Sets the color of the line.
     *
     * @param color The color of the line.
     */
    public void setLineColor(int color) {
        this.radiusPaint.setColor(color);
        this.endcapPaint.setColor(color);
    }

    /**
     * Sets the color of the line from resources.
     *
     * @param colorResId The resource of id of the color to use for the line.
     */
    @SuppressWarnings("deprecation")
    public void setLineColorResource(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setLineColor(getResources().getColor(colorResId, getContext().getTheme()));
        } else {
            setLineColor(getResources().getColor(colorResId));
        }
    }

    /**
     * Sets the thickness of the line.
     *
     * @param thickness The thickness of the line in pixels.
     */
    public void setLineThickness(int thickness) {
        this.lineThickness = thickness;
        radiusPaint.setStrokeWidth(thickness);
        updateCircleRect();
    }

    /**
     * Sets the current position of the first head.
     *
     * @param angle The angle of the first head in degrees.
     */
    public void setSweepAngle1(int angle) {
        this.sweepAngle1 = angle;
        invalidate();
    }

    /**
     * Sets the current position of the second head.
     *
     * @param angle The angle of the second head in degrees.
     */
    public void setSweepAngle2(int angle) {
        this.sweepAngle2 = angle;
        invalidate();
    }

    /**
     * Starts animating the circle continuously.
     */
    public void animateContinuous() {
        final int currentSweepLength = sweepAngle2 - sweepAngle1;
        final float deltaSweep = (swapSweepAngle - currentSweepLength) + (currentSweepLength == swapSweepAngle ? (swapSweepAngle * 2) : 0);
        final float sweepGainSpeed = fastSpeed - slowSpeed;

        final float timeUntilSwap = deltaSweep / sweepGainSpeed;

        final boolean isAngle1Fast = currentSweepLength >= 0;

        final int sweep1Target = (int) (sweepAngle1 + ((isAngle1Fast ? fastSpeed : slowSpeed) * timeUntilSwap));
        final int sweep2Target = (int) (sweepAngle2 + ((isAngle1Fast ? slowSpeed : fastSpeed) * timeUntilSwap));

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofInt(this, "sweepAngle1", sweepAngle1, sweep1Target))
                .with(ObjectAnimator.ofInt(this, "sweepAngle2", sweepAngle2, sweep2Target));

        long duration = (long) (timeUntilSwap * 1000);
        if(duration > 0){
            set.setDuration(duration);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    animateContinuous();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.setInterpolator(new LinearInterpolator());
            set.start();
        }
    }

    private void updateCircleRect() {
        final int halfThickness = lineThickness / 2;
        circleRect.left = halfThickness + 1 + getPaddingLeft();
        circleRect.top = halfThickness + 1 + getPaddingTop();
        circleRect.right = getWidth() - halfThickness - 1 - getPaddingRight();
        circleRect.bottom = getHeight() - halfThickness - 1 - getPaddingBottom();
    }
}
