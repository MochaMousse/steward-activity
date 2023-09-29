package cc.mousse.steward.activity.service;

import cc.mousse.steward.activity.bean.InfoDo;
import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.util.DataSourceUtil;
import org.intellij.lang.annotations.Language;

import java.util.List;

/**
 * @author PhineasZ
 */
public class InfoService {
  private InfoService() {}

  public static void init() {
    @Language(BasicCache.MYSQL)
    String sql =
        """
                CREATE TABLE IF NOT EXISTS info
                (
                    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
                    player                 VARCHAR(64) NOT NULL,
                    chance                 INT         NULL,
                    last_login             DATE        NULL,
                    days_of_month          INT         NULL,
                    days_of_total          INT         NULL,
                    duration_of_total      BIGINT      NULL,
                    days_of_month_reward   INT         NULL,
                    duration_of_day_reward BIGINT      NULL
                )
                """;
    DataSourceUtil.add(sql);
  }

  public static InfoDo one(String player) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM info WHERE LOWER(player) = ?";
    return DataSourceUtil.one(InfoDo.class, sql, player.toLowerCase());
  }

  public static List<InfoDo> list(String player) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM info WHERE LOWER(player) = ?";
    return DataSourceUtil.list(InfoDo.class, sql, player.toLowerCase());
  }

  public static void flush(InfoDo infoDo) {
    if (infoDo.getId() == null) {
      add(infoDo);
    } else {
      update(infoDo);
    }
  }

  public static void add(InfoDo infoDo) {
    @Language(BasicCache.MYSQL)
    String sql =
        "INSERT INTO info(player, chance, last_login, days_of_month, days_of_total, duration_of_total, days_of_month_reward, duration_of_day_reward) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    DataSourceUtil.add(
        sql,
        infoDo.getPlayer(),
        infoDo.getChance(),
        infoDo.getLastLogin(),
        infoDo.getDaysOfMonth(),
        infoDo.getDaysOfTotal(),
        infoDo.getDurationOfTotal(),
        infoDo.getDaysOfMonthReward(),
        infoDo.getDurationOfDayReward());
  }

  public static void update(InfoDo infoDo) {
    @Language(BasicCache.MYSQL)
    String sql =
        "UPDATE info SET player = ?, chance = ?, last_login = ?, days_of_month = ?, days_of_total = ?, duration_of_total = ?, days_of_month_reward = ?, duration_of_day_reward = ? WHERE id = ?";
    DataSourceUtil.update(
        sql,
        infoDo.getPlayer(),
        infoDo.getChance(),
        infoDo.getLastLogin(),
        infoDo.getDaysOfMonth(),
        infoDo.getDaysOfTotal(),
        infoDo.getDurationOfTotal(),
        infoDo.getDaysOfMonthReward(),
        infoDo.getDurationOfDayReward(),
        infoDo.getId());
  }
}
