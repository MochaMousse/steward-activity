package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
@Slf4j
public class LogUtil {
  private static final Logger LOG = BasicCache.getInstance().getLogger();

  private LogUtil() {}

  public static void warn(Exception e) {
    LOG.severe(e.getMessage());
    log.warn(BLANK, e);
  }

  public static void warn(String message) {
    LOG.severe(message);
  }

  public static void info(String message) {
    LOG.info(message);
  }
}
