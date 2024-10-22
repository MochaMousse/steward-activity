package cc.mousse.steward.activity.component;

import cc.mousse.steward.activity.bean.InfoDo;
import cc.mousse.steward.activity.bean.RecordDo;
import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.service.InfoService;
import cc.mousse.steward.activity.service.RecordService;
import cc.mousse.steward.activity.service.RobotService;
import cc.mousse.steward.activity.util.ChatUtil;
import cc.mousse.steward.activity.util.ConfigUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static cc.mousse.steward.activity.constant.LenConstant.*;
import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author MochaMousse
 */
public class Command implements CommandExecutor {
  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull org.bukkit.command.Command command,
      @NotNull String label,
      @NotNull String[] args) {
    if (command.getName().equalsIgnoreCase(CommandConstant.ACTIVITY)) {
      boolean gui = mainPage(args, sender);
      if (!gui) {
        CompletableFuture.runAsync(
            () -> {
              boolean opOrConsole = !(sender instanceof Player) || sender.isOp();
              if (opOrConsole) {
                @SuppressWarnings("unused")
                boolean ignore =
                    reload(args, sender)
                        || report(args, sender)
                        || clean(args, sender)
                        || cache(args, sender)
                        || signRecord(args, sender);
              }
            });
      }
    }
    return true;
  }

  private boolean mainPage(String[] args, CommandSender sender) {
    if (args.length == 0) {
      if (sender instanceof Player player) {
//        ViewCache.getGuide().openView(player, ViewCache.CALENDAR, player.getName());
      } else {
        ChatUtil.message(sender, PLAYER_ONLY);
      }
      return true;
    }
    return false;
  }

  private boolean reload(String[] args, CommandSender sender) {
    if (args.length == 1 && CommandConstant.RELOAD.equalsIgnoreCase(args[0])) {
      ConfigUtil.reload();
      ChatUtil.message(sender, RELOAD_OK);
      return true;
    }
    return false;
  }

  private boolean report(String[] args, CommandSender sender) {
    if (!CommandConstant.REPORT.equalsIgnoreCase(args[0])) {
      return false;
    }
    int len = args.length;
    PlayerCache.get().values().forEach(PlayerCache::flush);
    if (len == 1) {
      ChatUtil.message(
          sender,
          RobotService.dailyReport(
              DateTimeUtil.year(), DateTimeUtil.month(), DateTimeUtil.day(), false));
      return true;
    }
    String message;
    if (len == LEN_3 || len == LEN_4) {
      int year;
      int month;
      int day;
      try {
        year = Integer.parseInt(args[1]);
        month = Integer.parseInt(args[2]);
        day = len == LEN_4 ? Integer.parseInt(args[3]) : 0;
        message =
            day == 0
                ? RobotService.monthlyReport(year, month, false)
                : RobotService.dailyReport(year, month, day, false);
      } catch (NumberFormatException e) {
        message = DATE_FORMAT_ERROR;
      }
    } else {
      message = DATE_FORMAT_ERROR;
    }
    if (StringUtils.isNotBlank(message)) {
      ChatUtil.message(sender, message);
    }
    return true;
  }

  private boolean clean(String[] args, CommandSender sender) {
    if (args.length == 1 && CommandConstant.CLEAR.equalsIgnoreCase(args[0])) {
      InfoCache.clear();
      RecordCache.clear();
      ChatUtil.message(sender, CLEAR_OK);
      return true;
    }
    return false;
  }

  private boolean cache(String[] args, CommandSender sender) {
    int len = args.length;
    if (len >= 1 && len <= LEN_2 && CommandConstant.INFO.equalsIgnoreCase(args[0])) {
      if (len == 1) {
        cache(sender);
      } else {
        String playerName = args[1];
        if (PlayerCache.contains(playerName)) {
          cache(sender, playerName);
        } else {
          info(sender, playerName);
        }
      }
      return true;
    }
    return false;
  }

  private void cache(CommandSender sender) {
    Collection<PlayerCache.Data> playerDatas = PlayerCache.get().values();
    if (playerDatas.isEmpty()) {
      ChatUtil.message(sender, DATA_NOTFOUND);
    } else {
      playerDatas.forEach(playerData -> ChatUtil.message(sender, playerData.toString()));
    }
  }

  private void cache(CommandSender sender, String playerName) {
    PlayerCache.Data playerData = PlayerCache.get(playerName);
    if (playerData == null) {
      ChatUtil.message(sender, DATA_NOTFOUND);
    } else {
      ChatUtil.message(sender, playerData.toString());
    }
  }

  private void info(CommandSender sender, String playerName) {
    if (InfoCache.contains(playerName)) {
      ChatUtil.message(sender, InfoCache.get(playerName).toString());
    } else {
      InfoDo infoDo = InfoService.one(playerName);
      if (infoDo == null) {
        ChatUtil.message(sender, DATA_NOTFOUND);
      } else {
        ChatUtil.message(sender, infoDo.toString());
        InfoCache.put(playerName, infoDo);
      }
    }
  }

  private boolean signRecord(String[] args, CommandSender sender) {
    int len = args.length;
    if (len > LEN_2 && CommandConstant.INFO.equalsIgnoreCase(args[0])) {
      switch (len) {
        case LEN_3 -> {
          ChatUtil.message(sender, MONTH_NOT_SPECIFY);
        }
        case LEN_4 -> {
          ChatUtil.message(sender, DAY_NOT_SPECIFY);
        }
        default -> {
          try {
            signRecord(
                args[1],
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                sender);
            return true;
          } catch (NumberFormatException e) {
            ChatUtil.message(sender, DATE_FORMAT_ERROR);
          }
        }
      }
    }
    return false;
  }

  private void signRecord(
      String playerName, Integer year, Integer month, Integer day, CommandSender sender) {
    RecordDo recordDo;
    Date today = DateTimeUtil.date();
    if (PlayerCache.contains(playerName)
        && Objects.equals(DateTimeUtil.date(year, month, day), today)) {
      PlayerCache.Data playerData = PlayerCache.get(playerName);
      recordDo =
          new RecordDo(
              null,
              today,
              playerData.getState().getCode(),
              playerName,
              playerData.getDurationOfDay());
    } else if (RecordCache.containsDay(playerName, year, month, day)) {
      recordDo = RecordCache.get(playerName, year, month, day);
    } else {
      recordDo = RecordService.one(playerName, DateTimeUtil.date(year, month, day));
    }
    if (recordDo == null) {
      ChatUtil.message(sender, DATA_NOTFOUND);
    } else {
      ChatUtil.message(sender, recordDo.toString());
      RecordCache.put(playerName, year, month, day, recordDo);
    }
  }
}
