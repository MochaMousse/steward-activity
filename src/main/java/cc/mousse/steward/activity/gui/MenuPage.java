//package cc.mousse.steward.activity.gui;
//
//import static cc.mousse.steward.activity.constant.StyleConstant.*;
//import static cc.mousse.steward.activity.constant.TextConstant.*;
//
//import cc.mousse.steward.activity.cache.BasicCache;
//import cc.mousse.steward.activity.cache.ConfigCache;
//import cc.mousse.steward.activity.cache.ItemCache;
//import cc.mousse.steward.activity.cache.PlayerCache;
//import cc.mousse.steward.activity.util.DateTimeUtil;
//import java.util.Map;
//import org.bukkit.Bukkit;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.SkullMeta;
//import org.fireflyest.util.ItemUtils;
//
///**
// * @author MochaMousse
// */
//public class MenuPage {
//  public void init(Map<Integer, ItemStack> menu, boolean calendar) {
//    int minIdx = 7;
//    int maxIdx = 53;
//    int step = 9;
//    for (int i = minIdx; i < maxIdx; i += step) {
//      menu.put(i, ItemCache.BLANK);
//    }
//    menu.put(8, ItemCache.PLAYER.clone());
//    if (calendar) {
//      menu.put(35, ItemCache.MONTH.clone());
//      menu.put(53, ItemCache.CLOSE.clone());
//    } else {
//      menu.put(53, ItemCache.BACK.clone());
//    }
//    menu.put(17, ItemCache.DURATION_DAY_REWARD.clone());
//    menu.put(26, ItemCache.DAYS_MONTH_REWARD.clone());
//  }
//
//  public void update(Map<Integer, ItemStack> menu, String playerName) {
//    PlayerCache.Data playerData = PlayerCache.get(playerName);
//    playerButton(menu, playerData, playerName);
//    daysRewardButton(menu, playerData);
//    durationRewardButton(menu, playerData);
//  }
//
//  private void playerButton(
//      Map<Integer, ItemStack> menu, PlayerCache.Data playerData, String playerName) {
//    ItemStack player = menu.get(8);
//    SkullMeta meta = (SkullMeta) player.getItemMeta();
//    meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
//    player.setItemMeta(meta);
//    ItemUtils.setDisplayName(player, String.format(BLUE.concat(BOLD).concat(REPLACE), playerName));
//    ItemUtils.setLore(
//        player,
//        String.format(
//            BALANCE_INFO, BasicCache.getXConomyAPI().getPlayerData(playerName).getBalance()),
//        1);
//    ItemUtils.setLore(player, String.format(CHANCE_INFO, playerData.getChance()), 2);
//    ItemUtils.setLore(player, BACK_TO_MENU, 3);
//  }
//
//  private void daysRewardButton(Map<Integer, ItemStack> menu, PlayerCache.Data playerData) {
//    ItemStack days = menu.get(26);
//    ItemUtils.setLore(days, String.format(DAYS_OF_MONTH_INFO, playerData.getDaysOfMonth()), 1);
//    ItemUtils.setLore(days, String.format(DAYS_OF_TOTAL_INFO, playerData.getDaysOfTotal()), 2);
//    if (!ConfigCache.getDaysReward().isEmpty()) {
//      boolean flag = false;
//      for (Map.Entry<Integer, Integer> entry : ConfigCache.getDaysReward()) {
//        Integer requireDays = entry.getKey();
//        if (requireDays > playerData.getDaysOfMonth()
//            || requireDays > playerData.getDaysOfMonthReward()) {
//          flag = requireDays <= playerData.getDaysOfMonth();
//          break;
//        }
//      }
//      ItemUtils.setLore(days, flag ? GET_REWARD : SEE_REWARD, 3);
//    }
//  }
//
//  private void durationRewardButton(Map<Integer, ItemStack> menu, PlayerCache.Data playerData) {
//    ItemStack duration = menu.get(17);
//    ItemUtils.setLore(
//        duration,
//        String.format(
//            DURATION_OF_ONLINE_INFO, DateTimeUtil.duration(playerData.getDurationOnline())),
//        1);
//    ItemUtils.setLore(
//        duration,
//        String.format(DURATION_OF_DAY_INFO, DateTimeUtil.duration(playerData.getDurationOfDay())),
//        2);
//    ItemUtils.setLore(
//        duration,
//        String.format(
//            DURATION_OF_TOTAL_INFO, DateTimeUtil.duration(playerData.getDurationOfTotal())),
//        3);
//    if (!ConfigCache.getDurationReward().isEmpty()) {
//      boolean flag = false;
//      for (Map.Entry<Long, Integer> entry : ConfigCache.getDurationReward()) {
//        Long requireDuration = entry.getKey();
//        if (requireDuration > playerData.getDurationOfDay()
//            || requireDuration > playerData.getDurationOfDayReward()) {
//          flag = requireDuration <= playerData.getDurationOfDay();
//          break;
//        }
//      }
//      ItemUtils.setLore(duration, flag ? GET_REWARD : SEE_REWARD, 4);
//    }
//  }
//}
