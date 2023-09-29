package cc.mousse.steward.activity.gui;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;
import org.fireflyest.craftgui.api.View;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
@NoArgsConstructor
public class DaysRewardView implements View<DaysRewardPage> {
  private final Map<String, DaysRewardPage> pageMap = new HashMap<>();

  @Override
  public DaysRewardPage getFirstPage(String target) {
    pageMap.putIfAbsent(target, new DaysRewardPage(DAYS_REWARD, target));
    return pageMap.get(target);
  }

  @Override
  public void removePage(String target) {
    pageMap.remove(target);
  }
}
