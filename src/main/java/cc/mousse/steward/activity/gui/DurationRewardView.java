package cc.mousse.steward.activity.gui;

import lombok.NoArgsConstructor;
import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
@NoArgsConstructor
public class DurationRewardView implements View<DurationRewardPage> {
  private final Map<String, DurationRewardPage> pageMap = new HashMap<>();

  @Override
  public DurationRewardPage getFirstPage(String target) {
    pageMap.putIfAbsent(target, new DurationRewardPage(DURATION_REWARD, target));
    return pageMap.get(target);
  }

  @Override
  public void removePage(String target) {
    pageMap.remove(target);
  }
}
