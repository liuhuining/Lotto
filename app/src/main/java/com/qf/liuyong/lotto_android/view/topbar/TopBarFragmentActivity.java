package com.qf.liuyong.lotto_android.view.topbar;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/1/24 0024.
 */
public abstract class TopBarFragmentActivity extends FragmentActivity implements TopBar.OnItemSelectedListener {
    TopBar mTopBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public TopBar getTopBar(){
        if (mTopBar == null){
            mTopBar = new TopBarImpl(this);
            mTopBar.setOnTopBarSelectedListener(this);
        }
        return mTopBar;
    }

    @Override
    public void setContentView(int layoutResID) {
        getTopBar().setContentView(layoutResID);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getTopBar().setContentView(view,params);
    }

    @Override
    public void onItemSelected(int featrueId) {
        if (onTopBarItemSelected(featrueId))
            return;
        switch (featrueId){
            case TopBar.FEARURE_BACK:
                String title = getTopBar().getTitle().toString();
                finish();
                break;
        }
    }
    /**
     * TopBar动作回调
     *
     * @param featureId 动作Id
     * @return 返回 <code>true</code> 代表子类已经成功处理事件 不再处理,
     *         <code>false</code> 代表子类不处理事件，由{@link TopBarFragmentActivity } 处理
     *
     */
    public boolean onTopBarItemSelected(int featureId) {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean isAppOnForeground(){
        ActivityManager activityManager = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null){
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcessInfos){
            if (appProcessInfo.processName.equals(packageName)
                    && appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE){
                return true;
            }
        }
        return false;
    }
}
