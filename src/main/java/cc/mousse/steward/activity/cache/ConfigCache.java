package cc.mousse.steward.activity.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author MochaMousse
 */
public class ConfigCache {
  @Getter @Setter private static volatile boolean autoMode;
  @Getter @Setter private static volatile boolean showOnFirstLogin;
  @Getter @Setter private static volatile int chance;
  @Getter @Setter private static volatile int signReward;
  @Getter @Setter private static volatile int taskPeriod;
  @Getter @Setter private static volatile String goCqHttpUrl;
  @Getter @Setter private static volatile String messageQqGroupId;
  @Getter @Setter private static volatile String reportQqGroupId;
  @Getter @Setter private static volatile String starOfDayMessage;
  @Getter @Setter private static volatile Integer starOfDayReward;
  @Getter @Setter private static volatile String starOfMonthMessage;
  @Getter @Setter private static volatile Integer starOfMonthReward;
  @Getter private static List<Map.Entry<Integer, Integer>> daysReward;
  @Getter private static List<Map.Entry<Long, Integer>> durationReward;

  private ConfigCache() {}

  public static void setDaysReward(Map<Integer, Integer> map) {
    daysReward =
        map.entrySet().stream()
            .filter(entry -> entry.getKey() != null && entry.getValue() != null)
            .sorted(Comparator.comparingInt(Map.Entry::getKey))
            .toList();
  }

  public static void setDurationReward(Map<Integer, Integer> map) {
    Map<Long, Integer> durationMap = new HashMap<>(map.size());
    map.forEach(
        (key, value) -> {
          if (key != null && value != null) {
            durationMap.put(key * 60 * 1000L, value);
          }
        });
    durationReward =
        durationMap.entrySet().stream()
            .sorted(Comparator.comparingLong(Map.Entry::getKey))
            .toList();
  }

  public static class Key {

    private Key() {}

    private static final String MYSQL = "mysql.";
    public static final String MYSQL_URL = MYSQL.concat("url");
    public static final String MYSQL_USERNAME = MYSQL.concat("username");
    public static final String MYSQL_PASSWORD = MYSQL.concat("password");
    public static final String AUTO_MODE = "auto-mode";
    public static final String GO_CQ_HTTP_URL = "go-cqhttp";
    private static final String STAR_OF_DAY = "star-of-day.";
    private static final String STAR_OF_MONTH = "star-of-month.";
    private static final String MESSAGE = "message";
    private static final String REWARD = "reward";
    public static final String STAR_OF_DAY_MESSAGE = STAR_OF_DAY.concat(MESSAGE);
    public static final String STAR_OF_DAY_REWARD = STAR_OF_DAY.concat(REWARD);
    public static final String STAR_OF_MONTH_MESSAGE = STAR_OF_MONTH.concat(MESSAGE);
    public static final String STAR_OF_MONTH_REWARD = STAR_OF_MONTH.concat(REWARD);
    public static final String TASK_PERIOD = "task-period";
    public static final String SHOW_ON_FIRST_LOGIN = "show-on-first-login";
    public static final String CHANCE = "chance";
    public static final String SIGN_REWARD = "sign-reward";
    public static final String DAYS_REWARD = "days-reward";
    public static final String DURATION_REWARD = "duration-reward";
    private static final String QQ_GROUP_ID = "qq-group.";
    public static final String MESSAGE_QQ_GROUP_ID = QQ_GROUP_ID.concat(MESSAGE);
    public static final String REPORT_QQ_GROUP_ID = QQ_GROUP_ID.concat("report");
  }
}
