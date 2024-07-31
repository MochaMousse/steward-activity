package cc.mousse.steward.activity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MochaMousse
 */
@Getter
@AllArgsConstructor
public enum StateEnum {
  /** 签到状态 */
  UNSIGNED(0, "未签"),
  SIGNED(1, "已签"),
  RESIGNED(2, "补签");

  private final Integer code;
  private final String msg;

  public static StateEnum ofCode(Integer state) {
    for (StateEnum e : StateEnum.values()) {
      if (e.code.equals(state)) {
        return e;
      }
    }
    return null;
  }
}
