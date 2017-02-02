package com.qf.liuyong.lotto_android.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.app.App;
import com.qf.liuyong.lotto_android.model.bean.ProjectResult;
import com.qf.liuyong.lotto_android.model.http.JkhDataHandler;
import com.qf.liuyong.lotto_android.model.http.RequestInfo;
import com.qf.liuyong.lotto_android.model.http.ResponseHandler;
import com.qf.liuyong.lotto_android.model.http.ResponseListener;
import com.qf.liuyong.lotto_android.model.http.exception.RequestError;
import com.qf.liuyong.lotto_android.model.http.internal.PageData;
import com.qf.liuyong.lotto_android.utils.CommenUtil;
import com.qf.liuyong.lotto_android.utils.PreferencesUtils;
import com.qf.liuyong.lotto_android.utils.ScreenUtils;
import com.qf.liuyong.lotto_android.utils.ToastUtils;
import com.qf.liuyong.lotto_android.utils.UrlUtils;
import com.qf.liuyong.lotto_android.view.activity.LoginActivity;
import com.qf.liuyong.lotto_android.view.widget.StatuBarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class ProjectListAdapter extends BaseAdapter {

    public static final int PROJECT_LIST = 1;
    public static final int RELEASE_PROJECT_LIST = 2;
    public static final int LIKE_PROJECT_LIST = 3;
    private List<ProjectResult.ProjectListBean> mProjectList;
    private Dialog dialog;
    private View view;
    private Context context;
    private String state;
    private Animation big, small;
    private boolean isMyReleaseProject = false;
    private int type;

    public ProjectListAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
        view = LayoutInflater.from(context).inflate(R.layout.project_question_lauout, null);
        dialog = new AlertDialog.Builder(context).create();
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        small = AnimationUtils.loadAnimation(context, R.anim.scale_small);
        big = AnimationUtils.loadAnimation(context, R.anim.scale_big);
    }

    public void setData(List<ProjectResult.ProjectListBean> projectList) {
        this.mProjectList = projectList;
        notifyDataSetChanged();
    }

    public void addData(List<ProjectResult.ProjectListBean> projectList) {
        this.mProjectList.addAll(projectList);
        notifyDataSetChanged();
    }

    public void setIsMyReleaseProject(boolean isMyReleaseProject) {
        this.isMyReleaseProject = isMyReleaseProject;
    }

    @Override
    public int getCount() {
        return mProjectList == null ? 0 : mProjectList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mProjectList != null && mProjectList.size() != 0) {
            return mProjectList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.project_list_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final ProjectResult.ProjectListBean result = mProjectList.get(position);
        state = result.getStage();
        final ImageView imageView = vh.projectPic;
        Glide.with(context.getApplicationContext())
                .load(result.getUrl())
                .asBitmap()
                .dontAnimate()
                .placeholder(R.drawable.project_list_default_pic)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource == null)
                            return;
                        int screenWidth = ScreenUtils.getScreenWidthHeight(view.getContext())[0];
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.height = screenWidth * 340 / 750;
                        params.width = screenWidth;
                        imageView.setImageBitmap(resource);
                    }
                });

        vh.projectName.setText(result.getName());

        //分割线处理
        if (type == PROJECT_LIST) {
            if (position + 1 > 1 && (position + 1) % 5 == 0) {
                vh.line.setVisibility(View.GONE);
                vh.noticeLine.setVisibility(View.VISIBLE);
            } else {
                vh.line.setVisibility(View.VISIBLE);
                vh.noticeLine.setVisibility(View.GONE);
            }
        }

        LinearLayout.LayoutParams param1 = (LinearLayout.LayoutParams) vh.layout1.getLayoutParams();
        LinearLayout.LayoutParams param2 = (LinearLayout.LayoutParams) vh.layout2.getLayoutParams();
        LinearLayout.LayoutParams param3 = (LinearLayout.LayoutParams) vh.layout3.getLayoutParams();
        LinearLayout.LayoutParams param4 = (LinearLayout.LayoutParams) vh.layout4.getLayoutParams();
        LinearLayout.LayoutParams param3t = (LinearLayout.LayoutParams) vh.remainTimeLayout.getLayoutParams();

        switch (state) {
            //state----0：挂牌 1：众筹 2：历史
            case "0":
                //挂牌
                vh.ratingText.setVisibility(View.GONE);
                vh.projectProgress.setVisibility(View.GONE);
                vh.statuBarView.setVisibility(View.GONE);
                vh.projectRating.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(result.getProgress())) {
                    if (Double.parseDouble(result.getProgress())==0.00)
                        vh.ratingBar.setRating(0f);
                    else
                        vh.ratingBar.setRating((float) Math.ceil(Double.parseDouble(result.getProgress()) / 10 + 0.1) / 2);
                }
                vh.remainTimeLayout.setVisibility(View.GONE);
                vh.layout3.setVisibility(View.VISIBLE);
                vh.top_1.setText(result.getPraiseNum());
                vh.bottom_1.setText("点赞支持");
                vh.top_2.setText(result.getCollectNum());
                vh.bottom_2.setText("收藏关注");
                vh.top_3.setText(result.getCommentNum());
                vh.bottom_3.setText("项目评论");
                vh.top_4.setText(result.getExpertNum());
                vh.bottom_4.setText("专家点评");
                if (!TextUtils.isEmpty(result.getIsPraise()) && "1".equals(result.getIsPraise())) {
                    vh.likeProject.setImageResource(R.drawable.had_liked_2);
                } else {
                    vh.likeProject.setImageResource(R.drawable.ulike_2);
                }

                param1.weight = 186.5f;
                param2.weight = 186.5f;
                param3.weight = 186.5f;
                param4.weight = 186.5f;
                break;
            case "1":
                //众筹
                vh.projectProgress.setVisibility(View.VISIBLE);
                vh.statuBarView.setVisibility(View.GONE);
                vh.projectRating.setVisibility(View.GONE);
                vh.progressText.setText("众筹中");
                if (!TextUtils.isEmpty(result.getProgress())) {
                    vh.progressBar.setProgress((int) Float.parseFloat(result.getProgress()));
                }
                vh.remainTimeLayout.setVisibility(View.VISIBLE);
                vh.layout3.setVisibility(View.GONE);
                vh.top_1.setText(result.getSupportNum());
                vh.bottom_1.setText("支持人数");
                vh.top_2.setText(String.format("￥%s", result.getCurMoney()));
                vh.bottom_2.setText(String.format("目标(元)￥%s", result.getGoalMoney()));
                String leaveTimeStr = result.getLeaveTime();
                if (!TextUtils.isEmpty(leaveTimeStr) && CommenUtil.isNumeric(leaveTimeStr)) {
                    long leaveTime = Long.parseLong(result.getLeaveTime());
                    if (leaveTime <= 0) {
                        vh.time.stop();
                        vh.time.allShowZero();
                        vh.time.setVisibility(View.VISIBLE);
                        vh.days_remain.setVisibility(View.GONE);
                        vh.timeText.setText("剩余时间");
                        param1.weight = 150.0f;
                        param2.weight = 256f;
                        param3t.weight = 190.0f;
                        param4.weight = 150.0f;
                    } else {
                        vh.time.start(leaveTime);
                        vh.time.updateShow(leaveTime);
                        int days = vh.time.getDay();
                        int hours = vh.time.getHour();
                        int min = vh.time.getMinute();
                        int second = vh.time.getSecond();
                        if (days == 0 && (hours + min + second != 0)) {
                            vh.days_remain.setVisibility(View.GONE);
                            vh.time.setVisibility(View.VISIBLE);
                            vh.timeText.setText("剩余时间");
                            param1.weight = 150.0f;
                            param2.weight = 256f;
                            param3t.weight = 190.0f;
                            param4.weight = 150.0f;
                        } else if (days == 0 && (hours + min + second == 0)) {
                            vh.time.stop();
                            vh.time.allShowZero();
                            vh.time.setVisibility(View.VISIBLE);
                            vh.days_remain.setVisibility(View.GONE);
                            vh.timeText.setText("剩余时间");
                            param1.weight = 150.0f;
                            param2.weight = 256f;
                            param3t.weight = 190.0f;
                            param4.weight = 150.0f;
                        } else {
                            vh.time.stop();
                            vh.time.allShowZero();
                            vh.time.setVisibility(View.GONE);
                            vh.days_remain.setVisibility(View.VISIBLE);
                            vh.days_remain.setText(String.valueOf(days));
                            vh.timeText.setText("剩余天数");
                            param1.weight = 163.3f;
                            param2.weight = 256f;
                            param3t.weight = 163.3f;
                            param4.weight = 163.3f;
                        }
                    }
                } else {
                    vh.time.stop();
                    vh.time.allShowZero();
                    vh.time.setVisibility(View.VISIBLE);
                    vh.days_remain.setVisibility(View.GONE);
                    vh.timeText.setText("剩余时间");
                    param1.weight = 150.0f;
                    param2.weight = 256f;
                    param3t.weight = 190.0f;
                    param4.weight = 150.0f;
                }
                vh.top_4.setText(result.getCommentNum());
                vh.bottom_4.setText("用户评论");
                if (!TextUtils.isEmpty(result.getIsFavor()) && "1".equals(result.getIsFavor())) {
                    vh.likeProject.setImageResource(R.drawable.had_liked_1);
                } else {
                    vh.likeProject.setImageResource(R.drawable.unlike_1);
                }
                break;
            case "2":
                //历史 ---- 历史中不显示 1：挂牌中 3：众筹中
                // 2：挂牌失败 4：众筹失败 5：报备中 6：确权中 7：退款中 8：已完成 9：已退款
                String projectStatus = result.getProjectStatus();
                //设置是否显示更新按钮（只有6和8显示）
                if (isMyReleaseProject && ("6".equals(projectStatus) || "8".equals(projectStatus))) {
                    vh.likeProject.setVisibility(View.GONE);
                    vh.updateButton.setVisibility(View.VISIBLE);
                } else {
                    vh.likeProject.setVisibility(View.VISIBLE);
                    vh.updateButton.setVisibility(View.GONE);
                }

                switch (projectStatus) {
                    case "4":
                        //众筹失败
                    case "7":
                    case "9":
                        vh.failStatus.setVisibility(View.VISIBLE);
                        vh.projectProgress.setVisibility(View.GONE);
                        vh.statuBarView.setVisibility(View.GONE);
                        vh.projectRating.setVisibility(View.GONE);
                        vh.remainTimeLayout.setVisibility(View.VISIBLE);
                        vh.layout3.setVisibility(View.GONE);
                        vh.top_1.setText(result.getSupportNum());
                        vh.bottom_1.setText("支持人数");
                        vh.top_2.setText(String.format("￥%s", result.getCurMoney()));
                        vh.bottom_2.setText(String.format("目标(元)￥%s", result.getGoalMoney()));
                        //vh.time;
                        vh.timeText.setText("剩余时间");
                        vh.top_4.setText(result.getCommentNum());
                        vh.bottom_4.setText("用户评论");
                        if (!TextUtils.isEmpty(result.getIsFavor()) && "1".equals(result.getIsFavor())) {
                            vh.likeProject.setImageResource(R.drawable.had_liked_1);
                        } else {
                            vh.likeProject.setImageResource(R.drawable.unlike_1);
                        }
                        param1.weight = 150.0f;
                        param2.weight = 256f;
                        param3t.weight = 190.0f;
                        param4.weight = 150.0f;
                        break;
                    case "5":
                        //报备中
                    case "6":
                        //确权中
                    case "8":
                        //已完成
                        if ("5".equals(projectStatus))
                            vh.statuBarView.setStatu(1);
                        else if ("6".equals(projectStatus))
                            vh.statuBarView.setStatu(2);
                        else if ("8".equals(projectStatus))
                            vh.statuBarView.setStatu(3);
                        vh.failStatus.setVisibility(View.GONE);
                        vh.projectProgress.setVisibility(View.GONE);
                        vh.statuBarView.setVisibility(View.VISIBLE);
                        vh.projectRating.setVisibility(View.GONE);
                        vh.remainTimeLayout.setVisibility(View.GONE);
                        vh.layout3.setVisibility(View.VISIBLE);
                        vh.top_1.setText(result.getSupportNum());
                        vh.bottom_1.setText("支持人数");
                        vh.top_2.setText(String.format("￥%s", result.getCurMoney()));
                        vh.bottom_2.setText(String.format("目标(元)￥%s", result.getGoalMoney()));
                        vh.top_3.setText(result.getCommentNum());
                        vh.bottom_3.setText("用户评论");
                        vh.top_4.setText(result.getUpdateNum());
                        vh.bottom_4.setText("项目更新");
                        if (!TextUtils.isEmpty(result.getIsFavor()) && "1".equals(result.getIsFavor())) {
                            vh.likeProject.setImageResource(R.drawable.had_liked_1);
                        } else {
                            vh.likeProject.setImageResource(R.drawable.unlike_1);
                        }
                        param1.weight = 163.3f;
                        param2.weight = 256f;
                        param3.weight = 163.3f;
                        param4.weight = 163.3f;
                        break;
                    case "2":
                        //挂牌失败
                        vh.failStatus.setVisibility(View.VISIBLE);
                        vh.ratingText.setVisibility(View.GONE);
                        vh.projectProgress.setVisibility(View.GONE);
                        vh.statuBarView.setVisibility(View.GONE);
                        vh.projectRating.setVisibility(View.GONE);
                        vh.remainTimeLayout.setVisibility(View.GONE);
                        vh.layout3.setVisibility(View.VISIBLE);
                        vh.top_1.setText(result.getPraiseNum());
                        vh.bottom_1.setText("看好项目");
                        vh.top_2.setText(result.getCollectNum());
                        vh.bottom_2.setText("关注项目");
                        vh.top_3.setText(result.getCommentNum());
                        vh.bottom_3.setText("用户评价");
                        vh.top_4.setText(result.getExpertNum());
                        vh.bottom_4.setText("专家点评");
                        if (!TextUtils.isEmpty(result.getIsPraise()) && "1".equals(result.getIsPraise())) {
                            vh.likeProject.setImageResource(R.drawable.had_liked_2);
                        } else {
                            vh.likeProject.setImageResource(R.drawable.ulike_2);
                        }
                        param1.weight = 186.5f;
                        param2.weight = 186.5f;
                        param3.weight = 186.5f;
                        param4.weight = 186.5f;
                        break;
                }
                break;
        }

//        vh.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (PreferencesUtils.getBoolean("isLogin", false)) {
//                    DefaultActivity.start(parent.getContext(), "项目详情", false, false, ProjectSpecificsFragment.class.getName(), result.getpId());
//                } else {
//                    LoginActivity.start(context);
//                }
//            }
//        });
//
//        vh.updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UpdateActivity.start(context, result.getpId());
//            }
//        });

        final ImageView imageView1 = vh.likeProject;
        final String projectStatus = result.getProjectStatus();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferencesUtils.getBoolean("isLogin", false)) {
                    imageView1.setClickable(false);
                    if (projectStatus.equals("1") || projectStatus.equals("2")) {
                        //praise 0：未点赞 1：已点赞
                        if ("0".equals(result.getIsPraise())) {
                            praise("1", result.getpId(), imageView1, result);
                        } else if ("1".equals(result.getIsPraise())) {
                            praise("0", result.getpId(), imageView1, result);
                        }
                    } else {
                        //0：未收藏 1：已收藏
                        if ("0".equals(result.getIsFavor())) {
                            fav("1", result.getpId(), imageView1, result);
                        } else if ("1".equals(result.getIsFavor())) {
                            fav("0", result.getpId(), imageView1, result);
                        }
                    }
                } else {
                    LoginActivity.start(context);
                }
            }
        });

        vh.projectRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = ScreenUtils.getScreenWidthHeight(context)[0] - ScreenUtils.dip2px(context, 50);
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setContentView(view);
            }
        });
        return convertView;
    }

    /**
     * 点赞
     * type 0：项目 1：大众评论 2、专家评论 3、投资评论
     * action 0：取消 1：点赞
     */
    private void praise(final String action, String pcId, final ImageView imageView, final ProjectResult.ProjectListBean result) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "0");
        params.put("action", action);
        params.put("userId", PreferencesUtils.getString("userId", ""));
        params.put("pcId", pcId);
        JkhDataHandler<ProjectResult> jkhDataHandler = new JkhDataHandler<>(ProjectResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.PRAISE_PROJECT).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<ProjectResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<ProjectResult> pageData) {
                if (pageData.getErrorCode() == 200) {
                    imageView.setClickable(true);
                    if (action.equals("0")) {
                        result.setIsPraise("0");
                        if (!TextUtils.isEmpty(result.getPraiseNum()) && !"--".equals(result.getPraiseNum())) {
                            result.setPraiseNum(String.valueOf(Integer.parseInt(result.getPraiseNum()) - 1));
                        }
                    } else {
                        result.setIsPraise("1");
                        if (!TextUtils.isEmpty(result.getPraiseNum()) && !"--".equals(result.getPraiseNum())) {
                            result.setPraiseNum(String.valueOf(Integer.parseInt(result.getPraiseNum()) + 1));
                        } else {
                            result.setPraiseNum(String.valueOf(1));
                        }
                        imageView.setImageResource(R.drawable.had_liked_2);
                        setPraiseAnimation(imageView);
                    }
                    notifyDataSetChanged();
                } else {
                    ToastUtils.show(context, pageData.getErrorMessage(), 1);
                    imageView.setClickable(true);
                }
            }

            @Override
            public void onError(RequestError error) {
                imageView.setClickable(true);
            }

            @Override
            public void onCacheResponse(String data) {
            }

            @Override
            public void onResponse(String data) {
            }
        });
    }

    /**
     * 收藏
     * type 0为取消收藏，1为收藏
     */
    private void fav(final String type, String pId, final ImageView imageView, final ProjectResult.ProjectListBean result) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("userId", PreferencesUtils.getString("userId", ""));
        params.put("pId", pId);
        JkhDataHandler<ProjectResult> jkhDataHandler = new JkhDataHandler<>(ProjectResult.class);
        RequestInfo info = new RequestInfo.Builder().method(Request.Method.POST).params(params)
                .requestMode(RequestInfo.REQUEST_NETWORK)
                .url(UrlUtils.FAV_PROJECT).build();
        App.request.request(info, jkhDataHandler, new ResponseListener<PageData<ProjectResult>>() {

            @Override
            public void onHandlerComplete(ResponseHandler handler, PageData<ProjectResult> pageData) {
                if (pageData.getErrorCode() == 200) {
                    imageView.setClickable(true);
                    if (type.equals("0")) {
                        result.setIsFavor("0");
                        if (!TextUtils.isEmpty(result.getCollectNum()) && !"--".equals(result.getCollectNum())) {
                            result.setCollectNum(String.valueOf(Integer.parseInt(result.getCollectNum()) - 1));
                        }
                    } else {
                        result.setIsFavor("1");
                        if (!TextUtils.isEmpty(result.getCollectNum()) && !"--".equals(result.getCollectNum())) {
                            result.setCollectNum(String.valueOf(Integer.parseInt(result.getCollectNum()) + 1));
                        } else {
                            result.setCollectNum(String.valueOf(1));
                        }
                        imageView.setImageResource(R.drawable.had_liked_1);
                        setPraiseAnimation(imageView);
                    }
                    notifyDataSetChanged();
                } else {
                    ToastUtils.show(context, pageData.getErrorMessage(), 1);
                    imageView.setClickable(true);
                }
            }

            @Override
            public void onError(RequestError error) {
                imageView.setClickable(true);
            }

            @Override
            public void onCacheResponse(String data) {
            }

            @Override
            public void onResponse(String data) {
            }
        });
    }

    private void setPraiseAnimation(final ImageView imageView) {
        big.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(small);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(big);
    }

    private class ViewHolder {

        public final View root;
        public final ImageView projectPic;
        public final StatuBarView statuBarView;
        public final LinearLayout projectProgress;
        public final LinearLayout projectRating;
        public final ProgressBar progressBar;
        public final RatingBar ratingBar;
        public final TextView progressText;
        public final TextView projectName;
        public final TextView top_1;
        public final TextView bottom_1;
        public final TextView top_2;
        public final TextView bottom_2;
        public final TextView top_3;
        public final TextView bottom_3;
        public final TextView top_4;
        public final TextView bottom_4;
        public final LinearLayout remainTimeLayout;
        public final CountdownView time;
        public final TextView timeText;
        public final LinearLayout layout1, layout2, layout3, layout4;
        public final ImageView likeProject;
        public final ImageView question;
        public final TextView updateButton;
        public final View line;
        public final ImageView noticeLine;
        public final TextView ratingText;
        public final TextView days_remain;
        public final TextView failStatus;

        public ViewHolder(View root) {
            this.root = root;
            projectPic = (ImageView) root.findViewById(R.id.project_pic);
            statuBarView = (StatuBarView) root.findViewById(R.id.project_status);
            projectProgress = (LinearLayout) root.findViewById(R.id.project_progress_layout);
            projectRating = (LinearLayout) root.findViewById(R.id.project_rating_layout);
            progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
            ratingBar = (RatingBar) root.findViewById(R.id.ratingbar);
            progressText = (TextView) root.findViewById(R.id.progress_text);
            projectName = (TextView) root.findViewById(R.id.project_name);
            top_1 = (TextView) root.findViewById(R.id.top_1);
            bottom_1 = (TextView) root.findViewById(R.id.bottom_1);
            top_2 = (TextView) root.findViewById(R.id.top_2);
            bottom_2 = (TextView) root.findViewById(R.id.bottom_2);
            top_3 = (TextView) root.findViewById(R.id.top_3);
            bottom_3 = (TextView) root.findViewById(R.id.bottom_3);
            top_4 = (TextView) root.findViewById(R.id.top_4);
            bottom_4 = (TextView) root.findViewById(R.id.bottom_4);
            remainTimeLayout = (LinearLayout) root.findViewById(R.id.remain_time_layout);
            layout1 = (LinearLayout) root.findViewById(R.id.layout_1);
            layout2 = (LinearLayout) root.findViewById(R.id.layout_2);
            layout3 = (LinearLayout) root.findViewById(R.id.layout_3);
            layout4 = (LinearLayout) root.findViewById(R.id.layout_4);
            time = (CountdownView) root.findViewById(R.id.time);
            timeText = (TextView) root.findViewById(R.id.time_text);
            likeProject = (ImageView) root.findViewById(R.id.like_project);
            question = (ImageView) root.findViewById(R.id.question);
            updateButton = (TextView) root.findViewById(R.id.update_button);
            line = root.findViewById(R.id.line);
            noticeLine = (ImageView) root.findViewById(R.id.notice_line);
            ratingText = (TextView) root.findViewById(R.id.ratingbar_text);
            days_remain = (TextView) root.findViewById(R.id.days);
            failStatus = (TextView) root.findViewById(R.id.fail_status);
        }
    }
}
