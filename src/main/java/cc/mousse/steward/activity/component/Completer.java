package cc.mousse.steward.activity.component;

import cc.mousse.steward.activity.cache.BasicCache;
import cc.mousse.steward.activity.cache.PlayerCache;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.cache.RecordCache;
import cc.mousse.steward.activity.service.RecordService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static cc.mousse.steward.activity.constant.LenConstant.*;
import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
public class Completer implements TabCompleter {
  private static final List<String> SUB_COMMAND = new ArrayList<>();

  public Completer() {
    SUB_COMMAND.add(CommandConstant.INFO);
    SUB_COMMAND.add(CommandConstant.CLEAR);
    SUB_COMMAND.add(CommandConstant.REPORT);
    SUB_COMMAND.add(CommandConstant.RELOAD);
  }

  @Override
  public @Nullable List<String> onTabComplete(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      @NotNull String[] args) {
    List<String> tab = new ArrayList<>(2);
    if (sender.isOp() && command.getName().equalsIgnoreCase(CommandConstant.ACTIVITY)) {
      int len = args.length;
      if (len > 0) {
        if (len == 1) {
          tab = new ArrayList<>(SUB_COMMAND);
        } else if (len <= LEN_5 && CommandConstant.INFO.equalsIgnoreCase(args[0])) {
          if (len == LEN_2) {
            cache(tab);
          } else {
            info(args, tab);
          }
        }
      }
    }
    return tab;
  }

  private void cache(List<String> tab) {
    Collection<? extends Player> players = BasicCache.getInstance().getServer().getOnlinePlayers();
    for (Player player : players) {
      tab.add(player.getName());
    }
  }

  private void info(String[] args, List<String> tab) {
    try {
      List<Integer> signRecord = info(args);
      if (signRecord.isEmpty()) {
        tab.add(NULL);
      } else {
        tab.addAll(signRecord.stream().map(String::valueOf).toList());
      }
    } catch (NumberFormatException e) {
      tab.add(DATE_FORMAT_ERROR);
    }
  }

  private List<Integer> info(String[] args) throws NumberFormatException {
    int len = args.length;
    String playerName = args[1];
    List<Integer> signRecord;
    if (len == LEN_3) {
      if (RecordCache.containsPlayer(playerName)) {
        signRecord = RecordCache.getYears(playerName);
      } else {
        signRecord = RecordService.year(playerName);
        RecordCache.putYears(playerName, signRecord);
      }
      return signRecord;
    }
    if (len == LEN_4) {
      Integer year = Integer.valueOf(args[2]);
      if (RecordCache.containsYear(playerName, year)) {
        signRecord = RecordCache.getMonths(playerName, year);
      } else {
        signRecord = RecordService.month(playerName, year);
        RecordCache.putMonths(playerName, year, signRecord);
      }
      return signRecord;
    }
    Integer year = Integer.valueOf(args[2]);
    Integer month = Integer.valueOf(args[3]);
    if (RecordCache.containsMonth(playerName, year, month)) {
      signRecord = RecordCache.getDays(playerName, year, month);
    } else {
      signRecord = RecordService.day(playerName, year, month);
      int today = BasicCache.getDay();
      if (PlayerCache.contains(playerName) && !signRecord.contains(today)) {
        signRecord.add(today);
      }
      RecordCache.putDays(playerName, year, month, signRecord);
    }
    return signRecord;
  }
}
