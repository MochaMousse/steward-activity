package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

/**
 * @author PhineasZ
 */
@Slf4j
public class LogUtil {
  private static final Logger LOG = BasicCache.getInstance().getLogger();

  private LogUtil() {}

  public static void error(Exception e) {
    LOG.severe(e.getMessage());
    log.warn("日志发送错误: {}", e.getMessage(), e);
  }

  public static void error(String message) {
    LOG.severe(message);
  }
}
