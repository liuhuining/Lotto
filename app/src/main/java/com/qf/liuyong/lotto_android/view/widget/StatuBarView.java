package com.qf.liuyong.lotto_android.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class StatuBarView extends View{

    private Paint paintDefault, paint, currentStatuPaint;
    private int margin;
    private int screenWidth;
    private int point_1_x, point_1_y;
    private int point_2_x, point_2_y;
    private int point_3_x, point_3_y;
    private int statu;
    private Bitmap b2;
    private Bitmap b3;
    private Bitmap b1Select;
    private Bitmap b2Select;
    private Bitmap b3Select;
    private RectF rectF, rectF1, rectF2;

    public StatuBarView(Context context) {
        this(context, null);
    }

    public StatuBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatuBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StatuBarView);
        statu = ta.getInt(R.styleable.StatuBarView_statu, 0);
        String progressColor = ta.getString(R.styleable.StatuBarView_progressColor);
        String defaultProgressColor = ta.getString(R.styleable.StatuBarView_defaultProgressColor);
        ta.recycle();
        screenWidth = ScreenUtils.getScreenWidthHeight(context)[0];
        paintDefault = new Paint();
        paintDefault.setAntiAlias(true);
        paintDefault.setColor(Color.parseColor(defaultProgressColor));
        paintDefault.setStrokeWidth(10);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(progressColor));
        paint.setStrokeWidth(10);
        currentStatuPaint = new Paint();
        currentStatuPaint.setColor(Color.WHITE);
        currentStatuPaint.setAntiAlias(true);
        b2 = BitmapFactory.decodeResource(getResources(), R.drawable.statu_2);
        b3 = BitmapFactory.decodeResource(getResources(), R.drawable.statu_3);
        b1Select = BitmapFactory.decodeResource(getResources(), R.drawable.statu_1_select);
        b2Select = BitmapFactory.decodeResource(getResources(), R.drawable.statu_2_select);
        b3Select = BitmapFactory.decodeResource(getResources(), R.drawable.statu_3_select);
        point_1_y = point_2_y = point_3_y = ScreenUtils.dip2px(context,10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        margin = layoutParams.leftMargin;
        point_1_x = b1Select.getWidth() / 2;
        point_2_x = screenWidth / 2 - margin;
        point_3_x = screenWidth - (b3Select.getWidth() / 2) - margin * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int raduis = ScreenUtils.dip2px(getContext(),10);
        int radius_2 = ScreenUtils.dip2px(getContext(),7);
        int radius_3 = ScreenUtils.dip2px(getContext(),5);
        int startX = 0;
        int bitmapMarginTop = ScreenUtils.dip2px(getContext(),25);

        if (rectF == null) {
            rectF = new RectF(startX, point_1_y - 5, screenWidth - margin * 2, point_1_y + 5);
        }
        canvas.drawRoundRect(rectF, 5, 5 , paintDefault);
        int b1_start_x = 0;
        int b2_start_x = point_2_x - point_1_x;
        int b3_start_x = screenWidth - b3.getWidth() - margin * 2;
        switch (statu) {
            default:
            case 1:
                if (rectF1 == null) {
                    rectF1 = new RectF(startX, point_1_y - 5, point_1_x, point_1_y + 5);
                }
                canvas.drawRoundRect(rectF1, 5, 5, paint);
                canvas.drawCircle(point_1_x, point_1_y, raduis, paint);
                canvas.drawCircle(point_1_x, point_1_y, radius_3, currentStatuPaint);
                canvas.drawCircle(point_2_x, point_2_y, raduis, paintDefault);
                canvas.drawCircle(point_3_x, point_3_y, raduis, paintDefault);
                canvas.drawBitmap(b1Select, b1_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b2, b2_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b3, b3_start_x, bitmapMarginTop, paintDefault);
                break;
            case 2:
                if (rectF2 == null) {
                    rectF2 = new RectF(startX, point_1_y - 5, point_2_x, point_1_y + 5);
                }
                canvas.drawRoundRect(rectF2, 5, 5, paint);
                canvas.drawCircle(point_1_x, point_1_y, raduis, paint);
                canvas.drawCircle(point_1_x, point_1_y, radius_2, currentStatuPaint);
                canvas.drawCircle(point_2_x, point_2_y, raduis, paint);
                canvas.drawCircle(point_2_x, point_2_y, radius_3, currentStatuPaint);
                canvas.drawCircle(point_3_x, point_3_y, raduis, paintDefault);
                canvas.drawBitmap(b1Select, b1_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b2Select, b2_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b3, b3_start_x, bitmapMarginTop, paintDefault);
                break;
            case 3:
                canvas.drawRoundRect(rectF, 5, 5, paint);
                canvas.drawCircle(point_1_x, point_1_y, raduis, paint);
                canvas.drawCircle(point_1_x, point_1_y, radius_2, currentStatuPaint);
                canvas.drawCircle(point_2_x, point_2_y, raduis, paint);
                canvas.drawCircle(point_2_x, point_2_y, radius_2, currentStatuPaint);
                canvas.drawCircle(point_3_x, point_3_y, raduis, paint);
                canvas.drawCircle(point_3_x, point_3_y, radius_3, currentStatuPaint);
                canvas.drawBitmap(b1Select, b1_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b2Select, b2_start_x, bitmapMarginTop, paintDefault);
                canvas.drawBitmap(b3Select, b3_start_x, bitmapMarginTop, paintDefault);
                break;
        }
    }

    public void setStatu(int statu) {
        this.statu = statu;
        invalidate();
    }
}
