package cc.mousse.steward.activity.util;

import cc.mousse.steward.activity.cache.BasicCache;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import okhttp3.*;

/**
 * @author MochaMousse
 */
public class HttpUtil {
  private static final MediaType JSON = MediaType.parse(BasicCache.JSON_FORMAT);
  private static final OkHttpClient CLIENT = new OkHttpClient();
  private static final Gson GSON = BasicCache.GSON;

  private HttpUtil() {}

  public static Response post(String url, Map<String, Object> json) throws IOException {
    RequestBody body = RequestBody.Companion.create(GSON.toJson(json), JSON);
    Request request = new Request.Builder().url(url).post(body).build();
    return CLIENT.newCall(request).execute();
  }
}
