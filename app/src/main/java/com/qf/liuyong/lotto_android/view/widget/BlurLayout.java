package com.qf.liuyong.lotto_android.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qf.liuyong.lotto_android.utils.ImageUtils;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public class BlurLayout extends FrameLayout{

    public ImageView mBlurredImageView;
    private View mContentView;

    /**
     * Blur radius used for the background.
     */
    private int mBlurRadius = DEFAULT_BLUR_RADIUS;


    /**
     * Default Blur Radius
     */


    public static int DEFAULT_BLUR_RADIUS = 15;


    /**
     * Down scale factor to reduce blurring time and memory allocation.
     */
    private float mDownScaleFactor = DEFAULT_DOWNSCALEFACTOR;

    /**
     * Default Down Scale Factor
     */


    public static float DEFAULT_DOWNSCALEFACTOR = 5.0f;


    /**
     * Render flag
     * <p>
     * If true we must render
     * if false, we have already blurred the background
     */
    private boolean prepareToRender = true;

    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    public BlurLayout(Context context, View content) {
        super(context);
        mBlurredImageView = new ImageView(context);
        mBlurredImageView.setClickable(true);
        mBlurredImageView.setVisibility(GONE);
        mBlurredImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(content,params);
        addView(mBlurredImageView,params);
    }

    /**
     * Snapshots the specified layout and scale it using scaleBitmap() function
     * then we blur the scaled bitmap with the preferred blur radius.
     * Finally, we post it to our fake {@link ImageView}.
     */

    public void render(){
        if (prepareToRender){
            prepareToRender = false;

            Bitmap bitmap = loadBitmapFromView(mContentView);

            bitmap = scaleBitmap(bitmap);

            bitmap = ImageUtils.fastBlur(getContext(),bitmap,mBlurRadius,false);

            mBlurredImageView.setVisibility(VISIBLE);
            mBlurredImageView.setImageBitmap(bitmap);
        }
    }

    public void setRadius(int radius) {
        mBlurRadius = radius < 1 ? 1 : radius;
    }


    public void setDownScaleFactor(float downScaleFactor) {
        mDownScaleFactor = downScaleFactor < 1 ? 1 : downScaleFactor;
    }

    private void setAlpha(View view, float alpha, long durationMillis) {
        if (Build.VERSION.SDK_INT < 11) {
            final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
            animation.setDuration(durationMillis);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        } else {
            view.setAlpha(alpha);
        }
    }

    private Bitmap loadBitmapFromView(View mView){
        Bitmap b = Bitmap.createBitmap(
                mView.getWidth(),
                mView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        mView.draw(c);
        return b;
    }

    private Bitmap scaleBitmap(Bitmap bitmap){
        int width = (int) (bitmap.getWidth()/mDownScaleFactor);
        int height = (int) (bitmap.getHeight()/mDownScaleFactor);

        return Bitmap.createScaledBitmap(bitmap,width,height,false);
    }

    public void handleRecycle(){
        Drawable drawable = mBlurredImageView.getDrawable();

        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            if (bitmap != null){
                bitmap.recycle();
            }
            mBlurredImageView.setImageBitmap(null);
        }
        prepareToRender = true;
        mBlurredImageView.setVisibility(GONE);
    }

    public boolean isPrepareToRender() {
        return prepareToRender;
    }

    public void setPrepareToRender(boolean prepareToRender) {
        this.prepareToRender = prepareToRender;
    }
}
