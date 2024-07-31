package cc.mousse.steward.activity.cache;

import cc.mousse.steward.activity.Application;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import me.yic.xconomy.api.XConomyAPI;

import static cc.mousse.steward.activity.constant.TextConstant.*;
import static cc.mousse.steward.activity.constant.StyleConstant.*;

/**
 * @author MochaMousse
 */
public class BasicCache {
  @Getter @Setter private static volatile int day;
  @Getter @Setter private static volatile int month;
  @Getter @Setter private static volatile int year;
  @Getter @Setter private static Application instance;
  @Getter @Setter private static XConomyAPI xConomyAPI;
  public static final String NAME = "steward-activity";
  public static final String DISPLAY_NAME = "签到宝";
  public static final String CURRENCY_NAME = "咕咕币";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String MONTH_FORMAT = "MM";
  public static final String DAY_FORMAT = "dd";
  public static final String JSON_FORMAT = "application/json; charset=utf-8";
  public static final String PREFIX =
      GRAY.concat(BRACKET_LEFT)
          .concat(GOLD)
          .concat(BasicCache.DISPLAY_NAME)
          .concat(GRAY)
          .concat(BRACKET_RIGHT)
          .concat(WHITE)
          .concat(BLANK);
  public static final DruidDataSource DATA_SOURCE = new DruidDataSource();
  public static final Gson GSON =
      new GsonBuilder()
          .setDateFormat(DATE_FORMAT)
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create();
  public static final String MYSQL = "MySQL";

  private BasicCache() {}
}
