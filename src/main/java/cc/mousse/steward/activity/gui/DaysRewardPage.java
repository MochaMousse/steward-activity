package cc.mousse.steward.activity.gui;

import java.util.*;

import cc.mousse.steward.activity.cache.*;
import cc.mousse.steward.activity.constant.CommandConstant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static cc.mousse.steward.activity.constant.TextConstant.*;
import static cc.mousse.steward.activity.constant.StyleConstant.*;

/**
 * @author MochaMousse
 */
public class DaysRewardPage implements ViewPage {
  private final Map<Integer, ItemStack> menuMap = new HashMap<>();
  private Map<Integer, ItemStack> itemMap = new HashMap<>();
  private final Inventory inventory;
  private final String target;
  private final MenuPage menuPage;

  public DaysRewardPage(String title, String target) {
    this.target = target;
    menuPage = new MenuPage();
    if (target != null) {
      // 副标题
      title = title.concat(BLUE).concat(target);
    }
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
  public @NotNull Map<Integer, ItemStack> getItemMap() {
    // 侧边导航
    menuPage.update(menuMap, target);
    itemMap = new HashMap<>(menuMap);
    int daysOfMonthReward = PlayerCache.get(target).getDaysOfMonthReward();
    int idx = 0;
    for (Map.Entry<Integer, Integer> entry : ConfigCache.getDaysReward()) {
      int day = entry.getKey();
      String color;
      Material material;
      if (day <= daysOfMonthReward) {
        material = Material.GLASS_BOTTLE;
        color = WHITE.concat(BOLD);
      } else {
        material = Material.EXPERIENCE_BOTTLE;
        color = RED.concat(BOLD);
      }
      ItemStack item =
          new ItemCache.Builder()
              .material(material)
              .displayName(color.concat(String.format(DAY_X_INFO, day)))
              .lore(1)
              .lore(GREEN.concat(String.valueOf(entry.getValue())).concat(BasicCache.CURRENCY_NAME))
              .build();
      item.setAmount(day);
      itemMap.put(idx, item);
      idx = ++idx % 7;
    }
    return itemMap;
  }

  @Override
  public @NotNull Map<Integer, ItemStack> getButtonMap() {
    return new HashMap<>(menuMap);
  }

  @Override
  public @Nullable ItemStack getItem(int i) {
    return itemMap.get(i);
  }

  @Override
  public @NotNull Inventory getInventory() {
    return inventory;
  }

  @Override
  public String getTarget() {
    return target;
  }

  @Override
  public int getPage() {
    return 0;
  }

  @Override
  public ViewPage getNext() {
    return this;
  }

  @Override
  public ViewPage getPre() {
    return this;
  }

  @Override
  public void setPre(ViewPage pre) {
    // 单页
  }

  @Override
  public void setNext(ViewPage next) {
    // 单页
  }

  @Override
  public void refreshPage() {
    menuPage.init(menuMap, false);
  }

  @Override
  public void updateTitle(String title) {
    // 单页
  }
}
