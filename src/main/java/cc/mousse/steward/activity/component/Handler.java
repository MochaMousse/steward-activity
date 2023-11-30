package cc.mousse.steward.activity.component;

import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.cache.ConfigCache;
import cc.mousse.steward.activity.cache.PlayerCache;
import cc.mousse.steward.activity.cache.ViewCache;
import cc.mousse.steward.activity.constant.StateEnum;
import cc.mousse.steward.activity.service.RecordService;
import cc.mousse.steward.activity.util.BalanceUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.event.ViewClickEvent;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author PhineasZ
 */
public class Handler {
  public void sign(Player player, int signDay) {
    PlayerCache.Data playerData = PlayerCache.get(player.getName());
    Map<Integer, Set<Integer>> signedLog = playerData.getSignRecord();
    int today = DateTimeUtil.day();
    int thisMonth = DateTimeUtil.month();
    int chance = playerData.getChance();
    if (!signedLog.get(thisMonth).contains(signDay) && today >= signDay) {
      boolean ok = false;
      if (signDay == today) {
        playerData.setState(StateEnum.SIGNED);
        BalanceUtil.add(player, ConfigCache.getSignReward());
        ok = true;
      } else if (chance > 0) {
        playerData.setChance(playerData.getChance() - 1);
        String playerName = player.getName();
        Date date = DateTimeUtil.date(DateTimeUtil.year(), thisMonth, signDay);
        RecordDo recordDo = RecordService.one(playerName, date);
        if (recordDo == null) {
          recordDo = new RecordDo(null, date, StateEnum.RESIGNED.getCode(), playerName, -1L);
          RecordService.add(recordDo);
        } else {
          recordDo.setState(StateEnum.RESIGNED.getCode());
          RecordService.update(recordDo);
        }
        ok = true;
      }
      if (ok) {
        playerData.setDaysOfMonth(playerData.getDaysOfMonth() + 1);
        playerData.setDaysOfTotal(playerData.getDaysOfTotal() + 1);
        signedLog.get(thisMonth).add(signDay);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1F, 1F);
      }
    }
  }

  public void month(ViewClickEvent event, Player player) {
    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 1F);
    if (event.isRightClick()) {
      ViewCache.getGuide().nextPage(player);
    } else {
      ViewCache.getGuide().prePage(player);
    }
  }

  public void daysReward(Player player, boolean isAuto) {
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
      if (!isAuto) {
        ViewCache.getGuide().openView(player, ViewCache.DAYS_REWARD, playerName);
      }
    }
  }

  public void durationReward(Player player, boolean isAuto) {
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
      if (!isAuto) {
        ViewCache.getGuide().openView(player, ViewCache.DURATION_REWARD, playerName);
      }
    }
  }
}
