package com.example.canvasplayground.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class KNView extends View {

    // Paint
    Paint mBackgroundPaint = new Paint();
    Paint mPrimaryCircleColor = new Paint();
    Paint mSecondaryCircleColor = new Paint();
    Paint mTextPaint = new Paint();


    // Metrics
    Integer mTotalWidth;
    Integer mTotalHeight;
    Integer mLeftGrid;
    Integer mTopGrid;
    Integer mRightGrid;
    Integer mBottomGrid;
    Integer mMargin;
    float mRotateDegrees = 0f;


    // Flags
    boolean mIsVisible = false;

    // Speed
    Integer mRotationSpeedInMs = 1200;


    String text = "Βρίσκουμε το καλύτερο \n ραντεβού για εσάς...";

    // Path
    Path mCircleHolePath = new Path();
    Path mRectanglePath = new Path();


    public KNView(Context context) {
        super(context);
    }

    public KNView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public KNView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }

    private void init(){

        mMargin = 40;
        mRotationSpeedInMs = 1000;

        mTotalHeight = getHeight();
        mTotalWidth = getWidth();

        mLeftGrid = mMargin;
        mTopGrid = mMargin;
        mRightGrid = mTotalWidth - mMargin;
        mBottomGrid = mTotalHeight - mMargin;

        mBackgroundPaint.setColor(0x88094373);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setAntiAlias(true);

        mPrimaryCircleColor.setColor(Color.WHITE);
        mPrimaryCircleColor.setStyle(Paint.Style.FILL);
        mPrimaryCircleColor.setAntiAlias(true);

        mSecondaryCircleColor.setColor(Color.parseColor("#ff5723"));
        mSecondaryCircleColor.setStyle(Paint.Style.FILL);
        mSecondaryCircleColor.setAntiAlias(true);

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(70);
    }



    protected void drawHoledCircle(Canvas canvas){

        Integer workingWidth = (mRightGrid - mLeftGrid) / 2;
        Integer workingHeight = (mBottomGrid - mTopGrid) / 2;

        canvas.translate(workingWidth, workingHeight); //Move it to center

        canvas.save();
        canvas.rotate(mRotateDegrees);
        mCircleHolePath.reset();
        mCircleHolePath.addCircle(0,0, Math.min(workingHeight, workingWidth)/5, Path.Direction.CW);
        canvas.clipPath(mCircleHolePath, Region.Op.DIFFERENCE);

        canvas.drawCircle(0,0, Math.min(workingHeight, workingWidth)/4, mPrimaryCircleColor);

        mRectanglePath.addRect(0, -Math.min(workingHeight, workingWidth)/4,
                Math.min(workingHeight, workingWidth)/4,
                0, Path.Direction.CCW);
        canvas.clipPath(mRectanglePath, Region.Op.INTERSECT);

        canvas.drawCircle(0,0, Math.min(workingHeight, workingWidth)/4, mSecondaryCircleColor);
        canvas.restore();


        String[] lines = StringUtils.splitStringInLines(text);


        int i = 1;
        for (String line : lines){
            canvas.drawText(line, 0, Math.min(workingHeight, workingWidth)/4 +
                    i*((mTextPaint.descent() - mTextPaint.ascent()) / 2) + 40 +  i*20, mTextPaint );
            i++;
        }



    }






    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsVisible) return;

        init();
        canvas.translate(mLeftGrid, mTopGrid);
        canvas.drawPaint(mBackgroundPaint); // Draw background

        canvas.save();
        drawHoledCircle(canvas);
        canvas.restore();
    }



    // Determines if the loading spin in visible or not
    public void setIsVisible(Boolean isVisible){
        mIsVisible = isVisible;
        invalidate();

    }

    public void startAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.removeAllUpdateListeners();
        animator.setDuration(mRotationSpeedInMs);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateDegrees = (float)animation.getAnimatedValue();
                invalidate();
                Log.d("test", "onAnimationUpdate: " + mRotateDegrees);

                if (mRotateDegrees == 360){
                    startAnimation();
                }


            }
        });
        animator.start();
    }
}
