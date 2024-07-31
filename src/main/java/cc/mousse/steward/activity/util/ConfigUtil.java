package cc.mousse.steward.activity.util;

import static cc.mousse.steward.activity.cache.ConfigCache.Key;
import static cc.mousse.steward.activity.constant.TextConstant.*;

import cc.mousse.steward.activity.Application;
import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.cache.ConfigCache;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author MochaMousse
 */
public class ConfigUtil {
  private static FileConfiguration config;

  private ConfigUtil() {}

  public static void init() {
    // 加载配置文件
    BasicCache.getInstance().saveDefaultConfig();
    Application instance = BasicCache.getInstance();
    config = instance.getConfig();
    if (loadDataBase(instance)) {
      loadActivity();
      loadGoCqHttp();
    }
  }

  public static void reload() {
    Application instance = BasicCache.getInstance();
    instance.reloadConfig();
    config = instance.getConfig();
    loadActivity();
    loadGoCqHttp();
  }

  private static void loadGoCqHttp() {
    ConfigCache.setGoCqHttpUrl(config.getString(Key.GO_CQ_HTTP_URL));
    ConfigCache.setMessageQqGroupId(config.getString(Key.MESSAGE_QQ_GROUP_ID));
    ConfigCache.setReportQqGroupId(config.getString(Key.REPORT_QQ_GROUP_ID));
    ConfigCache.setStarOfDayMessage(config.getString(Key.STAR_OF_DAY_MESSAGE));
    ConfigCache.setStarOfMonthMessage(config.getString(Key.STAR_OF_MONTH_MESSAGE));
  }

  private static void loadActivity() {
    ConfigCache.setAutoMode(config.getBoolean(Key.AUTO_MODE));
    ConfigCache.setTaskPeriod(config.getInt(Key.TASK_PERIOD));
    ConfigCache.setShowOnFirstLogin(config.getBoolean(Key.SHOW_ON_FIRST_LOGIN));
    ConfigCache.setChance(config.getInt(Key.CHANCE));
    ConfigCache.setSignReward(config.getInt(Key.SIGN_REWARD));
    ConfigCache.setStarOfDayReward(config.getInt(Key.STAR_OF_DAY_REWARD));
    ConfigCache.setStarOfMonthReward(config.getInt(Key.STAR_OF_MONTH_REWARD));
    Map<Integer, Integer> daysRewardMap = HashMap.newHashMap(32);
    config.getMapList(Key.DAYS_REWARD).stream()
        .map(Map::entrySet)
        .forEach(
            entries ->
                entries.forEach(
                    entry ->
                        daysRewardMap.put((Integer) entry.getKey(), (Integer) entry.getValue())));
    ConfigCache.setDaysReward(daysRewardMap);
    Map<Integer, Integer> durationRewardMap = HashMap.newHashMap(32);
    config.getMapList(Key.DURATION_REWARD).stream()
        .map(Map::entrySet)
        .forEach(
            entries ->
                entries.forEach(
                    entry ->
                        durationRewardMap.put(
                            (Integer) entry.getKey(), (Integer) entry.getValue())));
    ConfigCache.setDurationReward(durationRewardMap);
  }

  private static boolean loadDataBase(Application instance) {
    boolean success;
    String url = config.getString(Key.MYSQL_URL);
    String username = config.getString(Key.MYSQL_USERNAME);
    String password = config.getString(Key.MYSQL_PASSWORD);
    if (StringUtils.isAnyBlank(url, username, password)) {
      LogUtil.warn(DATASOURCE_CONFIG_INCOMPLETE);
      Bukkit.getPluginManager().disablePlugin(instance);
      success = false;
    } else {
      try {
        DataSourceUtil.init(url, username, password);
        success = true;
      } catch (SQLException e) {
        LogUtil.warn(DATASOURCE_CONFIG_ERROR);
        LogUtil.warn(e);
        success = false;
      }
    }
    return success;
  }
}
