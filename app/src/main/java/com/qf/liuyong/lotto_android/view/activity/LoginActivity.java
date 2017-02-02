package com.qf.liuyong.lotto_android.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.bean.LoginResult;
import com.qf.liuyong.lotto_android.model.bean.PersonInfo;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.presenter.BusProvider;
import com.qf.liuyong.lotto_android.presenter.LoginEaseEvent;
import com.qf.liuyong.lotto_android.presenter.LoginEaseResultEvent;
import com.qf.liuyong.lotto_android.presenter.LoginEvent;
import com.qf.liuyong.lotto_android.presenter.MessageEvent;
import com.qf.liuyong.lotto_android.presenter.PublishLoginEvent;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.utils.ToastUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class LoginActivity extends Activity implements View.OnClickListener, View.OnTouchListener{

    @BindView(R.id.phone_num)
    EditText phoneNum;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.top_bar)
    RelativeLayout mTopBar;
    @BindView(R.id.content_view)
    LinearLayout mContentView;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.layout_bg)
    ImageView bg;
    @BindView(R.id.register)
    TextView mRegister;

    private Unbinder unbinder;

    public static final int START_FROM_PUBLISH = 10;
    private boolean progressShow;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        unbinder = ButterKnife.bind(this);
        initTopbar();
        BusProvider.getInstance().register(this);
        ViewGroup.LayoutParams params = bg.getLayoutParams();
        params.height = ScreenUtils.getScreenWidthHeight(this)[1] - ScreenUtils.getStatuBarHeight(this);
        params.width = ScreenUtils.getScreenWidthHeight(this)[0];
        SpannableString ss = new SpannableString(getResources().getString(R.string.register_text));
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.login_button_default_color)), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mRegister.setText(ss);
        phoneNum.setOnTouchListener(this);
        password.setOnTouchListener(this);
    }

    private void initTopbar() {
        ((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string.login));
        findViewById(R.id.back).setVisibility(View.GONE);
        ImageView close = (ImageView) findViewById(R.id.close);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void close(LoginEvent event) {
        if (event.b) {
            finish();
        }
    }

    private void scrollToEnd(final int viewId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //将ScrollView滚动到底
                mScrollView.fullScroll(View.FOCUS_DOWN);
                switch (viewId) {
                    case R.id.phone_num:
                        phoneNum.setFocusable(true);
                        phoneNum.setFocusableInTouchMode(true);
                        phoneNum.requestFocus();
                        break;
                    case R.id.password:
                        password.setFocusable(true);
                        password.setFocusableInTouchMode(true);
                        password.requestFocus();
                        break;
                }
            }
        }, 200);
    }

    @OnClick(R.id.login)
    public void login() {
        if (PreferencesUtils.getBoolean("isLogin", false)) {
            ToastUtils.show(this, "已登录", 1);
            mLogin.setClickable(true);
            return;
        }
        final String number = phoneNum.getText().toString().trim();
        final String pd = password.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            ToastUtils.show(this, getResources().getString(R.string.phonenum_empty), 1);
            return;
        } else if (number.length() < 11) {
            ToastUtils.show(this, getResources().getString(R.string.phonenum_error), 1);
            return;
        }
        if (TextUtils.isEmpty(pd)) {
            ToastUtils.show(this, getResources().getString(R.string.password_empty), 1);
            return;
        }
        PreferencesUtils.putString("number", number);
        mLogin.setClickable(false);
        Map<String, String> params = new HashMap<>();
        params.put("userName", number);
        params.put("password", pd);
        JkhDataHandler<LoginResult> jkhDataHandler = new JkhDataHandler<>(LoginResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.LOGIN).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<LoginResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, final PageData<LoginResult> pageData) {
                if (pageData == null) {
                    return;
                }
                if (pageData.getErrorCode() == 200) {
                    if (pageData.getT() != null && pageData.getT().getUserId() != null) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BusProvider.getInstance().post(new LoginEaseEvent(number, pd, pageData.getT().getUserId()));
                            }
                        });

                    }
                } else {
                    ToastUtils.show(LoginActivity.this, pageData.getErrorMessage(), 1);
                    mLogin.setClickable(true);
                }
            }

            @Override
            public void onError(RequestError error) {
                ToastUtils.show(LoginActivity.this, getResources().getString(R.string.login_fail), 1);
                if (mLogin != null)
                    mLogin.setClickable(true);
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
    public void loginEase(LoginEaseEvent event) {
        login(event.getName(), "123456", event.getUserId());
    }

    @Subscribe
    public void isEaseLoginSuccess(LoginEaseResultEvent event) {
        ToastUtils.show(LoginActivity.this, getResources().getString(R.string.login_success), 1);
        PreferencesUtils.putString("loginPhoneNumber", event.getPhoneNum());
        PreferencesUtils.putBoolean("isLogin", true);
        PreferencesUtils.putBoolean("isEMLogin", event.isLoginSuccess());
        PreferencesUtils.putString("userId", event.getUserId());
        PreferencesUtils.putString("access_token", event.getUserId());
        getPersonInfo();
        LoginActivity.this.finish();

        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type) && type.equals("exit") || !TextUtils.isEmpty(type) && type.equals("person_login")) {
            MessageEvent msg = new MessageEvent(null, null, "login");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("love")) {
            MessageEvent msg = new MessageEvent(null, null, "love");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("support")) {
            MessageEvent msg = new MessageEvent(null, null, "support");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("news")) {
            MessageEvent msg = new MessageEvent(null, null, "news");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("invest")) {
            MessageEvent msg = new MessageEvent(null, null, "invest");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("subscribe")) {
            MessageEvent msg = new MessageEvent(null, null, "subscribe");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("right")) {
            MessageEvent msg = new MessageEvent(null, null, "right");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("confirm")) {
            MessageEvent msg = new MessageEvent(null, null, "confirm");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("expert")) {
            MessageEvent msg = new MessageEvent(null, null, "expert");
            BusProvider.getInstance().post(msg);
        } else if (!TextUtils.isEmpty(type) && type.equals("publish")) {
            BusProvider.getInstance().post(new PublishLoginEvent(START_FROM_PUBLISH));
        }else if (!TextUtils.isEmpty(type)&&"my_release_project".equals(type)){
            BusProvider.getInstance().post(new MessageEvent(null, null, "my_release_project"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
        }
    }

    /**
     * 登录环信
     */
    public void login(final String currentUsername, final String currentPassword, final String userId) {

        if (TextUtils.isEmpty(currentUsername)) {
            ToastUtils.show(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            ToastUtils.show(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT);
            return;
        }

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        final long start = System.currentTimeMillis();

    }

    /**
     * 登陆成功更新个人信息
     */
    public void getPersonInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", PreferencesUtils.getString("userId", ""));
        JkhDataHandler<PersonInfo> jkhDataHandler = new JkhDataHandler<>(PersonInfo.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.PERSON_INFO).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<PersonInfo>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<PersonInfo> pageData) {
                if (pageData.getErrorCode() == 200 && pageData != null && pageData.getT() != null) {
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
                    PreferencesUtils.putInt("waitCount", pageData.getT().getWaitCount());
                    PreferencesUtils.putInt("supportCount", pageData.getT().getSupportCount());
                    PreferencesUtils.putInt("msgCount", pageData.getT().getMsgCount());
                    PreferencesUtils.putInt("identityValidated", pageData.getT().getIdentityValidated());
                    PreferencesUtils.putInt("identityValidatedBack", pageData.getT().getIdentityValidatedBack());
                    PreferencesUtils.putInt("emailValidated", pageData.getT().getEmailValidated());
                    PreferencesUtils.putString("introduction", pageData.getT().getIntroduction());
                    PreferencesUtils.putString("address", pageData.getT().getAddress());
                    PreferencesUtils.putString("identityNegative", pageData.getT().getIdentityNegative());
                    PreferencesUtils.putString("identityPositive", pageData.getT().getIdentityPositive());
                    PreferencesUtils.putString("contacts", String.valueOf(pageData.getT().getContacts()));
                    PreferencesUtils.putString("cellPhone", String.valueOf(pageData.getT().getCellPhone()));
                    PreferencesUtils.putInt("fill", pageData.getT().getFill());
                    PreferencesUtils.putInt("stateaudit", pageData.getT().getStateAudit());
                    PreferencesUtils.putString("subTotal", pageData.getT().getSubTotal());
                    PreferencesUtils.putString("investTotal", pageData.getT().getInvestTotal());
                    PreferencesUtils.putString("empCode", pageData.getT().getEmpCode());
                    PreferencesUtils.putInt("expertAuditState", pageData.getT().getExpertAuditState());
                    PreferencesUtils.putString("userId", pageData.getT().getUserId());
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
    public boolean onTouch(View v, MotionEvent event) {
        scrollToEnd(v.getId());
        return false;
    }
}
