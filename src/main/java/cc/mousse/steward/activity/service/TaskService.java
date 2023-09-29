package cc.mousse.steward.activity.service;

import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.cache.ConfigCache;
import cc.mousse.steward.activity.cache.PlayerCache;
import cc.mousse.steward.activity.util.DateTimeUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * @author PhineasZ
 */
public class TaskService extends BukkitRunnable {
  @Override
  public void run() {
    List<Map.Entry<Integer, Integer>> daysReward = ConfigCache.getDaysReward();
    List<Map.Entry<Long, Integer>> durationReward = ConfigCache.getDurationReward();
    if (BasicCache.getDay() != DateTimeUtil.day()) {
      PlayerCache.flush();
    } else if (!daysReward.isEmpty() || !durationReward.isEmpty()) {
      RobotService.sendRewardRemind(daysReward, durationReward);
    }
  }
}
