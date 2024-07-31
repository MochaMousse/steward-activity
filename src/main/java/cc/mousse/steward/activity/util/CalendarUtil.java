package cc.mousse.steward.activity.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.Data;

/**
 * @author MochaMousse
 */
public class CalendarUtil {
  private static final Map<String, Day> CACHE = HashMap.newHashMap(16);
  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
  private static final String[] CHINESE_NUMERAL_1 =
      new String[] {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"};
  private static final String[] CHINESE_NUMERAL_2 =
      new String[] {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
  private static final long[] LUNAR_INFO =
      new long[] {
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
      };
  private static final String[] SOLAR_TERM =
      new String[] {
        "小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋",
        "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
      };
  private static final int[] STERM_INFO =
      new int[] {
        0, 21208, 42467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693,
        263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532,
        504758
      };
  private static final String[] GAN =
      new String[] {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
  private static final String[] ZHI =
      new String[] {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
  private static final String[] ANIMAL =
      new String[] {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
  private static final String[] CHINESE_TEN = {"初", "十", "廿", "卅"};

  /** 国历节日 *表示放假日 */
  private static final String[] FESTIVAL =
      new String[] {
        "0101*元旦",
        "0214情人节",
        "0308妇女节",
        "0312植树节",
        "0401愚人节",
        "0422世界地球日",
        "0501*劳动节",
        "0504青年节",
        "0520情人节",
        "0601儿童节",
        "0701建党节",
        "0707抗日战争纪念日",
        "0801八一建军节",
        "0910教师节",
        "0917国际和平日",
        "0918九·一八事变纪念日",
        "1001*国庆节",
        "1010辛亥革命纪念日",
        "1031万圣节",
        "1111光棍节",
        "1212西安事变纪念日",
        "1213南京大屠杀",
        "1224平安夜",
        "1225圣诞节"
      };

  /** 农历节日 *表示放假日 */
  private static final String[] LUNAR_FESTIVAL =
      new String[] {
        "0101*春节",
        "0102*大年初二",
        "0103*大年初三",
        "0104*大年初四",
        "0105*大年初五",
        "0106*大年初六",
        "0107*大年初七",
        "0115元宵节",
        "0202龙抬头",
        "0404寒食节",
        "0505*端午节",
        "0624彝族火把节",
        "0707七夕",
        "0714鬼节(南方)",
        "0815*中秋节",
        "0909重阳节",
        "1001祭祖节",
        "1208腊八节",
        "1223小年",
        "1229*腊月二十九",
        "1230*除夕"
      };

  private CalendarUtil() {}

  /** 年中的总天数 */
  private static int lunarDayCountOfYear(int year) {
    int sum = 348;
    for (int i = 0x8000; i > 0x8; i >>= 1) {
      if ((LUNAR_INFO[year - 1900] & i) != 0) {
        sum += 1;
      }
    }
    return (sum + lunarDayCountOfLeapMonth(year));
  }

  /** 年中闰月的天数 */
  private static int lunarDayCountOfLeapMonth(int year) {
    int cnt = 0;
    if (leapMonthOfYear(year) != 0) {
      cnt = (LUNAR_INFO[year - 1900] & 0x10000) != 0 ? 30 : 29;
    }
    return cnt;
  }

  /** 年中哪个月为闰月 没闰: 0 */
  private static int leapMonthOfYear(int year) {
    return (int) (LUNAR_INFO[year - 1900] & 0xf);
  }

  /** 年中某月的总天数 */
  private static int dayCountOfYearMonth(int year, int month) {
    return (LUNAR_INFO[year - 1900] & (0x10000 >> month)) == 0 ? 29 : 30;
  }

  /** 年的生肖 */
  private static String animalOfYear(int year) {
    return ANIMAL[(year - 4) % 12];
  }

  /** 月日的干支, 甲子: 0 */
  private static String ganZhiOfYearOffset(int offset) {
    return (GAN[offset % 10] + ZHI[offset % 12]);
  }

  private static String dayOfWeek(int dow) {
    return switch (dow) {
      case 1 -> "星期天";
      case 2 -> "星期一";
      case 3 -> "星期二";
      case 4 -> "星期三";
      case 5 -> "星期四";
      case 6 -> "星期五";
      default -> "星期六";
    };
  }

  private static String lunarDay(int day) {
    String lunarDay = "";
    if (day <= 30) {
      switch (day) {
        case 10 -> lunarDay = "初十";
        case 20 -> lunarDay = "二十";
        case 30 -> lunarDay = "三十";
        default -> {
          var n = day % 10 == 0 ? 9 : day % 10 - 1;
          lunarDay = CHINESE_TEN[day / 10] + CHINESE_NUMERAL_2[n];
        }
      }
    }
    return lunarDay;
  }

  // 年中第n个节气为几日(从0小寒起算)
  private static int dayOfxTermByYearNum(int year, int num) throws ParseException {
    long utcTime2 =
        new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.CHINA)
            .parse("1900-01-06 02:05:00")
            .getTime();
    BigDecimal time2 =
        new BigDecimal("31556925974.7")
            .multiply(new BigDecimal(year - 1900))
            .add(new BigDecimal(STERM_INFO[num]).multiply(BigDecimal.valueOf(60000L)));
    BigDecimal time = time2.add(BigDecimal.valueOf(utcTime2));
    Date offDate = new Date(time.longValue());
    java.util.Calendar cal = java.util.Calendar.getInstance(Locale.CHINA);
    cal.setTime(offDate);
    // 日期从0算起
    return cal.get(java.util.Calendar.DATE);
  }

  /** 年月日对应的农历 */
  public static Day lunarDayOfDate(int sYear, int sMonth, int sDay) throws ParseException {
    String key = String.format("%s-%s-%s 08:00:00", sYear, sMonth, sDay);
    if (CACHE.containsKey(key)) {
      return CACHE.get(key);
    }
    Day xDay = new Day(sYear, sMonth, sDay);
    Date baseDate = null;
    Date thatDate = null;
    try {
      thatDate = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.CHINA).parse(key);
      baseDate =
          new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.CHINA).parse("1900-1-31 00:00:00");
    } catch (ParseException e) {
      LogUtil.warn(e);
    }
    if (baseDate == null || thatDate == null) {
      return xDay;
    }
    // 星期几
    java.util.Calendar calendar = java.util.Calendar.getInstance(Locale.CHINA);
    calendar.setTime(thatDate);
    boolean leap;
    int dow = calendar.get(java.util.Calendar.DAY_OF_WEEK);
    int year;
    int month;
    int day;
    int yearCyl;
    int monCyl;
    int dayCyl;
    int leapMonth;
    // 求出和1900年1月31日相差的天数
    int offset = (int) ((thatDate.getTime() - baseDate.getTime()) / 86400000L);
    dayCyl = offset + 40;
    monCyl = 14;
    // 用offset减去每农历年的天数
    // 计算当天是农历第几天
    // i最终结果是农历的年份
    // offset是当年的第几天
    int iYear;
    int daysOfYear = 0;
    for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
      daysOfYear = lunarDayCountOfYear(iYear);
      offset -= daysOfYear;
      monCyl += 12;
    }
    if (offset < 0) {
      offset += daysOfYear;
      iYear--;
      monCyl -= 12;
    }
    // 农历年份
    year = iYear;
    yearCyl = iYear - 1864;
    // 闰哪个月,1-12
    leapMonth = leapMonthOfYear(iYear);
    leap = false;
    // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
    int iMonth;
    int daysOfMonth = 0;
    for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
      // 闰月
      if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
        iMonth--;
        leap = true;
        daysOfMonth = lunarDayCountOfLeapMonth(year);
      } else {
        daysOfMonth = dayCountOfYearMonth(year, iMonth);
      }
      offset -= daysOfMonth;
      // 解除闰月
      if (leap && iMonth == (leapMonth + 1)) {
        leap = false;
      }
      if (!leap) {
        monCyl++;
      }
    }
    // offset为0时，并且刚才计算的月份是闰月，要校正
    if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
      if (leap) {
        leap = false;
      } else {
        leap = true;
        iMonth--;
        monCyl--;
      }
    }
    // offset小于0时，也要校正
    if (offset < 0) {
      offset += daysOfMonth;
      iMonth--;
      monCyl--;
    }
    month = iMonth;
    day = offset + 1;
    // 国历节日
    for (String f : FESTIVAL) {
      if (Integer.parseInt(f.substring(0, 2)) == sMonth
          && sDay == Integer.parseInt(f.substring(2, 4))) {
        xDay.setSolarFestival(f.substring(4));
        break;
      }
    }
    // 农历节日
    for (String f : LUNAR_FESTIVAL) {
      boolean isThisMonth = Integer.parseInt(f.substring(0, 2)) == month;
      boolean flag = false;
      if (isThisMonth && day == Integer.parseInt(f.substring(2, 4))) {
        xDay.setLunarFestival(f.substring(4));
        flag = true;
      } else if (month == 12 && isThisMonth && dayCountOfYearMonth(year, month) == day) {
        // 除夕
        xDay.setLunarFestival("*除夕");
        flag = true;
      }
      if (flag) {
        break;
      }
    }
    // 每个月的两个节气
    int term1 = dayOfxTermByYearNum(sYear, (sMonth - 1) * 2);
    int term2 = dayOfxTermByYearNum(sYear, (sMonth - 1) * 2 + 1);
    String termName1 = SOLAR_TERM[(sMonth - 1) * 2];
    String termName2 = SOLAR_TERM[(sMonth - 1) * 2 + 1];
    // 设置数据
    if (sDay == term1) {
      xDay.setSolarTerms(termName1);
    } else if (sDay == term2) {
      xDay.setSolarTerms(termName2);
    }
    xDay.setLeap(leap);
    xDay.setLunarYear(ganZhiOfYearOffset(yearCyl) + animalOfYear(year) + "年");
    xDay.setLunarMonth(ganZhiOfYearOffset(monCyl) + "月");
    xDay.setLunarDay(ganZhiOfYearOffset(dayCyl) + "日");
    xDay.setLunarTime(
        "农历" + (xDay.isLeap() ? "闰" : "") + CHINESE_NUMERAL_1[month - 1] + "月" + lunarDay(day));
    xDay.setDayOfWeek(dayOfWeek(dow));
    CACHE.put(key, xDay);
    return xDay;
  }

  @Data
  public static class Day {
    // 公历
    private final int year;
    private final int month;
    private final int day;
    private String dayOfWeek;
    // 农历
    private String lunarYear;
    private String lunarMonth;
    private String lunarDay;
    private String lunarTime;
    private boolean leap;
    // 节日
    private String lunarFestival;
    private String solarFestival;
    // 节气
    private String solarTerms;

    public Day(int year, int month, int day) {
      this.year = year;
      this.month = month;
      this.day = day;
    }
  }
}
