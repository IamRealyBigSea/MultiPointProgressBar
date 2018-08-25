package com.bigsea;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bigsea.library.R;

/**
 * Created by quanhaijiang@kugou.net on 2018/8/20.
 */

public class MultiPointProgressView extends View {

    private int progressColor, unProgressColor;
    private float lineHeight, corners, pointHeight, width, height, progressWidth;
    private Paint paint;
    private RectF rectF;
    private float pointCount, maxProgress;
    private int curProgress;
    private Bitmap bitmapPoint;


    public MultiPointProgressView(Context context) {
        this(context, null);
    }

    public MultiPointProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiPointProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiPointProgressView);
        progressColor = typedArray.getColor(R.styleable.MultiPointProgressView_progressColor, Color.parseColor("#ffff9600"));
        unProgressColor = typedArray.getColor(R.styleable.MultiPointProgressView_unProgressColor, Color.parseColor("#ff969696"));
        lineHeight = typedArray.getDimension(R.styleable.MultiPointProgressView_lineHeight, dp2px(6));
        pointHeight = typedArray.getDimension(R.styleable.MultiPointProgressView_pointHeight, dp2px(24));
        corners = typedArray.getDimension(R.styleable.MultiPointProgressView_corners, dp2px(3));
        curProgress = typedArray.getInteger(R.styleable.MultiPointProgressView_curProgress, 10);
        maxProgress = typedArray.getInteger(R.styleable.MultiPointProgressView_maxProgress, 100);
        pointCount = typedArray.getInteger(R.styleable.MultiPointProgressView_pointCount, 5);
        typedArray.recycle();
        paint = new Paint();
        paint.setDither(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        bitmapPoint = BitmapFactory.decodeResource(getResources(), R.drawable.point);
    }


    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public void setCurProgress(int curProgress) {
        this.curProgress = curProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        progressWidth = (width - pointHeight) / pointCount;
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        drawUnProgress(canvas);
        drawProgress(canvas);
        drawBitmapPoint(canvas);
        canvas.restoreToCount(layerId);
    }

    private void drawBitmapPoint(Canvas canvas) {
        for (int i = 1; i <= pointCount; i++) {
            canvas.drawBitmap(bitmapPoint, progressWidth * i + pointHeight / 2 - bitmapPoint.getWidth() / 2, height / 2 - bitmapPoint.getHeight() / 2, paint);
        }
    }

    private void drawProgress(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(progressColor);
        float top = height / 2 - pointHeight / 2;
        canvas.drawRect(0, top, (curProgress / maxProgress) * width, top + pointHeight, paint);
        paint.setXfermode(null);
    }

    private void drawUnProgress(Canvas canvas) {
        paint.setColor(unProgressColor);
        float top = height / 2 - lineHeight / 2;
        rectF.set(0, top, width, top + lineHeight);
        canvas.drawRoundRect(rectF, corners, corners, paint);
        for (int i = 1; i <= pointCount; i++) {
            canvas.drawCircle(progressWidth * i + pointHeight / 2, height / 2, pointHeight / 2, paint);
        }
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
