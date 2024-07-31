package cc.mousse.steward.activity.gui;

import cc.mousse.steward.activity.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.fireflyest.craftgui.api.View;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MochaMousse
 */
@AllArgsConstructor
public class CalendarView implements View<CalendarPage> {
  private final Map<String, CalendarPage> pageMap = new HashMap<>();
  private final String title;

  @Override
  public @Nullable CalendarPage getFirstPage(@Nullable String target) {
    pageMap.putIfAbsent(
        target, new CalendarPage(title, target, DateTimeUtil.year(), DateTimeUtil.month()));
    return pageMap.get(target);
  }

  @Override
  public void removePage(@Nullable String target) {
    pageMap.remove(target);
  }
}
