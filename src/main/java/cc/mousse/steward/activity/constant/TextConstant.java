package cc.mousse.steward.activity.constant;

import cc.mousse.steward.activity.cache.BasicCache;

import static cc.mousse.steward.activity.constant.StyleConstant.*;

/**
 * @author MochaMousse
 */
public class TextConstant {
  public static final String EMPTY = "";
  public static final String ZERO = "0";
  public static final String DOT = ".";
  public static final String RETURN = "\n";
  public static final String BLANK = " ";
  public static final String SLASH = "/";
  public static final String HYPHEN = "-";
  public static final String SHARP = "#";
  public static final String STAR = "*";
  public static final String VERTICAL = "|";
  public static final String REF = "&";
  public static final String SECTION = "§";
  public static final String COLON = ":";
  public static final String COMMA = ",";
  public static final String BRACKET_LEFT = "[";
  public static final String BRACKET_RIGHT = "]";
  public static final String ARROW_RIGHT = "->";
  public static final String SECOND = "秒";
  public static final String MINUTE = "分";
  public static final String HOUR = "时";
  public static final String DAY = "日";
  public static final String MONTH = "月";
  public static final String YEAR = "年";
  public static final String NULL = "无";
  public static final String REPLACE = "%s";
  public static final String CLOSE = RED.concat(BOLD).concat("关闭");
  public static final String CLOSE_LORE = GRAY.concat("点击关闭");
  public static final String BACK = RED.concat(BOLD).concat("返回");
  public static final String BACK_LORE = GRAY.concat("点击返回");
  public static final String MENU_PREFIX = YELLOW.concat(BOLD);
  public static final String PLAYER_BUTTON = MENU_PREFIX.concat("个人信息");
  public static final String DURATION_REWARD_BUTTON = MENU_PREFIX.concat("在线奖励");
  public static final String DAYS_REWARD_BUTTON = MENU_PREFIX.concat("签到奖励");
  public static final String SWITCH_MONTH_BUTTON = MENU_PREFIX.concat("切换月份");
  public static final String SWITCH_MONTH_LORE = GRAY.concat("左右键切换月份");
  public static final String SIGN_PREFIX = WHITE.concat(BOLD).concat(BRACKET_LEFT);
  public static final String SIGN_SUFFIX = WHITE.concat(BOLD).concat(BRACKET_RIGHT);
  public static final String SIGN_BUTTON =
      SIGN_PREFIX.concat(GREEN).concat(BOLD).concat("签到").concat(SIGN_SUFFIX);
  public static final String SIGNED_BUTTON =
      SIGN_PREFIX.concat(GOLD).concat(BOLD).concat("已签").concat(SIGN_SUFFIX);
  public static final String MISS_SIGN_BUTTON =
      SIGN_PREFIX.concat(RED).concat(BOLD).concat("漏签").concat(SIGN_SUFFIX);
  public static final String FUTURE_SIGN_BUTTON =
      SIGN_PREFIX.concat(DARK_GREEN).concat(BOLD).concat("待签").concat(SIGN_SUFFIX);
  public static final String PLAYER_ONLY = "该指令只能由玩家使用";
  public static final String RELOAD_OK = "配置已重载";
  public static final String CLEAR_OK = "缓存已释放";
  public static final String DATA_NOTFOUND = "数据不存在";
  public static final String DATE_FORMAT_ERROR = "日期格式错误";
  public static final String MONTH_NOT_SPECIFY = "未指定月份";
  public static final String DAY_NOT_SPECIFY = "未指定日期";
  public static final String DAYS_REWARD = "签到奖励";
  public static final String DURATION_REWARD = "在线奖励";
  public static final String DAYS_REWARD_AVAILABLE = "可领取签到奖励";
  public static final String DURATION_REWARD_AVAILABLE = "可领取在线奖励";
  public static final String DATASOURCE_CONFIG_INCOMPLETE = "数据源配置不完整";
  public static final String DATASOURCE_CONFIG_ERROR = "数据源配置异常";
  public static final String REWARD_AVAILABLE_HOVER = "点击打开签到宝";
  public static final String GROUP_MESSAGE_SEND_ERROR = "消息发送失败";
  public static final String GUI_NOTFOUND = "找不到GUI组件";
  public static final String LOGIN_DATE = "登录日期";
  public static final String LOGIN_TIME = "登录时间";
  public static final String SIGN_CHANCE = "补签机会";
  public static final String SIGN_STATE = "签到状态";
  public static final String DAYS_OF_MONTH = "月签到天数";
  public static final String DAYS_OF_TOTAL = "总签到天数";
  public static final String DURATION_OF_DAY = "日在线时长";
  public static final String DURATION_OF_TOTAL = "总在线时长";
  public static final String DAYS_REWARD_TAKEN = "已领取的签到天数奖励等级";
  public static final String DURATION_REWARD_TAKEN = "已领取的在线时长奖励等级";
  public static final String DAY_X_INFO = "第".concat(REPLACE).concat("天");
  public static final String ONLINE_X_MINUTES_INFO = "在线".concat(REPLACE).concat("分钟");
  public static final String GET_X_REWARD_INFO =
      "获得".concat(GREEN).concat(REPLACE).concat(WHITE).concat(BasicCache.CURRENCY_NAME);
  public static final String GET_REWARD = RED.concat("点击领取奖励");
  public static final String SEE_REWARD = GRAY.concat("点击查看奖励");
  public static final String BACK_TO_MENU = GRAY.concat("点击返回菜单");
  private static final String INFO_PREFIX = DARK_AQUA.concat(BOLD);
  private static final String INFO_SUFFIX =
      GRAY.concat(COLON).concat(BLANK).concat(WHITE).concat(REPLACE);
  public static final String BALANCE_INFO =
      INFO_PREFIX.concat(BasicCache.CURRENCY_NAME).concat("余额").concat(INFO_SUFFIX);
  public static final String CHANCE_INFO = INFO_PREFIX.concat("补签机会").concat(INFO_SUFFIX);
  public static final String DAYS_OF_MONTH_INFO = INFO_PREFIX.concat("当月签到").concat(INFO_SUFFIX);
  public static final String DAYS_OF_TOTAL_INFO = INFO_PREFIX.concat("累计签到").concat(INFO_SUFFIX);
  public static final String DURATION_OF_ONLINE_INFO =
      INFO_PREFIX.concat("持续在线").concat(INFO_SUFFIX);
  public static final String DURATION_OF_DAY_INFO = INFO_PREFIX.concat("当日在线").concat(INFO_SUFFIX);
  public static final String DURATION_OF_TOTAL_INFO =
      INFO_PREFIX.concat("累计在线").concat(INFO_SUFFIX);
  public static final String TOSTRING_START =
      DARK_GRAY.concat(BLANK).concat(ARROW_RIGHT).concat(BLANK).concat(BRACKET_LEFT).concat(GRAY);
  public static final String TOSTRING_NED = DARK_GRAY.concat(BRACKET_RIGHT).concat(RESET);
  public static final String TOSTRING_COLON = DARK_GRAY.concat(COLON).concat(BLANK).concat(RESET);
  public static final String TOSTRING_SPLIT = DARK_GRAY.concat(COMMA).concat(BLANK).concat(GRAY);
  public static final String DAILY_REPORT_TITLE =
      WHITE
          .concat(REPLACE)
          .concat(YEAR)
          .concat(REPLACE)
          .concat(MONTH)
          .concat(REPLACE)
          .concat(DAY)
          .concat(GRAY)
          .concat("在线")
          .concat(WHITE)
          .concat(REPLACE)
          .concat("人")
          .concat(DARK_GRAY)
          .concat(COMMA)
          .concat(BLANK)
          .concat(GRAY)
          .concat("平均在线")
          .concat(WHITE)
          .concat(REPLACE)
          .concat(DARK_GRAY)
          .concat(COMMA)
          .concat(BLANK)
          .concat(GRAY)
          .concat("中位在线")
          .concat(WHITE)
          .concat(REPLACE);

  public static final String DAILY_REPORT_INFO =
      DARK_GRAY
          .concat(RETURN)
          .concat(VERTICAL)
          .concat(BLANK)
          .concat(WHITE)
          .concat(REPLACE)
          .concat(TOSTRING_START)
          .concat(WHITE)
          .concat(REPLACE)
          .concat(TOSTRING_NED);
  public static final String DAILY_REPORT_NONE =
      WHITE
          .concat(REPLACE)
          .concat(YEAR)
          .concat(REPLACE)
          .concat(MONTH)
          .concat(REPLACE)
          .concat(DAY)
          .concat(GRAY)
          .concat("无人在线");
  public static final String MONTHLY_REPORT_TITLE =
      WHITE
          .concat(REPLACE)
          .concat(YEAR)
          .concat(REPLACE)
          .concat(MONTH)
          .concat(GRAY)
          .concat("在线")
          .concat(WHITE)
          .concat(REPLACE)
          .concat("人")
          .concat(DARK_GRAY)
          .concat(SLASH)
          .concat(WHITE)
          .concat(REPLACE)
          .concat("人次")
          .concat(DARK_GRAY)
          .concat(COMMA)
          .concat(BLANK)
          .concat(GRAY)
          .concat("平均在线")
          .concat(WHITE)
          .concat(REPLACE)
          .concat(DARK_GRAY)
          .concat(COMMA)
          .concat(BLANK)
          .concat(GRAY)
          .concat("中位在线")
          .concat(WHITE)
          .concat(REPLACE);
  public static final String MONTHLY_REPORT_INFO =
      DARK_GRAY
          .concat(RETURN)
          .concat(VERTICAL)
          .concat(BLANK)
          .concat(WHITE)
          .concat(REPLACE)
          .concat(TOSTRING_START)
          .concat("在线天数")
          .concat(TOSTRING_COLON)
          .concat(REPLACE)
          .concat(TOSTRING_SPLIT)
          .concat(DURATION_OF_TOTAL)
          .concat(TOSTRING_COLON)
          .concat(REPLACE)
          .concat(TOSTRING_SPLIT)
          .concat("平均在线")
          .concat(TOSTRING_COLON)
          .concat(REPLACE)
          .concat(TOSTRING_NED);
  public static final String MONTHLY_REPORT_NONE =
      WHITE.concat(REPLACE).concat(YEAR).concat(REPLACE).concat(MONTH).concat(GRAY).concat("无人在线");

  private TextConstant() {}
}
