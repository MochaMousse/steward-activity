package cc.mousse.steward.activity.component;

import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.constant.StateEnum;
import cc.mousse.steward.activity.service.RecordService;
import cc.mousse.steward.activity.util.BalanceUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.event.ViewClickEvent;
import org.fireflyest.util.ItemUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
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
                ViewCache.getGuide().openView(player, ViewCache.CALENDAR, playerName);
                DailyCache.add(playerName);
              }
            });
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    String playerName = event.getPlayer().getName();
    PlayerCache.flush(playerName);
  }

  @EventHandler
  public void onViewClick(ViewClickEvent event) {
    ClickEvent clickEvent = event.getView().title().clickEvent();
    if (clickEvent != null && CommandConstant.COMMAND.equals(clickEvent.value())) {
      // 获取点击的物品
      ItemStack item = event.getCurrentItem();
      // 获取点击数据
      String command = ItemUtils.getItemNbt(item, CommandConstant.COMMAND);
      if (StringUtils.isNotBlank(command)) {
        Player player = (Player) event.getWhoClicked();
        String playerName = player.getName();
        event.setRefresh(true);
        if (command.startsWith(CommandConstant.SIGN)) {
          sign(player, command);
        } else if (command.equals(CommandConstant.MONTH)) {
          month(event, player);
        } else if (command.equals(CommandConstant.MENU)) {
          player.chat(SLASH.concat(CommandConstant.MENU));
        } else if (command.equals(CommandConstant.DAYS_REWARD)) {
          daysReward(player);
        } else if (command.equals(CommandConstant.DURATION_REWARD)) {
          durationReward(player);
        } else if (command.equals(CommandConstant.BACK)) {
          ViewCache.getGuide().openView(player, ViewCache.CALENDAR, playerName);
          player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1F, 1F);
        } else if (command.equals(CommandConstant.CLOSE)) {
          player.closeInventory();
          player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1F, 1F);
        }
      }
    }
  }

  private void sign(Player player, String command) {
    int signDay = Integer.parseInt(command.split(BLANK)[1]);
    PlayerCache.Data playerData = PlayerCache.get(player.getName());
    Map<Integer, Set<Integer>> signedLog = playerData.getSignRecord();
    int thisMonth = DateTimeUtil.month();
    int today = DateTimeUtil.day();
    int chance = playerData.getChance();
    if (!signedLog.get(thisMonth).contains(signDay) && today >= signDay) {
      boolean ok = false;
      if (signDay == today) {
        playerData.setState(StateEnum.SIGNED);
        BalanceUtil.add(player, ConfigCache.getSignReward());
        ok = true;
      } else if (chance > 0) {
        Integer state = playerData.getState().getCode();
        playerData.setDaysOfMonth(playerData.getDaysOfMonth() - state + 1);
        playerData.setDaysOfTotal(playerData.getDaysOfTotal() - state + 1);
        playerData.setChance(playerData.getChance() - 1);
        state = StateEnum.RESIGNED.getCode();
        String playerName = player.getName();
        Date date = DateTimeUtil.date(DateTimeUtil.year(), thisMonth, signDay);
        RecordDo recordDo = RecordService.one(playerName, date);
        if (recordDo == null) {
          recordDo = new RecordDo(null, date, state, playerName, -1L);
          RecordService.add(recordDo);
        } else {
          recordDo.setState(StateEnum.RESIGNED.getCode());
          RecordService.update(recordDo);
        }
        ok = true;
      }
      if (ok) {
        signedLog.get(thisMonth).add(signDay);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1F, 1F);
      }
    }
  }

  private void month(ViewClickEvent event, Player player) {
    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
    if (event.isRightClick()) {
      ViewCache.getGuide().nextPage(player);
    } else {
      ViewCache.getGuide().prePage(player);
    }
  }

  private void daysReward(Player player) {
    List<Map.Entry<Integer, Integer>> daysReward = ConfigCache.getDaysReward();
    if (!daysReward.isEmpty()) {
      boolean flag = false;
      String playerName = player.getName();
      PlayerCache.Data playerData = PlayerCache.get(playerName);
      for (Map.Entry<Integer, Integer> entry : daysReward) {
        if (entry.getKey() > playerData.getDaysOfMonth()) {
          break;
        } else if (entry.getKey() > playerData.getDaysOfMonthReward()) {
          playerData.setDaysOfMonthReward(entry.getKey());
          BalanceUtil.add(player, entry.getValue());
          flag = true;
        }
      }
      player.playSound(
          player.getLocation(),
          flag ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ITEM_BOOK_PAGE_TURN,
          1F,
          1F);
      ViewCache.getGuide().openView(player, ViewCache.DAYS_REWARD, playerName);
    }
  }

  private void durationReward(Player player) {
    List<Map.Entry<Long, Integer>> durationReward = ConfigCache.getDurationReward();
    if (!durationReward.isEmpty()) {
      boolean flag = false;
      String playerName = player.getName();
      PlayerCache.Data playerData = PlayerCache.get(playerName);
      for (Map.Entry<Long, Integer> entry : durationReward) {
        if (entry.getKey() > playerData.getDurationOfDay()) {
          break;
        } else if (entry.getKey() > playerData.getDurationOfDayReward()) {
          BalanceUtil.add(player, entry.getValue());
          playerData.setDurationOfDayReward(entry.getKey());
          flag = true;
        }
      }
      player.playSound(
          player.getLocation(),
          flag ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ITEM_BOOK_PAGE_TURN,
          1F,
          1F);
      ViewCache.getGuide().openView(player, ViewCache.DURATION_REWARD, playerName);
    }
  }
}
