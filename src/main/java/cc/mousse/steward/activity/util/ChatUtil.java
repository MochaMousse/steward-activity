package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
public class ChatUtil {
  private ChatUtil() {}

  public static void commandButton(Player player, String display, String hover, String command) {
    if (player.isOnline()) {
      TextComponent textComponent =
          Component.text(BasicCache.PREFIX)
              .append(Component.text(display, NamedTextColor.GREEN))
              .hoverEvent(HoverEvent.showText(Component.text(hover)))
              .clickEvent(ClickEvent.runCommand(SLASH.concat(command)));
      player.sendMessage(textComponent);
    }
  }

  public static void message(CommandSender sender, String message) {
    sender.sendMessage(BasicCache.PREFIX.concat(message));
  }
}
