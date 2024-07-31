package cc.mousse.steward.activity.service;

import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.util.DataSourceUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.intellij.lang.annotations.Language;

/**
 * @author MochaMousse
 */
public class RecordService {
  private RecordService() {}

  public static void init() {
    @Language(BasicCache.MYSQL)
    String sql =
        """
                CREATE TABLE IF NOT EXISTS record
                (
                    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
                    date     DATE        NOT NULL,
                    state    TINYINT     NOT NULL,
                    player   VARCHAR(64) NOT NULL,
                    duration BIGINT      NULL
                )
                """;
    DataSourceUtil.add(sql);
  }

  public static List<RecordDo> list(String player, int year) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM record WHERE LOWER(player) = ? AND YEAR(date) = ? ORDER BY date";
    return DataSourceUtil.list(RecordDo.class, sql, player.toLowerCase(Locale.ROOT), year);
  }

  public static List<RecordDo> list(String player) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM record WHERE LOWER(player) = ?";
    return DataSourceUtil.list(RecordDo.class, sql, player.toLowerCase(Locale.ROOT));
  }

  public static List<RecordDo> list(int year, int month, int day) {
    @Language(BasicCache.MYSQL)
    String sql =
        "SELECT * FROM record WHERE YEAR(date) = ? AND MONTH(date) = ? AND DAY(date) = ? AND state != 2";
    return DataSourceUtil.list(RecordDo.class, sql, year, month, day);
  }

  public static List<RecordDo> list(int year, int month) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM record WHERE YEAR(date) = ? AND MONTH(date) = ? AND state != 2";
    return DataSourceUtil.list(RecordDo.class, sql, year, month);
  }

  public static List<String> topDuration(int year, int month, int day) {
    @Language(BasicCache.MYSQL)
    String sql =
        """
                SELECT player
                FROM (SELECT DISTINCT player, RANK() OVER (ORDER BY duration DESC) AS rk
                      FROM record
                      WHERE YEAR(date) = ?
                        AND MONTH(date) = ?
                        AND DAY(date) = ?) AS t1
                WHERE rk = 1;
           """;
    return DataSourceUtil.list(sql, year, month, day);
  }

  public static List<String> topDays(int year, int month) {
    @Language(BasicCache.MYSQL)
    String sql =
        """
                SELECT player
                FROM (SELECT player, RANK() OVER (ORDER BY cnt DESC) AS rk
                        FROM (SELECT player, COUNT(1) AS cnt
                                FROM record
                                WHERE YEAR(date) = ?
                                AND MONTH(date) = ?
                                GROUP BY player) AS t1) AS t2
                WHERE rk = 1;
            """;
    return DataSourceUtil.list(sql, year, month);
  }

  public static RecordDo one(String player, Date date) {
    @Language(BasicCache.MYSQL)
    String sql = "SELECT * FROM record WHERE LOWER(player) = ? AND date = ? limit 1";
    return DataSourceUtil.one(
        RecordDo.class, sql, player.toLowerCase(Locale.ROOT), DateTimeUtil.date(date));
  }

  public static List<Integer> year(String player) {
    @Language(BasicCache.MYSQL)
    String sql =
        """
            SELECT CAST(year AS CHAR)
            FROM (SELECT YEAR(date) AS year
                  FROM record
                  WHERE LOWER(player) = ?
                  GROUP BY YEAR(date)) AS t1
            """;
    return DataSourceUtil.list(sql, player.toLowerCase(Locale.ROOT)).stream()
        .map(Integer::valueOf)
        .toList();
  }

  public static List<Integer> month(String player, int year) {
    @Language(BasicCache.MYSQL)
    String sql =
        """
            SELECT CAST(month AS CHAR)
            FROM (SELECT MONTH(date) AS month
                  FROM record
                  WHERE LOWER(player) = ?
                    AND YEAR(date) = ?
                  GROUP BY MONTH(date)) AS t1
            """;
    return DataSourceUtil.list(sql, player.toLowerCase(Locale.ROOT), year).stream()
        .map(Integer::valueOf)
        .toList();
  }

  public static List<Integer> day(String player, int year, int month) {
    @Language(BasicCache.MYSQL)
    String sql =
        """
                SELECT CAST(day AS CHAR)
                FROM (SELECT DAY(date) AS day
                      FROM record
                      WHERE LOWER(player) = ?
                        AND YEAR(date) = ?
                        AND MONTH(date) = ?
                      GROUP BY DAY(date)) t1
                """;
    return DataSourceUtil.list(sql, player.toLowerCase(Locale.ROOT), year, month).stream()
        .map(Integer::valueOf)
        .toList();
  }

  public static void flush(RecordDo recordDo) {
    if (recordDo.getId() == null) {
      add(recordDo);
    } else {
      update(recordDo);
    }
  }

  public static void add(RecordDo recordDo) {
    @Language(BasicCache.MYSQL)
    String sql = "INSERT INTO record(date, state, player, duration) VALUES (?, ?, ?, ?)";
    DataSourceUtil.add(
        sql, recordDo.getDate(), recordDo.getState(), recordDo.getPlayer(), recordDo.getDuration());
  }

  public static void update(RecordDo recordDo) {
    @Language(BasicCache.MYSQL)
    String sql = "UPDATE record SET date = ?, state = ?, player = ?, duration = ? WHERE id = ?";
    DataSourceUtil.update(
        sql,
        recordDo.getDate(),
        recordDo.getState(),
        recordDo.getPlayer(),
        recordDo.getDuration(),
        recordDo.getId());
  }
}
