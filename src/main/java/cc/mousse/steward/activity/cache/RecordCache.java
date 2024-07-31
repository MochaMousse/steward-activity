package cc.mousse.steward.activity.cache;

import cc.mousse.steward.activity.bean.RecordDo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MochaMousse
 */
public class RecordCache {
  private RecordCache() {}

  private static final Map<String, Map<Integer, Map<Integer, Map<Integer, RecordDo>>>>
      RECORD_CACHE = new ConcurrentHashMap<>(8);

  public static boolean containsPlayer(String playerName) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    return RECORD_CACHE.containsKey(playerName);
  }

  public static boolean containsYear(String playerName, Integer year) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    return containsPlayer(playerName)
        && RECORD_CACHE.get(playerName).containsKey(year)
        && !RECORD_CACHE.get(playerName).get(year).isEmpty();
  }

  public static boolean containsMonth(String playerName, Integer year, Integer month) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    return containsYear(playerName, year)
        && RECORD_CACHE.get(playerName).get(year).containsKey(month)
        && !RECORD_CACHE.get(playerName).get(year).get(month).isEmpty();
  }

  public static boolean containsDay(String playerName, Integer year, Integer month, Integer day) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    return containsMonth(playerName, year, month)
        && RECORD_CACHE.get(playerName).get(year).get(month).containsKey(day)
        && RECORD_CACHE.get(playerName).get(year).get(month).get(day) != null;
  }

  public static List<Integer> getYears(String playerName) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    put(playerName, null, null, null, null);
    return RECORD_CACHE.get(playerName).keySet().stream().toList();
  }

  public static List<Integer> getMonths(String playerName, Integer year) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    put(playerName, year, null, null, null);
    return RECORD_CACHE.get(playerName).get(year).keySet().stream().toList();
  }

  public static List<Integer> getDays(String playerName, Integer year, Integer month) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    put(playerName, year, month, null, null);
    return RECORD_CACHE.get(playerName).get(year).get(month).keySet().stream().toList();
  }

  public static void putYears(String playerName, List<Integer> years) {
    if (!years.isEmpty()) {
      playerName = playerName.toLowerCase(Locale.ROOT);
      put(playerName, null, null, null, null);
      Map<Integer, Map<Integer, Map<Integer, RecordDo>>> map = RECORD_CACHE.get(playerName);
      years.forEach(year -> map.putIfAbsent(year, HashMap.newHashMap(2)));
    }
  }

  public static void putMonths(String playerName, Integer year, List<Integer> months) {
    if (!months.isEmpty()) {
      playerName = playerName.toLowerCase(Locale.ROOT);
      put(playerName, year, null, null, null);
      Map<Integer, Map<Integer, RecordDo>> map = RECORD_CACHE.get(playerName).get(year);
      months.forEach(month -> map.putIfAbsent(month, HashMap.newHashMap(6)));
    }
  }

  public static void putDays(String playerName, Integer year, Integer month, List<Integer> days) {
    if (!days.isEmpty()) {
      playerName = playerName.toLowerCase(Locale.ROOT);
      put(playerName, year, month, null, null);
      Map<Integer, RecordDo> map = RECORD_CACHE.get(playerName).get(year).get(month);
      days.forEach(day -> map.putIfAbsent(day, null));
    }
  }

  public static RecordDo get(String playerName, Integer year, Integer month, Integer day) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    return containsDay(playerName, year, month, day)
        ? RECORD_CACHE.get(playerName).get(year).get(month).get(day)
        : null;
  }

  public static void put(
      String playerName, Integer year, Integer month, Integer day, RecordDo recordDo) {
    playerName = playerName.toLowerCase(Locale.ROOT);
    RECORD_CACHE.putIfAbsent(playerName, HashMap.newHashMap(2));
    Map<Integer, Map<Integer, Map<Integer, RecordDo>>> yearMap = RECORD_CACHE.get(playerName);
    if (year != null) {
      yearMap.putIfAbsent(year, HashMap.newHashMap(6));
      Map<Integer, Map<Integer, RecordDo>> monthMap = yearMap.get(year);
      if (month != null) {
        monthMap.putIfAbsent(month, HashMap.newHashMap(12));
        Map<Integer, RecordDo> dayMap = monthMap.get(month);
        if (day != null) {
          dayMap.putIfAbsent(day, recordDo);
        }
      }
    }
  }

  public static void clear() {
    RECORD_CACHE.clear();
  }
}
