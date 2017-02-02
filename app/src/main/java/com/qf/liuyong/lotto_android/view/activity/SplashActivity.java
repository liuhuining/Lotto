package com.qf.liuyong.lotto_android.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.Hometab;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.utils.UrlUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.splash)
    RelativeLayout splashLayout;

    private Hometab hometab;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_layout);
        unbinder = ButterKnife.bind(this);
        getTopBar().hide();

        getTabPic();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (App.instance.isFirst()){
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hometab", hometab);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hometab",hometab);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splashLayout.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * 获取首页tab图
     */
    private void getTabPic() {
        JkhDataHandler<Hometab> jkhDataHandler = new JkhDataHandler<Hometab>(Hometab.class);
        RequestInfo info = new RequestInfo.Builder()
                .method(Request.Method.POST)
                .requestMode(RequestInfo.REQUEST_NETWORK_AFTER_LOADCACHE)
                .url(UrlUtils.GET_HOME_TAB_PIC).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<Hometab>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<Hometab> pageData) {

                if (pageData != null&&pageData.getErrorCode() == 200) {
                    hometab = pageData.getT();
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
