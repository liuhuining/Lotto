package com.qf.liuyong.lotto_android.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.qf.liuyong.lotto_android.model.bean.HomePageResult;

import java.util.List;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {

    List<HomePageResult.StageListBean> stageListBeanList;
    private List<Fragment> fragments;
    private FragmentManager fm;

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public void setFragments(List<Fragment> fragments, List<HomePageResult.StageListBean> stageListBeanList) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        this.stageListBeanList = stageListBeanList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return stageListBeanList == null ? 0 : stageListBeanList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return stageListBeanList.get(position).getName();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
