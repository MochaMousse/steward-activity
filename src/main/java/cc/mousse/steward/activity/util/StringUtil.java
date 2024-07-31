package cc.mousse.steward.activity.util;

import static cc.mousse.steward.activity.constant.TextConstant.*;

import java.util.regex.Pattern;

/**
 * @author MochaMousse
 */
public class StringUtil {
  private static final Pattern PATTERN = Pattern.compile(SECTION.concat(DOT));

  private StringUtil() {}

  public static String replace(String str, String... args) {
    for (String arg : args) {
      str = str.replaceFirst(REPLACE, arg);
    }
    return str;
  }

  public static String removeStyle(String str) {
    return PATTERN.matcher(str).replaceAll(EMPTY);
  }
}
