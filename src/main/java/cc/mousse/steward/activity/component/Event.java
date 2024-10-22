package cc.mousse.steward.activity.component;

import static cc.mousse.steward.activity.constant.TextConstant.*;

import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.util.DateTimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
//import org.fireflyest.craftgui.event.ViewClickEvent;
//import org.fireflyest.util.ItemUtils;

/**
 * @author MochaMousse
 */
public class Event implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    Bukkit.getScheduler()
        .runTask(
            BasicCache.getInstance(),
            () -> {
              String playerName = player.getName();
              PlayerCache.init(playerName);
              InfoCache.remove(playerName);
              if (ConfigCache.isShowOnFirstLogin() && !DailyCache.contains(playerName)) {
//                ViewCache.getGuide().openView(player, ViewCache.CALENDAR, playerName);
                DailyCache.add(playerName);
              }
              if (ConfigCache.isAutoMode()) {
                new Handler().sign(player, DateTimeUtil.day());
              }
            });
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    String playerName = event.getPlayer().getName();
    PlayerCache.flush(playerName);
  }

//  @EventHandler
//  public void onViewClick(ViewClickEvent event) {
//    ClickEvent clickEvent = event.getView().title().clickEvent();
//    if (clickEvent != null && CommandConstant.COMMAND.equals(clickEvent.value())) {
//      // 获取点击的物品
//      ItemStack item = event.getCurrentItem();
//      // 获取点击数据
//      String command = ItemUtils.getItemNbt(item, CommandConstant.COMMAND);
//      Handler handler = new Handler();
//      if (StringUtils.isNotBlank(command)) {
//        Player player = (Player) event.getWhoClicked();
//        String playerName = player.getName();
//        event.setRefresh(true);
//        if (command.startsWith(CommandConstant.SIGN)) {
//          handler.sign(player, Integer.parseInt(command.split(BLANK)[1]));
//        } else if (command.equals(CommandConstant.MONTH)) {
//          handler.month(event, player);
//        } else if (command.equals(CommandConstant.MENU)) {
//          player.chat(SLASH.concat(CommandConstant.MENU));
//        } else if (command.equals(CommandConstant.DAYS_REWARD)) {
//          handler.daysReward(player, false);
//        } else if (command.equals(CommandConstant.DURATION_REWARD)) {
//          handler.durationReward(player, false);
//        } else if (command.equals(CommandConstant.BACK)) {
//          ViewCache.getGuide().openView(player, ViewCache.CALENDAR, playerName);
//          player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1F, 1F);
//        } else if (command.equals(CommandConstant.CLOSE)) {
//          player.closeInventory();
//          player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1F, 1F);
//        }
//      }
//    }
//  }
}
