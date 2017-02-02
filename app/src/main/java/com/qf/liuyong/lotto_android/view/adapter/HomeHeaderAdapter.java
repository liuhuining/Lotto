package com.qf.liuyong.lotto_android.view.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.model.bean.HomePageResult;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;

import java.util.LinkedList;
import java.util.List;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class HomeHeaderAdapter extends PagerAdapter {

    private List<HomePageResult.BannerListBean> list;
    private AutoScrollViewPager autoScrollViewPager;
    //是否第一次计算banner中imageview的宽高 默认是true
    private boolean isFirst = true;
    private LinkedList<View> recycledViews = new LinkedList<>();

    public HomeHeaderAdapter(AutoScrollViewPager autoScrollViewPager, List<HomePageResult.BannerListBean> list) {
        this.autoScrollViewPager = autoScrollViewPager;
        this.list = list;
        isFirst = true;
    }

    @Override
    public int getCount() {
        return list == null || list.size() == 0 ? 0 : 2000000001;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        int index = position % list.size();
        View view = null;
        final HomePageResult.BannerListBean result = list.get(index);
        if (recycledViews != null && recycledViews.size() > 0) {
            view = recycledViews.getFirst();
            recycledViews.removeFirst();
        } else {
            view = View.inflate(container.getContext(), R.layout.homeheader_viewpager_item, null);
        }
        final ImageView imageView = (ImageView) view.findViewById(R.id.header_iv);
        Glide.with(container.getContext().getApplicationContext())
                .load(result.getUrl())
                .asBitmap()
                .dontAnimate()
                .placeholder(R.drawable.banner_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (bitmap == null)
                            return;
                        if (isFirst) {
                            int screenWidth = ScreenUtils.getScreenWidthHeight(view.getContext())[0] - ScreenUtils.dip2px(view.getContext(), 160);
                            //按道理来说这个地方的width和height应该获取loadeImage的width和height 没办法被要求改了(750*460)
                            int width = 675;
                            int height = 381;
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.height = screenWidth * height / width;
                            params.width = screenWidth;
                            imageView.setLayoutParams(params);
                            ViewGroup.LayoutParams para = autoScrollViewPager.getLayoutParams();
                            para.height = screenWidth * height / width;
                            para.width = screenWidth;
                            autoScrollViewPager.setLayoutParams(para);
                            isFirst = false;
                        }
                        imageView.setImageBitmap(bitmap);
                    }
                });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        if (object != null) {
            recycledViews.addLast((View) object);
        }
    }
}
