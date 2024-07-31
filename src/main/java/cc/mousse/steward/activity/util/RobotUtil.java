package cc.mousse.steward.activity.util;

import static cc.mousse.steward.activity.constant.RobotConstant.*;
import static cc.mousse.steward.activity.constant.TextConstant.*;

import cc.mousse.steward.activity.cache.ConfigCache;
import cc.mousse.steward.activity.constant.RobotConstant;
import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MochaMousse
 */
public class RobotUtil {
  private static final String URL = ConfigCache.getGoCqHttpUrl();

  private RobotUtil() {}

  public static void sendGroupMessage(String groupId, String message) {
    if (!StringUtils.isAnyBlank(URL, groupId)) {
      Map<String, Object> map = HashMap.newHashMap(2);
      map.put(RobotConstant.GROUP_ID, groupId);
      map.put(MESSAGE, message);
      doPost(SEND_GROUP_MSG, map);
    }
  }

  public static void setTitle(String userId, String title) {
    Map<String, Object> map = HashMap.newHashMap(3);
    map.put(RobotConstant.GROUP_ID, GROUP_ID);
    map.put(USER_ID, userId);
    map.put(SPECIAL_TITLE, title);
    doPost(SET_GROUP_SPECIAL_TITLE, map);
  }

  public static Map<String, List<String>> getGroupMemberList() {
    Map<String, List<String>> info = HashMap.newHashMap(512);
    if (!StringUtils.isAnyBlank(URL, GROUP_ID)) {
      Map<String, Object> map = HashMap.newHashMap(2);
      map.put(RobotConstant.GROUP_ID, GROUP_ID);
      map.put(NO_CACHE, true);
      String response = doPost(GET_GROUP_MEMBER_LIST, map);
      if (response != null) {
        JsonParser.parseString(response)
            .getAsJsonObject()
            .get(DATA)
            .getAsJsonArray()
            .forEach(
                element -> {
                  JsonObject jsonObj = element.getAsJsonObject();
                  String card = jsonObj.get(CARD).getAsString();
                  info.putIfAbsent(card, new ArrayList<>(1));
                  info.get(card).add(jsonObj.get(USER_ID).getAsString());
                });
      }
    }
    return info;
  }

  private static String doPost(String path, Map<String, Object> map) {
    String str = null;
    try (Response response = HttpUtil.post(URL.concat(SLASH).concat(path), map)) {
      if (response.body() != null) {
        str = response.body().string();
      }
    } catch (IOException e) {
      LogUtil.warn(GROUP_MESSAGE_SEND_ERROR);
      LogUtil.warn(e);
    }
    return str;
  }
}
