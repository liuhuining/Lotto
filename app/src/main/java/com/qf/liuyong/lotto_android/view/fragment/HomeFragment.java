package com.qf.liuyong.lotto_android.view.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.bean.HomePageNotice;
import com.qf.liuyong.lotto_android.model.bean.HomePageResult;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;
import com.qf.liuyong.lotto_android.view.activity.BannerPictureDetailActivity;
import com.qf.liuyong.lotto_android.view.adapter.HomeFloatLayerAdapter;
import com.qf.liuyong.lotto_android.view.adapter.TabViewPagerAdapter;
import com.qf.liuyong.lotto_android.view.widget.LoadingLayout;
import com.qf.liuyong.lotto_android.view.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/29 0029.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener,LoadingLayout.OnRefreshListener{

    private ViewPager mViewPager;
    private SlidingTabLayout slidingTabLayout;
    private TabViewPagerAdapter mTabViewPagerAdapter;
    private PopupWindow popWnd;
    private RelativeLayout floatLayerContainer;
    private ListView listView;
    private FrameLayout noticeLayout;
    private ImageView closeNoticeImage;
    private ImageView noticeImage;
    private TextView noticeRedDot;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initPopupWindow(container);
        return inflater.inflate(R.layout.new_home_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noticeRedDot = (TextView) view.findViewById(R.id.notice_red_dot);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        noticeLayout = (FrameLayout) view.findViewById(R.id.notice_layout);
        closeNoticeImage = (ImageView) view.findViewById(R.id.close);
        noticeImage = (ImageView) view.findViewById(R.id.image);
        closeNoticeImage.setOnClickListener(this);
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        // slidingTabLayout.setDistributeEvenly(true); 是否填充满屏幕的宽度
        mViewPager.setAdapter(mTabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager()));
        ImageView leftMenu = (ImageView) view.findViewById(R.id.left_menu);
        ImageView rightMenu = (ImageView) view.findViewById(R.id.right_menu);
        leftMenu.setVisibility(View.VISIBLE);
        rightMenu.setVisibility(View.VISIBLE);
        leftMenu.setOnClickListener(this);
        rightMenu.setOnClickListener(this);
        setOnRefreshListener(this);

        //自定义下划线颜色
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.project_list_tab_underline_color);
            }
        });

        getHomePageData();
        getNotice(PreferencesUtils.getString("notice_timeId", "0"));
    }

    /**
     * 初始化点击头部左侧按钮显示的浮层
     */
    private void initPopupWindow(ViewGroup viewGroup) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.home_fuceng, viewGroup, false);
        floatLayerContainer = (RelativeLayout) contentView.findViewById(R.id.fuceng_container);
        floatLayerContainer.setOnClickListener(this);
        contentView.findViewById(R.id.back).setOnClickListener(this);
        listView = (ListView) contentView.findViewById(R.id.listview);
        popWnd = new PopupWindow(getActivity());
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ScreenUtils.getScreenWidthHeight(getActivity())[1] - ScreenUtils.getStatuBarHeight(getActivity()) - ScreenUtils.dip2px(getActivity(), 50));
        popWnd.setBackgroundDrawable(new BitmapDrawable());
        popWnd.setOutsideTouchable(true);
        popWnd.setAnimationStyle(R.style.popwin_anim_style);
        popWnd.update();
    }

    /**
     * 获取首页数据
     */
    private void getHomePageData() {
        JkhDataHandler<HomePageResult> jkhDataHandler = new JkhDataHandler<>(HomePageResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST)
                .requestMode(RequestInfo.LOAD_CACHE_IF_NETWORK_ERROR)
                .url(UrlUtils.HOME_PAGE).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<HomePageResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<HomePageResult> pageData) {
                if (handler.isFromCache() && pageData == null) {
                    showError("无网络链接\n请检查您的手机网络后重新加载");
                } else {
                    loadComplete();
                }

                if (pageData != null && pageData.getT() != null) {
                    HomePageResult homePageResult = pageData.getT();
                    fragments.clear();
                    int size = homePageResult.getStageList().size();
                    for (int i = 0; i < size; i++) {
                        HomeTabFragment fragment = new HomeTabFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("HomePageResult", homePageResult);
                        bundle.putString("state", homePageResult.getStageList().get(i).getState());
                        fragment.setArguments(bundle);
                        fragments.add(fragment);
                    }
                    mTabViewPagerAdapter.setFragments(fragments, homePageResult.getStageList());
                    mViewPager.setCurrentItem(1);
                    slidingTabLayout.setViewPager(mViewPager);
                    listView.setAdapter(new HomeFloatLayerAdapter(homePageResult.getNoticeList()));
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

    @Override
    public void onRefresh() {
        getHomePageData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popWnd != null && popWnd.isShowing())
            popWnd.dismiss();
    }

    public boolean isPopShow() {
        return popWnd.isShowing();
    }

    public void hidePop() {
        popWnd.dismiss();
    }

    private void showNovice() {
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.novice_1, null);
        final PopupWindow popWnd = new PopupWindow(getActivity());
        View view = contentView.findViewById(R.id.close);
        Glide.with(getActivity())
                .load(R.drawable.novice_1)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) contentView.findViewById(R.id.novice_image));
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ScreenUtils.getScreenWidthHeight(getActivity())[1] - ScreenUtils.getStatuBarHeight(getActivity()));
        popWnd.setBackgroundDrawable(new BitmapDrawable());
        popWnd.setOutsideTouchable(false);
        popWnd.setAnimationStyle(R.style.popwin_anim_style);
        popWnd.update();
        popWnd.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWnd.dismiss();
                PreferencesUtils.putBoolean("isFirstEnterHome", false);
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 通知
     */
    private void getNotice(String timeID) {
        Map<String, String> params = new HashMap<>();
        params.put("timeId", timeID);
        JkhDataHandler<HomePageNotice> jkhDataHandler = new JkhDataHandler<>(HomePageNotice.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.HOMEPAGE_NOTICE).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<HomePageNotice>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<HomePageNotice> pageData) {
                if (pageData != null && pageData.getT() != null) {
                    final HomePageNotice result = pageData.getT();
                    PreferencesUtils.putString("notice_timeId", result.getTimeId());
                    //0：文字 1：图片
                    String noticeType = result.getInfoType();
                    if (!TextUtils.isEmpty(noticeType)) {
                        if (noticeType.equals("0")) {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(getActivity())
                                            .setMessage(result.getMsg())
                                            .setCancelable(false)
                                            .setPositiveButton(R.string.okey, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //0：非 1：是(用户点击确定，客户端退出系统)
                                                    if (!TextUtils.isEmpty(result.getIsMode()) && "1".equals(result.getIsMode())) {
                                                        getActivity().finish();
                                                    } else {
                                                        //新手指导
                                                        if (PreferencesUtils.getBoolean("isFirstEnterHome", true)) {
                                                            showNovice();
                                                        }
                                                    }
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //新手指导
                                                    if (PreferencesUtils.getBoolean("isFirstEnterHome", true)) {
                                                        showNovice();
                                                    }
                                                }
                                            });
                            builder.create().show();
                        } else {
                            noticeRedDot.setVisibility(View.VISIBLE);
                            noticeLayout.setVisibility(View.VISIBLE);
                            Glide.with(getActivity().getApplicationContext())
                                    .load(result.getImgUrl())
                                    .placeholder(R.drawable.banner_default)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(noticeImage);
                            noticeImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    BannerPictureDetailActivity.start(v.getContext(), result.getGotoUrl(), "详情");
                                    noticeLayout.setVisibility(View.GONE);
                                    //新手指导
                                    if (PreferencesUtils.getBoolean("isFirstEnterHome", true)) {
                                        showNovice();
                                    }
                                }
                            });
                        }
                    } else {
                        //新手指导
                        if (PreferencesUtils.getBoolean("isFirstEnterHome", true)) {
                            showNovice();
                        }
                    }
                } else {
                    //新手指导
                    if (PreferencesUtils.getBoolean("isFirstEnterHome", true)) {
                        showNovice();
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
}
