package cc.mousse.steward.activity.cache;

import lombok.Getter;
import lombok.Setter;
import org.fireflyest.craftgui.api.ViewGuide;

/**
 * @author PhineasZ
 */
public class ViewCache {
  private ViewCache() {}

  @Getter @Setter private static ViewGuide guide;
  public static final String CALENDAR = "activity.calendar";
  public static final String DAYS_REWARD = "activity.days-reward";
  public static final String DURATION_REWARD = "activity.duration-reward";
}
