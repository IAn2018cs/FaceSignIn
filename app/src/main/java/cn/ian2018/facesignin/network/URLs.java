package cn.ian2018.facesignin.network;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */
public class URLs {
    /**
     * 主机地址
     */
    public static final String HOST_ADDRESS = "http://123.206.57.216:8080/HBUCentreInterface/";

    /**
     * 登录
     */
    public static final String LOGIN = "login.do";
    /**
     * 检测更新
     */
    public static final String UPDATE = "getUpdateInfo.do";
    /**
     * 获取云子上的活动
     */
    public static final String GET_ACTIVE_BY_YUNZI = "HBUCentreInterface/getActivity.do";
    /**
     * 获取管理员添加的活动
     */
    public static final String GET_ACTIVE_BY_ACCOUNT = "HBUCentreInterface/getActivityByAccount.do";
    /**
     * 添加活动
     */
    public static final String ADD_ACTIVE = "HBUCentreInterface/addActivity.do";
    /**
     * 修改活动
     */
    public static final String UPDATE_ACTIVE = HOST_ADDRESS + "HBUCentreInterface/changeActivity.do";
    /**
     * 修改密码
     */
    public static final String CHANGE_PASSWORD = HOST_ADDRESS + "HBUCentreInterface/changePassword.do";
    /**
     * 签到
     */
    public static final String SIGN_IN = "sign.do";
    /**
     * 签离
     */
    public static final String SIGN_OUT = "signout.do";
    /**
     * 意见反馈
     */
    public static final String FEED_BACK = HOST_ADDRESS + "HBUCentreInterface/feedback.do";
    /**
     * 获取历史签到
     */
    public static final String GET_SING = "getHistoryActivity.do";
    /**
     * 获取反馈信息
     */
    public static final String GET_FEED_BACK = HOST_ADDRESS + "HBUCentreInterface/getfeedback.do";
    /**
     * 获取名言
     */
    public static final String GET_SAYING = "getSaying.do";
    /**
     * 获取活动的签到详情
     */
    public static final String GET_SIGN_INFO = HOST_ADDRESS + "HBUCentreInterface/getSignByActivityId.do";
    /**
     * 删除活动
     */
    public static final String DELETE_ACTIVE = HOST_ADDRESS + "HBUCentreInterface/deleteActivityFalse.do";
    /**
     * 获取值班总时间
     */
    public static final String GET_ALL_DUTY_TIME = HOST_ADDRESS + "HBUCentreInterface/getDutyTime.do";
    /**
     * 添加补班
     */
    public static final String ADD_BACK_DUTY = HOST_ADDRESS + "HBUCentreInterface/addBackDuty.do";
    /**
     * 注册
     */
    public static final String SIGN_UP = HOST_ADDRESS + "HBUCentreInterface/signUp.do";
    /**
     * 获取本周个人排名
     */
    public static final String GET_RANK_FOR_ME = HOST_ADDRESS + "HBUCentreInterface/getRankForMe.do";
    /**
     * 获取周列表
     */
    public static final String GET_WEEK_LIST = HOST_ADDRESS + "HBUCentreInterface/getWeekList.do";
    /**
     * 获取组内排名
     */
    public static final String GET_RANK = HOST_ADDRESS + "HBUCentreInterface/getRankList.do";
    /**
     * 获取维修人信息
     */
    public static final String GET_REPAIR_USER = HOST_ADDRESS + "HBUCentreInterface/getRepairUser.do";
    /**
     * 获取中心物品
     */
    public static final String GET_GOODS = HOST_ADDRESS + "HBUCentreInterface/getArticleList.do";
    /**
     * 租借
     */
    public static final String LEASE_GOODS = HOST_ADDRESS + "HBUCentreInterface/laonArticle.do";
    /**
     * 归还
     */
    public static final String BACK_GOODS = HOST_ADDRESS + "HBUCentreInterface/backArticle.do";
    /**
     * 获取个人租借记录
     */
    public static final String GET_LOANS = HOST_ADDRESS + "HBUCentreInterface/getLoanList.do";
    /**
     * 修改个人信息
     */
    public static final String CHANGE_INFO = HOST_ADDRESS + "HBUCentreInterface/changeInfo.do";
    /**
     * 获取值班签到记录
     */
    public static final String GET_ONDUTY_SIGN = HOST_ADDRESS + "HBUCentreInterface/getOnDutySign.do";
    /**
     * 根据日期和活动id去获取签到记录
     */
    public static final String GET_SIGN_BY_DATE = HOST_ADDRESS + "HBUCentreInterface/getSignByDate.do";
    /**
     * 根据活动类型和日期去获取签到记录
     */
    public static final String GET_SIGN_BY_RULE = HOST_ADDRESS + "HBUCentreInterface/getSignByRule.do";
    /**
     * 删除物品
     */
    public static final String DELETE_GOODS_BY_ID = HOST_ADDRESS + "HBUCentreInterface/deleteGoods.do";
    /**
     * 添加物品
     */
    public static final String ADD_GOODS = HOST_ADDRESS + "HBUCentreInterface/addGoods.do";
    /**
     * 根据物品名字查找物品
     */
    public static final String GET_GOODS_BY_NAME = HOST_ADDRESS + "HBUCentreInterface/getGoodsByName.do";
    /**
     * 获取物品当前租借人信息
     */
    public static final String GET_LOAD_USER = HOST_ADDRESS + "HBUCentreInterface/getLoadUser.do";
    /**
     * 修改物品
     */
    public static final String CHANGE_GOODS = HOST_ADDRESS + "HBUCentreInterface/changeGoods.do";
    /**
     * 获取维修记录
     */
    public static final String GET_REPAIR_LIST = HOST_ADDRESS + "HBUCentreInterface/getRepairList.do";
    /**
     * 添加维修记录
     */
    public static final String ADD_REPAIR_INFO = HOST_ADDRESS + "HBUCentreInterface/addRepair.do";
    /**
     * 根据组别和年级获取组内成员
     */
    public static final String GET_USER_BY_GROUP = HOST_ADDRESS + "HBUCentreInterface/getUserListByGroup.do";
    /**
     * 修改用户类型
     */
    public static final String CHANGE_USER_TYPE = HOST_ADDRESS + "HBUCentreInterface/changeUserType.do";
    /**
     * 获取七牛云上传认证
     */
    public static final String GET_TOKEN = HOST_ADDRESS + "HBUCentreInterface/auth.do";
    /**
     * 获取个人本周值班总时间
     */
    public static final String GET_DUTY_TIME_FOR_WEEK_BY_ACCOUNT = HOST_ADDRESS + "HBUCentreInterface/getDutyTimeForWeek.do";
    /**
     * 获取各组本周值班总时间
     */
    public static final String GET_DUTY_TIME_LIST_FOR_WEEK_BY_GROUP = HOST_ADDRESS + "HBUCentreInterface/getDutyTimeForWeekByGroup.do";
    /**
     * 根据云子id获取活动，无值班表方式
     */
    public static final String GET_ACTIVE_BY_YUNZI_NO_DUTY = "getActivityForNoDuty.do";
    /**
     * 获取兴趣小组列表
     */
    public static final String GET_INTEREST_GROUP_LIST = HOST_ADDRESS + "HBUCentreInterface/getInterestGroupList.do";
    /**
     * 修改兴趣小组
     */
    public static final String CHANGE_INTEREST_GROUP = HOST_ADDRESS + "HBUCentreInterface/changeInterestGroup.do";
}
