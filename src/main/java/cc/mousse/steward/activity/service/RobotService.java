package cc.mousse.steward.activity.service;

import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.cache.ConfigCache;
import cc.mousse.steward.activity.cache.PlayerCache;
import cc.mousse.steward.activity.util.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
public class RobotService {
  private RobotService() {}

  public static void sendMessage(int year, int month, int day) {
    Server server = BasicCache.getInstance().getServer();
    String qqGroupId = ConfigCache.getMessageQqGroupId();
    starOfDay(server, qqGroupId, year, month, day);
    starOfMonth(server, qqGroupId, year, month);
  }

  private static void starOfDay(Server server, String qqGroupId, int year, int month, int day) {
    String starOfDayMessage = ConfigCache.getStarOfDayMessage();
    if (StringUtils.isNotBlank(starOfDayMessage)) {
      List<String> list = RecordService.topDuration(year, month, day);
      if (!list.isEmpty()) {
        RobotUtil.sendGroupMessage(qqGroupId, String.format(starOfDayMessage, list));
        Integer starOfDayReward = ConfigCache.getStarOfDayReward();
        if (starOfDayReward != null) {
          list.forEach(
              playerName ->
                  BalanceUtil.add(
                      Objects.requireNonNull(server.getOfflinePlayer(playerName)),
                      starOfDayReward));
        }
      }
    }
  }

  private static void starOfMonth(Server server, String qqGroupId, int year, int month) {
    String starOfMonthMessage = ConfigCache.getStarOfMonthMessage();
    if (DateTimeUtil.month() != month && StringUtils.isNotBlank(starOfMonthMessage)) {
      List<String> list = RecordService.topDays(year, month);
      RobotUtil.sendGroupMessage(qqGroupId, starOfMonthMessage.replace(REPLACE, list.toString()));
      Integer starOfMonthReward = ConfigCache.getStarOfMonthReward();
      if (starOfMonthReward != null) {
        list.forEach(
            playerName ->
                BalanceUtil.add(
                    Objects.requireNonNull(server.getOfflinePlayer(playerName)),
                    starOfMonthReward));
      }
    }
  }

  public static void sendReport(int year, int month, int day) {
    LogUtil.info(StringUtil.removeStyle(RobotService.dailyReport(year, month, day)));
    if (DateTimeUtil.month() != month) {
      LogUtil.info("New month!");
      LogUtil.info(StringUtil.removeStyle(RobotService.monthlyReport(year, month)));
    }
  }

  public static String dailyReport(int year, int month, int day) {
    List<RecordDo> list =
        RecordService.list(year, month, day).stream()
            .sorted(Comparator.comparing(RecordDo::getPlayer))
            .toList();
    String title;
    List<String> messages = new ArrayList<>(2);
    StringBuilder builder = new StringBuilder();
    if (list.isEmpty()) {
      title =
          StringUtil.replace(
              DAILY_REPORT_NONE, String.valueOf(year), String.valueOf(month), String.valueOf(day));
    } else {
      int cnt = 0;
      int playerCount = list.size();
      long durationCount = 0L;
      List<Long> durations = new ArrayList<>(playerCount);
      for (RecordDo recordDo : list) {
        Long duration = recordDo.getDuration();
        durationCount += duration;
        durations.add(duration);
        builder.append(
            StringUtil.replace(
                DAILY_REPORT_INFO, recordDo.getPlayer(), DateTimeUtil.duration(duration)));
        if (++cnt == 40) {
          messages.add(builder.toString());
          builder = new StringBuilder();
          cnt = 0;
        }
      }
      messages.add(builder.toString());
      title =
          StringUtil.replace(
              DAILY_REPORT_TITLE,
              String.valueOf(year),
              String.valueOf(month),
              String.valueOf(day),
              String.valueOf(playerCount),
              DateTimeUtil.duration(durationCount / playerCount),
              DateTimeUtil.duration(median(durations)));
    }
    return reportAndReturn(title, messages);
  }

  public static String monthlyReport(int year, int month) {
    List<RecordDo> list = RecordService.list(year, month);
    String title;
    List<String> messages = new ArrayList<>(2);
    StringBuilder builder = new StringBuilder();
    if (list.isEmpty()) {
      title = StringUtil.replace(MONTHLY_REPORT_NONE, String.valueOf(year), String.valueOf(month));
    } else {
      int playerCount = list.size();
      long durationCount = 0L;
      List<Long> durations = new ArrayList<>(playerCount);
      Map<String, long[]> map = new HashMap<>(list.size() >>> 2);
      for (RecordDo recordDo : list) {
        String player = recordDo.getPlayer();
        Long duration = recordDo.getDuration();
        map.putIfAbsent(player, new long[2]);
        long[] info = map.get(player);
        info[0] += 1;
        info[1] += duration;
        durationCount += duration;
        durations.add(duration);
      }
      List<String> keys = map.keySet().stream().sorted().toList();
      int cnt = 0;
      for (String key : keys) {
        long[] info = map.get(key);
        builder.append(
            StringUtil.replace(
                MONTHLY_REPORT_INFO,
                key,
                String.valueOf(info[0]),
                DateTimeUtil.duration(info[1]),
                DateTimeUtil.duration(info[1] / info[0])));
        if (++cnt == 40) {
          messages.add(builder.toString());
          builder = new StringBuilder();
          cnt = 0;
        }
      }
      messages.add(builder.toString());
      title =
          StringUtil.replace(
              MONTHLY_REPORT_TITLE,
              String.valueOf(year),
              String.valueOf(month),
              String.valueOf(map.size()),
              String.valueOf(playerCount),
              DateTimeUtil.duration(durationCount / playerCount),
              DateTimeUtil.duration(median(durations)));
    }
    return reportAndReturn(title, messages);
  }

  private static String reportAndReturn(String title, List<String> messages) {
    String groupId = ConfigCache.getReportQqGroupId();
    if (messages.isEmpty()) {
      RobotUtil.sendGroupMessage(groupId, StringUtil.removeStyle(title));
      return title;
    }
    StringBuilder builder = new StringBuilder(title);
    for (String message : messages) {
      RobotUtil.sendGroupMessage(groupId, StringUtil.removeStyle(title.concat(message)));
      builder.append(message);
    }
    return builder.toString();
  }

  public static void sendRewardRemind(
      List<Map.Entry<Integer, Integer>> daysReward, List<Map.Entry<Long, Integer>> durationReward) {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      String player = onlinePlayer.getName();
      PlayerCache.Data playerData = PlayerCache.get(player);
      int daysOfMonth = playerData.getDaysOfMonth();
      int daysOfMonthReward = playerData.getDaysOfMonthReward();
      for (Map.Entry<Integer, Integer> entry : daysReward) {
        if (entry.getKey() > daysOfMonthReward && entry.getKey() <= daysOfMonth) {
          ChatUtil.commandButton(
              onlinePlayer,
              DAYS_REWARD_AVAILABLE,
              REWARD_AVAILABLE_HOVER,
              CommandConstant.ACTIVITY);
          onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
          break;
        }
      }
      long durationOfDay = playerData.getDurationOfDay();
      long durationOfDayReward = playerData.getDurationOfDayReward();
      for (Map.Entry<Long, Integer> entry : durationReward) {
        if (entry.getKey() > durationOfDayReward && entry.getKey() <= durationOfDay) {
          ChatUtil.commandButton(
              onlinePlayer,
              DURATION_REWARD_AVAILABLE,
              REWARD_AVAILABLE_HOVER,
              CommandConstant.ACTIVITY);
          onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
          break;
        }
      }
    }
  }

  private static Long median(List<Long> list) {
    int len = list.size();
    if (len == 0) {
      return 0L;
    }
    int mid = len >>> 1;
    Collections.sort(list);
    return (len & 1) == 1 ? list.get(mid) : (list.get(mid - 1) + list.get(mid)) >>> 1;
  }
}
