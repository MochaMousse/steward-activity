package cc.mousse.steward.activity.bean;

import cc.mousse.steward.activity.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static cc.mousse.steward.activity.constant.StyleConstant.*;
import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author MochaMousse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoDo {
  private Long id;
  private String player;
  private Date lastLogin;
  private Integer chance;
  private Integer daysOfMonth;
  private Integer daysOfTotal;
  private Long durationOfTotal;
  private Integer daysOfMonthReward;
  private Long durationOfDayReward;

  @Override
  public String toString() {
    return GRAY.concat(player)
        .concat(TOSTRING_START)
        .concat(LOGIN_TIME)
        .concat(TOSTRING_COLON)
        .concat(DateTimeUtil.date(lastLogin))
        .concat(TOSTRING_SPLIT)
        .concat(SIGN_CHANCE)
        .concat(TOSTRING_COLON)
        .concat(String.valueOf(chance))
        .concat(TOSTRING_SPLIT)
        .concat(DAYS_OF_MONTH)
        .concat(TOSTRING_COLON)
        .concat(String.valueOf(daysOfMonth))
        .concat(TOSTRING_SPLIT)
        .concat(DAYS_OF_TOTAL)
        .concat(TOSTRING_COLON)
        .concat(String.valueOf(daysOfTotal))
        .concat(TOSTRING_SPLIT)
        .concat(DURATION_OF_TOTAL)
        .concat(TOSTRING_COLON)
        .concat(DateTimeUtil.duration(durationOfTotal))
        .concat(TOSTRING_SPLIT)
        .concat(DAYS_REWARD_TAKEN)
        .concat(TOSTRING_COLON)
        .concat(daysOfMonthReward == -1 ? NULL : String.valueOf(daysOfMonthReward).concat(DAY))
        .concat(TOSTRING_SPLIT)
        .concat(DURATION_REWARD_TAKEN)
        .concat(TOSTRING_COLON)
        .concat(
            durationOfDayReward == -1
                ? NULL
                : String.valueOf(durationOfDayReward / 1000 / 60).concat(MINUTE))
        .concat(TOSTRING_NED);
  }
}
