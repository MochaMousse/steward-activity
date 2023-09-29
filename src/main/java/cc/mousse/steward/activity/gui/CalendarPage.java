package cc.mousse.steward.activity.gui;

import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.util.CalendarUtil;
import cc.mousse.steward.activity.util.LogUtil;
import cc.mousse.steward.activity.util.DateTimeUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.util.ItemUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.*;

import static cc.mousse.steward.activity.constant.TextConstant.*;
import static cc.mousse.steward.activity.constant.StyleConstant.*;

/**
 * @author PhineasZ
 */
public class CalendarPage implements ViewPage {
  private final Map<Integer, ItemStack> menuMap = new HashMap<>();
  private Map<Integer, ItemStack> itemMap = new HashMap<>();
  private final Inventory inventory;
  private String title;
  private final String target;
  private final int year;
  private final int month;
  private final MenuPage menuPage;
  private ViewPage nextPage;
  private ViewPage prevPage;

  public CalendarPage(String title, String target, int year, int month) {
    this.title = title;
    this.target = target;
    this.year = year;
    this.month = month;
    menuPage = new MenuPage();
    if (target != null) {
      // 副标题
      title = title.concat(BLUE).concat(target);
    }
    // 给标题加上页码
    title =
        title
            .concat(BLANK)
            .concat(GRAY)
            .concat(SHARP)
            .concat(DARK_GRAY)
            .concat(String.valueOf(month))
            .concat(MONTH);
    // 界面容器
    inventory =
        Bukkit.createInventory(
            null,
            54,
            Component.text(title)
                .clickEvent(
                    ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, CommandConstant.COMMAND)));
    refreshPage();
  }

  @Override
  public @Nonnull Map<Integer, ItemStack> getItemMap() {
    // 侧边导航
    menuPage.update(menuMap, target);
    itemMap = new HashMap<>(menuMap);
    int thisYear = DateTimeUtil.year();
    int thisMonth = DateTimeUtil.month();
    int today = DateTimeUtil.day();
    PlayerCache.Data data = PlayerCache.get(target);
    Set<Integer> signRecord = data.getSignRecord().get(month);
    // 放置物品
    int k = 0;
    int j = DateTimeUtil.firstDayOfMonth(month) - 1;
    for (int i = 0; i < DateTimeUtil.dayCountOfMonth(month); i++) {
      int day = i + 1;
      // 获取当天数据
      CalendarUtil.Day thatDay = null;
      try {
        thatDay = CalendarUtil.lunarDayOfDate(thisYear, month, day);
      } catch (ParseException e) {
        LogUtil.error(e);
      }
      ItemStack item;
      if (signRecord.contains(day)) {
        // 已签
        item = ItemCache.SIGNED.clone();
      } else if (month == thisMonth && day == today) {
        // 当天
        item = ItemCache.TODAY.clone();
      } else {
        // 未签
        item =
            ItemCache.getNotSign(thatDay, (month == thisMonth && day < today) || month < thisMonth);
      }
      // 展示天数(一些节日物品可能无法正常显示，例如鸡蛋最多叠16个)
      item.setAmount(day);
      // 点击指令只有本月可以签到
      if (thisMonth == month) {
        ItemCache.setCommand(item, CommandConstant.SIGN.concat(BLANK).concat(String.valueOf(day)));
      }
      // 设置物品样式
      updateCalendarItem(thisYear, thatDay, item);
      // 获取物品位置
      int idx = k * 9 + j++;
      if (j % 9 == 7) {
        j = 0;
        k++;
      }
      itemMap.put(idx, item);
    }
    return itemMap;
  }

  private void updateCalendarItem(int year, CalendarUtil.Day day, ItemStack item) {
    if (day != null) {
      // 节日
      String lore = getLore(day);
      ItemUtils.addLore(item, lore);
      // 日期
      lore =
          String.format(
              WHITE
                  .concat(REPLACE)
                  .concat(YEAR)
                  .concat(REPLACE)
                  .concat(MONTH)
                  .concat(REPLACE)
                  .concat(DAY)
                  .concat(BLANK)
                  .concat(REPLACE),
              year,
              month,
              day.getDay(),
              day.getDayOfWeek());
      ItemUtils.addLore(item, lore);
      lore = String.format(WHITE.concat(REPLACE), day.getLunarTime());
      ItemUtils.addLore(item, lore);
      lore =
          String.format(
              GRAY.concat(REPLACE).concat(BLANK).concat(REPLACE).concat(BLANK).concat(REPLACE),
              day.getLunarYear(),
              day.getLunarMonth(),
              day.getLunarDay());
      ItemUtils.addLore(item, lore);
    }
  }

  @NotNull
  private static String getLore(CalendarUtil.Day day) {
    String lore = EMPTY;
    if (day.getSolarFestival() != null) {
      lore +=
          YELLOW
              .concat(BOLD)
              .concat(day.getSolarFestival())
              .replace(STAR, GOLD.concat(STAR).concat(YELLOW).concat(BOLD));
    }
    if (day.getLunarFestival() != null) {
      lore +=
          AQUA.concat(BOLD)
              .concat(day.getLunarFestival())
              .replace(STAR, GOLD.concat(STAR).concat(AQUA).concat(BOLD));
    }
    if (day.getSolarTerms() != null) {
      lore += DARK_GREEN.concat(BOLD).concat(day.getSolarTerms());
    }
    return lore;
  }

  @Override
  public @Nonnull Map<Integer, ItemStack> getButtonMap() {
    return new HashMap<>(menuMap);
  }

  @Override
  public @Nullable ItemStack getItem(int i) {
    return itemMap.get(i);
  }

  @Override
  public @Nonnull Inventory getInventory() {
    return inventory;
  }

  @Override
  public @Nullable String getTarget() {
    return target;
  }

  @Override
  public int getPage() {
    return month;
  }

  @Override
  public @Nullable ViewPage getNext() {
    int maxMonth = 12;
    if (nextPage == null && month < maxMonth) {
      nextPage = new CalendarPage(title, target, year, month + 1);
      nextPage.setPre(this);
    }
    return nextPage;
  }

  @Override
  public @Nullable ViewPage getPre() {
    if (prevPage == null && month > 1) {
      prevPage = new CalendarPage(title, target, year, month - 1);
      prevPage.setNext(this);
    }
    return prevPage;
  }

  @Override
  public void setNext(@Nullable ViewPage next) {
    this.nextPage = next;
  }

  @Override
  public void setPre(@Nullable ViewPage pre) {
    this.prevPage = pre;
  }

  @Override
  public void refreshPage() {
    menuPage.init(menuMap, true);
  }

  @Override
  public void updateTitle(String title) {
    this.title = title;
  }
}
