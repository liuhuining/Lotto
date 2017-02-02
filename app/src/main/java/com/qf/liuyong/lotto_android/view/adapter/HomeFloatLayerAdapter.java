package com.qf.liuyong.lotto_android.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.model.bean.HomePageResult;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.view.activity.BannerPictureDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class HomeFloatLayerAdapter extends BaseAdapter{

    private List<HomePageResult.NoticeListBean> list;

    public HomeFloatLayerAdapter(List<HomePageResult.NoticeListBean> list) {
        this.list = list;
    }

    public void setData(List<HomePageResult.NoticeListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.home_fuceng_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final HomePageResult.NoticeListBean result = list.get(position);
        final ImageView imageView = viewHolder.imageView;
        Glide.with(parent.getContext())
                .load(result.getUrl())
                .asBitmap()
                .placeholder(R.drawable.home_list_default_pic)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource == null)
                            return;
                        int screenWidth = ScreenUtils.getScreenWidthHeight(view.getContext())[0] - ScreenUtils.dip2px(view.getContext(), 74);
                        //按道理来说这个地方的width和height应该获取loadeImage的width和height 没办法被要求改了(750*460)
                        int width = 600;
                        int height = 200;
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.height = screenWidth * height / width;
                        params.width = screenWidth;
                        imageView.setLayoutParams(params);
                        imageView.setImageBitmap(resource);
                    }
                });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerPictureDetailActivity.start(v.getContext(), result.getGotoUrl());
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public final View root;
        public final ImageView imageView;

        public ViewHolder(View root) {
            this.root = root;
            imageView = (ImageView) root.findViewById(R.id.imageview);
        }
    }
}
