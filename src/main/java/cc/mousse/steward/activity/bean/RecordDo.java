package cc.mousse.steward.activity.bean;

import java.util.Date;
import java.util.Objects;

import cc.mousse.steward.activity.constant.StateEnum;
import cc.mousse.steward.activity.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static cc.mousse.steward.activity.constant.StyleConstant.*;
import static cc.mousse.steward.activity.constant.TextConstant.*;

/**
 * @author PhineasZ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDo {
  private Long id;
  private Date date;
  private Integer state;
  private String player;
  private Long duration;

  @Override
  public String toString() {
    return DARK_AQUA
        .concat(player)
        .concat(TOSTRING_START)
        .concat(LOGIN_DATE)
        .concat(TOSTRING_COLON)
        .concat(DateTimeUtil.date(date))
        .concat(TOSTRING_SPLIT)
        .concat(DURATION_OF_DAY)
        .concat(TOSTRING_COLON)
        .concat(DateTimeUtil.duration(duration))
        .concat(TOSTRING_SPLIT)
        .concat(SIGN_STATE)
        .concat(TOSTRING_COLON)
        .concat(Objects.requireNonNull(StateEnum.ofCode(state)).getMsg())
        .concat(TOSTRING_NED);
  }
}
