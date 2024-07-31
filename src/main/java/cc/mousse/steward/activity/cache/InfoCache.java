package cc.mousse.steward.activity.cache;

import cc.mousse.steward.activity.bean.InfoDo;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MochaMousse
 */
public class InfoCache {
  private static final Map<String, InfoDo> INFO_CACHE = new ConcurrentHashMap<>(2);

  private InfoCache() {}

  public static boolean contains(String playerName) {
    return INFO_CACHE.containsKey(playerName.toLowerCase(Locale.ROOT));
  }

  public static InfoDo get(String playerName) {
    return INFO_CACHE.get(playerName.toLowerCase(Locale.ROOT));
  }

  public static void put(String playerName, InfoDo infoDo) {
    INFO_CACHE.put(playerName.toLowerCase(Locale.ROOT), infoDo);
  }

  public static void remove(String playerName) {
    INFO_CACHE.remove(playerName.toLowerCase(Locale.ROOT));
  }

  public static void clear() {
    INFO_CACHE.clear();
  }
}
