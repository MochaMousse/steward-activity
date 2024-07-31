package cc.mousse.steward.activity.service;

import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.cache.ConfigCache;
import cc.mousse.steward.activity.cache.PlayerCache;
import cc.mousse.steward.activity.component.Handler;
import cc.mousse.steward.activity.util.DateTimeUtil;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author MochaMousse
 */
public class TaskService extends BukkitRunnable {
  @Override
  public void run() {
    List<Map.Entry<Integer, Integer>> daysReward = ConfigCache.getDaysReward();
    List<Map.Entry<Long, Integer>> durationReward = ConfigCache.getDurationReward();
    if (BasicCache.getDay() != DateTimeUtil.day()) {
      PlayerCache.flush();
    } else if (!daysReward.isEmpty() || !durationReward.isEmpty()) {
      if (ConfigCache.isAutoMode()) {
        Handler handler = new Handler();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          handler.daysReward(onlinePlayer, true);
          handler.durationReward(onlinePlayer, true);
        }
      } else {
        RobotService.sendRewardRemind(daysReward, durationReward);
      }
    }
  }
}
