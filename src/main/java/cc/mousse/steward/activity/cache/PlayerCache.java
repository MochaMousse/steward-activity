package cc.mousse.steward.activity.cache;

import cc.mousse.steward.activity.bean.InfoDo;
import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.constant.StateEnum;
import cc.mousse.steward.activity.service.InfoService;
import cc.mousse.steward.activity.service.RecordService;
import cc.mousse.steward.activity.service.RobotService;
import cc.mousse.steward.activity.util.DateTimeUtil;
import cc.mousse.steward.activity.util.LogUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static cc.mousse.steward.activity.constant.TextConstant.*;
import static cc.mousse.steward.activity.constant.StyleConstant.*;

/**
 * @author PhineasZ
 */
public class PlayerCache {
  private static final Map<Integer, ConcurrentHashMap<String, Data>> DATA_CACHE =
      new ConcurrentHashMap<>(2);

  private PlayerCache() {}

  public static Map<String, Data> get() {
    return DATA_CACHE.get(init());
  }

  public static boolean contains(String playerName) {
    return DATA_CACHE.get(init()).containsKey(playerName.toLowerCase(Locale.ROOT));
  }

  public static Data get(String playerName) {
    int today = init();
    String key = playerName.toLowerCase(Locale.ROOT);
    if (!DATA_CACHE.get(today).containsKey(key)) {
      flush();
    }
    return DATA_CACHE.get(today).get(key);
  }

  public static void put(Data playerData) {
    DATA_CACHE.get(init()).putIfAbsent(playerData.getPlayer().toLowerCase(Locale.ROOT), playerData);
  }

  public static void close() {
    DATA_CACHE
        .values()
        .forEach(
            map -> {
              for (Data playerData : map.values()) {
                flush(playerData);
              }
            });
  }

  public static void flush() {
    int today = DateTimeUtil.day();
    int thisMonth = DateTimeUtil.month();
    if (today == BasicCache.getDay()) {
      return;
    }
    int cacheDay = BasicCache.getDay();
    int cacheMonth = BasicCache.getMonth();
    int cacheYear = BasicCache.getYear();
    if (DATA_CACHE.containsKey(cacheDay)) {
      for (Data playerData : DATA_CACHE.get(cacheDay).values()) {
        if (playerData != null) {
          String playerName = playerData.getPlayer();
          flush(cacheDay, playerName);
          init(playerName);
        }
      }
      DATA_CACHE.remove(cacheDay);
    }
    CompletableFuture.runAsync(
        () -> {
          try {
            RobotService.sendMessage(cacheYear, cacheMonth, cacheDay);
          } catch (Exception e) {
            LogUtil.warn(e);
          }
          try {
            RobotService.sendReport(cacheYear, cacheMonth, cacheDay);
          } catch (Exception e) {
            LogUtil.warn(e);
          }
        });
    BasicCache.setDay(today);
    BasicCache.setMonth(thisMonth);
    BasicCache.setYear(DateTimeUtil.year());
    DailyCache.clear();
  }

  public static void flush(String playerName) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    flush(get(playerName));
    DATA_CACHE.get(init()).remove(playerName);
  }

  public static void flush(Integer key, String playerName) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    Map<String, Data> cache = DATA_CACHE.get(key);
    if (cache.containsKey(playerName)) {
      flush(cache.get(playerName));
      cache.remove(playerName);
    }
  }

  public static void flush(Data playerData) {
    String playerName = playerData.getPlayer();
    Date date = new Date(playerData.getLoginTime());
    RecordDo recordDo = RecordService.one(playerName, date);
    RecordService.flush(
        new RecordDo(
            recordDo == null ? null : recordDo.getId(),
            date,
            playerData.getState().getCode(),
            playerName,
            playerData.getDurationOfDay()));
    InfoDo infoDo = InfoService.one(playerName);
    Long id = infoDo == null ? null : infoDo.getId();
    InfoService.flush(
        new InfoDo(
            id,
            playerName,
            date,
            playerData.getChance(),
            playerData.getDaysOfMonth(),
            playerData.getDaysOfTotal(),
            playerData.getDurationOfTotal(),
            playerData.getDaysOfMonthReward(),
            playerData.getDurationOfDayReward()));
  }

  public static void init(String playerName) {
    put(new Data(playerName));
  }

  public static int init() {
    int today = DateTimeUtil.day();
    DATA_CACHE.putIfAbsent(today, new ConcurrentHashMap<>(8));
    return today;
  }

  @lombok.Data
  public static class Data {
    private final String player;
    private volatile int chance;
    private volatile long loginTime;
    private volatile int daysOfMonth = 0;
    private volatile int daysOfTotal = 0;
    private volatile long durationOfDay = 0;
    private volatile long durationOfTotal = -1;
    private volatile int daysOfMonthReward = -1;
    private volatile long durationOfDayReward = -1L;
    private volatile StateEnum state = StateEnum.UNSIGNED;
    private final Map<Integer, Set<Integer>> signRecord = new HashMap<>(12);

    private Data(String player) {
      this.player = player;
      chance = ConfigCache.getChance();
      loginTime = System.currentTimeMillis();
      int maxMonth = 12;
      for (int i = 1; i <= maxMonth; i++) {
        signRecord.putIfAbsent(i, new HashSet<>(32));
      }
      InfoDo infoDo = InfoService.one(player);
      if (infoDo != null) {
        load(infoDo);
      }
    }

    private void load(InfoDo infoDo) {
      daysOfTotal = infoDo.getDaysOfTotal();
      durationOfTotal = infoDo.getDurationOfTotal();
      Date lastLogin = infoDo.getLastLogin();
      if (DateTimeUtil.month(lastLogin) == DateTimeUtil.month()) {
        chance = infoDo.getChance();
        daysOfMonth = infoDo.getDaysOfMonth();
        daysOfMonthReward = infoDo.getDaysOfMonthReward();
        RecordDo recordDo = RecordService.one(player, DateTimeUtil.date());
        if (recordDo != null) {
          state = StateEnum.ofCode(recordDo.getState());
          durationOfDay = recordDo.getDuration();
          durationOfDayReward = infoDo.getDurationOfDayReward();
        }
      }
      RecordService.list(player, DateTimeUtil.year())
          .forEach(
              log -> {
                if (!StateEnum.UNSIGNED.getCode().equals(log.getState())) {
                  Date date = log.getDate();
                  signRecord.get(DateTimeUtil.month(date)).add(DateTimeUtil.day(date));
                }
              });
    }

    public long getDurationOnline() {
      return System.currentTimeMillis() - loginTime;
    }

    public long getDurationOfDay() {
      return durationOfDay + getDurationOnline();
    }

    public long getDurationOfTotal() {
      return durationOfTotal + getDurationOnline();
    }

    @Override
    public String toString() {
      int daysReward = this.getDaysOfMonthReward();
      long durationReward = this.getDurationOfDayReward();
      return DARK_AQUA
          .concat(this.getPlayer())
          .concat(TOSTRING_START)
          .concat(LOGIN_TIME)
          .concat(TOSTRING_COLON)
          .concat(DateTimeUtil.date(this.getLoginTime()))
          .concat(TOSTRING_SPLIT)
          .concat(SIGN_CHANCE)
          .concat(TOSTRING_COLON)
          .concat(String.valueOf(this.getChance()))
          .concat(TOSTRING_SPLIT)
          .concat(SIGN_STATE)
          .concat(TOSTRING_COLON)
          .concat(this.getState().getMsg())
          .concat(TOSTRING_SPLIT)
          .concat(DAYS_OF_MONTH)
          .concat(TOSTRING_COLON)
          .concat(String.valueOf(this.getDaysOfMonth()))
          .concat(TOSTRING_SPLIT)
          .concat(DAYS_OF_TOTAL)
          .concat(TOSTRING_COLON)
          .concat(String.valueOf(this.getDaysOfTotal()))
          .concat(TOSTRING_SPLIT)
          .concat(DURATION_OF_DAY)
          .concat(TOSTRING_COLON)
          .concat(DateTimeUtil.duration(this.getDurationOfDay()))
          .concat(TOSTRING_SPLIT)
          .concat(DURATION_OF_TOTAL)
          .concat(TOSTRING_COLON)
          .concat(DateTimeUtil.duration(this.getDurationOfTotal()))
          .concat(TOSTRING_SPLIT)
          .concat(DAYS_REWARD_TAKEN)
          .concat(TOSTRING_COLON)
          .concat(daysReward == -1 ? NULL : String.valueOf(this.getDaysOfMonthReward()).concat(DAY))
          .concat(TOSTRING_SPLIT)
          .concat(DURATION_REWARD_TAKEN)
          .concat(TOSTRING_COLON)
          .concat(
              durationReward == -1
                  ? NULL
                  : String.valueOf(durationReward / 1000 / 60).concat(MINUTE))
          .concat(TOSTRING_NED);
    }
  }
}
