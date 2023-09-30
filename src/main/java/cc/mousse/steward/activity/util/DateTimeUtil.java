package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
public class DateTimeUtil {
  private DateTimeUtil() {}

  /** 获取该月第一天为星期几 */
  public static int firstDayOfMonth(int month) {
    Calendar calendar = new GregorianCalendar(Locale.CHINA);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, month - 1);
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  /** 该月天数 */
  public static int dayCountOfMonth(int month) {
    Calendar calendar = new GregorianCalendar(Locale.CHINA);
    calendar.set(Calendar.MONTH, month - 1);
    return calendar.getActualMaximum(Calendar.DATE);
  }

  /** 当前年份 */
  public static int year() {
    return Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
  }

  /** 当前月份 */
  public static int month() {
    return Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
  }

  public static int month(Date date) {
    return Integer.parseInt(new SimpleDateFormat(BasicCache.MONTH_FORMAT).format(date));
  }

  /** 当前日期 */
  public static int day() {
    return Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_MONTH);
  }

  public static int day(Date date) {
    return Integer.parseInt(new SimpleDateFormat(BasicCache.DAY_FORMAT).format(date));
  }

  public static int minute(long milliseconds) {
    return (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
  }

  public static String duration(long millisecond) {
    StringBuilder sb = new StringBuilder();
    long second = millisecond / 1000;
    int year = (int) (second / 31536000);
    if (year != 0) {
      sb.append(year).append(YEAR);
      second %= (year * 31536000L);
    }
    int month = (int) (second / 2592000);
    if (month != 0) {
      sb.append(month).append(MONTH);
      second %= (month * 2592000L);
    }
    int day = (int) (second / 86400);
    if (day != 0) {
      sb.append(day).append(DAY);
      second %= (day * 86400L);
    }
    int hour = (int) (second / 3600);
    if (hour != 0) {
      sb.append(hour).append(HOUR);
      second %= (hour * 3600L);
    }
    int minute = (int) (second / 60);
    if (minute != 0) {
      sb.append(minute).append(MINUTE);
      second %= (minute * 60L);
    }
    sb.append(second).append(SECOND);
    return sb.toString();
  }

  public static Date date() {
    return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date date(int year, int month, int day) {
    SimpleDateFormat format = new SimpleDateFormat(BasicCache.DATE_FORMAT);
    String str =
        String.valueOf(year)
            .concat(HYPHEN)
            .concat(String.valueOf(month))
            .concat(HYPHEN)
            .concat(String.valueOf(day));
    Date date = null;
    try {
      date = format.parse(str);
    } catch (ParseException e) {
      LogUtil.warn(e);
    }
    return date;
  }

  public static String date(long millisecond) {
    return new SimpleDateFormat(BasicCache.DATETIME_FORMAT).format(millisecond);
  }

  public static String date(Date date) {
    return new SimpleDateFormat(BasicCache.DATE_FORMAT).format(date);
  }
}
