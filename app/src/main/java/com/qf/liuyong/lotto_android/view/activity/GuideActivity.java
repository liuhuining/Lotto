package com.qf.liuyong.lotto_android.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.qf.liuyong.lotto_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class GuideActivity extends BaseActivity{

    private List<View> list = new ArrayList<>();
    private ImageView btn;
    private int[] imageIds = {R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3,
            R.drawable.guide_4,};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        getTopBar().hide();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        for (int i = 0; i < 4; i++) {
            View view = View.inflate(this, R.layout.guide_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            btn = (ImageView) view.findViewById(R.id.btn);
            if (i == 3){
                btn.setVisibility(View.VISIBLE);
            }else {
                btn.setVisibility(View.GONE);
            }
            list.add(view);
        }
        viewPager.setAdapter(new MyPagerAdapter(list));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hometab", getIntent().getSerializableExtra("hometab"));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    class MyPagerAdapter extends PagerAdapter{

        private List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews){
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position),0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
