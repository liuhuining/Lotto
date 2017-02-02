package com.qf.liuyong.lotto_android.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qf.liuyong.lotto_android.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.bean.HomePageResult;
import com.qf.liuyong.lotto_android.model.bean.ProjectResult;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.presenter.BusProvider;
import com.qf.liuyong.lotto_android.presenter.MessageEvent;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;
import com.qf.liuyong.lotto_android.view.adapter.HomeHeaderAdapter;
import com.qf.liuyong.lotto_android.view.adapter.IndustryFilterContentListAdapter;
import com.qf.liuyong.lotto_android.view.adapter.ProjectListAdapter;
import com.qf.liuyong.lotto_android.view.adapter.SortFilterContentAdapter;
import com.qf.liuyong.lotto_android.view.widget.UpMarqueeTextView;
import com.squareup.otto.Subscribe;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class HomeTabFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2,
        PullToRefreshBase.OnPullEventListener<ListView>, View.OnClickListener, AdapterView.OnItemClickListener,
        SortFilterContentAdapter.ChangeText, IndustryFilterContentListAdapter.ChangeText{

    private ListView mListView;
    private PullToRefreshListView mPullToRefreshListView;
    private AutoScrollViewPager mViewPager;
    private ProjectListAdapter mProjectListAdapter;
    private View mHeaderView;
    private LinearLayout speakerLayout;
    private UpMarqueeTextView speakerContent;
    private View background;
    private ListView industryFilterContent;
    private ListView sortFilterContent;
    private CheckBox leftCheckBox, rightCheckBox, stickLeftCheckBox, stickRightCheckBox;
    private ImageView leftArrow, rightArrow, stickLeftArrow, stickRightArrow;
    private String currentPage;
    private String industryType = "";
    private String sortType = "";
    private final String TAG = "HOMEPAGE_LIST";
    //首页滚动消息
    private List<String> scrollMsg;
    private int index = 0;
    private SendMsgRunnable mSendMsgRunnable;
    private Handler mHandler = new Handler();
    private HomePageResult mHomePageResult;
    //当前fragment对应头部tab的位置
    private String state;
    private IndustryFilterContentListAdapter mIndustryFilterContentListAdapter;
    private SortFilterContentAdapter mSortFilterContentAdapter;
    //公告浮层
    private PopupWindow novicePop;

    public class SendMsgRunnable implements Runnable {

        @Override
        public void run() {
            if (scrollMsg != null && scrollMsg.size() > 0) {
                String text = scrollMsg.get(index);
                index++;
                if (index >= scrollMsg.size()) {
                    index = 0;
                }
                speakerContent.setText(text);
            }
            mHandler.postDelayed(this, 3000);
        }
    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHomePageResult = (HomePageResult) getArguments().getSerializable("HomePageResult");
        state = getArguments().getString("state");
        BusProvider.getInstance().register(this);
        initBanner();
        //用于浮在listview上的筛选栏(热门推荐，全部分类)
        final View headerFilter = view.findViewById(R.id.header_filter);
        //background是显示筛选内容时的背景
        background = view.findViewById(R.id.background);
        background.setOnClickListener(this);
        //用于显示筛选内容的LinearLayout
        industryFilterContent = (ListView) view.findViewById(R.id.industry_filter_content);
        industryFilterContent.setAdapter(mIndustryFilterContentListAdapter = new IndustryFilterContentListAdapter(mHomePageResult.getIndustryList()));
        sortFilterContent = (ListView) view.findViewById(R.id.sort_filter_content);
        sortFilterContent.setAdapter(mSortFilterContentAdapter = new SortFilterContentAdapter(mHomePageResult.getSortList().get(Integer.parseInt(state)).getValList()));
        industryFilterContent.setOnItemClickListener(this);
        sortFilterContent.setOnItemClickListener(this);
        mIndustryFilterContentListAdapter.setChangeTextListner(this);
        mSortFilterContentAdapter.setChangeTextListner(this);
        //初始化listview
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.listview);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setOnPullEventListener(this);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshListView.getLoadingLayoutProxy().setPullLabelColor(Color.parseColor("#6e6e6e"));
        mListView = mPullToRefreshListView.getRefreshableView();
        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.addHeaderView(mHeaderView);
        //filterView是用于添加到listview中的筛选栏(热门推荐，全部分类)
        View filterView = View.inflate(getActivity(), R.layout.condition_filter_layout, null);
        filterView.setVisibility(View.VISIBLE);
        mListView.addHeaderView(filterView);
        //listview中左边筛选的箭头
        leftArrow = (ImageView) filterView.findViewById(R.id.left_arrow);
        //listview中右边筛选的箭头
        rightArrow = (ImageView) filterView.findViewById(R.id.right_arrow);
        //listview中左边筛选的checkbox
        leftCheckBox = (CheckBox) filterView.findViewById(R.id.left_condition_checkbox);
        //listview中右边筛选的checkbox
        rightCheckBox = (CheckBox) filterView.findViewById(R.id.right_condition_checkbox);
        //浮层中左边筛选的箭头
        stickLeftArrow = (ImageView) headerFilter.findViewById(R.id.left_arrow);
        //浮层中左边筛选的箭头
        stickRightArrow = (ImageView) headerFilter.findViewById(R.id.right_arrow);
        //浮层中左边筛选的箭头
        stickLeftCheckBox = (CheckBox) headerFilter.findViewById(R.id.left_condition_checkbox);
        //浮层中左边筛选的箭头
        stickRightCheckBox = (CheckBox) headerFilter.findViewById(R.id.right_condition_checkbox);
        //给浮层和listview中的筛选栏添加点击事件
        filterView.findViewById(R.id.left_condition).setOnClickListener(this);
        filterView.findViewById(R.id.right_condition).setOnClickListener(this);
        headerFilter.findViewById(R.id.left_condition).setOnClickListener(this);
        headerFilter.findViewById(R.id.right_condition).setOnClickListener(this);

        mListView.setAdapter(mProjectListAdapter = new ProjectListAdapter(getActivity(), ProjectListAdapter.PROJECT_LIST));
        speakerLayout = (LinearLayout) view.findViewById(R.id.speaker_layout);
        speakerContent = (UpMarqueeTextView) view.findViewById(R.id.speaker_content);
        mPullToRefreshListView.setRefreshing();

        //设置滚动消息
        setScrollMsg(mHomePageResult);
        if (mSendMsgRunnable == null) {
            mSendMsgRunnable = new SendMsgRunnable();
        }
        mHandler.postDelayed(mSendMsgRunnable, 3000);

        //用于做浮层
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 2) {
                    if (mViewPager != null)
                        mViewPager.stopAutoScroll();
                    headerFilter.setVisibility(View.VISIBLE);
                } else {
                    if (mViewPager != null)
                        mViewPager.startAutoScroll();
                    headerFilter.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setScrollMsg(HomePageResult homePageResult) {
        scrollMsg = homePageResult.getMsgList();
        if (scrollMsg.size() == 0) {
            speakerLayout.setVisibility(View.GONE);
        } else {
            speakerLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initBanner() {
        mHeaderView = View.inflate(getActivity(), R.layout.home_header, null);
        //container是banner的父容器
        View container = mHeaderView.findViewById(R.id.container);
        mViewPager = (AutoScrollViewPager) mHeaderView.findViewById(R.id.viewpager);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.home_indicator);
        if (mHomePageResult != null)
            mViewPager.setAdapter(new HomeHeaderAdapter(mViewPager, mHomePageResult.getBannerList()));
        circlePageIndicator.setViewPager(mViewPager);
        circlePageIndicator.setSnap(true);
        circlePageIndicator.setVisibility(View.GONE);
        mViewPager.setScrollFactgor(6);
        mViewPager.startAutoScroll(5000);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageMargin(
                ScreenUtils.dip2px(getActivity(), 5));
        //将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mHomePageResult != null && mHomePageResult.getBannerList().size() > 0)
                    mViewPager.setCurrentItem(10002, false);
            }
        }, 1000);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && "0".equals(state) && PreferencesUtils.getBoolean("isFirstEnterGuapai", true)) {
            showNovice();
        }
    }

    private void showNovice() {
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.novice_3, null);
        novicePop = new PopupWindow(getActivity());
        View view = contentView.findViewById(R.id.close);
        Glide.with(getActivity())
                .load(R.drawable.novice_3)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) contentView.findViewById(R.id.novice_image));
        novicePop.setContentView(contentView);
        novicePop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        novicePop.setHeight(ScreenUtils.getScreenWidthHeight(getActivity())[1] - ScreenUtils.getStatuBarHeight(getActivity()));
        novicePop.setBackgroundDrawable(new BitmapDrawable());
        novicePop.setOutsideTouchable(false);
        novicePop.setAnimationStyle(R.style.popwin_anim_style);
        novicePop.update();
        novicePop.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (novicePop != null && novicePop.isShowing())
                    novicePop.dismiss();
                PreferencesUtils.putBoolean("isFirstEnterGuapai", false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (novicePop != null && novicePop.isShowing())
            novicePop.dismiss();
        mHandler.removeCallbacksAndMessages(null);
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        if (PullToRefreshBase.Mode.PULL_FROM_START == direction) {
            mPullToRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel2("最近更新:" +
                    App.instance.friendly_time(App.instance.getSavedTime(TAG)));
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        App.instance.saveCurrentTime(TAG);
        getProjectList("10", "1", industryType, sortType);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getProjectList("10", String.valueOf(Integer.valueOf(currentPage) + 1), industryType, sortType);
    }

    @Subscribe
    public void state_event(MessageEvent event) {
        if (event.type.equals("state_update")) {
            getProjectList("10", "1", industryType, sortType);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager != null)
            mViewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mViewPager != null)
            mViewPager.stopAutoScroll();
    }

    /**
     * 获取项目列表
     */
    private void getProjectList(String pageSize, String pageNo, String industryType, String sortType) {
        Map<String, String> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("pageNo", pageNo);
        params.put("projectStage", state);
        params.put("industryType", industryType);
        params.put("sortType", sortType);
        params.put("userId", PreferencesUtils.getString("userId", ""));
        JkhDataHandler<ProjectResult> jkhDataHandler = new JkhDataHandler<>(ProjectResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.LOAD_CACHE_IF_NETWORK_ERROR)
                .url(UrlUtils.PROJECT_LIST_GYTYPE).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<ProjectResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<ProjectResult> pageData) {
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (pageData == null) {
                    return;
                }
                if (pageData.getErrorCode() == 200 && pageData.getT() != null) {
                    if (pageData.getT().getIsLast()) {
                        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    currentPage = pageData.getT().getPageNo();
                    if (Integer.valueOf(currentPage) == 1) {
                        mProjectListAdapter.setData(pageData.getT().getProjectList());
                    } else {
                        mProjectListAdapter.addData(pageData.getT().getProjectList());
                    }
                }
            }

            @Override
            public void onError(RequestError error) {
                if (mPullToRefreshListView != null)
                    mPullToRefreshListView.onRefreshComplete();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_condition:
                smooth(mListView);
                if (isDismissing)
                    return;
                if (rightCheckBox.isChecked() && stickRightCheckBox.isChecked()) {
                    rightCheckBox.setChecked(false);
                    rightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickRightCheckBox.setChecked(false);
                    stickRightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    dismissBackground();
                    dismissFilterContent(industryFilterContent);
                }
                if (leftCheckBox.isChecked() && stickLeftCheckBox.isChecked()) {
                    leftCheckBox.setChecked(false);
                    leftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickLeftCheckBox.setChecked(false);
                    stickLeftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    dismissBackground();
                    dismissFilterContent(sortFilterContent);
                } else {
                    leftCheckBox.setChecked(true);
                    leftArrow.setImageResource(R.drawable.checked_arrow_filter);
                    stickLeftCheckBox.setChecked(true);
                    stickLeftArrow.setImageResource(R.drawable.checked_arrow_filter);
                    showBackground();
                    showFilterContent(sortFilterContent);
                }
                break;
            case R.id.right_condition:
                smooth(mListView);
                if (isDismissing)
                    return;
                if (leftCheckBox.isChecked() && stickLeftCheckBox.isChecked()) {
                    leftCheckBox.setChecked(false);
                    leftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickLeftCheckBox.setChecked(false);
                    stickLeftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    dismissBackground();
                    dismissFilterContent(sortFilterContent);
                }
                if (rightCheckBox.isChecked() && stickRightCheckBox.isChecked()) {
                    rightCheckBox.setChecked(false);
                    rightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickRightCheckBox.setChecked(false);
                    stickRightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    dismissBackground();
                    dismissFilterContent(industryFilterContent);
                } else {
                    rightCheckBox.setChecked(true);
                    rightArrow.setImageResource(R.drawable.checked_arrow_filter);
                    stickRightCheckBox.setChecked(true);
                    stickRightArrow.setImageResource(R.drawable.checked_arrow_filter);
                    showBackground();
                    showFilterContent(industryFilterContent);
                }
                break;
            case R.id.background:
                dismissBackground();
                if (industryFilterContent.getVisibility() == View.VISIBLE)
                    dismissFilterContent(industryFilterContent);
                if (sortFilterContent.getVisibility() == View.VISIBLE)
                    dismissFilterContent(sortFilterContent);
                break;
        }
    }

    private void smooth(ListView lv) {
        if (mPullToRefreshListView.isRefreshing())
            return;
        RelativeLayout.MarginLayoutParams industryParams = (RelativeLayout.LayoutParams) industryFilterContent.getLayoutParams();
        RelativeLayout.MarginLayoutParams sortParams = (RelativeLayout.LayoutParams) sortFilterContent.getLayoutParams();
        //因为header的原因，当只有header时getCount()==4
        if (lv.getAdapter().getCount() <= 6) {
            industryParams.topMargin = ScreenUtils.dip2px(getContext(), 151);
            sortParams.topMargin = ScreenUtils.dip2px(getContext(), 151);
            industryFilterContent.setLayoutParams(industryParams);
            sortFilterContent.setLayoutParams(sortParams);
            background.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent));
            return;
        } else {
            industryParams.topMargin = ScreenUtils.dip2px(getContext(), 40);
            sortParams.topMargin = ScreenUtils.dip2px(getContext(), 40);
            industryFilterContent.setLayoutParams(industryParams);
            sortFilterContent.setLayoutParams(sortParams);
            background.setBackgroundColor(Color.parseColor("#88131313"));
        }
        View c = lv.getChildAt(1);
        if (c != null) {
            int top = c.getTop();
            int firstVisiblePosition = lv.getFirstVisiblePosition();
            if (firstVisiblePosition == 0)
                lv.smoothScrollBy(c.getHeight() + 2, 300);
            else if (firstVisiblePosition == 1)
                lv.smoothScrollBy(top + 2, 200);
        }
    }

    private void showBackground() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        background.startAnimation(alphaAnimation);
        background.setVisibility(View.VISIBLE);
    }

    private boolean isDismissing = false;

    private void dismissBackground() {
        if (!isDismissing) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(500);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isDismissing = true;
                    leftCheckBox.setChecked(false);
                    leftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickLeftCheckBox.setChecked(false);
                    stickLeftArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    rightCheckBox.setChecked(false);
                    rightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                    stickRightCheckBox.setChecked(false);
                    stickRightArrow.setImageResource(R.drawable.unchecked_arrow_filter);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isDismissing = false;
                        }
                    }, 200);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            background.startAnimation(alphaAnimation);
            background.setVisibility(View.GONE);
        }
    }

    private void showFilterContent(ListView listView) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -1500, 0);
        animation.setDuration(500);
        listView.startAnimation(animation);
        listView.setVisibility(View.VISIBLE);
    }

    private void dismissFilterContent(ListView listView) {
        if (!isDismissing) {
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1500);
            animation.setDuration(500);
            listView.startAnimation(animation);
            listView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.industry_filter_content:
                mIndustryFilterContentListAdapter.clickItem(position);
                dismissFilterContent();
                break;
            case R.id.sort_filter_content:
                mSortFilterContentAdapter.clickItem(position);
                dismissFilterContent();
                break;
        }
    }

    private void dismissFilterContent() {
        dismissBackground();
        if (industryFilterContent.getVisibility() == View.VISIBLE)
            dismissFilterContent(industryFilterContent);
        if (sortFilterContent.getVisibility() == View.VISIBLE)
            dismissFilterContent(sortFilterContent);
    }

    @Override
    public void changeSortFilteText(String string, String sortType) {
        this.sortType = sortType;
        leftCheckBox.setText(string);
        stickLeftCheckBox.setText(string);
        getProjectList("10", "1", industryType, sortType);
    }

    @Override
    public void changeIndustryFilteText(String string, String industryType) {
        this.industryType = industryType;
        rightCheckBox.setText(string);
        stickRightCheckBox.setText(string);
        getProjectList("10", "1", industryType, sortType);
    }
}
