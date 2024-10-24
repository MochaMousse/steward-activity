package cc.mousse.steward.activity.util;

import static cc.mousse.steward.activity.constant.TextConstant.*;

import cc.mousse.steward.activity.cache.BasicCache;
import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author MochaMousse
 */
public class BalanceUtil {
  private BalanceUtil() {}

  public static void add(Player player, int value) {
    BasicCache.getXConomyAPI()
        .changePlayerBalance(
            player.getUniqueId(),
            player.getName(),
            BigDecimal.valueOf(value),
            true,
            BasicCache.NAME);
    String message = BasicCache.PREFIX.concat(String.format(GET_X_REWARD_INFO, value));
    player.sendMessage(message);
  }

  public static void add(OfflinePlayer offlinePlayer, int value) {
    BasicCache.getXConomyAPI()
        .changePlayerBalance(
            offlinePlayer.getUniqueId(),
            offlinePlayer.getName(),
            BigDecimal.valueOf(value),
            true,
            BasicCache.NAME);
  }
}
