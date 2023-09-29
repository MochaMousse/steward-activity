package cc.mousse.steward.activity.cache;

import java.util.*;

/**
 * @author PhineasZ
 */
public class DailyCache {
  private static final Set<String> DATA_CACHE = new HashSet<>(64);

  private DailyCache() {}

  public static void add(String playerName) {
    DATA_CACHE.add(playerName.toLowerCase(Locale.ROOT));
  }

  public static boolean contains(String playerName) {
    return DATA_CACHE.contains(playerName);
  }

  public static void clear() {
    DATA_CACHE.clear();
  }
}
