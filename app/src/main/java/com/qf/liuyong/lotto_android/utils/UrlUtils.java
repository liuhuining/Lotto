package com.qf.liuyong.lotto_android.utils;

import com.qf.liuyong.lotto_android.BuildConfig;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class UrlUtils {

    //接口IP
    public static final String API_IP = "http://app.hcspark.com";
    //旧版网页IP
    public static final String WEBPAGE_IP = "http://app.hcspark.com";
    //新版网页用的微信站页面
    public static final String WX_IP = "http://wx.hcspark.com";
    //渠道编号
    public static final String CHANNEL_CODE = "6965657144554344696565736967657145/";
    //上传文件
    public static final String UPLOAD = API_IP + "/ud/" + CHANNEL_CODE;

    public static final String RELEASE_URL = API_IP + "/api/" + CHANNEL_CODE;
    public static final String PAGE_HOST = WEBPAGE_IP + "/page";
    public static final String DEBUG_URL = API_IP + "/api/" + CHANNEL_CODE;
    public static final String URL_HOST = BuildConfig.BUILD_TYPE.equals("debug") ? DEBUG_URL : RELEASE_URL;

    //---------------------------------html(jsp)page url------------------------------------------------
    /**
     * 项目详情
     */
    public static final String PROJECT_DETAIL = WX_IP + "/wechat/detail/index.html?weixin=1";

    /**
     * 免责声明
     */
    public static final String RELIEF_NOTICE = WX_IP + "/xieyi/msg.html";

    /**
     * 预约支付
     */
    public static final String SUBSCRIBE = PAGE_HOST + "/appview/goapp";

    /**
     * 支付协议界面
     */
    public static final String PAY = PAGE_HOST + "/appview/xieyi";

    /**
     * 股权协议
     */
    public static final String PROJECT_DETAIL_AGREEMENT = PAGE_HOST + "/appview/detailxy";

    /**
     * 投资意向确认书
     */
    public static final String CONFIRM_BOOK = PAGE_HOST + "/appview/confirmation";

    /**
     * 发布协议
     */
    public static final String PUBLISH_XIEYI = PAGE_HOST + "/appview/fwxy";

    /**
     * 扫描二维码
     */
    public static final String QR_CODE = PAGE_HOST + "/appview/qrCodeLogin";

    /**
     * 注册协议
     */
    public static final String REGISTER_AGREEMENT = PAGE_HOST + "/appview/xieyi1";

    /**
     * 汇款页面
     */
    public static final String REMIT_PAGE = PAGE_HOST + "/appview/transfer";

    /**
     * 推荐好友
     */
    public static final String RECOMMEND_FRIEND = PAGE_HOST + "/appview/ltdown";

    /**
     * 首页banner详情
     */
    public static final String BANNER_DETAIL = PAGE_HOST + "/appview/viewimg";

    /**
     * 视频网页地址
     */
    public static final String VIDEO_PAGE_URL = PAGE_HOST + "/appview/videoplay?link_url=";

    /**
     * 签署合同
     */
    public static final String HESIGN = PAGE_HOST + "/appview/hesign?pid=%1$s&access_token=%2$s&vcstatus=%3$s";

    /**
     * 查看合同
     */
    public static final String VIEW_HESIGN = PAGE_HOST + "/appview/viewhesign?pid=%1$s&access_token=%2$s&vcstatus=%3$s";

    /**
     * 退款帮助
     */
    public static final String REFUND_HELP = WEBPAGE_IP + "/xieyi/tkbz.html";
    //---------------------------------html(jsp)page url------------------------------------------------


    /**
     * 首页
     */
    public static final String HOME_PAGE = URL_HOST + "101003";

    /**
     * 首页项目列表
     */
    public static final String PROJECT_LIST_GYTYPE = URL_HOST + "101005";

    /**
     * 获取个人信息接口
     */
    public static final String PERSON_INFO = URL_HOST + "100008";

    /**
     * 修改个人信息
     */
    public static final String UPLOAD_PERSON_INFO = URL_HOST + "100015";

    /**
     * 我的消息队列
     */
    public static final String NEWS_INFO = URL_HOST + "103006";

    /**
     * 关于我们
     */
    public static final String ABOUT_US_INFO = URL_HOST + "106002";

    /**
     * 实名认证
     */
    public static final String REAL_NAME_AUTHENTIC = URL_HOST + "100018";

    /**
     * 登录
     */
    public static final String LOGIN = URL_HOST + "100001";

    /**
     * 获取验证码
     */
    public static final String GET_CODE = URL_HOST + "100002";

    /**
     * 注册
     */
    public static final String REGISTER = URL_HOST + "100003";

    /**
     * 校验验证码
     */
    public static final String CHECK_CODE = URL_HOST + "100004";

    /**
     * 修改密码
     */
    public static final String MODIFY_PASSWORD = URL_HOST + "100005";

    /**
     * 我支持的
     */
    public static final String MY_SUPPORT_PROJECT = URL_HOST + "100016";

    /**
     * 我喜欢的
     */
    public static final String MY_LOVE_PROJECT = URL_HOST + "100016";

    /**
     * 获取投资金额范围
     */
    public static final String SUPPORT_AMOUNT_RANG = URL_HOST + "101016";

    /**
     * 是否是股东
     */
    public static final String IS_SHAREHOLDER = URL_HOST + "100007";

    /**
     * 是否是发起人
     */
    public static final String IS_SPONSOR = URL_HOST + "100006";

    /**
     * 收藏/取消收藏
     */
    public static final String FAV = URL_HOST + "101009";

    /**
     * 是否收藏该项目
     */
    public static final String GET_FAV_STATUS = URL_HOST + "101011";

    /**
     * 出让股份比例
     */
    public static final String TRANSFER_SHARE = URL_HOST + "101019";

    /**
     * 募集比例
     */
    public static final String RAISE_RATIO = URL_HOST + "101018";

    /**
     * 项目阶段
     */
    public static final String PROJECT_STAGE = URL_HOST + "101017";

    /**
     * 版本更新
     */
    public static final String GET_VERSION_INFO = URL_HOST + "106001";

    /**
     * 加入到群组
     */
    public static final String ADD_TO_GROUP = URL_HOST + "103007";

    /**
     * 退出群组
     */
    public static final String EXIT_GROUP = URL_HOST + "103008";

    /**
     * 获取群组对应的项目id
     */
    public static final String GET_PROJECT_ID = URL_HOST + "103009";

    /**
     * 估值
     */
    public static final String INVEST_VALUATION = URL_HOST + "101025";

    /**
     * 获取群公告及在群中角色
     */
    public static final String GET_ROLE_NOTICE = URL_HOST + "103010";

    /**
     * 获取所有公开群
     */
    public static final String GET_ALL_GROUP = URL_HOST + "103011";

    /**
     * 判断用户是否注册了环信
     */
    public static final String IS_REGISTER_EM = URL_HOST + "103012";

    /**
     * 推荐群组
     */
    public static final String INTEREST_GROUP = URL_HOST + "103013";

    /**
     * 首页tab图片
     */
    public static final String GET_HOME_TAB_PIC = URL_HOST + "106006";

    /**
     * 修改群组公告
     */
    public static final String CHANGE_GROUP_NOTICE = URL_HOST + "103014";

    /**
     * 确权状态
     */
    public static final String RIGHT_CONFIRM = URL_HOST + "107002";

    /**
     * 修改确权信息
     */
    public static final String RIGHT_INFO = URL_HOST + "107014";

    /**
     * 查看确权信息
     */
    public static final String RIGHT_CHECK_STATE = URL_HOST + "107003";

    /**
     * 查询最大投资额
     */
    public static final String GET_MAX_INVESTMONEY = URL_HOST + "100013";

    /**
     * 获取公司认证信息
     */
    public static final String GET_COMPANY_CERTIFY_INFO = URL_HOST + "107005";

    /**
     * 更新公司认证电话
     */
    public static final String UPDATE_COMPANY_CERTIFY_PHONE = URL_HOST + "107004";

    /**
     * 获取门店信息
     */
    public static final String JOB_INFO = URL_HOST + "100012";

    /**
     * 我的邀请记录
     */
    public static final String INVITER_RECORD = URL_HOST + "100011";

    /**
     * 更改身份证上传状态
     */
    public static final String IDENTITY_STATE = URL_HOST + "100010";

    /**
     * 校验签署协议验证
     */
    public static final String CHECK_SIGN_CODE = URL_HOST + "101024";

    /**
     * 投资认证接口（新）
     */
    public static final String CERTIFICATE_NEW = URL_HOST + "107015";

    /**
     * 财富顾问(专家列表)
     */
    public static final String EXPERT_LIST = URL_HOST + "105009";

    /**
     * 投资达人
     */
    public static final String GREAT_INVESTMENT = URL_HOST + "105003";

    /**
     * 专家报名
     */
    public static final String EXPERT_UPLOAD = URL_HOST + "105012";

    /**
     * 获取专家信息
     */
    public static final String GET_EXPERT = URL_HOST + "105011";

    /**
     * 所属行业
     */
    public static final String BP_INDUSTRY = URL_HOST + "101006";

    /**
     * 评论列表
     */
    public static final String COMMENT_LIST = URL_HOST + "105014";

    /**
     * 新增(留言/评论)
     */
    public static final String COMMENT_LEAVEMESSAGE = URL_HOST + "103001";

    /**
     * 专家点评
     */
    public static final String EXPERT_POST_DISCUSS = URL_HOST + "105010";

    /**
     * 项目投资列表
     */
    public static final String INVEST_LIST = URL_HOST + "105004";
    /**
     * 项目详情
     */
    public static final String PROJECT_SPECIFIC = URL_HOST + "101012";

    /**
     * 我的投资
     */
    public static final String MY_INVEST = URL_HOST + "101026";

    /**
     * 问答列表
     */
    public static final String QA_LIST = URL_HOST + "106008";

    /**
     * 获取专家基本信息
     */
    public static final String EXPERT_INFO = URL_HOST + "105015";

    /**
     * 专家的项目点评列表
     */
    public static final String EXPERT_COMMENT_LIST = URL_HOST + "105002";

    /**
     * 项目退款申请
     */
    public static final String APPLY_REFUND = URL_HOST + "102005";

    /**
     * 创建订单接口
     */
    public static final String CREATE_ORDER = URL_HOST + "102001";

    /**
     * 项目动态列表
     */
    public static final String PROJECT_UPDATE_HISTORY = URL_HOST + "101030";

    /**
     * 我发布的项目
     */
    public static final String MY_RELEASE_PROJECT = URL_HOST + "100021";

    /**
     * 大讲堂列表
     */
    public static final String LARGE_CLASSROOM = URL_HOST + "106007";

    /**
     * 项目点赞
     */
    public static final String PRAISE_PROJECT = URL_HOST + "103015";

    /**
     * 项目收藏
     */
    public static final String FAV_PROJECT = URL_HOST + "101009";

    /**
     * 搜索
     */
    public static final String SEARCH = URL_HOST + "101027";

    /**
     * 热门项目
     */
    public static final String HOT_PROJECT = URL_HOST + "101028";

    /**
     * 首页弹出公告
     */
    public static final String HOMEPAGE_NOTICE = URL_HOST + "106010";

    /**
     * 项目退款详情
     */
    public static final String REFUND_DETAIL = URL_HOST + "102006";

    /**
     * 是否是投资人
     */
    public static final String IS_INVEST = URL_HOST + "100007";

    /**
     * 举报
     */
    public static final String REPORT = URL_HOST + "106009";

    /**
     * 项目专家点评列表
     */
    public static final String EXPERT_PROJECT = URL_HOST + "105016";

    /**
     * 消息是否已读
     */
    public static final String UPLOAD_NEWS_ISREADE = URL_HOST + "106004";

    /**
     * 项目动态更新
     */
    public static final String PROJECT_UPDATE = URL_HOST + "101029";

    /**
     * 项目详情相关协议
     */
    public static final String PROJECT_PROTOCOL = URL_HOST + "101034";

    /**
     * 项目详情相关协议
     */
    public static final String PROJECT_PUBLISH = URL_HOST + "101001";

    /**
     * 创建订单
     */
    public static final String PROJECT_PAYMENT = URL_HOST + "102001";

    /**
     * 更改用户签订项目协议
     */
    public static final String UPDATE_PROCOTL = URL_HOST + "101036";

    /**
     * 更改用户签订项目协议
     */
    public static final String SHARE_SPECFIC = URL_HOST + "101038";
}
