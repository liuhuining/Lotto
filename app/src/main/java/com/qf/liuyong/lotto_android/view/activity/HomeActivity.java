package com.qf.liuyong.lotto_android.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.presenter.BusProvider;
import com.qf.liuyong.lotto_android.presenter.EditCompleteOnComputerEvent;
import com.qf.liuyong.lotto_android.presenter.FinishAppEvent;
import com.qf.liuyong.lotto_android.presenter.PublishLoginEvent;
import com.qf.liuyong.lotto_android.model.Hometab;
import com.qf.liuyong.lotto_android.model.bean.PersonInfo;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.presenter.RefreshFragmentEvent;
import com.qf.liuyong.lotto_android.presenter.RemitSuccessEvent;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.utils.ToastUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;
import com.qf.liuyong.lotto_android.view.fragment.ConversationListFragment;
import com.qf.liuyong.lotto_android.view.fragment.HomeFragment;
import com.qf.liuyong.lotto_android.view.fragment.LeftMenuFragment;
import com.qf.liuyong.lotto_android.view.fragment.ReleaseFragment;
import com.qf.liuyong.lotto_android.view.service.DownloadService;
import com.qf.liuyong.lotto_android.view.topbar.TopBar;
import com.qf.liuyong.lotto_android.view.widget.FragmentTabHost;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class HomeActivity extends BaseActivity {

    protected static final String TAG = "HomeActivity";
    public FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    //未读消息textview
    private TextView unreadLabel;
    //账号在别处登录
    public boolean isConflict = false;
    //账号被移除
    private boolean isCurrentAccountRemoved = false;
    private List<StateListDrawable> drawableList;

    //Fragment界面
    private Class fragmentArray[] = {
            HomeFragment.class,
            ReleaseFragment.class,
            ConversationListFragment.class,
            LeftMenuFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {
            R.drawable.home_tab_1_selector,
            R.drawable.home_tab_3_selector,
            R.drawable.home_tab_4_selector,
            R.drawable.home_tab_5_selector};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"乐投", "发布", "交流", "我的"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smoothSwitchScreen();
        setContentView(R.layout.home);
        BusProvider.getInstance().register(this);
        App.instance.setFirst();
        getTopBar().hide();

        initDrawableList((Hometab) getIntent().getSerializableExtra("hometab"));

        initView();

        DownloadService.getInstance().init(this);
        DownloadService.getInstance().checkUpdate(false);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

    }

    private StateListDrawable createSelector(String nomarlUrl, String pressedUrl){
        final StateListDrawable drawable = new StateListDrawable();
        int height = ScreenUtils.dip2px(this, 50);

        Glide.with(getApplicationContext())
                .load(nomarlUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                                new BitmapDrawable(resource));
                        drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                                new BitmapDrawable(resource));
                    }
                });
        Glide.with(getApplicationContext())
                .load(pressedUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>(height, height) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        //Pressed
                        drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
                                new BitmapDrawable(bitmap));
                        drawable.addState(new int[]{android.R.attr.state_pressed},
                                new BitmapDrawable(bitmap));
                    }
                });
        Glide.with(getApplicationContext())
                .load(pressedUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>(height, height) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        //Selected
                        drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
                                new BitmapDrawable(bitmap));
                        drawable.addState(new int[]{android.R.attr.state_selected},
                                new BitmapDrawable(bitmap));
                    }
                });

        return drawable;
    }

    private void initDrawableList(Hometab hometab){
        if (hometab == null || hometab.getTabBarList() == null){
            return;
        }
        if (drawableList == null){
            drawableList = new ArrayList<>();

        }else {
            drawableList.clear();
        }
        for (int i = 0; i < hometab.getTabBarList().size(); i++) {
            Hometab.TabBarListBean result = hometab.getTabBarList().get(i);
            StateListDrawable drawable0 = createSelector(result.getNormal3x(),result.getSelect3x());
            drawableList.add(drawable0);
        }
    }

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    /**
     * 未登录时点击发布跳转至登陆页登陆成功后的事件
     */
    @Subscribe
    public void getPublishLoginEvent(PublishLoginEvent event) {
        mTabHost.setCurrentTab(1);
        getPersonMessage();
    }

    /**
     * 获取个人信息
     */
    public void getPersonMessage() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", PreferencesUtils.getString("userId", ""));
        JkhDataHandler<PersonInfo> jkhDataHandler = new JkhDataHandler<>(PersonInfo.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK_AFTER_LOADCACHE)
                .url(UrlUtils.PERSON_INFO).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<PersonInfo>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<PersonInfo> pageData) {

                if (pageData != null) {
                    if (pageData.getErrorCode() == 200 && pageData.getT() != null) {
                        PreferencesUtils.putString("realName", pageData.getT().getRealName());
                        PreferencesUtils.putString("identify", pageData.getT().getIdentify());
                        PreferencesUtils.putString("sex", pageData.getT().getSex());
                        PreferencesUtils.putString("portrait", pageData.getT().getPortrait());
                        PreferencesUtils.putString("mobile", pageData.getT().getMobile());
                        PreferencesUtils.putString("email", pageData.getT().getEmail());
                        PreferencesUtils.putString("school", pageData.getT().getSchool());
                        PreferencesUtils.putString("workposition", pageData.getT().getWorkposition());
                        PreferencesUtils.putString("inviter", pageData.getT().getInviter());
                        PreferencesUtils.putInt("collectCount", pageData.getT().getCollectCount());
                        PreferencesUtils.putInt("supportCount", pageData.getT().getSupportCount());
                        PreferencesUtils.putInt("msgCount", pageData.getT().getMsgCount());
                        PreferencesUtils.putInt("identityValidated", pageData.getT().getIdentityValidated());
                        PreferencesUtils.putInt("emailValidated", pageData.getT().getEmailValidated());
                        PreferencesUtils.putString("introduction", pageData.getT().getIntroduction());
                    }
                }
            }

            @Override
            public void onError(RequestError error) {
            }

            @Override
            public void onCacheResponse(String data) {
            }

            @Override
            public void onResponse(String data) {
            }
        });
    }


    @Subscribe
    public void completeEvent(EditCompleteOnComputerEvent event) {
        mTabHost.setCurrentTab(0);
    }

    @Subscribe
    public void finishApp(FinishAppEvent event) {
        finish();
    }

    @Subscribe
    public void remitSuccess(RemitSuccessEvent event) {
        mTabHost.setCurrentTab(3);
    }



    @Override
    protected void onResume() {
        super.onResume();
        DownloadService.getInstance().registReceiver(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        DownloadService.getInstance().unregistReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.content_frame);
        // 去掉Tab divider
        mTabHost.getTabWidget().setDividerDrawable(null);

        //得到fragment的个数,添加一个个人中心
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);

            //设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        unreadLabel = (TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(R.id.unread_msg_number);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (mTabHost.getCurrentTab() == 1) {
                    if (!PreferencesUtils.getBoolean("isLogin", false)) {
                        Intent intent = new Intent();
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        intent.putExtra("type", "publish");
                        startActivity(intent);
                        mTabHost.setCurrentTab(0);
                    }
                } else if (mTabHost.getCurrentTab() == 2) {
                    if (!PreferencesUtils.getBoolean("isLogin", false)) {
                        Intent intent = new Intent();
                        intent.setClass(HomeActivity.this, LoginActivity.class);
                        intent.putExtra("type", "chat_page_login");
                        startActivity(intent);
                    }
                } else if (mTabHost.getCurrentTab() == 3) {
                    BusProvider.getInstance().post(new RefreshFragmentEvent());
                }
            }
        });
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.home_tab_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        if (drawableList != null && drawableList.size() > 0 && index < drawableList.size()) {
            imageView.setImageDrawable(drawableList.get(index));
        } else {
            imageView.setImageResource(mImageViewArray[index]);
        }

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

    /**
     * 解决全屏界面到带有状态栏界面切换问题(SplashActivity--->HomeActivity)
     */
    private void smoothSwitchScreen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup rootView = ((ViewGroup) this.findViewById(android.R.id.content));
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            rootView.setPadding(0, statusBarHeight, 0, 0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    @Override
    public boolean onTopBarItemSelected(int featureId) {
        return featureId == TopBar.FEATRUE_ACTION || super.onTopBarItemSelected(featureId);
    }

    private long touchTime = 0;

    @Override
    public void onBackPressed() {

        //首页左上角显示的浮层
        if (((HomeFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[0])).isPopShow()) {
            ((HomeFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[0])).hidePop();
            return;
        }
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= 2000) {
            ToastUtils.show(this, R.string.more_click_exit, 1);
            touchTime = currentTime;
        } else {
            moveTaskToBack(false);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
