package cc.mousse.steward.activity.cache;

import java.util.*;

import cc.mousse.steward.activity.constant.CommandConstant;
import cc.mousse.steward.activity.constant.TextConstant;
import cc.mousse.steward.activity.util.CalendarUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fireflyest.craftitem.nbtapi.NBTItem;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author MochaMousse
 */
public class ItemCache {
  public static final ItemStack BACK;
  public static final ItemStack CLOSE;
  public static final ItemStack MONTH;
  public static final ItemStack BLANK;
  public static final ItemStack TODAY;
  public static final ItemStack PLAYER;
  public static final ItemStack SIGNED;
  public static final ItemStack FESTIVAL;
  public static final ItemStack QING_MING;
  public static final ItemStack MISS_SIGN;
  public static final ItemStack FUTURE_SIGN;
  public static final ItemStack DAYS_MONTH_REWARD;
  public static final ItemStack DURATION_DAY_REWARD;
  private static final Map<String, Material> FESTIVAL_ITEM_MAP;

  static {
    CLOSE =
        new Builder()
            .material(Material.REDSTONE)
            .displayName(TextConstant.CLOSE)
            .lore(CLOSE_LORE)
            .command(CommandConstant.CLOSE)
            .build();
    BACK =
        new Builder()
            .material(Material.REDSTONE)
            .displayName(TextConstant.BACK)
            .lore(BACK_LORE)
            .command(CommandConstant.BACK)
            .build();
    BLANK = new Builder().material(Material.WHITE_STAINED_GLASS_PANE).displayName(true).build();
    TODAY =
        new Builder()
            .material(Material.WRITABLE_BOOK)
            .displayName(SIGN_BUTTON)
            .command(CommandConstant.SIGN)
            .build();
    SIGNED = new Builder().material(Material.KNOWLEDGE_BOOK).displayName(SIGNED_BUTTON).build();
    MISS_SIGN = new Builder().material(Material.BOOK).displayName(MISS_SIGN_BUTTON).build();
    FUTURE_SIGN = new Builder().material(Material.BOOK).displayName(FUTURE_SIGN_BUTTON).build();
    QING_MING =
        new Builder().material(Material.OXEYE_DAISY).displayName(FUTURE_SIGN_BUTTON).build();
    FESTIVAL = new Builder().material(Material.ENCHANTED_BOOK).displayName(true).build();
    PLAYER =
        new Builder()
            .material(Material.PLAYER_HEAD)
            .displayName(PLAYER_BUTTON)
            .lore(3)
            .command(CommandConstant.MENU)
            .build();
    DURATION_DAY_REWARD =
        new Builder()
            .material(Material.CLOCK)
            .displayName(DURATION_REWARD_BUTTON)
            .lore(5)
            .command(CommandConstant.DURATION_REWARD)
            .build();
    DAYS_MONTH_REWARD =
        new Builder()
            .material(Material.ENCHANTING_TABLE)
            .displayName(DAYS_REWARD_BUTTON)
            .lore(4)
            .command(CommandConstant.DAYS_REWARD)
            .build();
    MONTH =
        new Builder()
            .material(Material.ITEM_FRAME)
            .displayName(SWITCH_MONTH_BUTTON)
            .lore(SWITCH_MONTH_LORE)
            .command(CommandConstant.MONTH)
            .build();
    FESTIVAL_ITEM_MAP = HashMap.newHashMap(17);
    FESTIVAL_ITEM_MAP.put("*元旦", Material.BLAZE_SPAWN_EGG);
    FESTIVAL_ITEM_MAP.put("情人节", Material.ROSE_BUSH);
    FESTIVAL_ITEM_MAP.put("植树节", Material.OAK_SAPLING);
    FESTIVAL_ITEM_MAP.put("愚人节", Material.PUFFERFISH);
    FESTIVAL_ITEM_MAP.put("*劳动节", Material.IRON_PICKAXE);
    FESTIVAL_ITEM_MAP.put("青年节", Material.BOOK);
    FESTIVAL_ITEM_MAP.put("儿童节", Material.LEATHER_HORSE_ARMOR);
    FESTIVAL_ITEM_MAP.put("教师节", Material.WRITABLE_BOOK);
    FESTIVAL_ITEM_MAP.put("*国庆节", Material.NETHER_STAR);
    FESTIVAL_ITEM_MAP.put("万圣节", Material.JACK_O_LANTERN);
    FESTIVAL_ITEM_MAP.put("光棍节", Material.STICK);
    FESTIVAL_ITEM_MAP.put("平安夜", Material.APPLE);
    FESTIVAL_ITEM_MAP.put("圣诞节", Material.BLAZE_POWDER);
    FESTIVAL_ITEM_MAP.put("*春节", Material.FIREWORK_ROCKET);
    FESTIVAL_ITEM_MAP.put("元宵节", Material.LANTERN);
    FESTIVAL_ITEM_MAP.put("*端午节", Material.DRAGON_HEAD);
    FESTIVAL_ITEM_MAP.put("*中秋节", Material.PUMPKIN_PIE);
    FESTIVAL_ITEM_MAP.put("*除夕", Material.RED_BANNER);
  }

  private ItemCache() {}

  public static void setCommand(ItemStack item, String command) {
    NBTItem nbtItem = new NBTItem(item, true);
    nbtItem.setString(CommandConstant.COMMAND, command);
  }

  public static ItemStack getNotSign(CalendarUtil.Day day, boolean miss) {
    ItemStack defaultNotSign = miss ? MISS_SIGN.clone() : FUTURE_SIGN.clone();
    if (day == null) {
      return defaultNotSign;
    }
    boolean festival = false;
    Material material = null;
    String memorialDay = "清明";
    if (day.getSolarTerms() != null && memorialDay.equals(day.getSolarTerms())) {
      return QING_MING.clone();
    }
    if (day.getSolarFestival() != null) {
      festival = true;
      material = FESTIVAL_ITEM_MAP.get(day.getSolarFestival());
    }
    if (day.getLunarFestival() != null) {
      festival = true;
      material = FESTIVAL_ITEM_MAP.get(day.getLunarFestival());
    }
    // 非有按钮的节日
    if (!festival) {
      return defaultNotSign;
    }
    // 给按钮设置材料
    if (material == null) {
      material = FESTIVAL.getType();
    }
    return new Builder()
        .material(material)
        .displayName(miss ? MISS_SIGN_BUTTON : FUTURE_SIGN_BUTTON)
        .itemFlag(ItemFlag.HIDE_ATTRIBUTES)
        .itemFlag(ItemFlag.HIDE_DYE)
        .build();
  }

  public static class Builder {
    private Material material;
    private String displayName;
    private String command;
    private final List<String> lore = new ArrayList<>();
    private final List<ItemFlag> itemFlag = new ArrayList<>();

    public Builder material(Material material) {
      this.material = material;
      return this;
    }

    public Builder displayName(String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder displayName(boolean blank) {
      if (blank) {
        this.displayName = TextConstant.BLANK;
      }
      return this;
    }

    public Builder command(String command) {
      this.command = command;
      return this;
    }

    public Builder lore(int line) {
      for (int i = 0; i < line; i++) {
        this.lore.add(EMPTY);
      }
      return this;
    }

    public Builder lore(String lore) {
      this.lore.add(lore);
      return this;
    }

    public Builder itemFlag(ItemFlag flag) {
      this.itemFlag.add(flag);
      return this;
    }

    public org.bukkit.inventory.ItemStack build() {
      org.bukkit.inventory.ItemStack itemStack =
          new org.bukkit.inventory.ItemStack(material == null ? Material.AIR : material);
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (displayName != null) {
        itemMeta.displayName(Component.text(displayName));
      }
      itemMeta.lore(lore.stream().map(l -> Component.text(l.replace(REF, SECTION))).toList());
      itemMeta.addItemFlags(itemFlag.toArray(new ItemFlag[0]));
      itemStack.setItemMeta(itemMeta);
      NBTItem nbtItem = new NBTItem(itemStack, true);
      if (command != null) {
        nbtItem.setString(CommandConstant.COMMAND, command);
      }
      return itemStack;
    }
  }
}
