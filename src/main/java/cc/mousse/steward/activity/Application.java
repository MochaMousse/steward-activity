package cc.mousse.steward.activity;

import cc.mousse.steward.activity.component.Command;
import cc.mousse.steward.activity.component.Completer;
import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.component.Event;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.gui.CalendarView;
import cc.mousse.steward.activity.gui.DaysRewardView;
import cc.mousse.steward.activity.gui.DurationRewardView;
import cc.mousse.steward.activity.service.RecordService;
import cc.mousse.steward.activity.service.InfoService;
import cc.mousse.steward.activity.service.TaskService;
import cc.mousse.steward.activity.util.ConfigUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import cc.mousse.steward.activity.util.LogUtil;
import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftgui.api.ViewGuide;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
public final class Application extends JavaPlugin {
  @Override
  public void onEnable() {
    // Plugin startup logic
    BasicCache.setDay(DateTimeUtil.day());
    BasicCache.setMonth(DateTimeUtil.month());
    BasicCache.setYear(DateTimeUtil.year());
    BasicCache.setInstance(this);
    BasicCache.setXConomyAPI(new XConomyAPI());
    PlayerCache.init();
    ConfigUtil.init();
    RecordService.init();
    InfoService.init();
    initViewGuide();
    getServer().getPluginManager().registerEvents(new Event(), this);
    // 注册指令
    PluginCommand command = getCommand(CommandConstant.ACTIVITY);
    if (command != null) {
      command.setExecutor(new Command());
      command.setTabCompleter(new Completer());
    }
    // 在线提醒
    new TaskService().runTaskTimerAsynchronously(this, 20, 20 * 60L * ConfigCache.getTaskPeriod());
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    PlayerCache.close();
    BasicCache.DATA_SOURCE.close();
  }

  /** 界面初始化 */
  public void initViewGuide() {
    RegisteredServiceProvider<ViewGuide> rsp =
        Bukkit.getServer().getServicesManager().getRegistration(ViewGuide.class);
    if (rsp == null) {
      LogUtil.error(GUI_NOTFOUND);
    } else {
      ViewCache.setGuide(rsp.getProvider());
      ViewCache.getGuide().addView(ViewCache.CALENDAR, new CalendarView(BasicCache.DISPLAY_NAME));
      ViewCache.getGuide().addView(ViewCache.DAYS_REWARD, new DaysRewardView());
      ViewCache.getGuide().addView(ViewCache.DURATION_REWARD, new DurationRewardView());
    }
  }
}
